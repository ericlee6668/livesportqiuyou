package com.yunbao.common.bean;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/25 20:15
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class GameIntelligence {
    private Intelligence good;
    private Intelligence bad;
    private List<List<String>> neutral;

    public Intelligence getGood() {
        return good;
    }

    public void setGood(Intelligence good) {
        this.good = good;
    }

    public Intelligence getBad() {
        return bad;
    }

    public void setBad(Intelligence bad) {
        this.bad = bad;
    }

    public List<List<String>> getNeutral() {
        return neutral;
    }

    public void setNeutral(List<List<String>> neutral) {
        this.neutral = neutral;
    }

    public static class Intelligence {
        private List<List<String>> home;
        private List<List<String>> away;

        public List<List<String>> getHome() {
            return home;
        }

        public void setHome(List<List<String>> home) {
            this.home = home;
        }

        public List<List<String>> getAway() {
            return away;
        }

        public void setAway(List<List<String>> away) {
            this.away = away;
        }
    }
}
