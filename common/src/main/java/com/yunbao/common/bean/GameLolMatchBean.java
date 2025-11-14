package com.yunbao.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GameLolMatchBean implements Parcelable {
    private String id;
    private String season_id;
    private String competition_id;
    private String home_team_id;
    private String away_team_id;
    private String status_id;
    private long match_time;
    private String neutral;
    private String note;
    private String home_scores;
    private String away_scores;
    private String home_position;
    private String away_position;
    private String coverage;
    private String venue_id;
    private String referee_id;
    private String round;
    private String environment;
    private String trend;
    private String live_url_1;
    private String live_url_2;
    private String live_url_3;
    private String pc_link;
    private String mobile_link;
    private String is_deleted;
    private long updated_time;
    private String match_date;
    private String state_str;
    private int is_playing;
    private int live_class_id;
    private Team home_team;
    private Team away_team;
    private League league;


    protected GameLolMatchBean(Parcel in) {
        id = in.readString();
        season_id = in.readString();
        competition_id = in.readString();
        home_team_id = in.readString();
        away_team_id = in.readString();
        status_id = in.readString();
        match_time = in.readLong();
        neutral = in.readString();
        note = in.readString();
        home_scores = in.readString();
        away_scores = in.readString();
        home_position = in.readString();
        away_position = in.readString();
        coverage = in.readString();
        venue_id = in.readString();
        referee_id = in.readString();
        round = in.readString();
        environment = in.readString();
        trend = in.readString();
        live_url_1 = in.readString();
        live_url_2 = in.readString();
        live_url_3 = in.readString();
        pc_link = in.readString();
        mobile_link = in.readString();
        is_deleted = in.readString();
        updated_time = in.readLong();
        match_date = in.readString();
        state_str = in.readString();
        is_playing = in.readInt();
        live_class_id = in.readInt();
        home_team = in.readParcelable(Team.class.getClassLoader());
        away_team = in.readParcelable(Team.class.getClassLoader());
        league = in.readParcelable(League.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeason_id() {
        return season_id;
    }

    public void setSeason_id(String season_id) {
        this.season_id = season_id;
    }

    public String getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(String competition_id) {
        this.competition_id = competition_id;
    }

    public String getHome_team_id() {
        return home_team_id;
    }

    public void setHome_team_id(String home_team_id) {
        this.home_team_id = home_team_id;
    }

    public String getAway_team_id() {
        return away_team_id;
    }

    public void setAway_team_id(String away_team_id) {
        this.away_team_id = away_team_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public long getMatch_time() {
        return match_time;
    }

    public void setMatch_time(long match_time) {
        this.match_time = match_time;
    }

    public String getNeutral() {
        return neutral;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHome_scores() {
        return home_scores;
    }

    public void setHome_scores(String home_scores) {
        this.home_scores = home_scores;
    }

    public String getAway_scores() {
        return away_scores;
    }

    public void setAway_scores(String away_scores) {
        this.away_scores = away_scores;
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

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
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

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
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

    public int getIs_playing() {
        return is_playing;
    }

    public void setIs_playing(int is_playing) {
        this.is_playing = is_playing;
    }

    public int getLive_class_id() {
        return live_class_id;
    }

    public void setLive_class_id(int live_class_id) {
        this.live_class_id = live_class_id;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(season_id);
        dest.writeString(competition_id);
        dest.writeString(home_team_id);
        dest.writeString(away_team_id);
        dest.writeString(status_id);
        dest.writeLong(match_time);
        dest.writeString(neutral);
        dest.writeString(note);
        dest.writeString(home_scores);
        dest.writeString(away_scores);
        dest.writeString(home_position);
        dest.writeString(away_position);
        dest.writeString(coverage);
        dest.writeString(venue_id);
        dest.writeString(referee_id);
        dest.writeString(round);
        dest.writeString(environment);
        dest.writeString(trend);
        dest.writeString(live_url_1);
        dest.writeString(live_url_2);
        dest.writeString(live_url_3);
        dest.writeString(pc_link);
        dest.writeString(mobile_link);
        dest.writeString(is_deleted);
        dest.writeLong(updated_time);
        dest.writeString(match_date);
        dest.writeString(state_str);
        dest.writeInt(is_playing);
        dest.writeInt(live_class_id);
        dest.writeParcelable(home_team, flags);
        dest.writeParcelable(away_team, flags);
        dest.writeParcelable(league, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GameLolMatchBean> CREATOR = new Creator<GameLolMatchBean>() {
        @Override
        public GameLolMatchBean createFromParcel(Parcel in) {
            return new GameLolMatchBean(in);
        }

        @Override
        public GameLolMatchBean[] newArray(int size) {
            return new GameLolMatchBean[size];
        }
    };
}
