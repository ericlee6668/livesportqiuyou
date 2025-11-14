package com.yunbao.common.bean;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/3 15:07
 *
 * @package: com.yunbao.common.bean
 * Description：联赛积分
 */
public class Integral {
    private int position = 0;//排名
    private int pts = 0;//积分
    private int played = 0; //已赛场数
    private int won = 0; //胜场数
    private int drawn = 0; //平场数
    private int lost = 0; //负场数
    private int goals = 0; //进球数
    private int away_goals = 0; //客场进球数
    private int against = 0; //失球数
    private int diff = 0; //净胜球数
    private long team_id = 0; //球队id

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int drawn) {
        this.drawn = drawn;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAway_goals() {
        return away_goals;
    }

    public void setAway_goals(int away_goals) {
        this.away_goals = away_goals;
    }

    public int getAgainst() {
        return against;
    }

    public void setAgainst(int against) {
        this.against = against;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(long team_id) {
        this.team_id = team_id;
    }
}
