package com.yunbao.common.bean;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/3 15:06
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class MatchAnalysis {

    private List<List<List<Integer>>> table;
    private FightHistory history;

    public List<List<List<Integer>>> getTable() {
        return table;
    }

    public void setTable(List<List<List<Integer>>> table) {
        this.table = table;
    }

    public FightHistory getHistory() {
        return history;
    }

    public void setHistory(FightHistory history) {
        this.history = history;
    }

    public static class Table {
        private int stage_id = 0;//阶段id
        private String event_name = null; //联赛名称
        private int season_id = 0;//最新赛季id
        private String season = null; //最新赛季年份
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public int getStage_id() {
            return stage_id;
        }

        public void setStage_id(int stage_id) {
            this.stage_id = stage_id;
        }

        public String getEvent_name() {
            return event_name;
        }

        public void setEvent_name(String event_name) {
            this.event_name = event_name;
        }

        public int getSeason_id() {
            return season_id;
        }

        public void setSeason_id(int season_id) {
            this.season_id = season_id;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }
    }

    public static class Data {
        private Integral all;
        private Integral home;
        private Integral away;

        public Integral getAll() {
            return all;
        }

        public void setAll(Integral all) {
            this.all = all;
        }

        public Integral getHome() {
            return home;
        }

        public void setHome(Integral home) {
            this.home = home;
        }

        public Integral getAway() {
            return away;
        }

        public void setAway(Integral away) {
            this.away = away;
        }
    }
}
