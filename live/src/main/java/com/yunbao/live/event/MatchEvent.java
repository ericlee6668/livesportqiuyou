package com.yunbao.live.event;

import com.yunbao.common.bean.GameBasketballMatchBean;
import com.yunbao.common.bean.GameFootballMatchBean;
import com.yunbao.common.bean.GameLolMatchBean;

import java.util.ArrayList;

public class MatchEvent {
    private int type;
    private ArrayList<GameLolMatchBean> lolBeanList;
    private ArrayList<GameBasketballMatchBean> basketBallBeanList;
    private ArrayList<GameFootballMatchBean> footballBallBeanList;

    public MatchEvent(int type, ArrayList<GameLolMatchBean> lolBeanList, ArrayList<GameBasketballMatchBean> basketBallBeanList, ArrayList<GameFootballMatchBean> footballBallBeanList) {
        this.type = type;
        this.lolBeanList = lolBeanList;
        this.basketBallBeanList = basketBallBeanList;
        this.footballBallBeanList = footballBallBeanList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<GameLolMatchBean> getLolBeanList() {
        return lolBeanList;
    }

    public void setLolBeanList(ArrayList<GameLolMatchBean> lolBeanList) {
        this.lolBeanList = lolBeanList;
    }

    public ArrayList<GameBasketballMatchBean> getBasketBallBeanList() {
        return basketBallBeanList;
    }

    public void setBasketBallBeanList(ArrayList<GameBasketballMatchBean> basketBallBeanList) {
        this.basketBallBeanList = basketBallBeanList;
    }

    public ArrayList<GameFootballMatchBean> getFootballBallBeanList() {
        return footballBallBeanList;
    }

    public void setFootballBallBeanList(ArrayList<GameFootballMatchBean> footballBallBeanList) {
        this.footballBallBeanList = footballBallBeanList;
    }
}
