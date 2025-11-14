package com.yunbao.common.bean;

import java.util.List;

public class GameLolMarchDateBean {
    private String date;
    private List<GameLolMatchBean> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<GameLolMatchBean> getList() {
        return list;
    }

    public void setList(List<GameLolMatchBean> list) {
        this.list = list;
    }
}
