package com.yunbao.common.bean;

import java.util.List;

public class MatchListBean {

    private String date;
    private String week_day_str;
    private List<MatchBean> list;

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

    public List<MatchBean> getList() {
        return list;
    }

    public void setList(List<MatchBean> list) {
        this.list = list;
    }
}
