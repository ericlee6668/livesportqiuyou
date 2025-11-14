package com.yunbao.common.bean;


import java.io.Serializable;
import java.util.List;

public class GameBasketballMatchBean implements Serializable {
    /**
     * matchId : 12723
     * leagueId : 79
     * homeId : 2449
     * awayId : 1049
     * homeRank : null
     * awayRank : null
     * state : 90
     * matchStartTime : 1604709000
     * matchConductTime : null
     * homeScore : null
     * awayScore : null
     * nodeCount : null
     * homeNode1Score : null
     * homeNode2Score : null
     * homeNode3Score : null
     * homeNode4Score : null
     * homeNode5Score : null
     * awayNode1Score : null
     * awayNode2Score : null
     * awayNode3Score : null
     * awayNode4Score : null
     * awayNode5Score : null
     * letgoalHomeOdds : 0
     * letgoalGoal : 0
     * letgoalAwayOdds : 0
     * letgoalIsEntertained : 0
     * europeHomeOdds : null
     * europeFlatOdds : null
     * europeAwayOdds : null
     * europeIsEntertained : 1
     * totalScoreHomeOdds : 0
     * totalScoreGoal : 0
     * totalScoreAwayOdds : 0
     * totalScoreIsEntertained : 0
     * isSources : 0
     * isAnimation : 0
     * isLive : 0
     * homeNodePauseCount : null
     * awayNodePauseCount : null
     * homeNodeFoulsCount : null
     * awayNodeFoulsCount : null
     * homeAssists : null
     * homeSteals : null
     * homeBlocks : null
     * awayAssists : null
     * awaySteals : null
     * awayBlocks : null
     * homeThreeCount : null
     * homeTwoCount : null
     * homeFreeCount : null
     * homeFreeRate : null
     * awayThreeCount : null
     * awayTwoCount : null
     * awayFreeCount : null
     * awayFreeRate : null
     * match_date : 2020-11-07
     * url :
     * state_str : 未开赛
     * is_playing : false
     * home_team : {"teamId":2449,"rank":0,"nameCn":"圣洛伦索","nameCnShort":"圣洛伦索","nameTrad":"聖羅倫素","nameTradShort":"聖羅倫素","nameEn":"San Lorenzo- -Casla","nameEnShort":"San Lorenzo- -Casla","logo":""}
     * away_team : {"teamId":1049,"rank":0,"nameCn":"奥布拉斯","nameCnShort":"奥布拉斯","nameTrad":"奧布拉斯","nameTradShort":"奥布拉斯","nameEn":"Obras Sanitarias Buenos Aires","nameEnShort":"Obras","logo":""}
     * league : {"leagueId":79,"leagueNameCnShort":"阿篮联","leagueNameTradShort":"阿籃聯","leagueNameEnShort":"LNB"}
     */

    private String matchId;
    private String leagueId;
    private String homeId;
    private String awayId;
    private String homeRank;
    private String awayRank;
    private int state; //比赛状态: 0：比赛一场，1：未开赛，2：第一节，3：第一节完，4：第二节，5：第二节完，6：第三节，7：第三节完，8：第四节，9：加时，10：完场，11：中断，12：取消，13：延期，14：腰斩，15：待定
    private String matchStartTime;
    private int matchConductTime;
    private String homeScore;
    private String awayScore;
    private String nodeCount;
    private String homeNode1Score;
    private String homeNode2Score;
    private String homeNode3Score;
    private String homeNode4Score;
    private String homeNode5Score;
    private String awayNode1Score;
    private String awayNode2Score;
    private String awayNode3Score;
    private String awayNode4Score;
    private String awayNode5Score;
    private String letgoalHomeOdds;
    private String letgoalGoal;
    private String letgoalAwayOdds;
    private String letgoalIsEntertained;
    private String europeHomeOdds;
    private String europeFlatOdds;
    private String europeAwayOdds;
    private String europeIsEntertained;
    private String totalScoreHomeOdds;
    private String totalScoreGoal;
    private String totalScoreAwayOdds;
    private String totalScoreIsEntertained;
    private String isSources;
    private String isAnimation;
    private String isLive;
    private String homeNodePauseCount;
    private String awayNodePauseCount;
    private String homeNodeFoulsCount;
    private String awayNodeFoulsCount;
    private String homeAssists;
    private String homeSteals;
    private String homeBlocks;
    private String awayAssists;
    private String awaySteals;
    private String awayBlocks;
    private String homeThreeCount;
    private String homeTwoCount;
    private String homeFreeCount;
    private String homeFreeRate;
    private String awayThreeCount;
    private String awayTwoCount;
    private String awayFreeCount;
    private String awayFreeRate;
    private String match_date;
    private List<String> live_url;
    private String state_str;
    private int is_playing;
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

    public String getHomeRank() {
        return homeRank;
    }

    public void setHomeRank(String homeRank) {
        this.homeRank = homeRank;
    }

    public String getAwayRank() {
        return awayRank;
    }

    public void setAwayRank(String awayRank) {
        this.awayRank = awayRank;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMatchStartTime() {
        return matchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        this.matchStartTime = matchStartTime;
    }

    public int getMatchConductTime() {
        return matchConductTime;
    }

    public void setMatchConductTime(int matchConductTime) {
        this.matchConductTime = matchConductTime;
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

    public String getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(String nodeCount) {
        this.nodeCount = nodeCount;
    }

    public String getHomeNode1Score() {
        return homeNode1Score;
    }

    public void setHomeNode1Score(String homeNode1Score) {
        this.homeNode1Score = homeNode1Score;
    }

    public String getHomeNode2Score() {
        return homeNode2Score;
    }

    public void setHomeNode2Score(String homeNode2Score) {
        this.homeNode2Score = homeNode2Score;
    }

    public String getHomeNode3Score() {
        return homeNode3Score;
    }

    public void setHomeNode3Score(String homeNode3Score) {
        this.homeNode3Score = homeNode3Score;
    }

    public String getHomeNode4Score() {
        return homeNode4Score;
    }

    public void setHomeNode4Score(String homeNode4Score) {
        this.homeNode4Score = homeNode4Score;
    }

    public String getHomeNode5Score() {
        return homeNode5Score;
    }

    public void setHomeNode5Score(String homeNode5Score) {
        this.homeNode5Score = homeNode5Score;
    }

    public String getAwayNode1Score() {
        return awayNode1Score;
    }

    public void setAwayNode1Score(String awayNode1Score) {
        this.awayNode1Score = awayNode1Score;
    }

    public String getAwayNode2Score() {
        return awayNode2Score;
    }

    public void setAwayNode2Score(String awayNode2Score) {
        this.awayNode2Score = awayNode2Score;
    }

    public String getAwayNode3Score() {
        return awayNode3Score;
    }

    public void setAwayNode3Score(String awayNode3Score) {
        this.awayNode3Score = awayNode3Score;
    }

    public String getAwayNode4Score() {
        return awayNode4Score;
    }

    public void setAwayNode4Score(String awayNode4Score) {
        this.awayNode4Score = awayNode4Score;
    }

    public String getAwayNode5Score() {
        return awayNode5Score;
    }

    public void setAwayNode5Score(String awayNode5Score) {
        this.awayNode5Score = awayNode5Score;
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

    public String getIsSources() {
        return isSources;
    }

    public void setIsSources(String isSources) {
        this.isSources = isSources;
    }

    public String getIsAnimation() {
        return isAnimation;
    }

    public void setIsAnimation(String isAnimation) {
        this.isAnimation = isAnimation;
    }

    public String getIsLive() {
        return isLive;
    }

    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }

    public String getHomeNodePauseCount() {
        return homeNodePauseCount;
    }

    public void setHomeNodePauseCount(String homeNodePauseCount) {
        this.homeNodePauseCount = homeNodePauseCount;
    }

    public String getAwayNodePauseCount() {
        return awayNodePauseCount;
    }

    public void setAwayNodePauseCount(String awayNodePauseCount) {
        this.awayNodePauseCount = awayNodePauseCount;
    }

    public String getHomeNodeFoulsCount() {
        return homeNodeFoulsCount;
    }

    public void setHomeNodeFoulsCount(String homeNodeFoulsCount) {
        this.homeNodeFoulsCount = homeNodeFoulsCount;
    }

    public String getAwayNodeFoulsCount() {
        return awayNodeFoulsCount;
    }

    public void setAwayNodeFoulsCount(String awayNodeFoulsCount) {
        this.awayNodeFoulsCount = awayNodeFoulsCount;
    }

    public String getHomeAssists() {
        return homeAssists;
    }

    public void setHomeAssists(String homeAssists) {
        this.homeAssists = homeAssists;
    }

    public String getHomeSteals() {
        return homeSteals;
    }

    public void setHomeSteals(String homeSteals) {
        this.homeSteals = homeSteals;
    }

    public String getHomeBlocks() {
        return homeBlocks;
    }

    public void setHomeBlocks(String homeBlocks) {
        this.homeBlocks = homeBlocks;
    }

    public String getAwayAssists() {
        return awayAssists;
    }

    public void setAwayAssists(String awayAssists) {
        this.awayAssists = awayAssists;
    }

    public String getAwaySteals() {
        return awaySteals;
    }

    public void setAwaySteals(String awaySteals) {
        this.awaySteals = awaySteals;
    }

    public String getAwayBlocks() {
        return awayBlocks;
    }

    public void setAwayBlocks(String awayBlocks) {
        this.awayBlocks = awayBlocks;
    }

    public String getHomeThreeCount() {
        return homeThreeCount;
    }

    public void setHomeThreeCount(String homeThreeCount) {
        this.homeThreeCount = homeThreeCount;
    }

    public String getHomeTwoCount() {
        return homeTwoCount;
    }

    public void setHomeTwoCount(String homeTwoCount) {
        this.homeTwoCount = homeTwoCount;
    }

    public String getHomeFreeCount() {
        return homeFreeCount;
    }

    public void setHomeFreeCount(String homeFreeCount) {
        this.homeFreeCount = homeFreeCount;
    }

    public String getHomeFreeRate() {
        return homeFreeRate;
    }

    public void setHomeFreeRate(String homeFreeRate) {
        this.homeFreeRate = homeFreeRate;
    }

    public String getAwayThreeCount() {
        return awayThreeCount;
    }

    public void setAwayThreeCount(String awayThreeCount) {
        this.awayThreeCount = awayThreeCount;
    }

    public String getAwayTwoCount() {
        return awayTwoCount;
    }

    public void setAwayTwoCount(String awayTwoCount) {
        this.awayTwoCount = awayTwoCount;
    }

    public String getAwayFreeCount() {
        return awayFreeCount;
    }

    public void setAwayFreeCount(String awayFreeCount) {
        this.awayFreeCount = awayFreeCount;
    }

    public String getAwayFreeRate() {
        return awayFreeRate;
    }

    public void setAwayFreeRate(String awayFreeRate) {
        this.awayFreeRate = awayFreeRate;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public List<String> getLiveUrl() {
        return live_url;
    }

    public void setLiveUrl(List<String> url) {
        this.live_url = url;
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
         * teamId : 2449
         * rank : 0
         * nameCn : 圣洛伦索
         * nameCnShort : 圣洛伦索
         * nameTrad : 聖羅倫素
         * nameTradShort : 聖羅倫素
         * nameEn : San Lorenzo- -Casla
         * nameEnShort : San Lorenzo- -Casla
         * logo :
         */

        private String teamId;
        private String rank;
        private String nameCn;
        private String nameCnShort;
        private String nameTrad;
        private String nameTradShort;
        private String nameEn;
        private String nameEnShort;
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

        public String getNameCnShort() {
            return nameCnShort;
        }

        public void setNameCnShort(String nameCnShort) {
            this.nameCnShort = nameCnShort;
        }

        public String getNameTrad() {
            return nameTrad;
        }

        public void setNameTrad(String nameTrad) {
            this.nameTrad = nameTrad;
        }

        public String getNameTradShort() {
            return nameTradShort;
        }

        public void setNameTradShort(String nameTradShort) {
            this.nameTradShort = nameTradShort;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getNameEnShort() {
            return nameEnShort;
        }

        public void setNameEnShort(String nameEnShort) {
            this.nameEnShort = nameEnShort;
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
         * teamId : 1049
         * rank : 0
         * nameCn : 奥布拉斯
         * nameCnShort : 奥布拉斯
         * nameTrad : 奧布拉斯
         * nameTradShort : 奥布拉斯
         * nameEn : Obras Sanitarias Buenos Aires
         * nameEnShort : Obras
         * logo :
         */

        private String teamId;
        private String rank;
        private String nameCn;
        private String nameCnShort;
        private String nameTrad;
        private String nameTradShort;
        private String nameEn;
        private String nameEnShort;
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

        public String getNameCnShort() {
            return nameCnShort;
        }

        public void setNameCnShort(String nameCnShort) {
            this.nameCnShort = nameCnShort;
        }

        public String getNameTrad() {
            return nameTrad;
        }

        public void setNameTrad(String nameTrad) {
            this.nameTrad = nameTrad;
        }

        public String getNameTradShort() {
            return nameTradShort;
        }

        public void setNameTradShort(String nameTradShort) {
            this.nameTradShort = nameTradShort;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getNameEnShort() {
            return nameEnShort;
        }

        public void setNameEnShort(String nameEnShort) {
            this.nameEnShort = nameEnShort;
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
         * leagueId : 79
         * leagueNameCnShort : 阿篮联
         * leagueNameTradShort : 阿籃聯
         * leagueNameEnShort : LNB
         */

        private String leagueId;
        private String leagueNameCnShort;
        private String leagueNameTradShort;
        private String leagueNameEnShort;

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
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
