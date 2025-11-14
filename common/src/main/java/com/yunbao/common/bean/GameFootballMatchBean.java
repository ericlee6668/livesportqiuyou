package com.yunbao.common.bean;


import java.io.Serializable;
import java.util.List;

public class GameFootballMatchBean implements Serializable {

    /**
     * matchId : 71908
     * leagueId : 557
     * matchStartTime : 1604577600
     * state : 10
     * isNeutral : 2
     * locationCn :
     * locationEn :
     * homeId : 6353
     * awayId : 3431
     * homeScore : 1
     * awayScore : 1
     * homeHalfScore : 0
     * awayHalfScore : 0
     * homeCornerNum : 0
     * awayCornerNum : 1
     * extraFirstKick : 0
     * extraNormalTime : 0
     * extraNormalScore :
     * extraTwoLegsScore :
     * extraType : 0
     * extraScore :
     * extraPenaltyKickScore :
     * extraWin : 0
     * letgoalHomeOdds : 0.87
     * letgoalGoal : -1.25
     * letgoalAwayOdds : 0.92
     * letgoalIsEntertained : 1
     * europeHomeOdds : 5.5
     * europeFlatOdds : 4.5
     * europeAwayOdds : 1.4
     * europeIsEntertained : 1
     * totalScoreHomeOdds : 0.8
     * totalScoreGoal : 3
     * totalScoreAwayOdds : 1
     * totalScoreIsEntertained : 1
     * hasSources : 0
     * hasAnimation : 1
     * hasLive : 1
     * weatherEn :
     * weatherCn :
     * weatherIcon :
     * temperature :
     * match_date : 2020-11-05
     * live_url : rtmp://live2.cdn.ymlykj.com/live/stream10184
     * state_str : 上半场
     * is_playing : 1
     * home_team : {"teamId":6353,"rank":6,"nameCn":"本尤德科","nameTrad":"賓約哥","nameEn":"Kuruvchi Bunyodkor","logo":"https://guqniupic.oss-cn-hangzhou.aliyuncs.com/fb/team/20150222150929.jpg"}
     * away_team : {"teamId":3431,"rank":1,"nameCn":"塔什干棉农","nameTrad":"柏克塔哥","nameEn":"Pakhtakor","logo":"https://guqniupic.oss-cn-hangzhou.aliyuncs.com/fb/team/20121220225105.jpg"}
     * league : {"leagueId":557,"leagueNameCn":"乌兹别克斯坦超级联赛","leagueNameTrad":"烏茲別克斯坦超級聯賽","leagueNameEn":"Uzbek League","leagueNameCnShort":"乌兹超","leagueNameTradShort":"烏茲超","leagueNameEnShort":"UZB D1"}
     */

    private String matchId;
    private String leagueId; //联赛/杯赛ID
    private String matchStartTime; //比赛开始时间(时间戳)
    private int state; //比赛状态:-14:推迟，-13:中断，-12:腰斩，-11:待定，-10:取消，-1.完场，0:未开始，1:上半场，2:中场，3:下半场，4:加时，5:点球
    private String isNeutral;//是否中立场，1：是，2：否
    private String locationCn;//比赛场地(中文,繁体也使用这个字段)
    private String locationEn;//比赛场地(英文)
    private String homeId; //主队ID
    private String awayId;//客队ID
    private String homeScore;//主队全场比分
    private String awayScore;//客队全场比分
    private String homeHalfScore;//主队半场比分
    private String awayHalfScore;//客队半场比分
    private String homeCornerNum;//主队角球数
    private String awayCornerNum;//客队角球数
    private String extraFirstKick;//加时: 先开球方，1：主队先开球，2：客队先开球
    private String extraNormalTime;//加时: 常规时间
    private String extraNormalScore;//加时: 常规时间的比分
    private String extraTwoLegsScore;//加时: 两回合总比分，该字段仅在比赛有加时、点球的情况才更新
    private String extraType;//加时: 加时阶段类型，1:120分钟，2：加时，3：加时中
    private String extraScore;//加时: 加时阶段比分
    private String extraPenaltyKickScore;//加时: 点球大战比分
    private String extraWin;//加时: 获胜方，1：主队获胜，2：客队获胜
    private String letgoalHomeOdds;//让球即时主队赔率
    private String letgoalGoal;//让球即时盘口
    private String letgoalAwayOdds;//让球即时客队赔率
    private String letgoalIsEntertained;//亚盘：是否封盘,1：默认，2：临时性封盘或停止走地
    private String europeHomeOdds;//欧赔即时主队赔率
    private String europeFlatOdds;//欧赔即时盘口
    private String europeAwayOdds;//欧赔即时客队赔率
    private String europeIsEntertained;//欧赔：是否封盘,1：默认，2：临时性封盘或停止走地
    private String totalScoreHomeOdds;//大小球即时主队赔率
    private String totalScoreGoal;//大小球即时盘口
    private String totalScoreAwayOdds;//大小球即时客队赔率
    private String totalScoreIsEntertained;//大小球：是否封盘, 1：默认，2：临时性封盘或停止走地
    private String hasSources;//是否有情报,0：无，1：有
    private String hasAnimation;
    private String hasLive;//是否有直播（购买直播信号可关联），无使用时，该字段无意义,0：无，1：有
    private String weatherEn;//天气（英文）
    private String weatherCn;//天气（中文）
    private String weatherIcon;//天气图标
    private String temperature;//温度
    private String match_date;//比赛日期
    private List<String> live_url;
    private String state_str; //比赛状态文本
    private int is_playing;//是否进行中，true进行中
    private HomeTeamBean home_team;
    private AwayTeamBean away_team;
    private LeagueBean league;
    private String week_day_str;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIsNeutral() {
        return isNeutral;
    }

    public void setIsNeutral(String isNeutral) {
        this.isNeutral = isNeutral;
    }

    public String getLocationCn() {
        return locationCn;
    }

    public void setLocationCn(String locationCn) {
        this.locationCn = locationCn;
    }

    public String getLocationEn() {
        return locationEn;
    }

    public void setLocationEn(String locationEn) {
        this.locationEn = locationEn;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getAwayId() {
        return awayId;
    }

    public void setAwayId(String awayId) {
        this.awayId = awayId;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getHomeHalfScore() {
        return homeHalfScore;
    }

    public void setHomeHalfScore(String homeHalfScore) {
        this.homeHalfScore = homeHalfScore;
    }

    public String getAwayHalfScore() {
        return awayHalfScore;
    }

    public void setAwayHalfScore(String awayHalfScore) {
        this.awayHalfScore = awayHalfScore;
    }

    public String getHomeCornerNum() {
        return homeCornerNum;
    }

    public void setHomeCornerNum(String homeCornerNum) {
        this.homeCornerNum = homeCornerNum;
    }

    public String getAwayCornerNum() {
        return awayCornerNum;
    }

    public void setAwayCornerNum(String awayCornerNum) {
        this.awayCornerNum = awayCornerNum;
    }

    public String getExtraFirstKick() {
        return extraFirstKick;
    }

    public void setExtraFirstKick(String extraFirstKick) {
        this.extraFirstKick = extraFirstKick;
    }

    public String getExtraNormalTime() {
        return extraNormalTime;
    }

    public void setExtraNormalTime(String extraNormalTime) {
        this.extraNormalTime = extraNormalTime;
    }

    public String getExtraNormalScore() {
        return extraNormalScore;
    }

    public void setExtraNormalScore(String extraNormalScore) {
        this.extraNormalScore = extraNormalScore;
    }

    public String getExtraTwoLegsScore() {
        return extraTwoLegsScore;
    }

    public void setExtraTwoLegsScore(String extraTwoLegsScore) {
        this.extraTwoLegsScore = extraTwoLegsScore;
    }

    public String getExtraType() {
        return extraType;
    }

    public void setExtraType(String extraType) {
        this.extraType = extraType;
    }

    public String getExtraScore() {
        return extraScore;
    }

    public void setExtraScore(String extraScore) {
        this.extraScore = extraScore;
    }

    public String getExtraPenaltyKickScore() {
        return extraPenaltyKickScore;
    }

    public void setExtraPenaltyKickScore(String extraPenaltyKickScore) {
        this.extraPenaltyKickScore = extraPenaltyKickScore;
    }

    public String getExtraWin() {
        return extraWin;
    }

    public void setExtraWin(String extraWin) {
        this.extraWin = extraWin;
    }

    public String getLetgoalHomeOdds() {
        return letgoalHomeOdds;
    }

    public void setLetgoalHomeOdds(String letgoalHomeOdds) {
        this.letgoalHomeOdds = letgoalHomeOdds;
    }

    public String getLetgoalGoal() {
        return letgoalGoal;
    }

    public void setLetgoalGoal(String letgoalGoal) {
        this.letgoalGoal = letgoalGoal;
    }

    public String getLetgoalAwayOdds() {
        return letgoalAwayOdds;
    }

    public void setLetgoalAwayOdds(String letgoalAwayOdds) {
        this.letgoalAwayOdds = letgoalAwayOdds;
    }

    public String getLetgoalIsEntertained() {
        return letgoalIsEntertained;
    }

    public void setLetgoalIsEntertained(String letgoalIsEntertained) {
        this.letgoalIsEntertained = letgoalIsEntertained;
    }

    public String getEuropeHomeOdds() {
        return europeHomeOdds;
    }

    public void setEuropeHomeOdds(String europeHomeOdds) {
        this.europeHomeOdds = europeHomeOdds;
    }

    public String getEuropeFlatOdds() {
        return europeFlatOdds;
    }

    public void setEuropeFlatOdds(String europeFlatOdds) {
        this.europeFlatOdds = europeFlatOdds;
    }

    public String getEuropeAwayOdds() {
        return europeAwayOdds;
    }

    public void setEuropeAwayOdds(String europeAwayOdds) {
        this.europeAwayOdds = europeAwayOdds;
    }

    public String getEuropeIsEntertained() {
        return europeIsEntertained;
    }

    public void setEuropeIsEntertained(String europeIsEntertained) {
        this.europeIsEntertained = europeIsEntertained;
    }

    public String getTotalScoreHomeOdds() {
        return totalScoreHomeOdds;
    }

    public void setTotalScoreHomeOdds(String totalScoreHomeOdds) {
        this.totalScoreHomeOdds = totalScoreHomeOdds;
    }

    public String getTotalScoreGoal() {
        return totalScoreGoal;
    }

    public void setTotalScoreGoal(String totalScoreGoal) {
        this.totalScoreGoal = totalScoreGoal;
    }

    public String getTotalScoreAwayOdds() {
        return totalScoreAwayOdds;
    }

    public void setTotalScoreAwayOdds(String totalScoreAwayOdds) {
        this.totalScoreAwayOdds = totalScoreAwayOdds;
    }

    public String getTotalScoreIsEntertained() {
        return totalScoreIsEntertained;
    }

    public void setTotalScoreIsEntertained(String totalScoreIsEntertained) {
        this.totalScoreIsEntertained = totalScoreIsEntertained;
    }

    public String getHasSources() {
        return hasSources;
    }

    public void setHasSources(String hasSources) {
        this.hasSources = hasSources;
    }

    public String getHasAnimation() {
        return hasAnimation;
    }

    public void setHasAnimation(String hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    public String getHasLive() {
        return hasLive;
    }

    public void setHasLive(String hasLive) {
        this.hasLive = hasLive;
    }

    public String getWeatherEn() {
        return weatherEn;
    }

    public void setWeatherEn(String weatherEn) {
        this.weatherEn = weatherEn;
    }

    public String getWeatherCn() {
        return weatherCn;
    }

    public void setWeatherCn(String weatherCn) {
        this.weatherCn = weatherCn;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public List<String> getLive_url() {
        return live_url;
    }

    public void setLive_url(List<String> live_url) {
        this.live_url = live_url;
    }

    public String getState_str() {
        return state_str;
    }

    public void setState_str(String state_str) {
        this.state_str = state_str;
    }

    public int isIs_playing() {
        return is_playing;
    }

    public void setIs_playing(int is_playing) {
        this.is_playing = is_playing;
    }

    public HomeTeamBean getHome_team() {
        return home_team;
    }

    public void setHome_team(HomeTeamBean home_team) {
        this.home_team = home_team;
    }

    public AwayTeamBean getAway_team() {
        return away_team;
    }

    public void setAway_team(AwayTeamBean away_team) {
        this.away_team = away_team;
    }

    public LeagueBean getLeague() {
        return league;
    }

    public void setLeague(LeagueBean league) {
        this.league = league;
    }

    public String getWeek_day_str() {
        return week_day_str;
    }

    public void setWeek_day_str(String week_day_str) {
        this.week_day_str = week_day_str;
    }

    public static class HomeTeamBean implements Serializable {
        /**
         * teamId : 6353
         * rank : 6
         * nameCn : 本尤德科
         * nameTrad : 賓約哥
         * nameEn : Kuruvchi Bunyodkor
         * logo : https://guqniupic.oss-cn-hangzhou.aliyuncs.com/fb/team/20150222150929.jpg
         */

        private String teamId;
        private String rank;
        private String nameCn;
        private String nameTrad;
        private String nameEn;
        private String logo;

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getNameTrad() {
            return nameTrad;
        }

        public void setNameTrad(String nameTrad) {
            this.nameTrad = nameTrad;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    public static class AwayTeamBean implements Serializable{
        /**
         * teamId : 3431
         * rank : 1
         * nameCn : 塔什干棉农
         * nameTrad : 柏克塔哥
         * nameEn : Pakhtakor
         * logo : https://guqniupic.oss-cn-hangzhou.aliyuncs.com/fb/team/20121220225105.jpg
         */

        private String teamId;
        private String rank;
        private String nameCn;
        private String nameTrad;
        private String nameEn;
        private String logo;

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getNameTrad() {
            return nameTrad;
        }

        public void setNameTrad(String nameTrad) {
            this.nameTrad = nameTrad;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    public static class LeagueBean implements Serializable{
        /**
         * leagueId : 557
         * leagueNameCn : 乌兹别克斯坦超级联赛
         * leagueNameTrad : 烏茲別克斯坦超級聯賽
         * leagueNameEn : Uzbek League
         * leagueNameCnShort : 乌兹超
         * leagueNameTradShort : 烏茲超
         * leagueNameEnShort : UZB D1
         */

        private String leagueId;
        private String leagueNameCn;
        private String leagueNameTrad;
        private String leagueNameEn;
        private String leagueNameCnShort;
        private String leagueNameTradShort;
        private String leagueNameEnShort;

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }

        public String getLeagueNameCn() {
            return leagueNameCn;
        }

        public void setLeagueNameCn(String leagueNameCn) {
            this.leagueNameCn = leagueNameCn;
        }

        public String getLeagueNameTrad() {
            return leagueNameTrad;
        }

        public void setLeagueNameTrad(String leagueNameTrad) {
            this.leagueNameTrad = leagueNameTrad;
        }

        public String getLeagueNameEn() {
            return leagueNameEn;
        }

        public void setLeagueNameEn(String leagueNameEn) {
            this.leagueNameEn = leagueNameEn;
        }

        public String getLeagueNameCnShort() {
            return leagueNameCnShort;
        }

        public void setLeagueNameCnShort(String leagueNameCnShort) {
            this.leagueNameCnShort = leagueNameCnShort;
        }

        public String getLeagueNameTradShort() {
            return leagueNameTradShort;
        }

        public void setLeagueNameTradShort(String leagueNameTradShort) {
            this.leagueNameTradShort = leagueNameTradShort;
        }

        public String getLeagueNameEnShort() {
            return leagueNameEnShort;
        }

        public void setLeagueNameEnShort(String leagueNameEnShort) {
            this.leagueNameEnShort = leagueNameEnShort;
        }
    }
}
