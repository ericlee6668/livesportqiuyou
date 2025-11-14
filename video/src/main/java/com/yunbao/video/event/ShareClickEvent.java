package com.yunbao.video.event;

import com.yunbao.video.bean.VideoBean;

public class ShareClickEvent {
    private VideoBean mVideoBean;
    public ShareClickEvent(VideoBean mVideoBean) {
        this.mVideoBean = mVideoBean;
    }

    public VideoBean getmVideoBean() {
        return mVideoBean;
    }

    public void setmVideoBean(VideoBean mVideoBean) {
        this.mVideoBean = mVideoBean;
    }
}
