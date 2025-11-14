package com.yunbao.live.socket;

import com.google.gson.Gson;
import com.yunbao.common.Constants;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.SystemUtil;

import java.util.HashMap;

import okhttp3.WebSocket;

/**
 * Created by cxf on 2018/10/9.
 * 直播间发言
 */

public class WebSocketChatUtil {


//    public static void initWebSocket(JWebSocketClient client, String uid, String liveUid) {
//        if (client == null) {
//            return;
//        }
//        if (client.isOpen()) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("type", "init");
//            HashMap<String, String> msgMap = new HashMap<>();
//            msgMap.put("uid", uid == null ? "0" : uid);
//            msgMap.put("liveuid", liveUid);
//            msgMap.put("livetype", "live");
//            msgMap.put("send", "");
//            map.put("msg", msgMap);
//            String json = new Gson().toJson(map);
//
//            Log.e("mWebSocketClient", json);
//            client.send(json);
//        }
//    }


    /**
     * 发言
     */
    public static void sendMessage(WebSocket client, String uid, String liveUid, String content, String type) {
        if (client == null) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        HashMap<String, String> msgMap = new HashMap<>();
        msgMap.put("uid", uid == null ? "0" : uid);
        msgMap.put("liveuid", liveUid);
        msgMap.put("livetype", "live");
        if (type.equals(Constants.CHAT_INIT)) {
            msgMap.put("device", "android");
        }
        msgMap.put("send", content);
        map.put("msg", msgMap);
        String json = new Gson().toJson(map);

        AppLog.e("mWebSocketClient", json);
        client.send(json);
    }

    /**
     * 赛事信息
     *
     * @param socket
     */
    public static void sendMatchMessage(WebSocket socket, String match_type, String match_id) {
        if (socket == null) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("match_type ", match_type);
        map.put("match_id", match_id);
        map.put("device ", "android");
        map.put("DeviceId", SystemUtil.getDeviceId());
        String json = new Gson().toJson(map);
        AppLog.e("mWebSocketClient", json);
        socket.send(json);
    }

}
