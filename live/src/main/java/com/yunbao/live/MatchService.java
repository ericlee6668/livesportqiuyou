package com.yunbao.live;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.GameBasketballMatchBean;
import com.yunbao.common.bean.GameFootballMatchBean;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.utils.AppLog;
import com.yunbao.live.event.MatchEvent;
import com.yunbao.live.http.WsManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

public class MatchService extends Service {
    private WsManager wsManager;
    private WebSocket mWebSocketClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLog.e("开启推送赛事");
        startSocketConnect();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 赛事socket
     */
    private void startSocketConnect() {
        wsManager = new WsManager.Builder(this, false, true).client(
                new OkHttpClient().newBuilder()
                        .pingInterval(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true)
                .wsUrl(CommonAppConfig.WEBSOCKET_MATCH).build();
        wsManager.setOnReceiveListener(new WsManager.OnReceiveListener() {
            @Override
            public void onMessage(String message) {
                sendMsg(message);
            }

            @Override
            public void onOpen() {
                mWebSocketClient = wsManager.getWebSocket();
            }
        });
        wsManager.startConnect();
    }

    /**
     * 收到的socket数据
     *
     * @param message
     */
    private void sendMsg(String message) {
        try {
            org.json.JSONObject object = new org.json.JSONObject(message);
            int code = object.getInt("code");
            if (code == 1) {
                org.json.JSONObject data = object.getJSONObject("data");
                if (data.has("data")) {
                    JSONObject dat = data.getJSONObject("data");
                    int match_type = dat.getInt("type");
                    String info = dat.getString("info");
                    AppLog.e(info);
//                    1-电竞 2-蓝球  3-足球
                    if (match_type == 1) {
                        AppLog.e("推送赛事---------->电竞");
                        ArrayList<GameLolMatchBean> list = new Gson().fromJson(info, new TypeToken<ArrayList<GameLolMatchBean>>() {
                        }.getType());
                        EventBus.getDefault().post(new MatchEvent(1, list, null, null));
                    }
                    if (match_type == 2) {
                        AppLog.e("推送赛事---------->蓝球");
                        ArrayList<GameBasketballMatchBean> list = new Gson().fromJson(info, new TypeToken<ArrayList<GameBasketballMatchBean>>() {
                        }.getType());
                        EventBus.getDefault().post(new MatchEvent(2, null, list, null));
                    }
                    if (match_type == 3) {
                        AppLog.e("推送赛事---------->足球");
                        ArrayList<GameFootballMatchBean> list = new Gson().fromJson(info, new TypeToken<ArrayList<GameFootballMatchBean>>() {
                        }.getType());
                        EventBus.getDefault().post(new MatchEvent(3, null, null, list));
                    }
                }
//                org.json.JSONObject data2 = data.getJSONObject("data");
//                String info = data2.getString("info");
            }
        } catch (JSONException e) {
            AppLog.e("sendMsg:" + e.toString());
        }
    }

    public void disConnect() {
        if (wsManager != null) {
            wsManager.stopConnect();
        }
        if (mWebSocketClient != null) {
            mWebSocketClient.cancel();
        }
    }

    @Override
    public void onDestroy() {
        disConnect();
        AppLog.e("推送赛事服务销毁");
        super.onDestroy();
    }
}
