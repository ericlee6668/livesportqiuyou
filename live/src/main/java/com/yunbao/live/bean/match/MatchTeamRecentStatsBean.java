package com.yunbao.live.bean.match;

import java.util.List;

/**
 * 战队最近战绩 W.
 */
public class MatchTeamRecentStatsBean {
    private int win_rate;
    private int lost_rate;
    private List<MatchTeamBattleRecordBean> matches;

    public int getWin_rate() {
        return win_rate;
    }

    public void setWin_rate(int win_rate) {
        this.win_rate = win_rate;
    }

    public int getLost_rate() {
        return lost_rate;
    }

    public void setLost_rate(int lost_rate) {
        this.lost_rate = lost_rate;
    }

    public List<MatchTeamBattleRecordBean> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchTeamBattleRecordBean> matches) {
        this.matches = matches;
    }
}
