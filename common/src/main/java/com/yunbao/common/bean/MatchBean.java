package com.yunbao.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/22 19:36
 *
 * @package: com.yunbao.common.bean
 * Description：比赛实体类
 */
public class MatchBean implements Parcelable {
    private long id = 0;
    private long season_id = 0;
    private long competition_id = 0;
    private long home_team_id = 0;
    private long away_team_id = 0;
    private int status_id = 0;
    @SerializedName("match_time")
    private long matchTime = 0;
    private int neutral = 0;
    private String note = null;

    /**
     * [
     * 0,//主队比分(常规时间)
     * 0,//主队半场比分
     * 0,//主队红牌
     * 0,//主队黄牌
     * -1,//主队角球，-1表示没有角球数据
     * 0,//主队加时比分(120分钟)，加时赛才有
     * 0,//主队点球大战比分，点球大战才有
     * ],
     */
    @SerializedName("home_scores")
    private String homeScores = null;
    @SerializedName("away_scores")
    private String awayScores = null;
    private String home_position = null;
    private String away_position = null;
    private String coverage = null;
    private long venue_id = 0;
    private String referee_id = null;
    private String round = null;

    private String environment = null;
    private String trend = null;
    private String live_url_1 = null;
    private String live_url_2 = null;
    private String live_url_3 = null;
    private String pc_link = null;
    private String mobile_link = null;
    private int is_deleted = 0;
    private long updated_time = 0;
    private String match_date = null;
    private String state_str = null;
    @SerializedName("is_playing")
    private int isPlaying = 0;
    private Team home_team = null;
    private Team away_team = null;
    private League league = null;
    @SerializedName("period_count")
    private int periodCount = 0;
    private String homeScoreStr = null;
    private String awayScoreStr = null;

    protected MatchBean(Parcel in) {
        id = in.readLong();
        season_id = in.readLong();
        competition_id = in.readLong();
        home_team_id = in.readLong();
        away_team_id = in.readLong();
        status_id = in.readInt();
        matchTime = in.readLong();
        neutral = in.readInt();
        note = in.readString();
        homeScores = in.readString();
        awayScores = in.readString();
        home_position = in.readString();
        away_position = in.readString();
        coverage = in.readString();
        venue_id = in.readLong();
        referee_id = in.readString();
        round = in.readString();
        environment = in.readString();
        trend = in.readString();
        live_url_1 = in.readString();
        live_url_2 = in.readString();
        live_url_3 = in.readString();
        pc_link = in.readString();
        mobile_link = in.readString();
        is_deleted = in.readInt();
        updated_time = in.readLong();
        match_date = in.readString();
        state_str = in.readString();
        isPlaying = in.readInt();
        home_team = in.readParcelable(Team.class.getClassLoader());
        away_team = in.readParcelable(Team.class.getClassLoader());
        league = in.readParcelable(League.class.getClassLoader());
        periodCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(season_id);
        dest.writeLong(competition_id);
        dest.writeLong(home_team_id);
        dest.writeLong(away_team_id);
        dest.writeInt(status_id);
        dest.writeLong(matchTime);
        dest.writeInt(neutral);
        dest.writeString(note);
        dest.writeString(homeScores);
        dest.writeString(awayScores);
        dest.writeString(home_position);
        dest.writeString(away_position);
        dest.writeString(coverage);
        dest.writeLong(venue_id);
        dest.writeString(referee_id);
        dest.writeString(round);
        dest.writeString(environment);
        dest.writeString(trend);
        dest.writeString(live_url_1);
        dest.writeString(live_url_2);
        dest.writeString(live_url_3);
        dest.writeString(pc_link);
        dest.writeString(mobile_link);
        dest.writeInt(is_deleted);
        dest.writeLong(updated_time);
        dest.writeString(match_date);
        dest.writeString(state_str);
        dest.writeInt(isPlaying);
        dest.writeParcelable(home_team, flags);
        dest.writeParcelable(away_team, flags);
        dest.writeParcelable(league, flags);
        dest.writeInt(periodCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MatchBean> CREATOR = new Creator<MatchBean>() {
        @Override
        public MatchBean createFromParcel(Parcel in) {
            return new MatchBean(in);
        }

        @Override
        public MatchBean[] newArray(int size) {
            return new MatchBean[size];
        }
    };

    public int getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(int periodCount) {
        this.periodCount = periodCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSeason_id() {
        return season_id;
    }

    public void setSeason_id(long season_id) {
        this.season_id = season_id;
    }

    public long getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(long competition_id) {
        this.competition_id = competition_id;
    }

    public long getHome_team_id() {
        return home_team_id;
    }

    public void setHome_team_id(long home_team_id) {
        this.home_team_id = home_team_id;
    }

    public long getAway_team_id() {
        return away_team_id;
    }

    public void setAway_team_id(long away_team_id) {
        this.away_team_id = away_team_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public long getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(long matchTime) {
        this.matchTime = matchTime;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHomeScores() {
        return homeScores;
    }

    public void setHomeScores(String homeScores) {
        this.homeScores = homeScores;
    }

    public String getAwayScores() {
        return awayScores;
    }

    public void setAwayScores(String awayScores) {
        this.awayScores = awayScores;
    }

    public String getHome_position() {
        return home_position;
    }

    public void setHome_position(String home_position) {
        this.home_position = home_position;
    }

    public String getAway_position() {
        return away_position;
    }

    public void setAway_position(String away_position) {
        this.away_position = away_position;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public long getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public String getReferee_id() {
        return referee_id;
    }

    public void setReferee_id(String referee_id) {
        this.referee_id = referee_id;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getLive_url_1() {
        return live_url_1;
    }

    public void setLive_url_1(String live_url_1) {
        this.live_url_1 = live_url_1;
    }

    public String getLive_url_2() {
        return live_url_2;
    }

    public void setLive_url_2(String live_url_2) {
        this.live_url_2 = live_url_2;
    }

    public String getLive_url_3() {
        return live_url_3;
    }

    public void setLive_url_3(String live_url_3) {
        this.live_url_3 = live_url_3;
    }

    public String getPc_link() {
        return pc_link;
    }

    public void setPc_link(String pc_link) {
        this.pc_link = pc_link;
    }

    public String getMobile_link() {
        return mobile_link;
    }

    public void setMobile_link(String mobile_link) {
        this.mobile_link = mobile_link;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public long getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(long updated_time) {
        this.updated_time = updated_time;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public String getState_str() {
        return state_str;
    }

    public void setState_str(String state_str) {
        this.state_str = state_str;
    }

    public int getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(int isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Team getHome_team() {
        return home_team;
    }

    public void setHome_team(Team home_team) {
        this.home_team = home_team;
    }

    public Team getAway_team() {
        return away_team;
    }

    public void setAway_team(Team away_team) {
        this.away_team = away_team;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getHomeScoreStr() {
        return homeScoreStr;
    }

    public void setHomeScoreStr(String homeScoreStr) {
        this.homeScoreStr = homeScoreStr;
    }

    public String getAwayScoreStr() {
        return awayScoreStr;
    }

    public void setAwayScoreStr(String awayScoreStr) {
        this.awayScoreStr = awayScoreStr;
    }
}
