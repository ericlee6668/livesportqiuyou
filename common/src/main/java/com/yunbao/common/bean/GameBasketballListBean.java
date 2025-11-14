package com.yunbao.common.bean;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/28 20:30
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class GameBasketballListBean {

    private String date;
    private String week_day_str;
    private List<BasketballBean> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek_day_str() {
        return week_day_str;
    }

    public void setWeek_day_str(String week_day_str) {
        this.week_day_str = week_day_str;
    }

    public List<BasketballBean> getList() {
        return list;
    }

    public void setList(List<BasketballBean> list) {
        this.list = list;
    }
}
