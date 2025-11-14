package com.yunbao.live.bean.match;

import java.util.List;

/**
 * 战队对战 W.
 */
public class MatchTeamBattleBean {
    private Long team_a_id;
    private Long team_b_id;
    private List<MatchTeamBattleStatsBean> battle_stats;
    private List<MatchTeamRecentStatsBean> team_a_recent_stats;
    private List<MatchTeamRecentStatsBean> team_b_recent_stats;

//    public int getTeam_a_id() {
//        return team_a_id;
//    }
//
//    public void setTeam_a_id(int team_a_id) {
//        this.team_a_id = team_a_id;
//    }

//    public int getTeam_b_id() {
//        return team_b_id;
//    }
//
//    public void setTeam_b_id(int team_b_id) {
//        this.team_b_id = team_b_id;
//    }


    public Long getTeam_a_id() {
        return team_a_id;
    }

    public void setTeam_a_id(Long team_a_id) {
        this.team_a_id = team_a_id;
    }

    public Long getTeam_b_id() {
        return team_b_id;
    }

    public void setTeam_b_id(Long team_b_id) {
        this.team_b_id = team_b_id;
    }

    public List<MatchTeamBattleStatsBean> getBattle_stats() {
        return battle_stats;
    }

    public void setBattle_stats(List<MatchTeamBattleStatsBean> battle_stats) {
        this.battle_stats = battle_stats;
    }

    public List<MatchTeamRecentStatsBean> getTeam_a_recent_stats() {
        return team_a_recent_stats;
    }

    public void setTeam_a_recent_stats(List<MatchTeamRecentStatsBean> team_a_recent_stats) {
        this.team_a_recent_stats = team_a_recent_stats;
    }

    public List<MatchTeamRecentStatsBean> getTeam_b_recent_stats() {
        return team_b_recent_stats;
    }

    public void setTeam_b_recent_stats(List<MatchTeamRecentStatsBean> team_b_recent_stats) {
        this.team_b_recent_stats = team_b_recent_stats;
    }
}
