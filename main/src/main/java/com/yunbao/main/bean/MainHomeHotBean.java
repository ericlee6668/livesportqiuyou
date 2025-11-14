package com.yunbao.main.bean;

import com.yunbao.common.bean.MainRecommendBean;

import java.util.List;

/**
 * @author Juwan
 * @date 2020/10/1
 * Description:
 */
public class MainHomeHotBean {

    private List<SlideEventsBean> slide;
    private List<MainRecommendBean> list;

    public List<SlideEventsBean> getSlide() {
        return slide;
    }

    public void setSlide(List<SlideEventsBean> slide) {
        this.slide = slide;
    }

    public List<MainRecommendBean> getList() {
        return list;
    }

    public void setList(List<MainRecommendBean> list) {
        this.list = list;
    }
}
