package com.yunbao.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 14:18
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class BasketballLineup {
    private Lineups lineup;
    private Injury injury;

    public Lineups getLineup() {
        return lineup;
    }

    public void setLineup(Lineups lineup) {
        this.lineup = lineup;
    }

    public Injury getInjury() {
        return injury;
    }

    public void setInjury(Injury injury) {
        this.injury = injury;
    }

    public static class Lineups {
        @SerializedName("home_manager_name")
        private String home_manager_name;
        @SerializedName("away_manager_name")
        private String away_manager_name;
        private List<Lineup> home;
        private List<Lineup> away;

        public String getHome_manager_name() {
            return home_manager_name;
        }

        public void setHome_manager_name(String home_manager_name) {
            this.home_manager_name = home_manager_name;
        }

        public String getAway_manager_name() {
            return away_manager_name;
        }

        public void setAway_manager_name(String away_manager_name) {
            this.away_manager_name = away_manager_name;
        }

        public List<Lineup> getHome() {
            return home;
        }

        public void setHome(List<Lineup> home) {
            this.home = home;
        }

        public List<Lineup> getAway() {
            return away;
        }

        public void setAway(List<Lineup> away) {
            this.away = away;
        }
    }

    public static class Injury {
        private List<Lineup> home;
        private List<Lineup> away;

        public List<Lineup> getHome() {
            return home;
        }

        public void setHome(List<Lineup> home) {
            this.home = home;
        }

        public List<Lineup> getAway() {
            return away;
        }

        public void setAway(List<Lineup> away) {
            this.away = away;
        }
    }
}
