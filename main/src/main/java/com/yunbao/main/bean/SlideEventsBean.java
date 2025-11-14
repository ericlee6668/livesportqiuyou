package com.yunbao.main.bean;

import com.yunbao.common.bean.GameLolMatchBean;

import java.util.List;

public class SlideEventsBean {


    private List<BannerBean> slide;
    private List<GameLolMatchBean> list;

    public List<BannerBean> getSlide() {
        return slide;
    }

    public void setSlide(List<BannerBean> slide) {
        this.slide = slide;
    }

    public List<GameLolMatchBean> getList() {
        return list;
    }

    public void setList(List<GameLolMatchBean> list) {
        this.list = list;
    }

}
