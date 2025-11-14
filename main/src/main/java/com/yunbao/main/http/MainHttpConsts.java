package com.yunbao.main.http;

/**
 * Created by cxf on 2019/2/16.
 */

public class MainHttpConsts {
    public static final String LOGIN = "setLoginInfo";
    public static final String LOGIN_BY_THIRD = "loginByThird";
    public static final String GET_LOGIN_INFO = "getLoginInfo";
    public static final String CANCEL_ACCOUNT = "cancelAccount";
    public static final String GET_CANCEL_CONDITION = "getCancelCondition";
    public static final String REQUEST_BONUS = "requestBonus";
    public static final String GET_BONUS = "getBonus";
    public static final String SET_DISTRIBUT = "setDistribut";
    public static final String GET_HOT = "getHot";
    public static final String GET_FOLLOW = "getFollow";
    public static final String GET_NEAR = "getNear";
    public static final String PROFIT_LIST = "profitList";
    public static final String CONSUME_LIST = "consumeList";
    public static final String GET_BASE_INFO = "getBaseInfo";
    public static final String SET_BLACK = "setBlack";
    public static final String GET_USER_HOME = "getUserHome";
    public static final String GET_SETTING_LIST = "getSettingList";
    public static final String SEARCH = "search";
    public static final String GET_FOLLOW_LIST = "getFollowList";
    public static final String GET_FANS_LIST = "getFansList";
    public static final String UPDATE_AVATAR = "updateAvatar";
    public static final String UPDATE_FIELDS = "updateFields";
    public static final String GET_MY_IMPRESS = "getMyImpress";
    public static final String GET_PROFIT = "getProfit";
    public static final String GET_USER_ACCOUNT_LIST = "GetUserAccountList";
    public static final String ADD_CASH_ACCOUNT = "addCashAccount";
    public static final String DEL_CASH_ACCOUNT = "deleteCashAccount";
    public static final String DO_CASH = "doCash";
    public static final String GET_CLASS_LIVE = "getClassLive";
    public static final String GET_RECOMMEND = "getRecommend";
    public static final String RECOMMEND_FOLLOW = "recommendFollow";
    public static final String GET_REGISTER_CODE = "getRegisterCode";
    public static final String REGISTER = "register";
    public static final String FIND_PWD = "findPwd";
    public static final String GET_FIND_PWD_CODE = "getFindPwdCode";
    public static final String GET_QR_CODE = "getQrCode";
    public static final String MODIFY_PWD = "modifyPwd";
    public static final String GET_SHOP = "Shop.GetShop";
    public static final String SET_GOODS = " Shop.SetGoods";
    public static final String GET_RECOMMENT = "Shop.GetRecomment";
    public static final String SHOP_UPSTATUS = "Shop.UpStatus";
    public static final String DEL_GOODS = "Shop.DelGoods";
    public static final String GET_APPLETS = "Shop.GetApplets";
    public static final String GET_SHOP_INFO = "Shop.GetShop_info";
    public static final String ACTIVE_PUBLISH = "activePublish";
    public static final String GET_HOME_ACTIVE = "getHomeActive";
    public static final String GET_ACTIVE_RECOMMEND = "getActiveRecommend";
    public static final String GET_ACTIVE_FOLLOW = "getActiveFollow";
    public static final String GET_ACTIVE_NEWEST = "getActiveNewest";
    public static final String ACTIVE_ADD_LIKE = "activeAddLike";
    public static final String ACTIVE_REPORT = "activeReport";
    public static final String GET_ACTIVE_REPORT_LIST = "getActiveReportList";
    public static final String ACTIVE_DELETE = "activeDelete";
    public static final String ACTIVE_COMMENT = "activeComment";
    public static final String GET_ACTIVE_COMMENTS = "getActiveComments";
    public static final String SET_ACTIVE_COMMENT_LIKE = "setActiveCommentLike";
    public static final String GET_ACTIVE_COMMENT_REPLY = "getActiveCommentReply";


    /// ******************************************** 以下V2.0新增 *********************************************** ///
    public static final String GET_RECOMMEND_LIVE = "Home.GetRecommendLive";
    public static final String GET_SLIDE_EVENTS = "Home.GetSlideEvents";
    public static final String GET_LOL_MATCH_LIST = "Match.GetLOLMatchList";
    public static final String GET_LOL_MATCH_LIS_BY_STAT = "Match.GetLOLMatchListByStat";
    public static final String GET_SubscribeAnchor = "Home.SubscribeAnchor";
    public static final String GET_RECOMMEND_ANCHORR = "Home.GetRecommendAnchorr";
    public static final String GET_HOME_GET_HOT = "Home.GetHot";

    public static final String GET_SUBSCRIBE = "User.GetSubscribe";
    public static final String GET_SLIDE = "Slide.GetSlide";
    public static final String GET_SLIDE_CICK_SLIDE_ITEM = "Slide.ClickSlideItem";

    public static final String FOOTBALL_NATCH_ALL_LIST = "/api/v3/football/getMatchAllList";
    public static final String FOOTBALL_NATCH_PLAYING_LIST = "/api/v3/football/getMatchPlayingList";
    public static final String FOOTBALL_NATCH_LIST_BY_DATE = "/api/v3/football/getMatchListByDate";
    public static final String BASKETBALL_NATCH_ALL_LIST = "/api/v3/basketball/getMatchAllList";
    public static final String BASKETBALL_NATCH_PLAYING_LIST = "/api/v3/basketball/getMatchPlayingList";
    public static final String BASKETBALL_NATCH_LIST_BY_DATE = "/api/v3/basketball/getMatchListByDate";
}
