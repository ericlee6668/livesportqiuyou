package com.yunbao.video.views;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.video.R;
import com.yunbao.video.bean.VideoBean;
import com.yunbao.video.http.VideoHttpConsts;
import com.yunbao.video.http.VideoHttpUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by cxf on 2018/11/30.
 * 视频播放器
 */

public class VideoPlayViewHolder extends AbsViewHolder implements ITXVodPlayListener, View.OnClickListener {

    private final static String TAG = "VideoPlayViewHolder";
    private TXCloudVideoView mTXCloudVideoView;
    private View mVideoCover;
    private TXVodPlayer mPlayer;
    private boolean mPaused;//生命周期暂停
    private boolean mClickPaused;//点击暂停
    private ActionListener mActionListener;
    private View mPlayBtn;
    private ObjectAnimator mPlayBtnAnimator;//暂停按钮的动画
    private boolean mStartPlay;
    private boolean mEndPlay;
    private VideoBean mVideoBean;
    private String mCachePath;
    private TXVodPlayConfig mTXVodPlayConfig;
    private RelativeLayout mRoot;
    private View emptyLayout;
    private OnPlayStartListener mListener;
    private String endUrl;
    String changeEnd = "";

    public VideoPlayViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_play;
    }

    @Override
    public void init() {
        mCachePath = mContext.getCacheDir().getAbsolutePath();
        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        TextView btnReload = (TextView) findViewById(R.id.btn_reload);
        emptyLayout = findViewById(R.id.empty_layout);
        mTXCloudVideoView.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mTXCloudVideoView.showLog(true);
        mTXCloudVideoView.showVideoDebugLog(0);
        mPlayer = new TXVodPlayer(mContext);
        mTXVodPlayConfig = new TXVodPlayConfig();
        mTXVodPlayConfig.setMaxBufferSize(5 * 1024);
        mTXVodPlayConfig.setProgressInterval(200);
        mTXVodPlayConfig.setHeaders(CommonAppConfig.HEADER);
        mTXVodPlayConfig.setCacheFolderPath(mContext.getCacheDir().getAbsolutePath());
        mTXVodPlayConfig.setMaxCacheItems(10);
        mPlayer.setConfig(mTXVodPlayConfig);
        mPlayer.setAutoPlay(true);
        mPlayer.setVodListener(this);
        mPlayer.setPlayerView(mTXCloudVideoView);
        mRoot = findViewById(R.id.root);
        mRoot.setOnClickListener(this);
        mVideoCover = findViewById(R.id.video_cover);
        mPlayBtn = findViewById(R.id.btn_play);
        //暂停按钮动画
        mPlayBtnAnimator = ObjectAnimator.ofPropertyValuesHolder(mPlayBtn,
                PropertyValuesHolder.ofFloat("scaleX", 4f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 4f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("alpha", 0f, 1f));
        mPlayBtnAnimator.setDuration(150);
        mPlayBtnAnimator.setInterpolator(new AccelerateInterpolator());
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoBean != null) {
                    startPlay(mVideoBean);
                }
            }
        });
    }

    /**
     * 播放器事件回调
     */
    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int e, Bundle bundle) {
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN://加载完成，开始播放的回调
                mStartPlay = true;
                if (mActionListener != null) {
                    mActionListener.onPlayBegin();
                }
                if (mListener != null) {
                    mListener.onPlayStart();
                }

                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING: //开始加载的回调
                if (mActionListener != null) {
                    mActionListener.onPlayLoading();
                }

                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END://获取到视频播放完毕的回调
                replay();
                if (!mEndPlay) {
                    mEndPlay = true;
                    if (mVideoBean != null) {
                        VideoHttpUtil.videoWatchEnd(mVideoBean.getUid(), mVideoBean.getId());
                    }
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME://获取到视频首帧回调
                if (mActionListener != null) {
                    mActionListener.onFirstFrame();
                }
                if (mPaused && mPlayer != null) {
                    mPlayer.pause();
                }
                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION://获取到视频宽高回调
                onVideoSizeChanged(bundle.getInt("EVT_PARAM1", 0), bundle.getInt("EVT_PARAM2", 0));
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                // 加载进度, 单位是秒
//                int progress = bundle.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION);


                // 播放进度, 单位是秒
                int progress2 = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);

                // 视频总长, 单位是秒
                int duration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                // 可以用于设置时长显示等等
                L.e("播放进度：" + progress2 + "---- 总时长：" + duration);
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT://播放失败
            case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
                if (mActionListener != null) {
                    mActionListener.onPlayFail();
                }
                emptyLayout.setVisibility(View.VISIBLE);
                ToastUtil.show("播放失败");
                break;
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
        L.e("Current status, CPU:" + bundle.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                ", RES:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                ", SPEED:" + bundle.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                ", FPS:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                ", ARA:" + bundle.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                ", BUFFER:" + mPlayer.getBufferDuration() +
                ", SERVER_IP:" + bundle.getString(TXLiveConstants.NET_STATUS_SERVER_IP) +
                ", VRA:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
    }

    /**
     * 获取到视频宽高回调
     */
    public void onVideoSizeChanged(float videoWidth, float videoHeight) {
        if (mTXCloudVideoView != null && videoWidth > 0 && videoHeight > 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTXCloudVideoView.getLayoutParams();
            int targetH = 0;
            if (videoWidth / videoHeight > 0.5625f) {//横屏 9:16=0.5625
                targetH = (int) (mTXCloudVideoView.getWidth() / videoWidth * videoHeight);
            } else {
                targetH = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            if (targetH != params.height) {
                params.height = targetH;
                mTXCloudVideoView.requestLayout();
            }
            if (mVideoCover != null && mVideoCover.getVisibility() == View.VISIBLE) {
                mVideoCover.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 开始播放
     */
    public void startPlay(VideoBean videoBean) {
        mStartPlay = false;
        mClickPaused = false;
        mEndPlay = false;
        mVideoBean = videoBean;
        emptyLayout.setVisibility(View.GONE);
        if (mVideoCover != null && mVideoCover.getVisibility() != View.VISIBLE) {
            mVideoCover.setVisibility(View.VISIBLE);
        }
        hidePlayBtn();
        L.e("播放视频--->" + videoBean);
        if (videoBean == null) {
            return;
        }
        String url = videoBean.getHref();
        if (TextUtils.isEmpty(url)) {
            return;
        }
//        if (mTXVodPlayConfig == null) {
//            mTXVodPlayConfig = new TXVodPlayConfig();
//            mTXVodPlayConfig.setMaxCacheItems(15);
//            mTXVodPlayConfig.setProgressInterval(200);
//            mTXVodPlayConfig.setHeaders(CommonAppConfig.HEADER);
//        }
//        if (url.endsWith(".m3u8")) {
//            mTXVodPlayConfig.setCacheFolderPath(null);
//        } else {
//            mTXVodPlayConfig.setCacheFolderPath(mCachePath);
//        }
//        mPlayer.setConfig(mTXVodPlayConfig);
        AppLog.e(TAG, "url原始==>" + url);
        if (url.endsWith(".m3u8")) {
            File file = new File(mContext.getCacheDir().getAbsolutePath(), Uri.parse(url).getLastPathSegment());
            if (file.exists()) {
                AppLog.e(TAG, "文件存在，不需要需要鉴权==>" + file.getName());
                if (mPlayer != null) {
                    String resultUrl = SpUtil.getInstance().getStringValue(url);
                    AppLog.e(TAG, "文件存在，不需要需要鉴权,resultUrl==>" + resultUrl);
                    if (!TextUtils.isEmpty(resultUrl)) {
                        mPlayer.startPlay(resultUrl);
                    } else {
                        AppLog.e(TAG, "需要鉴权==>" + url);
                     checkPermission(url);
                    }
                }
            } else {
                AppLog.e(TAG, "需要鉴权==>" + url);
                checkPermission(url);
            }
        } else {
            AppLog.e(TAG, "不需要鉴权==>>");
            if (mPlayer != null) {
                mPlayer.startPlay(url);
            }
        }
        VideoHttpUtil.videoWatchStart(videoBean.getUid(), videoBean.getId());
    }

    private String getPathUrl(String url){
        Uri uri = Uri.parse(url);
        return  url.split(uri.getScheme() + "://" + uri.getAuthority())[1];
    }
    /**
     * 鉴权
     *
     * @param url
     * @return
     */

    private void checkPermission(final String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Uri uri = Uri.parse(url);
                long timeMillis = System.currentTimeMillis() / 1000;
                String path = getPathUrl(url);
//                String path = url.split(".com")[1];
                String link = url
                        + "?sign="
                        + MD5Util.getMD5(Constants.VIDEO_PLAY_KEY + path + timeMillis)
                        + "&t=" + timeMillis;
                AppLog.e(TAG, "第一次鉴权==》" + link);
                OkGo.<File>get(link)
                        .execute(new FileCallback(mContext.getCacheDir().getAbsolutePath(), uri.getLastPathSegment()) {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                                //指定下载的路径  下载文件名
                                AppLog.e(TAG, "onSuccess，拿到文件解析");
                                File file = response.body();
                                String s = readFileContent(file);
                                String[] split = s.split("&&&&");
                                for (String value : split) {
                                    if (value.endsWith(".m3u8")) {
                                        changeEnd = value.replace(" ", "");
                                    }
                                }
                                AppLog.e(TAG, "解析到m3u8地址==>" + changeEnd);
                                String[] urlSplit = url.split("/");
                                String end = urlSplit[urlSplit.length - 1];
                                endUrl = url.replace(end, changeEnd);
                                AppLog.e(TAG, "修改拼接Url==>" + endUrl);
                                long timeMillis = System.currentTimeMillis() / 1000;

                                String path = getPathUrl(endUrl);
                                String resultUrl = endUrl
                                        + "?sign="
                                        + MD5Util.getMD5(Constants.VIDEO_PLAY_KEY + path + timeMillis)
                                        + "&t=" + timeMillis;
                                AppLog.e(TAG, "第二次鉴权==>" + resultUrl);
                                SpUtil.getInstance().setStringValue(url, resultUrl);
                                if (mPlayer != null) {
                                    mPlayer.startPlay(resultUrl);
                                }
                            }

                            @Override
                            public void onError(Response<File> response) {
                                super.onError(response);
                                AppLog.e(TAG, "鉴权出错==>" + response.getException().toString());
                                playFail();
                            }
                        });
            } else {
                if (mActionListener != null) {
                    mActionListener.onPlayFail();
                }
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playFail() {
        if (mActionListener != null) {
            mActionListener.onPlayFail();
        }
        emptyLayout.setVisibility(View.VISIBLE);
        ToastUtil.show("播放失败");
    }

    public static String readFileContent(File file) {
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr).append("&&&&");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
        }
    }

    /**
     * 循环播放
     */
    private void replay() {
        if (mPlayer != null) {
            mPlayer.seek(0);
            mPlayer.resume();
        }
    }

    public void release() {
        VideoHttpUtil.cancel(VideoHttpConsts.VIDEO_WATCH_START);
        VideoHttpUtil.cancel(VideoHttpConsts.VIDEO_WATCH_END);
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
            mPlayer.setPlayListener(null);
        }
        mPlayer = null;
        mActionListener = null;
    }

    /**
     * 生命周期暂停
     */
    public void pausePlay() {
        mPaused = true;
        if (!mClickPaused && mPlayer != null) {
            mPlayer.pause();
        }
    }

    /**
     * 生命周期恢复
     */
    public void resumePlay() {
        if (mPaused) {
            if (!mClickPaused && mPlayer != null) {
                mPlayer.resume();
            }
        }
        mPaused = false;
    }

    /**
     * 显示开始播放按钮
     */
    private void showPlayBtn() {
        if (mPlayBtn != null && mPlayBtn.getVisibility() != View.VISIBLE) {
            mPlayBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏开始播放按钮
     */
    private void hidePlayBtn() {
        if (mPlayBtn != null && mPlayBtn.getVisibility() == View.VISIBLE) {
            mPlayBtn.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 点击切换播放和暂停
     */
    private void clickTogglePlay() {
        if (!mStartPlay) {
            return;
        }
        if (mPlayer != null) {
            if (mClickPaused) {
                mPlayer.resume();
            } else {
                mPlayer.pause();
            }
        }
        mClickPaused = !mClickPaused;
        if (mClickPaused) {
            showPlayBtn();
            if (mPlayBtnAnimator != null) {
                mPlayBtnAnimator.start();
            }
        } else {
            hidePlayBtn();
        }
    }

    /**
     * 暂停
     */
    public void rootClick() {
        if (!mClickPaused) {
            mRoot.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.root) {
            clickTogglePlay();
        }
    }

    public interface ActionListener {
        void onPlayBegin();

        void onPlayLoading();

        void onFirstFrame();

        void onPlayFail();
    }

    public interface OnPlayStartListener {
        void onPlayStart();
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void setOnPlayStartListener(OnPlayStartListener actionListener) {
        mListener = actionListener;
    }

}
