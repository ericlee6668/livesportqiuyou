package com.yunbao.common.utils.downmumusicutils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

public class MediaService extends Service {

    private MediaPlayer mediaPlayer;
    private MediaBinder binder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        initMedia();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MediaBinder();
        }
    }

    private void initMedia() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    public class MediaBinder extends Binder {

        String nowPath = "";
        int repeatCount = 1;

        public void play(String path) {
            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        stop();
                    }
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                        }
                    });
                    mediaPlayer.prepareAsync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //完全停止播放
        public synchronized void stop() {
            try {
                if (mediaPlayer != null) {
//                    if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //暂停播放
        public synchronized void pausePlay() {
            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //继续播放
        public synchronized void resumePlay() {
            try {
                if (mediaPlayer != null) {
//                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //如果未播放过当前音频，默认播放；
        // 如果当前播放的已经是该音乐，将被暂停；
        // 如果当前播放的已经是该音乐，并且为暂停状态，则默认继续播放；
        public void singlePlay(String path) {
            if (mediaPlayer != null) {
                if(TextUtils.isEmpty(path)){
                    return;
                }
                if(nowPath.equals(path)){
                    if (mediaPlayer.isPlaying()) {
                        pausePlay();
                    }else{
                        resumePlay();
                    }
                }else{
                    stop();
                    play(path);
                }
            }
        }

        //count=-1时循环播放
        //不为-1时指定次数播放
        public void countPlay(int count,String path){
            try {
                repeatCount = count;
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        stop();
                    }
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if(repeatCount == -1){
                                mp.start();
                            }else{
                                repeatCount--;
                                if(repeatCount<=0){
                                    stop();
                                }else{
                                    mp.start();
                                }
                            }
                        }
                    });
                    mediaPlayer.prepareAsync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
