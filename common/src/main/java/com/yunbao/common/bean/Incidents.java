package com.yunbao.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/27 20:44
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class Incidents implements MultiItemEntity {

    public static final int TYPE_START = 0;
    public static final int TYPE_NEUTRAL = 1;
    public static final int TYPE_HOME = 2;
    public static final int TYPE_AWAY = 3;
    public static final int TYPE_END = 4;

    private int type = 0;
    private int type_v2 = 0;
    private int position = 0;
    private int time = 0;
    private int second = 0;
    private int player_id = 0;
    private String player_name = null;
    @SerializedName("in_player_name")
    private String inPlayerName = null;
    @SerializedName("out_player_name")
    private String outPlayerName = null;
    @SerializedName("assist1_name")
    private String assistName = null;
    private int home_score = 0;
    private int away_score = 0;
    private int var_reason = 0;
    private int var_result = 0;
    private int reason_type = 0;

    public Incidents() {
    }

    public Incidents(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType_v2() {
        return type_v2;
    }

    public void setType_v2(int type_v2) {
        this.type_v2 = type_v2;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public int getAway_score() {
        return away_score;
    }

    public void setAway_score(int away_score) {
        this.away_score = away_score;
    }

    public int getVar_reason() {
        return var_reason;
    }

    public void setVar_reason(int var_reason) {
        this.var_reason = var_reason;
    }

    public int getVar_result() {
        return var_result;
    }

    public void setVar_result(int var_result) {
        this.var_result = var_result;
    }

    public int getReason_type() {
        return reason_type;
    }

    public void setReason_type(int reason_type) {
        this.reason_type = reason_type;
    }

    public String getInPlayerName() {
        return inPlayerName;
    }

    public void setInPlayerName(String inPlayerName) {
        this.inPlayerName = inPlayerName;
    }

    public String getOutPlayerName() {
        return outPlayerName;
    }

    public void setOutPlayerName(String outPlayerName) {
        this.outPlayerName = outPlayerName;
    }

    public String getAssistName() {
        return assistName;
    }

    public void setAssistName(String assistName) {
        this.assistName = assistName;
    }

    @Override
    public int getItemType() {
        int itemType;
        if (position == 0) {
            if (type == -1) {
                itemType = TYPE_START;
            } else if (type == 12) {
                itemType = TYPE_END;
            } else {
                itemType = TYPE_NEUTRAL;
            }
        } else if (position == 1) {
            itemType = TYPE_HOME;
        } else {
            itemType = TYPE_AWAY;
        }
        return itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incidents incidents = (Incidents) o;
        return type == incidents.type &&
                type_v2 == incidents.type_v2 &&
                position == incidents.position &&
                time == incidents.time &&
                second == incidents.second &&
                player_id == incidents.player_id &&
                home_score == incidents.home_score &&
                away_score == incidents.away_score &&
                var_reason == incidents.var_reason &&
                var_result == incidents.var_result &&
                reason_type == incidents.reason_type &&
                Objects.equals(player_name, incidents.player_name) &&
                Objects.equals(inPlayerName, incidents.inPlayerName) &&
                Objects.equals(outPlayerName, incidents.outPlayerName) &&
                Objects.equals(assistName, incidents.assistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, type_v2, position, time, second, player_id, player_name, inPlayerName, outPlayerName, assistName, home_score, away_score, var_reason, var_result, reason_type);
    }
}
