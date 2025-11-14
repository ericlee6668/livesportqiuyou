package com.yunbao.common.bean;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/26 18:44
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class FootballHistory {

    private List<Incidents> incidents;
    private List<Stats> stats;
    private List<TLive> tlive;

    public List<Incidents> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incidents> incidents) {
        this.incidents = incidents;
    }

    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    public List<TLive> getTlive() {
        return tlive;
    }

    public void setTlive(List<TLive> tlive) {
        this.tlive = tlive;
    }
}
