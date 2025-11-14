package com.yunbao.common.utils.downmumusicutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.yunbao.common.CommonAppContext.sInstance;

public class DownUtils {

    private List<String> downQueue = new ArrayList<>();
    private List<String> temperaList = new ArrayList<>();
    private boolean isDownLoading = false;
    private String TAG = "DOWNLOAD..";
    private boolean isCanDown = true;
    private int downIndex = 0;
    private static DownUtils downUtils;
    private OnDownListener onDownListener;

    public void setOnDownListener(OnDownListener on){
        this.onDownListener = on;
    }

    public static DownUtils getInstance(){
        if(downUtils == null){
            downUtils = new DownUtils();
        }
        return downUtils;
    }

    @SuppressLint("HandlerLeak")
    private Handler mUpdateProgressHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.v("进度",""+msg);
            switch (msg.what){
                case 1:     //开始下载
                    if(onDownListener!=null){
                        onDownListener.onDownloadStart(msg.obj.toString());
                    }
                    Log.i(TAG,"开始下载第"+downIndex+"个任务，文件名："+msg.obj);
                    break;
                case 2:     //下载完成
                    if(onDownListener!=null){
                        onDownListener.onDownFinish(msg.obj.toString());
                    }
                    Log.i(TAG,"第"+downIndex+"个任务下载完成，文件名："+msg.obj);
                    break;
                case 3:     //下载进度
                    if(onDownListener!=null){
                        onDownListener.onDowning(msg.arg2);
                    }
                    Log.i(TAG,"文件总大小："+msg.arg1 + "  当前已下载："+msg.arg2);
                    break;
                case 4:
                    if(onDownListener!=null){
                        onDownListener.onDownExist(msg.obj.toString());
                    }
                    break;
            }
        };
    };

    //停止下载
    public void stopDown(){
        isDownLoading = false;
        isCanDown = false;
    }

    //停止下载，并取消正在下载的任务、删除下载未完成的文件
    public void stopCancelDown(){
        isDownLoading = false;
        isCanDown = false;
        if(downQueue.size()>0){
            for(int i =0;i<downQueue.size();i++){
                File file = getFile(downQueue.get(i));
                if(file!=null && file.exists()){
                    file.delete();
                }
            }
        }
        downQueue.clear();
        downIndex = 0;
    }

    //恢复等待下载状态
    public void startDown(){
        isCanDown = true;
        formatFileLength();
    }

    //添加到下载队列
    public synchronized void addDownload(final List<String> httpUrl) {
        isCanDown = true;
        if(httpUrl == null || httpUrl.size() == 0){
            return;
        }
        checkFileExist(httpUrl);
    }

    //添加到下载队列
    public synchronized void addDownload(String url){
        isCanDown = true;
        if(!TextUtils.isEmpty(url)){
            temperaList.clear();
            temperaList.add(url);
            addDownload(temperaList);
        }
    }

    //校验文件是否存在，已存在的不下载
    private void checkFileExist(List<String> httpUrl){
        synchronized (this){
            for(int i=0;i<httpUrl.size();i++){
                File file = getFile(httpUrl.get(i));
                if(file.getParentFile().exists()){
                    if(!file.exists()){
                        downQueue.add(httpUrl.get(i));
                    }else{
                        //该mp3文件已存在，不下载
                        if (mUpdateProgressHandler!=null){
                            Message message2 = mUpdateProgressHandler.obtainMessage();
                            message2.what = 4;
                            message2.obj = file.getAbsoluteFile();
                            mUpdateProgressHandler.sendMessage(message2);
                        }
                    }
                }else{
                    downQueue.add(httpUrl.get(i));
                }
            }
            formatFileLength();
        }
    }



    //开始新的下载任务
    public synchronized void nextDown(){
        if(downQueue.size()>0){
            downFile(downQueue.get(0));
        }
    }

    //获取本地音频根目录
    public String getMusicLocalPath(){
        String path = "";
        File file = new File(getMusicParentPath());
        if(file!=null){
            if(file.exists()){
                File[] files = file.listFiles();
                if(files!=null && files.length>0){
                    path = files[0].getAbsolutePath();
                }
            }
        }
        return path;
    }

    //开始下载
    public synchronized void downFile(final String httpUrl){
        if(isDownLoading){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    try {
                        downIndex++;
                        isDownLoading = true;
                        URL url = new URL(httpUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(30000);
                        FileOutputStream fileOutputStream = null;
                        File file = null;
                        InputStream inputStream;
                        if (connection.getResponseCode() == 200) {
                            int totalSize = connection.getContentLength();//获取文件总大小
                            int downSize = 0; //当前下载多少
                            inputStream = connection.getInputStream();

                            if (inputStream != null) {
                                file = getFile(httpUrl);
                                Message message = mUpdateProgressHandler.obtainMessage();
                                message.what = 1;
                                message.obj = file.getAbsoluteFile();
                                mUpdateProgressHandler.sendMessage(message);
                                //父文件夹不存在先创建
                                File parent = new File(file.getParent());
                                String a= parent.getAbsolutePath();
                                if(!parent.exists()){
                                    boolean aaa = file.getParentFile().mkdir();
                                    Log.i("","");
                                }
                                fileOutputStream = new FileOutputStream(file);
                                byte[] buffer = new byte[1024];
                                int length = 0;

                                while ((length = inputStream.read(buffer)) != -1) {
                                    if(isCanDown){
                                        fileOutputStream.write(buffer, 0, length);
                                        downSize += length;
                                        Message message2 = mUpdateProgressHandler.obtainMessage();
                                        message2.what = 3;
                                        message2.arg1 = totalSize;
                                        message2.arg2 = (downSize * 100) / totalSize;
                                        mUpdateProgressHandler.sendMessage(message2);
                                    }else{
                                        break;
                                    }
                                }
                                try{
                                    buffer = null;
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                    fileOutputStream.flush();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                try{
                                    inputStream.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            connection.disconnect();
                            if(isCanDown){
                                Log.i("download", "run: "+"下载完成---->"+file.getAbsolutePath());
                                String pkg = "";

                                try{
//                                    MediaScannerConnection.scanFile(sInstance,new String[]{file.getAbsolutePath()},null,null);
                                    MediaScannerConnection.scanFile(sInstance,new String[]{file.getAbsolutePath()},null,null);
                                    MediaScannerConnection.scanFile(sInstance,new String[]{getMusicParentPath()},null,null);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                // 往handler发送一条消息 更改button的text属性
                                Message message2 = mUpdateProgressHandler.obtainMessage();
                                message2.what = 2;
                                message2.obj = file.getAbsoluteFile();
                                mUpdateProgressHandler.sendMessage(message2);
                                isDownLoading = false;
                                if(downQueue.size()>0){
                                    downQueue.remove(downQueue.get(0));
                                }
                                formatFileLength();
                            }else{
                                if(file!=null){
                                    file.delete();
                                }
                            }
                        }else{
                            //下载地址不正确
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 根据传过来url创建文件
     */
    private File getFile(String url) {
        File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "SportMusic" + getFilePath(url));
        return files;
    }

    public String getMusicParentPath(){
        return new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "SportMusic").getAbsolutePath();
    }

    /**
     * 截取出url后面的文件名后缀
     * @param url
     * @return
     */
    private String getFilePath(String url) {
        return url.substring(url.lastIndexOf("/"));
    }
//    private String getFilePath(String url) {
//        return "/"+System.currentTimeMillis()+".mp3";
//    }

    public interface OnDownListener{
        void onDownloadStart(String path);
        void onDownFinish(String path);
        void onDowning(int progress);
        void onDownExist(String path);
    }



    //删除所有文件
    public void deleteAll(){
        try{
            File file = new File(getMusicParentPath());
            List<File> list = new ArrayList<>();
            if(file!=null && file.listFiles()!=null){
                for(int i=0;i<file.listFiles().length;i++){
                    list.add(file.listFiles()[i]);
                }
            }
            Iterator<File> iterable = list.iterator();
            while (iterable.hasNext()){
                File file1 = iterable.next();
                file1.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //根据下标删除
    public void deleteByIndex(int index){
        try{
            File file = new File(getMusicParentPath());
            if(file!=null && file.listFiles()!=null){
                for(int i=0;i<file.listFiles().length;i++){
                    if(index == i){
                        file.listFiles()[i].delete();
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 转换文件大小  根据大小自动转换 带单位
     * @param fileS
     * @return
     *
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        try{
            String wrongSize = "0B";
            if (fileS == 0) {
                return wrongSize;
            }
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "GB";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileSizeString;
    }



    /**
     * 转换文件大小  根据大小自动转换 不带单位
     * @param fileS
     * @return
     *
     */
    public static String formatFileSizeNoUnit(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        try{
            String wrongSize = "0B";
            if (fileS == 0) {
                return wrongSize;
            }
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS);
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024);
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576);
            } else {
                fileSizeString = df.format((double) fileS / 1073741824);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileSizeString;
    }









    //获取文件大小 返回单位KB
    public static String formatFileSizeB(long fileS){
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        try{
            String wrongSize = "0";
            if (fileS == 0) {
                return wrongSize;
            }
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileSizeString;
    }

    //获取文件大小 返回单位MB
    public static String formatFileSizeMB(long fileS){
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        try{
            String wrongSize = "0";
            if (fileS == 0) {
                return wrongSize;
            }
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileSizeString;
    }

    //获取文件大小 返回单位MB
    public static String formatFileSizeGB(long fileS){
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        try{
            String wrongSize = "0";
            if (fileS == 0) {
                return wrongSize;
            }
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileSizeString;
    }

    //获取文件大小 返回单位B
    public long getFileSize(int index){
        long size = 0;
        try{
            String aa = getMusicParentPath();
            File file = new File(aa);
            if(file!=null && file.listFiles()!=null && file.listFiles().length > index){
                size = file.listFiles()[index].length();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }


    //先进先出，如果本地已存在5个文件，先删除最旧的文件，再进行下载新的
    private void formatFileLength(){
        synchronized (this){
            if(downQueue.size() == 0){
                downIndex = 0;
                return;
            }
            File file = new File(getMusicParentPath());
            if(file!=null){
                if(!file.exists()){
                    file.mkdir();
                }
                File[] files = file.listFiles();
                if(files!=null){
                    if(files!=null && files.length >= 5){
                        Arrays.sort(files, new Comparator<File>(){
                            public int compare(File f1, File f2)
                            {
                                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                            } });
                        Log.i("=============","=============:即将删除的文件："+files[0].getAbsolutePath());
                        files[0].delete();
                    }
                }
                nextDown();
            }
        }
    }

    /**
     * 所有文件大小
     * @return
     */
    public double getFileTotalSize(){
        String distance = "";
        long size = 0;
        try{
            String aa = getMusicParentPath();
            File file = new File(aa);
            if(file!=null && file.listFiles()!=null && file.listFiles().length > 0){
                for(int i=0;i<file.listFiles().length;i++){
                    size += file.listFiles()[i].length();
                }
            }
            String cc = formatFileSizeMB2(size);
            BigDecimal bd = new BigDecimal(cc);
            distance = bd.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Double.valueOf(distance+"");
    }


    //获取文件大小 返回单位MB
    public static String formatFileSizeMB2(long fileS){
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        try{
            String wrongSize = "0";
            if (fileS == 0) {
                return wrongSize;
            }
            fileSizeString = df.format((double) fileS / 1048576);
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileSizeString;
    }








}
