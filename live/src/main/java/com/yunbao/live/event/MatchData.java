package com.yunbao.live.event;

import com.yunbao.live.bean.BasketPushData;
import com.yunbao.live.bean.FootballLive;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/10 12:03
 *
 * @package: com.yunbao.live.event
 * Description：TODO
 */
public class MatchData {
    private int type;
    private List<FootballLive> footballList;
    private List<BasketPushData> basketballList;

    public MatchData(int type, List<FootballLive> footballList, List<BasketPushData> basketballList) {
        this.type = type;
        this.footballList = footballList;
        this.basketballList = basketballList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<FootballLive> getFootballList() {
        return footballList;
    }

    public void setFootballList(List<FootballLive> footballList) {
        this.footballList = footballList;
    }

    public List<BasketPushData> getBasketballList() {
        return basketballList;
    }

    public void setBasketballList(List<BasketPushData> basketballList) {
        this.basketballList = basketballList;
    }
}
