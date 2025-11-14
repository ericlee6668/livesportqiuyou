package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.Response;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.bean.VideoClassBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.JsonBean;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.JsonUtil;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MainHomeVideoClassAdapter;
import com.yunbao.video.bean.VideoBean;
import com.yunbao.video.http.VideoHttpUtil;
import com.yunbao.video.interfaces.VideoScrollDataHelper;
import com.yunbao.video.utils.VideoStorge;
import com.yunbao.video.views.VideoPlayViewHolder;
import com.yunbao.video.views.VideoScrollViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainHomeVideoPlayHolder extends AbsMainHomeVideoViewHolder {

    private VideoScrollViewHolder mVideoScrollViewHolder;
    private VideoScrollDataHelper mVideoScrollDataHelper;
    private RecyclerView mClassRecyclerView;
    private LinearLayout mLlNoVideo;
    private MainHomeVideoClassAdapter mClassAdapter;
    private static final int ID_RECOMMEND = -1;
    private int mVideoClassId = ID_RECOMMEND;
    private boolean isVisible = true;

    public MainHomeVideoPlayHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    public void init() {
        super.init();
        mClassRecyclerView = findViewById(R.id.recyclerView_class);
        mLlNoVideo = findViewById(R.id.ll_no_video);
        mClassRecyclerView.setHasFixedSize(true);
        mClassRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        List<VideoClassBean> videoClassList = new ArrayList<>();
        videoClassList.add(new VideoClassBean(ID_RECOMMEND, WordUtil.getString(R.string.recommend), true));
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        if (configBean != null) {
            List<VideoClassBean> list = JSON.parseArray(configBean.getVideoClass(), VideoClassBean.class);
            if (list != null && list.size() > 0) {
                videoClassList.addAll(list);
            }
        }
        mClassAdapter = new MainHomeVideoClassAdapter(mContext, videoClassList);
        mClassAdapter.setOnItemClickListener(new OnItemClickListener<VideoClassBean>() {
            @Override
            public void onItemClick(VideoClassBean bean, int position) {
                if (!ClickUtil.canClick()) return;
                mVideoClassId = bean.getId();
                refreshData();
            }
        });
        mClassRecyclerView.setAdapter(mClassAdapter);
//        refreshData();
    }

    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            refreshData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 刷新视频
     */
    private void refreshData() {
        if (mVideoClassId == ID_RECOMMEND) {
            VideoHttpUtil.getHomeVideoList(1, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        resetVideo(JsonUtil.getJsonToList(Arrays.toString(info), VideoBean.class));
                    } else {
                        resetVideo(null);
                    }
                }

                @Override
                public void onError(int ret, String msg) {
                    resetVideo(null);
                }

                @Override
                public void onError(Response<JsonBean> response) {
                    resetVideo(null);
                }
            });
        } else {
            VideoHttpUtil.getHomeVideoClassList(mVideoClassId, 1, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        resetVideo(JsonUtil.getJsonToList(Arrays.toString(info), VideoBean.class));
                    } else {
                        resetVideo(null);
                    }
                }

                @Override
                public void onError(int ret, String msg) {
                    resetVideo(null);
                }

                @Override
                public void onError(Response<JsonBean> response) {
                    resetVideo(null);
                }
            });
        }
    }

    //刷新视频
    private void resetVideo(List<VideoBean> list) {
        if (list == null || list.isEmpty()) {
            mLlNoVideo.setVisibility(View.VISIBLE);

        } else {
            mLlNoVideo.setVisibility(View.GONE);
        }
        int page = 1;
        int position = 0;
        if (mVideoScrollDataHelper == null) {
            mVideoScrollDataHelper = new VideoScrollDataHelper() {

                @Override
                public void loadData(int p, HttpCallback callback) {
                    if (mVideoClassId == ID_RECOMMEND) {
                        VideoHttpUtil.getHomeVideoList(p, callback);
                    } else {
                        VideoHttpUtil.getHomeVideoClassList(mVideoClassId, p, callback);
                    }
                }
            };
        }
        VideoStorge.getInstance().put(Constants.VIDEO_HOME, list);
        VideoStorge.getInstance().putDataHelper(Constants.VIDEO_HOME, mVideoScrollDataHelper);
        if (mVideoScrollViewHolder != null) {
            mVideoScrollViewHolder.release();
            mVideoScrollViewHolder = null;
        }
        mVideoScrollViewHolder = new VideoScrollViewHolder(mContext, (ViewGroup) findViewById(R.id.container), position, Constants.VIDEO_HOME, page, Constants.VIDEO_MAIN_PLAY_IN);
        mVideoScrollViewHolder.addToParent();
        mVideoScrollViewHolder.subscribeActivityLifeCycle();
        if (list != null) {
            mVideoScrollViewHolder.getRefreshLayout().setEnabled(list.size() > 0);
        }
        VideoPlayViewHolder holder = mVideoScrollViewHolder.getVideoPlayViewHolder();
        if (holder != null) {
            holder.setOnPlayStartListener(new VideoPlayViewHolder.OnPlayStartListener() {
                @Override
                public void onPlayStart() {
                    if (!isVisible) {
                        stopPlay();
                    }
                }
            });
        }
    }

    public void startPlay() {
        if (!isFirstLoadData() && mVideoScrollViewHolder != null) {
            mVideoScrollViewHolder.onReStart();
            mVideoScrollViewHolder.onResume();
        }
    }

    public void stopPlay() {
//        if (mVideoScrollViewHolder != null) {
//            mVideoScrollViewHolder.onPause();
//            mVideoScrollViewHolder.onStop();
//        }

        if (mVideoScrollViewHolder != null) {
            mVideoScrollViewHolder.stopClick();
        }
    }


    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
        L.e("VideoPlayActivity------->onDestroy");
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
