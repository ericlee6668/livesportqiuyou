package com.yunbao.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/26 17:47
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class FootballLineup {

    private HashMap<String, List<Injury>> injury;
    private Lineups lineup;

    public HashMap<String, List<Injury>> getInjury() {
        return injury;
    }

    public void setInjury(HashMap<String, List<Injury>> injury) {
        this.injury = injury;
    }

    public Lineups getLineup() {
        return lineup;
    }

    public void setLineup(Lineups lineup) {
        this.lineup = lineup;
    }

    public static class Lineups {
        @SerializedName("home_formation")
        private String homeFormation;
        @SerializedName("away_formation")
        private String awayFormation;
        private List<Player> home;
        private List<Player> away;

        public String getHomeFormation() {
            return homeFormation;
        }

        public void setHomeFormation(String homeFormation) {
            this.homeFormation = homeFormation;
        }

        public String getAwayFormation() {
            return awayFormation;
        }

        public void setAwayFormation(String awayFormation) {
            this.awayFormation = awayFormation;
        }

        public List<Player> getHome() {
            return home;
        }

        public void setHome(List<Player> home) {
            this.home = home;
        }

        public List<Player> getAway() {
            return away;
        }

        public void setAway(List<Player> away) {
            this.away = away;
        }
    }

    /**
     * 比赛事件字段说明
     * {
     * "type": 1,//类型，详见状态码->足球技术统计
     * "type_v2": 1,//类型，新增事件码29-点球(点球大战)、30-点球未进(点球大战)，其它事件类型与type一致
     * "position": 2,//事件发生方，0-中立 1-主队 2-客队
     * "time": 47,	//时间(分钟)
     * "second": 2820,	//时间(秒)
     * <p>
     * "player_id": 31727,//事件相关球员id，可能为空
     * "player_name": "内马尔",//事件相关球员名称，可能为空
     * <p>
     * "assist1_id": 12395,//进球时，助攻球员1 id，可能为空
     * "assist1_name": "梅西",//进球时，助攻球员1 名称，可能为空
     * <p>
     * "assist2_id": 12395,//进球时，助攻球员2 id，可能为空
     * "assist2_name": "梅西",//进球时，助攻球员2 名称，可能为空
     * <p>
     * "in_player_id ": 12395,//换人时，换上球员id，可能为空
     * "in_player_name ": "梅西",//换人时，换上球员名称，可能为空
     * <p>
     * "out_player_id ": 10156,//换人时，换下球员id，可能为空
     * "out_player_name ": "C罗",//换人时，换下球员名称，可能为空
     * <p>
     * "home_score": 1,//进球时，主队比分，可能不存在
     * "away_score": 2,//进球时，客队比分，可能不存在
     * <p>
     * "var_reason": 1,//VAR原因，可能不存在，1-进球判定、2-进球未判定、3-点球判定、4-点球未判定、5-红牌判定、6-出牌处罚判定、7-错认身份、0-其他
     * "var_result": 2,//VAR结果，可能不存在，1-进球有效、2-进球无效、3-点球有效、4-点球取消、5-红牌有效、6-红牌取消、7-出牌处罚核实、8-出牌处罚更改、9-维持原判、10-判罚更改、0-未知
     * <p>
     * "reason_type": 1,//红黄牌、换人事件原因，详见状态码->事件原因（红黄牌、换人事件关联字段，可能不存在）
     * }
     */
    public static class Incidents {
        private int type = 0;
        @SerializedName("type_v2")
        private int typeV2 = 0;
        private String time;
        private int minute = 0;
        @SerializedName("addtime")
        private int addTime = 0;
        private int belong = 0;
        private String text = null;
        @SerializedName("home_score")
        private int homeScore = 0;
        @SerializedName("away_score")
        private int awayScore = 0;
        private Player player;
        @SerializedName("reason_type")
        private int reasonType = 0;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getTypeV2() {
            return typeV2;
        }

        public void setTypeV2(int typeV2) {
            this.typeV2 = typeV2;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getAddTime() {
            return addTime;
        }

        public void setAddTime(int addTime) {
            this.addTime = addTime;
        }

        public int getBelong() {
            return belong;
        }

        public void setBelong(int belong) {
            this.belong = belong;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getHomeScore() {
            return homeScore;
        }

        public void setHomeScore(int homeScore) {
            this.homeScore = homeScore;
        }

        public int getAwayScore() {
            return awayScore;
        }

        public void setAwayScore(int awayScore) {
            this.awayScore = awayScore;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public int getReasonType() {
            return reasonType;
        }

        public void setReasonType(int reasonType) {
            this.reasonType = reasonType;
        }
    }

    public static class Player {
        private long id = 0;
        @SerializedName("team_id")
        private long teamId = 0;
        private int first = 0;
        private String name;
        private String logo;
        @SerializedName("shirt_number")
        private int shirtNumber = 0;
        private String position;
        private int x;
        private int y;
        private String rating;
        private List<Incidents> incidents;

        public List<Incidents> getIncidents() {
            return incidents;
        }

        public void setIncidents(List<Incidents> incidents) {
            this.incidents = incidents;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getTeamId() {
            return teamId;
        }

        public void setTeamId(long teamId) {
            this.teamId = teamId;
        }

        public int getFirst() {
            return first;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getShirtNumber() {
            return shirtNumber;
        }

        public void setShirtNumber(int shirtNumber) {
            this.shirtNumber = shirtNumber;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }
    }

    public static class Injury {
        private long id = 0;
        private String name = null;
        private String position = null;
        private String logo = null;
        private int type = 0;
        private String reason = null;
        @SerializedName("start_time")
        private long startTime = 0;
        @SerializedName("end_time")
        private long endTime = 0;
        @SerializedName("missed_matches")
        private int missedMatches = 0;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getMissedMatches() {
            return missedMatches;
        }

        public void setMissedMatches(int missedMatches) {
            this.missedMatches = missedMatches;
        }
    }
}
