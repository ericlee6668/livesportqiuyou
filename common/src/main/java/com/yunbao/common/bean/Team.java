package com.yunbao.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/13 11:52
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class Team implements Parcelable {
    private long id = 0;
    private long competition_id = 0;
    private long country_id = 0;
    @SerializedName("name_zh")
    private String nameZh = null;
    private String name_zht = null;
    private String name_en = null;
    @SerializedName("short_name_zh")
    private String shortNameZh = null;
    private String short_name_zht = null;
    private String short_name_en = null;
    private String logo = null;
    private int national = 0;
    private long foundation_time = 0;
    private String website = null;
    private long manager_id = 0;
    private long venue_id = 0;
    private int market_value = 0;
    private String market_value_currency = null;
    private String country_logo = null;
    private int total_players = 0;
    private int foreign_players = 0;
    private int national_players = 0;
    private int is_deleted = 0;
    private String points = null;
    private String ranking = null;
    private String position_changed = null;
    private String previous_points = null;
    private long updated_time = 0;

    protected Team(Parcel in) {
        id = in.readLong();
        competition_id = in.readLong();
        country_id = in.readLong();
        nameZh = in.readString();
        name_zht = in.readString();
        name_en = in.readString();
        shortNameZh = in.readString();
        short_name_zht = in.readString();
        short_name_en = in.readString();
        logo = in.readString();
        national = in.readInt();
        foundation_time = in.readLong();
        website = in.readString();
        manager_id = in.readLong();
        venue_id = in.readLong();
        market_value = in.readInt();
        market_value_currency = in.readString();
        country_logo = in.readString();
        total_players = in.readInt();
        foreign_players = in.readInt();
        national_players = in.readInt();
        is_deleted = in.readInt();
        points = in.readString();
        ranking = in.readString();
        position_changed = in.readString();
        previous_points = in.readString();
        updated_time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(competition_id);
        dest.writeLong(country_id);
        dest.writeString(nameZh);
        dest.writeString(name_zht);
        dest.writeString(name_en);
        dest.writeString(shortNameZh);
        dest.writeString(short_name_zht);
        dest.writeString(short_name_en);
        dest.writeString(logo);
        dest.writeInt(national);
        dest.writeLong(foundation_time);
        dest.writeString(website);
        dest.writeLong(manager_id);
        dest.writeLong(venue_id);
        dest.writeInt(market_value);
        dest.writeString(market_value_currency);
        dest.writeString(country_logo);
        dest.writeInt(total_players);
        dest.writeInt(foreign_players);
        dest.writeInt(national_players);
        dest.writeInt(is_deleted);
        dest.writeString(points);
        dest.writeString(ranking);
        dest.writeString(position_changed);
        dest.writeString(previous_points);
        dest.writeLong(updated_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(long competition_id) {
        this.competition_id = competition_id;
    }

    public long getCountry_id() {
        return country_id;
    }

    public void setCountry_id(long country_id) {
        this.country_id = country_id;
    }

    public String getName_zht() {
        return name_zht;
    }

    public void setName_zht(String name_zht) {
        this.name_zht = name_zht;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getShortNameZh() {
        return shortNameZh;
    }

    public void setShortNameZh(String shortNameZh) {
        this.shortNameZh = shortNameZh;
    }

    public String getShort_name_zht() {
        return short_name_zht;
    }

    public void setShort_name_zht(String short_name_zht) {
        this.short_name_zht = short_name_zht;
    }

    public String getShort_name_en() {
        return short_name_en;
    }

    public void setShort_name_en(String short_name_en) {
        this.short_name_en = short_name_en;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getNational() {
        return national;
    }

    public void setNational(int national) {
        this.national = national;
    }

    public long getFoundation_time() {
        return foundation_time;
    }

    public void setFoundation_time(long foundation_time) {
        this.foundation_time = foundation_time;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }

    public long getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public int getMarket_value() {
        return market_value;
    }

    public void setMarket_value(int market_value) {
        this.market_value = market_value;
    }

    public String getMarket_value_currency() {
        return market_value_currency;
    }

    public void setMarket_value_currency(String market_value_currency) {
        this.market_value_currency = market_value_currency;
    }

    public String getCountry_logo() {
        return country_logo;
    }

    public void setCountry_logo(String country_logo) {
        this.country_logo = country_logo;
    }

    public int getTotal_players() {
        return total_players;
    }

    public void setTotal_players(int total_players) {
        this.total_players = total_players;
    }

    public int getForeign_players() {
        return foreign_players;
    }

    public void setForeign_players(int foreign_players) {
        this.foreign_players = foreign_players;
    }

    public int getNational_players() {
        return national_players;
    }

    public void setNational_players(int national_players) {
        this.national_players = national_players;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getPosition_changed() {
        return position_changed;
    }

    public void setPosition_changed(String position_changed) {
        this.position_changed = position_changed;
    }

    public String getPrevious_points() {
        return previous_points;
    }

    public void setPrevious_points(String previous_points) {
        this.previous_points = previous_points;
    }

    public long getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(long updated_time) {
        this.updated_time = updated_time;
    }

}