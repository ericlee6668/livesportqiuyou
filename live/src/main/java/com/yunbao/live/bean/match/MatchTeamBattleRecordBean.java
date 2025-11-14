package com.yunbao.live.bean.match;

/**
 * 战队对战记录 W.
 */
public class MatchTeamBattleRecordBean {
    private String match_time;
    private String team_a_name;
    private String team_b_name;
    private String team_a_logo;
    private String team_b_logo;
    private int team_a_score;
    private int team_b_score;

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getTeam_a_name() {
        return team_a_name;
    }

    public void setTeam_a_name(String team_a_name) {
        this.team_a_name = team_a_name;
    }

    public String getTeam_b_name() {
        return team_b_name;
    }

    public void setTeam_b_name(String team_b_name) {
        this.team_b_name = team_b_name;
    }

    public String getTeam_a_logo() {
        return team_a_logo;
    }

    public void setTeam_a_logo(String team_a_logo) {
        this.team_a_logo = team_a_logo;
    }

    public String getTeam_b_logo() {
        return team_b_logo;
    }

    public void setTeam_b_logo(String team_b_logo) {
        this.team_b_logo = team_b_logo;
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
}
