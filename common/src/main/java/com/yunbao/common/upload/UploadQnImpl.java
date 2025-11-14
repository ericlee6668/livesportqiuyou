package com.yunbao.common.upload;

import android.content.Context;

import com.obs.services.ObsClient;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.HuaWeiCloudBean;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.utils.DateFormatUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.StringUtil;

import java.io.File;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by cxf on 2019/4/16.
 * 七牛上传文件
 */

public class UploadQnImpl implements UploadStrategy {

    private static final String TAG = "UploadQnImpl";
    private Context mContext;
    private List<UploadBean> mList;
    private int mIndex;
    private boolean mNeedCompress;
    private UploadCallback mUploadCallback;
    private UploadManager mUploadManager;
    private UpCompletionHandler mCompletionHandler;//上传回调
    private Luban.Builder mLubanBuilder;

    public UploadQnImpl(Context context) {
        mContext = context;
    }

    @Override
    public void upload(List<UploadBean> list, boolean needCompress, UploadCallback callback) {
        if (callback == null) {
            return;
        }
        if (list == null || list.size() == 0) {
            callback.onFinish(list, false);
            return;
        }
        boolean hasFile = false;
        for (UploadBean bean : list) {
            if (bean.getOriginFile() != null) {
                hasFile = true;
                break;
            }
        }
        if (!hasFile) {
            callback.onFinish(list, true);
            return;
        }
        mList = list;
        mNeedCompress = needCompress;
        mUploadCallback = callback;
        mIndex = 0;

        uploadNext();


    }

    @Override
    public void cancelUpload() {
        CommonHttpUtil.cancel(CommonHttpConsts.GET_UPLOAD_QI_NIU_TOKEN);
        if (mList != null) {
            mList.clear();
        }
        mUploadCallback = null;
    }


    private void uploadNext() {
        UploadBean bean = null;
        while (mIndex < mList.size() && (bean = mList.get(mIndex)).getOriginFile() == null) {
            mIndex++;
        }
        if (mIndex >= mList.size()) {
            if (mUploadCallback != null) {
                mUploadCallback.onFinish(mList, true);
            }
            return;
        }

        bean.setRemoteAccessUrl("image/dynamic/" + DateFormatUtil.getCurrentDate() + "/" + bean.getOriginFile().getName());
        if (bean.getType() == UploadBean.IMG) {
            bean.setRemoteFileName(StringUtil.contact(StringUtil.generateFileName(), ".jpg"));
        } else if (bean.getType() == UploadBean.VIDEO) {
            bean.setRemoteFileName(StringUtil.contact(StringUtil.generateFileName(), ".mp4"));
        } else if (bean.getType() == UploadBean.VOICE) {
            bean.setRemoteFileName(StringUtil.contact(StringUtil.generateFileName(), ".m4a"));
        }
        if (bean.getType() == UploadBean.IMG && mNeedCompress) {
            if (mLubanBuilder == null) {
                mLubanBuilder = Luban.with(mContext)
                        .ignoreBy(100)//100k以下不压缩
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                UploadBean uploadBean = mList.get(mIndex);
                                uploadBean.setCompressFile(file);
                                uploadTask(uploadBean);
                            }

                            @Override
                            public void onError(Throwable e) {
                                uploadTask(mList.get(mIndex));
                            }
                        });
            }
            mLubanBuilder.load(bean.getOriginFile()).launch();
        } else {
            uploadTask(bean);
        }

    }


    private void uploadTask(UploadBean bean) {
        new Thread(new MyNetWorkTask(bean)).start();
    }

    private class MyNetWorkTask implements Runnable {

        private UploadBean bean;

        public MyNetWorkTask(UploadBean bean) {
            this.bean = bean;
        }

        @Override
        public void run() {
            uploadImage(bean);
        }
    }

    private void uploadImage(UploadBean bean) {
        try {
            HuaWeiCloudBean huaWei = CommonAppConfig.getInstance().getHuaWei();
            String endPoint = huaWei.getEndPoint();
            String accessKey = huaWei.getAccessKey();
            String secretKey = huaWei.getSecretKey();
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(accessKey, secretKey, endPoint);
            PutObjectRequest request = new PutObjectRequest(huaWei.getBucket(), bean.getRemoteAccessUrl());
            request.setFile(mNeedCompress ? bean.getCompressFile() : bean.getOriginFile()); // localfile为上传的本地文件路径，需要指定到具体的文件名
            request.setProgressListener(new ProgressListener() {

                @Override
                public void progressChanged(ProgressStatus status) {
                    // 获取上传平均速率
                    L.e("PutObject", "AverageSpeed:" + status.getAverageSpeed());
                    // 获取上传进度百分比
                    L.e("PutObject", "TransferPercentage:" + status.getTransferPercentage());
                    if (status.getTransferPercentage() == 100) {
                        L.e("PutObject", "第:" + (mIndex + 1) + "张图片" + mList.get(mIndex).getRemoteAccessUrl() + "上传成功");
                        mList.get(mIndex).setSuccess(true);
                        mIndex++;
                        if (mIndex < mList.size()) {
                            uploadNext();
                        } else {
                            if (mUploadCallback != null) {
                                mUploadCallback.onFinish(mList, true);
                            }
                        }
                    }
                }
            });
            // 每上传1MB数据反馈上传进度
            request.setProgressInterval(1024 * 1024L);
            obsClient.putObject(request);
        } catch (Exception e) {
            e.printStackTrace();
            if (mUploadCallback != null) {
                mUploadCallback.onFinish(mList, false);
            }
        }
    }

}
