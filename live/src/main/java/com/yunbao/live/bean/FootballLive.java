package com.yunbao.live.bean;

import com.google.gson.JsonArray;
import com.yunbao.common.bean.Incidents;
import com.yunbao.common.bean.Stats;
import com.yunbao.common.bean.TLive;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/6 21:12
 *
 * @package: com.yunbao.live.bean
 * Description：TODO
 */
public class FootballLive {
    private long id;
    private List<Stats> stats;
    private List<Incidents> incidents;
    private List<TLive> tlive;
    private JsonArray score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    public List<Incidents> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incidents> incidents) {
        this.incidents = incidents;
    }

    public List<TLive> getTlive() {
        return tlive;
    }

    public void setTlive(List<TLive> tlive) {
        this.tlive = tlive;
    }

    public JsonArray getScore() {
        return score;
    }

    public void setScore(JsonArray score) {
        this.score = score;
    }
}
