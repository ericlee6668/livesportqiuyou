package com.yunbao.live.httpX;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.bean.BasketballLineup;
import com.yunbao.common.bean.FightHistory;
import com.yunbao.common.bean.FootballHistory;
import com.yunbao.common.bean.FootballLineup;
import com.yunbao.common.bean.GameIntelligence;
import com.yunbao.common.bean.GameOpening;
import com.yunbao.common.bean.MatchAnalysis;
import com.yunbao.common.bean.MatchBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.HttpCallbackNewX;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.http.HttpClient;
import com.yunbao.common.http.HttpClientNewX;
import com.yunbao.common.http.JsonBean;
import com.yunbao.common.http.V2Callback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.live.LiveConfig;
import com.yunbao.live.bean.BasketballLive;
import com.yunbao.live.bean.MatchLiveBean;
import com.yunbao.live.http.LiveHttpConsts;

import java.io.File;
import java.util.List;

/**
 * Created by cxf on 2019/3/21.
 */

public class LiveHttpUtilNewX {


    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }





//TODO 2021-11-06
    /**
     * 获取推荐直播
     */
    public static void getHomeHotNewX(int p, int limit, HttpCallbackNewX callback) {
        HttpClientNewX.getInstanceNewX()
                .get(LiveHttpConstsNewX.GET_HOME_GET_HOT_LIST, LiveHttpConstsNewX.GET_HOME_GET_HOT_LIST)
                .params("limit", limit)
                .params("page", p)
                .execute(callback);
    }

}
