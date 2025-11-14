package com.yunbao.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/13 11:56
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class League implements Parcelable {
    private long id = 0;
    private long category_id = 0;
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
    private int type = 0;
    private long cur_season_id = 0;
    private long cur_stage_id = 0;
    private int cur_round = 0;
    private String round_count = null;
    private String title_holder = null;
    private String most_titles = null;
    private String newcomers = null;
    private String divisions = null;
    private String host = null;
    private String primary_color = null;
    private String secondary_color = null;
    private int is_deleted = 0;
    private long updated_time = 0;

    protected League(Parcel in) {
        id = in.readLong();
        category_id = in.readLong();
        country_id = in.readLong();
        nameZh = in.readString();
        name_zht = in.readString();
        name_en = in.readString();
        shortNameZh = in.readString();
        short_name_zht = in.readString();
        short_name_en = in.readString();
        logo = in.readString();
        type = in.readInt();
        cur_season_id = in.readLong();
        cur_stage_id = in.readLong();
        cur_round = in.readInt();
        round_count = in.readString();
        title_holder = in.readString();
        most_titles = in.readString();
        newcomers = in.readString();
        divisions = in.readString();
        host = in.readString();
        primary_color = in.readString();
        secondary_color = in.readString();
        is_deleted = in.readInt();
        updated_time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(category_id);
        dest.writeLong(country_id);
        dest.writeString(nameZh);
        dest.writeString(name_zht);
        dest.writeString(name_en);
        dest.writeString(shortNameZh);
        dest.writeString(short_name_zht);
        dest.writeString(short_name_en);
        dest.writeString(logo);
        dest.writeInt(type);
        dest.writeLong(cur_season_id);
        dest.writeLong(cur_stage_id);
        dest.writeInt(cur_round);
        dest.writeString(round_count);
        dest.writeString(title_holder);
        dest.writeString(most_titles);
        dest.writeString(newcomers);
        dest.writeString(divisions);
        dest.writeString(host);
        dest.writeString(primary_color);
        dest.writeString(secondary_color);
        dest.writeInt(is_deleted);
        dest.writeLong(updated_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<League> CREATOR = new Creator<League>() {
        @Override
        public League createFromParcel(Parcel in) {
            return new League(in);
        }

        @Override
        public League[] newArray(int size) {
            return new League[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public long getCountry_id() {
        return country_id;
    }

    public void setCountry_id(long country_id) {
        this.country_id = country_id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCur_season_id() {
        return cur_season_id;
    }

    public void setCur_season_id(long cur_season_id) {
        this.cur_season_id = cur_season_id;
    }

    public long getCur_stage_id() {
        return cur_stage_id;
    }

    public void setCur_stage_id(long cur_stage_id) {
        this.cur_stage_id = cur_stage_id;
    }

    public int getCur_round() {
        return cur_round;
    }

    public void setCur_round(int cur_round) {
        this.cur_round = cur_round;
    }

    public String getRound_count() {
        return round_count;
    }

    public void setRound_count(String round_count) {
        this.round_count = round_count;
    }

    public String getTitle_holder() {
        return title_holder;
    }

    public void setTitle_holder(String title_holder) {
        this.title_holder = title_holder;
    }

    public String getMost_titles() {
        return most_titles;
    }

    public void setMost_titles(String most_titles) {
        this.most_titles = most_titles;
    }

    public String getNewcomers() {
        return newcomers;
    }

    public void setNewcomers(String newcomers) {
        this.newcomers = newcomers;
    }

    public String getDivisions() {
        return divisions;
    }

    public void setDivisions(String divisions) {
        this.divisions = divisions;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPrimary_color() {
        return primary_color;
    }

    public void setPrimary_color(String primary_color) {
        this.primary_color = primary_color;
    }

    public String getSecondary_color() {
        return secondary_color;
    }

    public void setSecondary_color(String secondary_color) {
        this.secondary_color = secondary_color;
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

}
