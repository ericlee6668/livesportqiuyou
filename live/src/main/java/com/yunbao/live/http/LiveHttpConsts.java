package com.yunbao.live.http;

/**
 * Created by cxf on 2019/3/21.
 */

public class LiveHttpConsts {
    public static final String GET_COIN = "getCoin";
    public static final String GET_USER_LIST = "getUserList";
    public static final String ROOM_CHARGE = "roomCharge";
    public static final String TIME_CHARGE = "timeCharge";
    public static final String GET_LIVE_RECORD = "getLiveRecord";
    public static final String GET_ALI_CDN_RECORD = "getAliCdnRecord";
    public static final String GET_ALL_IMPRESS = "getAllImpress";
    public static final String SET_IMPRESS = "setImpress";
    public static final String GET_ADMIN_LIST = "getAdminList";
    public static final String SET_ADMIN = "setAdmin";
    public static final String GET_LIVE_SHUT_UP_LIST = "getLiveShutUpList";
    public static final String LIVE_CANCEL_SHUT_UP = "LiveCancelShutUp";
    public static final String GET_LIVE_BLACK_LIST = "getLiveBlackList";
    public static final String LIVE_CANCEL_BLACK = "liveCancelBlack";
    public static final String GET_LIVE_END_INFO = "getLiveEndInfo";
    public static final String SET_REPORT = "setReport";
    public static final String GET_LIVE_REPORT_LIST = "getLiveReportList";
    public static final String GET_LIVE_USER = "getLiveUser";
    public static final String KICKING = "kicking";
    public static final String SET_SHUT_UP = "setShutUp";
    public static final String SUPER_CLOSE_ROOM = "superCloseRoom";
    public static final String GET_GUARD_BUY_LIST = "getGuardBuyList";
    public static final String BUY_GUARD = "buyGuard";
    public static final String GET_GUARD_LIST = "getGuardList";
    public static final String GET_LINK_MIC_STREAM = "getLinkMicStream";
    public static final String LINK_MIC_SHOW_VIDEO = "linkMicShowVideo";
    public static final String SET_LINK_MIC_ENABLE = "setLinkMicEnable";
    public static final String CHECK_LINK_MIC_ENABLE = "checkLinkMicEnable";
    public static final String LIVE_PK_CHECK_LIVE = "livePkCheckLive";
    public static final String SEND_RED_PACK = "sendRedPack";
    public static final String GET_RED_PACK_LIST = "getRedPackList";
    public static final String ROB_RED_PACK = "robRedPack";
    public static final String GET_RED_PACK_RESULT = "getRedPackResult";
    public static final String SEND_DANMU = "sendDanmu";
    public static final String CHECK_LIVE = "checkLive";
    public static final String ENTER_ROOM = "enterRoom";
    public static final String GET_GIFT_LIST = "getGiftList";
    public static final String SEND_GIFT = "sendGift";
    public static final String LIVE_PK_SEARCH_ANCHOR = "livePkSearchAnchor";
    public static final String GET_LIVE_PK_LIST = "getLivePkList";
    public static final String SEARCH_MUSIC = "searchMusic";
    public static final String GET_MUSIC_URL = "getMusicUrl";
    public static final String CREATE_ROOM = "createRoom";
    public static final String CHANGE_LIVE = "changeLive";
    public static final String STOP_LIVE = "stopLive";
    public static final String GET_LIVE_SDK = "getLiveSdk";
    public static final String GET_TX_LINK_MIC_ACC_URL = "getTxLinkMicAccUrl";
    public static final String LINK_MIC_TX_MIX_STREAM = "linkMicTxMixStream";
    public static final String GET_MY_ADMIN_ROOM_LIST = "getMyAdminRoomList";
    public static final String GET_LIVE_GIFT_PRIZE_POOL = "getLiveGiftPrizePool";
    public static final String ANCHOR_CHECK_LIVE = "anchorCheckLive";
    public static final String GET_LIVE_INFO = "getLiveInfo";
    public static final String GET_TURN_TABLE = "GetTurntable";
    public static final String TURN = "Turn";
    public static final String GET_WIN = "Turntable.GetWin";
    public static final String GET_BACK_PACK = "Backpack.getBackpack";
    public static final String GET_SALE = "Shop.GetSale";

    public static final String SET_SALE = " Shop.SetSale";
    public static final String SEARCH_LIVE_GOODS_LIST = "Shop.GetGoodsList";
    public static final String SET_LIVE_GOODS_SHOW = "setLiveGoodsShow";
    public static final String GET_MATCH_DATA = "Match.GetMatchData";
    public static final String GET_MATCH_INDEX = "Match.GetMatchIndex";
    public static final String GET_LOL_MATCH_LIST_BY_ANALYSIS = "Match.GetMatchAnalysis";
    public static final String GET_LOL_MATCH_PLAYER = "Match.GetMatchPlayer";
    public static final String GET_LOL_RANDOM_RECOMMEND = "Home.GetRecommendedStream";
    public static final String GET_LOL_BATTLE_OUTLINE = "Match.GetMatchOutline";
    public static final String GET_HOT = "Home.GetHot";
    public static final String WEB_SOCKET_GAME_URL = "ws://139.9.54.17:9501/"; //赛事测试ws
    public static final String WEB_SOCKET_CHAT_URL = "ws://124.71.10.194:9511/";//聊天测试ws
    public static final String GET_SUBSCRIBE_ANCHOR = "Home.SubscribeAnchor";
    public static final String GET_MATCH_INFO = "Match.GetMatchInfo";
    public static final String GET_WATER_MARK = "Watermark.GetWatermark";
    public static final String LEAVE_ROOM = "Live.LeaveRoom";
    public static final String LIVE_RECOMMEND = "/api/v1/live/getRandomList";
    public static final String GET_FOOTBALL_MATCH = "/api/v3/football/getMatch";
    public static final String GET_BASKETBALL_MATCH = "/api/v3/basketball/getMatch";
    public static final String GET_LIVE_INFO_CONTACT = "Live.getLiveInfo";

    /**
     * 足球阵容
     */
    public static final String FOOTBALL_LINE_UP = "/api/v3/football/getMatchOldLineup";
    /**
     * 足球情报
     */
    public static final String FOOTBALL_INTELLIGENCE = "/api/v3/football/getMatchIntelligence";
    /**
     * 足球赛况
     */
    public static final String FOOTBALL_LIVE_URL = "/api/v3/football/getMatchLive";
    /**
     * 足球赛况分析
     */
    public static final String FOOTBALL_MATCH_ANALYSIS = "/api/v3/football/getMatchAnalysis";
    /**
     * 篮球情报
     */
    public static final String BASKETBALL_INTELLIGENCE = "/api/v3/basketball/getMatchIntelligence";
    /**
     * 足球指数
     */
    public static final String FOOTBALL_MATCH_INDEX = "/api/v3/football/getMatchIndex";
    /**
     * 足球指数
     */
    public static final String BASKETBALL_MATCH_INDEX = "/api/v3/basketball/getMatchIndex";
    public static final String BASKETBALL_LINEUP = "/api/v3/basketball/getMatchLineup";
    public static final String BASKETBALL_ANALYSIS = "/api/v3/basketball/getMatchAnalysis";
    public static final String BASKETBALL_MATCH_LIVE = "/api/v3/basketball/getMatchLive";
    public static final String BASKETBALL_RELATED_LIVE = "/api/v3/basketball/getMatchRelatedLive";
    public static final String FOOTBALL_RELATED_LIVE = "/api/v3/football/getMatchRelatedLive";
}
