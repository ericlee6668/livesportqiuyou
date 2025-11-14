package com.yunbao.common.bean;

import java.util.List;

public class GameLOLMatchListBean {

    private List<GameLolMatchBean> today;
    private List<GameLolMatchBean> tomorrow;
    private List<GameLolMatchBean> over;

    public List<GameLolMatchBean> getToday() {
        return today;
    }

    public void setToday(List<GameLolMatchBean> today) {
        this.today = today;
    }

    public List<GameLolMatchBean> getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(List<GameLolMatchBean> tomorrow) {
        this.tomorrow = tomorrow;
    }

    public List<GameLolMatchBean> getOver() {
        return over;
    }

    public void setOver(List<GameLolMatchBean> over) {
        this.over = over;
    }

}
