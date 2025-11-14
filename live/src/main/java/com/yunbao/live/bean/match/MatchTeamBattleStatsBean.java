package com.yunbao.live.bean.match;

import java.util.List;

/**
 * 战队对战状态 W.
 */
public class MatchTeamBattleStatsBean {
    private int team_a_win_count;
    private int team_b_win_count;
    private int team_a_lose_count;
    private int team_b_lose_count;
    private int team_a_five_kill_count;
    private int team_b_five_kill_count;
    private int team_a_first_blood_count;
    private int team_b_first_blood_count;
    private int team_a_first_tower_count;
    private int team_b_first_tower_count;
    private int team_a_first_dragon_count;
    private int team_b_first_dragon_count;
    private int team_a_first_nash_count;
    private int team_b_first_nash_count;
    private List<MatchTeamBattleRecordBean> records;

    public int getTeam_a_win_count() {
        return team_a_win_count;
    }

    public void setTeam_a_win_count(int team_a_win_count) {
        this.team_a_win_count = team_a_win_count;
    }

    public int getTeam_b_win_count() {
        return team_b_win_count;
    }

    public void setTeam_b_win_count(int team_b_win_count) {
        this.team_b_win_count = team_b_win_count;
    }

    public int getTeam_a_lose_count() {
        return team_a_lose_count;
    }

    public void setTeam_a_lose_count(int team_a_lose_count) {
        this.team_a_lose_count = team_a_lose_count;
    }

    public int getTeam_b_lose_count() {
        return team_b_lose_count;
    }

    public void setTeam_b_lose_count(int team_b_lose_count) {
        this.team_b_lose_count = team_b_lose_count;
    }

    public int getTeam_a_five_kill_count() {
        return team_a_five_kill_count;
    }

    public void setTeam_a_five_kill_count(int team_a_five_kill_count) {
        this.team_a_five_kill_count = team_a_five_kill_count;
    }

    public int getTeam_b_five_kill_count() {
        return team_b_five_kill_count;
    }

    public void setTeam_b_five_kill_count(int team_b_five_kill_count) {
        this.team_b_five_kill_count = team_b_five_kill_count;
    }

    public int getTeam_a_first_blood_count() {
        return team_a_first_blood_count;
    }

    public void setTeam_a_first_blood_count(int team_a_first_blood_count) {
        this.team_a_first_blood_count = team_a_first_blood_count;
    }

    public int getTeam_b_first_blood_count() {
        return team_b_first_blood_count;
    }

    public void setTeam_b_first_blood_count(int team_b_first_blood_count) {
        this.team_b_first_blood_count = team_b_first_blood_count;
    }

    public int getTeam_a_first_tower_count() {
        return team_a_first_tower_count;
    }

    public void setTeam_a_first_tower_count(int team_a_first_tower_count) {
        this.team_a_first_tower_count = team_a_first_tower_count;
    }

    public int getTeam_b_first_tower_count() {
        return team_b_first_tower_count;
    }

    public void setTeam_b_first_tower_count(int team_b_first_tower_count) {
        this.team_b_first_tower_count = team_b_first_tower_count;
    }

    public int getTeam_a_first_dragon_count() {
        return team_a_first_dragon_count;
    }

    public void setTeam_a_first_dragon_count(int team_a_first_dragon_count) {
        this.team_a_first_dragon_count = team_a_first_dragon_count;
    }

    public int getTeam_b_first_dragon_count() {
        return team_b_first_dragon_count;
    }

    public void setTeam_b_first_dragon_count(int team_b_first_dragon_count) {
        this.team_b_first_dragon_count = team_b_first_dragon_count;
    }

    public int getTeam_a_first_nash_count() {
        return team_a_first_nash_count;
    }

    public void setTeam_a_first_nash_count(int team_a_first_nash_count) {
        this.team_a_first_nash_count = team_a_first_nash_count;
    }

    public int getTeam_b_first_nash_count() {
        return team_b_first_nash_count;
    }

    public void setTeam_b_first_nash_count(int team_b_first_nash_count) {
        this.team_b_first_nash_count = team_b_first_nash_count;
    }

    public List<MatchTeamBattleRecordBean> getRecords() {
        return records;
    }

    public void setRecords(List<MatchTeamBattleRecordBean> records) {
        this.records = records;
    }
}
