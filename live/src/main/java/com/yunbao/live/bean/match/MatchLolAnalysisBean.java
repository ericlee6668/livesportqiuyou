package com.yunbao.live.bean.match;

import java.util.List;

/**
 * Match.GetMatchAnalysis
 * 赛事 - LOL比赛分析查询实体类 W.
 */
public class MatchLolAnalysisBean {
    private int id;
    private String league_cat;
    private int league_id;
    private int match_id;
    private int status;
    private int team_a_score;
    private int team_b_score;
    private String round_son_name;
    private String startdate;
    private String starttime;
    private String league_name;
    private int battle_current_index;
    private int battle_current_id;
    private int battle_duration;
    private String start_time;
    private String team_a_name;
    private String team_a_logo;
    private String team_b_name;
    private String team_b_logo;
    private int team_a_kill_count;
    private int team_b_kill_count;
    private double team_a_odds;
    private double team_b_odds;
    private int viewnum;
    private List<MatchTeamBattleBean> data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeague_cat() {
        return league_cat;
    }

    public void setLeague_cat(String league_cat) {
        this.league_cat = league_cat;
    }

    public int getLeague_id() {
        return league_id;
    }

    public void setLeague_id(int league_id) {
        this.league_id = league_id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTeam_a_score() {
        return team_a_score;
    }

    public void setTeam_a_score(int team_a_score) {
        this.team_a_score = team_a_score;
    }

    public int getTeam_b_score() {
        return team_b_score;
    }

    public void setTeam_b_score(int team_b_score) {
        this.team_b_score = team_b_score;
    }

    public String getRound_son_name() {
        return round_son_name;
    }

    public void setRound_son_name(String round_son_name) {
        this.round_son_name = round_son_name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getLeague_name() {
        return league_name;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }

    public int getBattle_current_index() {
        return battle_current_index;
    }

    public void setBattle_current_index(int battle_current_index) {
        this.battle_current_index = battle_current_index;
    }

    public int getBattle_current_id() {
        return battle_current_id;
    }

    public void setBattle_current_id(int battle_current_id) {
        this.battle_current_id = battle_current_id;
    }

    public int getBattle_duration() {
        return battle_duration;
    }

    public void setBattle_duration(int battle_duration) {
        this.battle_duration = battle_duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTeam_a_name() {
        return team_a_name;
    }

    public void setTeam_a_name(String team_a_name) {
        this.team_a_name = team_a_name;
    }

    public String getTeam_a_logo() {
        return team_a_logo;
    }

    public void setTeam_a_logo(String team_a_logo) {
        this.team_a_logo = team_a_logo;
    }

    public String getTeam_b_name() {
        return team_b_name;
    }

    public void setTeam_b_name(String team_b_name) {
        this.team_b_name = team_b_name;
    }

    public String getTeam_b_logo() {
        return team_b_logo;
    }

    public void setTeam_b_logo(String team_b_logo) {
        this.team_b_logo = team_b_logo;
    }

    public int getTeam_a_kill_count() {
        return team_a_kill_count;
    }

    public void setTeam_a_kill_count(int team_a_kill_count) {
        this.team_a_kill_count = team_a_kill_count;
    }

    public int getTeam_b_kill_count() {
        return team_b_kill_count;
    }

    public void setTeam_b_kill_count(int team_b_kill_count) {
        this.team_b_kill_count = team_b_kill_count;
    }

    public double getTeam_a_odds() {
        return team_a_odds;
    }

    public void setTeam_a_odds(double team_a_odds) {
        this.team_a_odds = team_a_odds;
    }

    public double getTeam_b_odds() {
        return team_b_odds;
    }

    public void setTeam_b_odds(double team_b_odds) {
        this.team_b_odds = team_b_odds;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public List<MatchTeamBattleBean> getData() {
        return data;
    }

    public void setData(List<MatchTeamBattleBean> data) {
        this.data = data;
    }
}
