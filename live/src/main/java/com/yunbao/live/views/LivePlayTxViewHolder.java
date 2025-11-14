package com.yunbao.live.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.downmumusicutils.DownUtils;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveGameActivity;
import com.yunbao.live.activity.LiveSportsActivity;
import com.yunbao.live.adapter.CoverFlowAdapter;
import com.yunbao.live.bean.LiveWaterMarkBean;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by cxf on 2018/10/10.
 * 直播间播放器  腾讯播放器
 */

public class LivePlayTxViewHolder extends LiveRoomPlayViewHolder implements ITXLivePlayListener {

    private static final String TAG = "LiveTxPlayViewHolder";
    private ViewGroup mRoot;
    private ViewGroup mSmallContainer;
    private ViewGroup mLeftContainer;
    private ViewGroup mRightContainer;
    private ViewGroup mPkContainer;
    private TXCloudVideoView mVideoView;
    private ImageView mCover;
    private TXLivePlayer mPlayer;
    private boolean mPaused;//是否切后台了
    private boolean mStarted;//是否开始了播放
    private boolean mEnd;//是否结束了播放
    private boolean mPausedPlay = false;//是否被动暂停了播放
    private long mLastStopTime = 0;
    private boolean mChangeToLeft = true;
    private boolean mChangeToAnchorLinkMic;
    private String mUrl;
    private int mPlayType;
    private HttpCallback mGetTxLinkMicAccUrlCallback;
    private Handler mHandler;
    private int mVideoLastProgress;
    private ImageView mIvFullScreen;
    private ImageView mLivePause, mLeftPlayIcon;
    private List<MainRecommendBean> mNoAnchorCoverList;
    private RecyclerCoverFlow mRecyclerCoverFlow;
    private String mLiveId;
    private ImageView mIvLoading;
    private View mLoadingView;
    private ConstraintLayout mLiveCclFullScreenView;
    private long mDelayTime = 600;
    private List<LiveWaterMarkBean> mLiveWaterMarkBeans;
    private RelativeLayout mRlWaterMark;
    private ImageView mIvWaterMark1;
    private ImageView mIvWaterMark2;
    private ImageView mIvWaterMark3;
    private ImageView mIvWaterMark4;
    private boolean isFirstLoad = true;
    private CoverFlowAdapter coverFlowAdapter;
    private LiveGameActivity.MediaConnect mMediaConnect=null;//控制播放器 背景音乐的播放器类
    private String musicPath="";//背景音乐当前播放路径
    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public LiveGameActivity.MediaConnect getmMediaConnect() {
        return mMediaConnect;
    }

    public void setmMediaConnect(LiveGameActivity.MediaConnect mMediaConnect) {
        this.mMediaConnect = mMediaConnect;
    }


    public LivePlayTxViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_play_tx;
    }

    public void setLiveId(String id) {
        mLiveId = id;
    }

    @Override
    public TXLivePlayer getTXLivePlayer() {
        return mPlayer;
    }

    @Override
    public void init() {
        mRoot = (ViewGroup) findViewById(R.id.root);
        mSmallContainer = (ViewGroup) findViewById(R.id.small_container);
        mLeftContainer = (ViewGroup) findViewById(R.id.left_container);
        mRightContainer = (ViewGroup) findViewById(R.id.right_container);
        mPkContainer = (ViewGroup) findViewById(R.id.pk_container);
        mIvFullScreen = findViewById(R.id.live_iv_full_screen);
        mLoadingView = findViewById(R.id.ll_loading);
        mIvLoading = findViewById(R.id.iv_loading);
        mLiveCclFullScreenView = findViewById(R.id.live_ccl_full_screen_view);
        mCover = (ImageView) findViewById(R.id.cover);
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLivePause = findViewById(R.id.live_iv_pause);
        mLivePause.setVisibility(View.GONE);
        mCover.setVisibility(View.GONE);
        mLeftPlayIcon = findViewById(R.id.live_iv_left_play_icon);
        mRlWaterMark = findViewById(R.id.rl_water_mark);
        mIvWaterMark1 = findViewById(R.id.iv_water_mark1);
        mIvWaterMark2 = findViewById(R.id.iv_water_mark2);
        mIvWaterMark3 = findViewById(R.id.iv_water_mark3);
        mIvWaterMark4 = findViewById(R.id.iv_water_mark4);
        mLivePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time = new Date().getTime() - mLastStopTime;
                if (time > mDelayTime && mPausedPlay) { // 防抖
                    videoStart();
                    mLastStopTime = new Date().getTime();
                    if (mMediaConnect==null|| mMediaConnect.getMediaBinder()==null){
                        return;
                    }
                    if(TextUtils.isEmpty(musicPath)){
                        return;
                    }
//                    mMediaConnect.getMediaBinder().play(musicPath);
                    mMediaConnect.getMediaBinder().resumePlay();
                }
            }
        });

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLiveCclFullScreenView.getVisibility() == View.GONE) {
                    mLiveCclFullScreenView.setVisibility(View.VISIBLE);
                    hideBar(false);
                    setBarVisibility();
                } else {
                    long time = new Date().getTime() - mLastStopTime;
                    if (time > mDelayTime) {
                        videoStop();
                        mLastStopTime = new Date().getTime();
                    }

                }
            }
        });
        mLeftPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPausedPlay) {
                    videoStart();
                    if (mMediaConnect==null|| mMediaConnect.getMediaBinder()==null){
                        return;
                    }
//                    String path = DownUtils.getInstance().getMusicLocalPath();
                    if(TextUtils.isEmpty(musicPath)){
                        return;
                    }
//                    mMediaConnect.getMediaBinder().play(musicPath);
                    mMediaConnect.getMediaBinder().resumePlay();
                } else {
                    videoStop();
                    mLastStopTime = new Date().getTime();
                    if (mMediaConnect==null|| mMediaConnect.getMediaBinder()==null){
                        return;
                    }
                    mMediaConnect.getMediaBinder().pausePlay();
                }
            }
        });
        mRoot.setClickable(false);
        mLeftPlayIcon.setClickable(false);
        mPlayer = new TXLivePlayer(mContext);
        mPlayer.setPlayListener(this);
        setPlayerView();
        mPlayer.enableHardwareDecode(true);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);//自适应
//        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);//铺满
        TXLivePlayConfig playConfig = new TXLivePlayConfig();

        //自动模式
//        playConfig.setAutoAdjustCacheTime(true);
//        playConfig.setMinAutoAdjustCacheTime(1);
//        playConfig.setMaxAutoAdjustCacheTime(5);

         //极速模式
//        playConfig.setAutoAdjustCacheTime(true);
//        playConfig.setMinAutoAdjustCacheTime(1);
//        playConfig.setMaxAutoAdjustCacheTime(1);

        //流畅模式
        playConfig.setAutoAdjustCacheTime(true);
//        playConfig.setCacheTime(5);
        playConfig.setEnableNearestIP(true);
//        playConfig.setCacheFolderPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
//        playConfig.setHeaders(CommonAppConfig.HEADER);
        mPlayer.setConfig(playConfig);
        mIvFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof LiveGameActivity) {
                    ((LiveGameActivity) mContext).showFullScreen();
                }
                if (mContext instanceof LiveSportsActivity) {
                    ((LiveSportsActivity) mContext).showFullScreen();
                }
            }
        });
        Glide.with(mContext).asGif().load(R.mipmap.live_loading).into(mIvLoading);

        initNoAnchor();
        getWaterMark();
        requestHot();
    }
    //隐藏标题栏
    private void hideBar(boolean isHilde) {
        if(isHilde){
            if (mContext instanceof LiveGameActivity) {
                ((LiveGameActivity) mContext).mRlTitle.setVisibility(View.GONE);
            }
            if (mContext instanceof LiveSportsActivity) {
                ((LiveSportsActivity) mContext).mRlTitle.setVisibility(View.GONE);
            }
        }else{
            if (mContext instanceof LiveGameActivity) {
                ((LiveGameActivity) mContext).mRlTitle.setVisibility(View.VISIBLE);
            }
            if (mContext instanceof LiveSportsActivity) {
                ((LiveSportsActivity) mContext).mRlTitle.setVisibility(View.VISIBLE);
            }
        }

    }

    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_water_mark_default)
            .error(R.mipmap.icon_water_mark_default);

    private void getWaterMark() {
        LiveHttpUtil.getWatermark(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                try {
                    if (code == 0) {
                        List<LiveWaterMarkBean> liveWaterMarkBeans = JSON.parseArray(Arrays.toString(info), LiveWaterMarkBean.class);
                        if (liveWaterMarkBeans != null && !liveWaterMarkBeans.isEmpty()) {
                            mLiveWaterMarkBeans = liveWaterMarkBeans;
                            setWaterMark();
                        }
                    }
                } catch (Exception e) {
                    AppLog.e("", e.toString());
                }
            }
        });
    }

    /**
     * 设置水印
     */
    private void setWaterMark() {
        if (mLiveWaterMarkBeans != null) {
            hideMark();
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int widthPixels = dm.widthPixels;
            int heightPixels = dm.heightPixels;
//            Log.e(TAG, "mLiveWaterMarkBeans=" + mLiveWaterMarkBeans.size() + ",mChangeToLeft=" + mChangeToLeft + ",widthPixels = " + dm.widthPixels + ",heightPixels = " + dm.heightPixels);
            for (int i = 0; i < mLiveWaterMarkBeans.size(); i++) {
                LiveWaterMarkBean mLiveWaterMarkBean = mLiveWaterMarkBeans.get(i);
                String xpos = mLiveWaterMarkBean.getXpos();
                String ypos = mLiveWaterMarkBean.getYpos();
                String width = mLiveWaterMarkBean.getWidth();
                String height = mLiveWaterMarkBean.getHeight();
                ImageView imageView = null;
                if (mLiveWaterMarkBean.getPosition() == 1) {
                    imageView = mIvWaterMark1;
                    mIvWaterMark1.setVisibility(View.VISIBLE);
                }
                if (mLiveWaterMarkBean.getPosition() == 2) {
                    imageView = mIvWaterMark2;
                    mIvWaterMark2.setVisibility(View.VISIBLE);
                }
                if (mLiveWaterMarkBean.getPosition() == 3) {
                    imageView = mIvWaterMark3;
                    mIvWaterMark3.setVisibility(View.VISIBLE);
                }
                if (mLiveWaterMarkBean.getPosition() == 4) {
                    imageView = mIvWaterMark4;
                    mIvWaterMark4.setVisibility(View.VISIBLE);
                }

                if (imageView == null) {
                    return;
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                if (isFirstLoad) {
                    layoutParams.width = (int) (widthPixels * (Double.parseDouble(width) / 100));
                    layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//                    layoutParams.height = DpUtil.dp2px((int) (250 * (Double.parseDouble(height) / 100)));
                    layoutParams.leftMargin = (int) (widthPixels * Double.parseDouble(xpos) / 100);
                    layoutParams.topMargin = mChangeToLeft ? (int) (DpUtil.dp2px(250) * Double.parseDouble(ypos) / 100)
                            : (int) (widthPixels * Double.parseDouble(ypos) / 100);
                } else {
                    layoutParams.width = mChangeToLeft ? (int) (heightPixels * (Double.parseDouble(width) / 100))
                            : (int) (heightPixels * (Double.parseDouble(width) / 100));
                    layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;;
//                    layoutParams.height = !mChangeToLeft ? (int) (widthPixels * (Double.parseDouble(height) / 100)) : DpUtil.dp2px((int) (250 * (Double.parseDouble(height) / 100)));
                    layoutParams.leftMargin = mChangeToLeft ? (int) (heightPixels * Double.parseDouble(xpos) / 100)
                            : (int) (heightPixels * Double.parseDouble(xpos) / 100);
                    layoutParams.topMargin = mChangeToLeft ? (int) (DpUtil.dp2px(250) * Double.parseDouble(ypos) / 100)
                            : (int) (widthPixels * Double.parseDouble(ypos) / 100);
                }
                imageView.setLayoutParams(layoutParams);

                Glide.with(mContext)
                        .load(mLiveWaterMarkBean.getImage())
                        .apply(options)
                        .into(imageView);
            }
            isFirstLoad = false;
        }
    }

    private void hideMark() {
        mIvWaterMark1.setVisibility(View.GONE);
        mIvWaterMark2.setVisibility(View.GONE);
        mIvWaterMark3.setVisibility(View.GONE);
        mIvWaterMark4.setVisibility(View.GONE);
    }

    private void setBarVisibility() {
        if (mHandler == null) {
            mHandler = new Handler();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLiveCclFullScreenView.setVisibility(View.GONE);
//              setAnimation(0, 1, mLiveCclFullScreenView);
                hideBar(true);

            }
        }, 5000);
    }

    private void initNoAnchor() {
        mRecyclerCoverFlow = findViewById(R.id.live_recycler_no_anchor);
        if (mNoAnchorCoverList == null) {
            mNoAnchorCoverList = new ArrayList<>();
        }
        MainRecommendBean defaultInfo = new MainRecommendBean();
        defaultInfo.setNetError(false);
        mNoAnchorCoverList.add(defaultInfo);

        coverFlowAdapter = new CoverFlowAdapter(mContext, mNoAnchorCoverList);
        mRecyclerCoverFlow.setAlphaItem(true);
        mRecyclerCoverFlow.setAdapter(coverFlowAdapter);
        coverFlowAdapter.setOnCoverClickListener(new CoverFlowAdapter.OnCoverClickListener() {
            @Override
            public void onClick() {
                if(!NetworkUtil.isNetworkAvailable(mContext)){
                    return;
                }

                mStarted =false;
                mPaused = false;
                onPlayStartUiChange();
                play(mUrl);
            }
        });
    }

    private void requestHot() {
        LiveHttpUtil.getHot(1, 6, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<MainRecommendBean> mainRecommendBeanList = JSON.parseArray(Arrays.toString(info), MainRecommendBean.class);
                    if (mainRecommendBeanList != null && mainRecommendBeanList.size() > 0) {
                        if (mLiveId != null) {
                            // 去除自己和没有封面的，多几条推荐直播
                            for (int i = 0; i < mainRecommendBeanList.size(); i++) {
                                if (!mLiveId.equals(mainRecommendBeanList.get(i).getUid()) && !mainRecommendBeanList.get(i).getThumb().isEmpty()) {
                                    mNoAnchorCoverList.add(mainRecommendBeanList.get(i));
                                }
                            }
                            coverFlowAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void videoStop() {
        pausePlay();
        mLivePause.setVisibility(View.VISIBLE);
        mLeftPlayIcon.setImageResource(R.mipmap.icon_live_start);
    }

    private void videoStart() {
        resumePlay();
        mLivePause.setVisibility(View.GONE);
        mLeftPlayIcon.setImageResource(R.mipmap.icon_live_stop);
    }


    //    private boolean isStop = false; //需要手动设置暂停
    @Override
    public void setPlayerView() {
        mPlayer.setPlayerView(mVideoView);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);

    }


    @Override
    public void onPlayEvent(int e, Bundle bundle) {
        if (mEnd) {
            return;
        }
        switch (e) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN://播放开始
                onPlayStartUiChange();
                setBarVisibility();
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:

                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME://第一帧
                hideCover();
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END://播放结束
                replay();
                break;
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION://获取视频宽高
                if (mChangeToLeft || mChangeToAnchorLinkMic) {
                    return;
                }
                float width = bundle.getInt("EVT_PARAM1", 0);
                float height = bundle.getInt("EVT_PARAM2", 0);
//                L.e(TAG, "流---width----->" + width);
//                L.e(TAG, "流---height----->" + height);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
                int targetH = 0;
                if (width >= height) {//横屏 9:16=0.5625
                    targetH = (int) (mVideoView.getWidth() / width * height);
                } else {
//                    targetH = ViewGroup.LayoutParams.MATCH_PARENT;
                    targetH = DpUtil.dp2px(264);
//                    targetH = DpUtil.dp2px(250);
                }
                if (targetH != params.height) {
                    params.height = targetH;
                    params.gravity = Gravity.CENTER;
                    mVideoView.requestLayout();
                }
//                View txVideoView = mVideoView.getVideoView();
//                FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) txVideoView.getLayoutParams();
//                p.height = targetH;
//                txVideoView.requestLayout();
                break;
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT://播放失败
                showCover(!NetworkUtil.isNetworkAvailable(mContext));
                break;
            case TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND:
//                ToastUtil.show(WordUtil.getString(R.string.live_play_error));
                showCover(false);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                Log.e("ttt","加载中...");
                int progress = bundle.getInt("EVT_PLAY_PROGRESS_MS");
                if (mVideoLastProgress == progress) {
                    replay();
                } else {
                    mVideoLastProgress = progress;
                }
                break;
        }
    }
    private void onPlayStartUiChange(){
        mRecyclerCoverFlow.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mRlWaterMark.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        mRoot.setClickable(true);
        mLeftPlayIcon.setClickable(true);
        mRoot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
    }

    public void showCover(boolean isNetError) {
        findViewById(R.id.live_ccl_full_screen_view).setVisibility(View.GONE);
        hideCover();
        mVideoView.setVisibility(View.GONE);
        mPaused = true;
        mRoot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.live_error_bg));
        mRecyclerCoverFlow.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mRlWaterMark.setVisibility(View.INVISIBLE);
        mPlayer.stopPlay(true);
        if(isNetError){
            mNoAnchorCoverList.get(0).setNetError(true);
            coverFlowAdapter.notifyDataSetChanged();
        }else {
            if (mContext instanceof LiveGameActivity) {
                ((LiveGameActivity) mContext).mRlTitle.setVisibility(View.VISIBLE);
//                ((LiveGameActivity) mContext).setInPutEnable(false);
            }
            if (mContext instanceof LiveSportsActivity) {
                ((LiveSportsActivity) mContext).mRlTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        L.e("Current status, CPU:" + bundle.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                ", RES:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                ", SPEED:" + bundle.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                ", FPS:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                ", ARA:" + bundle.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                ", SERVER_IP:" + bundle.getString(TXLiveConstants.NET_STATUS_SERVER_IP) +
                ", VRA:" + bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
    }


    @Override
    public void hideCover() {
        if (mCover != null) {
            mCover.animate().alpha(0).setDuration(500).start();
        }
    }

    @Override
    public void setCover(String coverUrl) {
        if (mCover != null) {
            ImgLoader.displayBlur(mContext, coverUrl, mCover);
        }
    }

    /**
     * 循环播放
     */
    private void replay() {
        if (mStarted && mPlayer != null) {
            mPlayer.seek(0);
            mPlayer.resume();
        }
    }


    /**
     * 暂停播放
     */
    @Override
    public void pausePlay() {
        if (!mPausedPlay) {
            mPausedPlay = true;
            if (!mPaused) {
                if (mCover != null) {
                    if (mPlayer != null) {
                        mPlayer.snapshot(new TXLivePlayer.ITXSnapshotListener() {
                            @Override
                            public void onSnapshot(Bitmap bitmap) {
                                mCover.setImageBitmap(bitmap);
                                mPlayer.setMute(true);
                                mCover.setAlpha(1f);
                                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mCover.getLayoutParams();
                                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                                layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                                mCover.setLayoutParams(layoutParams);
                                if (mCover.getVisibility() != View.VISIBLE) {
                                    mCover.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 暂停播放后恢复
     */
    @Override
    public void resumePlay() {
        if (mPausedPlay) {
            mPausedPlay = false;
            if (!mPaused) {
                if (mPlayer != null) {
                    mPlayer.setMute(false);
                }
            }
            hideCover();
        }
    }

    /**
     * 开始播放
     *
     * @param url 流地址
     */
    @Override
    public void play(String url) {
        if (TextUtils.isEmpty(url)){
            return;
        }
        mUrl = url;
        if(!NetworkUtil.isNetworkAvailable(mContext)){
            showCover(true);
            return;
        }

        int playType = -1;
        if (url.startsWith("rtmp://")) {
            playType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        } else if (url.contains(".flv")) {
            playType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
        } else if (url.contains(".m3u8")) {
            playType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
        } else if (url.contains(".mp4")) {
            playType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
        }
        if (playType == -1) {
            showCover(false);
//            ToastUtil.show(R.string.live_play_error_2);
            return;
        }
        if (mPlayer != null) {
            int result = mPlayer.startPlay(url, playType);
            if (result == 0) {
                mStarted = true;
                mUrl = url;
                mPlayType = playType;
            }
        }
        L.e(TAG, "play----url--->" + url);
    }

    @Override
    public void stopPlay() {
        mChangeToLeft = false;
        mChangeToAnchorLinkMic = false;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mCover != null) {
            mCover.setAlpha(1f);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mCover.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            mCover.setLayoutParams(layoutParams);
            if (mCover.getVisibility() != View.VISIBLE) {
                mCover.setVisibility(View.VISIBLE);
            }
        }
        stopPlay2();
    }

    @Override
    public void stopPlay2() {
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
        }
    }

    @Override
    public void release() {
        mEnd = true;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
        LiveHttpUtil.cancel(LiveHttpConsts.GET_TX_LINK_MIC_ACC_URL);
        if (mPlayer != null) {
            mPlayer.stopPlay(false);
            mPlayer.setPlayListener(null);
        }
        mPlayer = null;
        L.e(TAG, "release------->");
    }


    @Override
    public ViewGroup getSmallContainer() {
        return mSmallContainer;
    }


    @Override
    public ViewGroup getRightContainer() {
        return mRightContainer;
    }

    @Override
    public ViewGroup getPkContainer() {
        return mPkContainer;
    }

    @Override
    public void changeToLeft() {
        mChangeToLeft = true;
        if (mVideoView != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
            params.width = mVideoView.getWidth() / 2;
            params.height = DpUtil.dp2px(250);
            mVideoView.setLayoutParams(params);

            // TODO: 2020/10/25
            if (mLiveWaterMarkBeans != null) {
                setWaterMark();
            } else {
                getWaterMark();
            }
        }
    }

    @Override
    public void changeToBig() {
        mChangeToLeft = false;
        if (mVideoView != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.topMargin = 0;
            mVideoView.setLayoutParams(params);
            // TODO: 2020/10/25
            if (mLiveWaterMarkBeans != null) {
                setWaterMark();
            } else {
                getWaterMark();
            }
        }
    }
    public void changeWaterMark(){
        if (mLiveWaterMarkBeans != null) {
            setWaterMark();
        } else {
            getWaterMark();
        }
    }

    @Override
    public void onResume() {
        if (!mPausedPlay && mPaused && mPlayer != null) {
            mPlayer.setMute(false);
        }
        mPaused = false;
    }

    @Override
    public void onPause() {
        if (!mPausedPlay && mPlayer != null) {
            mPlayer.setMute(true);
        }
        mPaused = true;
    }

    @Override
    public void onDestroy() {
        release();
    }

    /**
     * 腾讯sdk连麦时候切换低延时流
     */
    public void onLinkMicTxAccEvent(boolean linkMic) {
        if (mStarted && mPlayer != null && !TextUtils.isEmpty(mUrl)) {
            mPlayer.stopPlay(false);
            if (linkMic) {
                if (mGetTxLinkMicAccUrlCallback == null) {
                    mGetTxLinkMicAccUrlCallback = new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0 && info.length > 0) {
                                JSONObject obj = JSON.parseObject(info[0]);
                                if (obj != null) {
                                    String accUrl = obj.getString("streamUrlWithSignature");
                                    if (!TextUtils.isEmpty(accUrl) && mPlayer != null) {
                                        L.e(TAG, "低延时流----->" + accUrl);
                                        mPlayer.startPlay(accUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
                                    }
                                }
                            }
                        }
                    };
                }
                LiveHttpUtil.getTxLinkMicAccUrl(mUrl, mGetTxLinkMicAccUrlCallback);
            } else {
                mPlayer.startPlay(mUrl, mPlayType);
            }
        }
    }

    /**
     * 设置主播连麦模式
     *
     * @param anchorLinkMic
     */
    public void setAnchorLinkMic(final boolean anchorLinkMic, int delayTime) {
        if (mVideoView == null) {
            return;
        }
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mChangeToAnchorLinkMic = anchorLinkMic;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
                if (anchorLinkMic) {
                    params.height = DpUtil.dp2px(250);
                    params.topMargin = DpUtil.dp2px(130);
                    params.gravity = Gravity.TOP;
                } else {
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.topMargin = 0;
                    params.gravity = Gravity.CENTER;
                }
                mVideoView.setLayoutParams(params);
            }
        }, delayTime);

    }


    public void setPlayMode(String anyway, String isvideo) {
        AppLog.e(TAG, "anyway:" + anyway + ",isvideo:" + isvideo);
        //0表示竖屏，1表示横屏
        if ("1".equals(anyway)) {
            mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);//铺满
//            mCover.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        //是不是视频
        if ("1".equals(isvideo)) {
            mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);//自适应
            mCover.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
}
