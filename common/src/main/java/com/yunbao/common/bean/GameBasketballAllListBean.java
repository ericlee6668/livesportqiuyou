package com.yunbao.common.bean;

import java.util.List;

public class GameBasketballAllListBean {
    /**
     * date : 2020-11-06
     * list : [{"matchId":91704,"leagueId":1162,"matchStartTime":1604646000,"state":10,"isNeutral":2,"locationCn":"","locationEn":"","homeId":35215,"awayId":22717,"homeScore":0,"awayScore":0,"homeHalfScore":0,"awayHalfScore":0,"homeCornerNum":1,"awayCornerNum":0,"extraFirstKick":0,"extraNormalTime":0,"extraNormalScore":"","extraTwoLegsScore":"","extraType":0,"extraScore":"","extraPenaltyKickScore":"","extraWin":0,"letgoalHomeOdds":0.95,"letgoalGoal":1.25,"letgoalAwayOdds":0.85,"letgoalIsEntertained":2,"europeHomeOdds":1.44,"europeFlatOdds":4.75,"europeAwayOdds":4.75,"europeIsEntertained":1,"totalScoreHomeOdds":0.95,"totalScoreGoal":4,"totalScoreAwayOdds":0.85,"totalScoreIsEntertained":1,"hasSources":0,"hasAnimation":0,"hasLive":0,"weatherEn":"","weatherCn":"","weatherIcon":"","temperature":"","match_date":"2020-11-06","live_url":null,"state_str":"上半场","is_playing":true,"home_team":{"teamId":35215,"rank":0,"nameCn":"霍巴特联","nameTrad":"霍巴特联","nameEn":"Hobart United","logo":""},"away_team":{"teamId":22717,"rank":0,"nameCn":"海滨","nameTrad":"海濱","nameEn":"Beach City","logo":""},"league":{"leagueId":1162,"leagueNameCn":"澳大利亚塔斯马尼亚锦标赛","leagueNameTrad":"澳洲塔斯馬尼亞锦标賽","leagueNameEn":"TAS Premier Championship","leagueNameCnShort":"澳塔锦","leagueNameTradShort":"澳塔锦","leagueNameEnShort":"TSA PC"}},{"matchId":91705,"leagueId":1162,"matchStartTime":1604646000,"state":10,"isNeutral":2,"locationCn":"","locationEn":"","homeId":29343,"awayId":17911,"homeScore":0,"awayScore":0,"homeHalfScore":0,"awayHalfScore":0,"homeCornerNum":0,"awayCornerNum":0,"extraFirstKick":0,"extraNormalTime":0,"extraNormalScore":"","extraTwoLegsScore":"","extraType":0,"extraScore":"","extraPenaltyKickScore":"","extraWin":0,"letgoalHomeOdds":0.9,"letgoalGoal":0.75,"letgoalAwayOdds":0.9,"letgoalIsEntertained":1,"europeHomeOdds":1.6600000000000001,"europeFlatOdds":4.2,"europeAwayOdds":3.6,"europeIsEntertained":1,"totalScoreHomeOdds":0.85,"totalScoreGoal":3.75,"totalScoreAwayOdds":0.95,"totalScoreIsEntertained":1,"hasSources":0,"hasAnimation":0,"hasLive":0,"weatherEn":"","weatherCn":"","weatherIcon":"","temperature":"","match_date":"2020-11-06","live_url":null,"state_str":"上半场","is_playing":true,"home_team":{"teamId":29343,"rank":0,"nameCn":"塔洛纳","nameTrad":"塔洛纳","nameEn":"Taroona","logo":""},"away_team":{"teamId":17911,"rank":0,"nameCn":"新城鹰","nameTrad":"新城鷹","nameEn":"New Town Eagles","logo":""},"league":{"leagueId":1162,"leagueNameCn":"澳大利亚塔斯马尼亚锦标赛","leagueNameTrad":"澳洲塔斯馬尼亞锦标賽","leagueNameEn":"TAS Premier Championship","leagueNameCnShort":"澳塔锦","leagueNameTradShort":"澳塔锦","leagueNameEnShort":"TSA PC"}},{"matchId":28885,"leagueId":4,"matchStartTime":1604622600,"state":60,"isNeutral":2,"locationCn":"卡斯特洛球场","locationEn":"Estadio Placido Aderaldo Castelo","homeId":1764,"awayId":415,"homeScore":0,"awayScore":0,"homeHalfScore":0,"awayHalfScore":0,"homeCornerNum":0,"awayCornerNum":0,"extraFirstKick":0,"extraNormalTime":0,"extraNormalScore":"","extraTwoLegsScore":"","extraType":0,"extraScore":"","extraPenaltyKickScore":"","extraWin":0,"letgoalHomeOdds":0,"letgoalGoal":0,"letgoalAwayOdds":0,"letgoalIsEntertained":0,"europeHomeOdds":0,"europeFlatOdds":0,"europeAwayOdds":0,"europeIsEntertained":0,"totalScoreHomeOdds":0,"totalScoreGoal":0,"totalScoreAwayOdds":0,"totalScoreIsEntertained":0,"hasSources":0,"hasAnimation":1,"hasLive":0,"weatherEn":"","weatherCn":"","weatherIcon":"","temperature":"","match_date":"2020-11-06","live_url":null,"state_str":"未开始","is_playing":false,"home_team":{"teamId":1764,"rank":15,"nameCn":"塞阿拉","nameTrad":"施亞拉","nameEn":"Ceara","logo":"https://guqniupic.oss-cn-hangzhou.aliyuncs.com/fb/team/20130916173715.png"},"away_team":{"teamId":415,"rank":4,"nameCn":"圣保罗","nameTrad":"聖保羅","nameEn":"Sao Paulo","logo":"https://guqniupic.oss-cn-hangzhou.aliyuncs.com/fb/team/20130913230159.png"},"league":{"leagueId":4,"leagueNameCn":"巴西甲组联赛","leagueNameTrad":"巴西甲組聯賽","leagueNameEn":"Brazil Serie A","leagueNameCnShort":"巴西甲","leagueNameTradShort":"巴西甲","leagueNameEnShort":"BRA D1"}}]
     */

    private String date;
    private String week_day_str;
    private List<GameBasketballMatchBean> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek_day_str() {
        return week_day_str;
    }

    public void setWeek_day_str(String week_day_str) {
        this.week_day_str = week_day_str;
    }

    public List<GameBasketballMatchBean> getList() {
        return list;
    }

    public void setList(List<GameBasketballMatchBean> list) {
        this.list = list;
    }
}
