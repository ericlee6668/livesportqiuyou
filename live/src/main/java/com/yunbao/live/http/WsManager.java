package com.yunbao.live.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.utils.AppLog;
import com.yunbao.live.interfaces.IWsManager;
import com.yunbao.live.socket.WebSocketChatUtil;
import com.yunbao.live.utils.WsStatus;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WsManager implements IWsManager {
    private final static int RECONNECT_INTERVAL = 10 * 1000;    //重连自增步长
    private final static long RECONNECT_MAX_TIME = 120 * 1000;   //最大重连间隔
    private final boolean isChat;
    private final boolean isMatch;
    private final Context mContext;
    private final String wsUrl;
    private WebSocket mWebSocket;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private int mCurrentStatus = WsStatus.DISCONNECTED;     //websocket连接状态
    private final boolean isNeedReconnect;          //是否需要断线自动重连
    private boolean isManualClose = false;         //是否为手动关闭websocket连接
    private final Lock mLock;
    private final Handler wsMainHandler = new Handler(Looper.getMainLooper());
    private int reconnectCount = 0;   //重连次数
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔30秒进行一次对长连接的心跳检测
    private final Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            AppLog.e("websocket", "服务器重连接中...");
            buildConnect();
        }
    };
    private OnReceiveListener listener;
    private String liveUid;
    private String match_type;
    private String match_id;
    private long sendTime =0L;

    public void setLiveId(String mLiveUid) {
        this.liveUid = mLiveUid;
    }

    public void setMatchType(String match_type) {
        this.match_type = match_type;
    }

    public void setMatchId(String match_id) {
        this.match_id = match_id;
    }

    public interface OnReceiveListener {
        void onMessage(String msg);

        void onOpen();
    }

    public void setOnReceiveListener(OnReceiveListener listener) {
        this.listener = listener;
    }

    public WebSocketListener mWebSocketListener = new WebSocketListener() {

        @Override
        public void onOpen(WebSocket webSocket, final Response response) {
            mWebSocket = webSocket;
            listener.onOpen();
            setCurrentStatus(WsStatus.CONNECTED);
            connected();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                if (isChat) {
                    WebSocketChatUtil.sendMessage(webSocket, CommonAppConfig.getInstance().getUid(), liveUid, "", Constants.CHAT_INIT);
                }
                if (isMatch) {
                    WebSocketChatUtil.sendMatchMessage(webSocket, match_type, match_id);
                }

            } else {
                AppLog.e("websocket", "服务器连接成功");
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, final ByteString bytes) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppLog.e("websocket", "WsManager-----onMessage");

                    }
                });
            } else {
                AppLog.e("websocket", "WsManager-----onMessage");
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppLog.e("websocket", "WsManager-----onMessage:" + text);
                        if (listener != null) {
                            listener.onMessage(text);
                        }
                    }
                });
            } else {
                AppLog.e("websocket", "WsManager-----onMessage");
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, final int code, final String reason) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppLog.e("websocket", "服务器连接关闭中");
                    }
                });
            } else {
                AppLog.e("websocket", "服务器连接关闭中");
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, final int code, final String reason) {

            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppLog.e("websocket", "服务器连接已关闭");
                    }
                });
            } else {
                AppLog.e("websocket", "服务器连接已关闭");
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, final Throwable t, final Response response) {
            try {
                if ("Socket closed".equals(t.getMessage())) {
                    return;
                }
                tryReconnect();
                AppLog.e("websocket", "[走的链接失败这里！！！！！！！！！！！！！！！！]");
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AppLog.e("websocket", "服务器连接失败");
                        }
                    });
                } else {
                    AppLog.e("websocket", "服务器连接失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public WsManager(Builder builder) {
        mContext = builder.mContext;
        isMatch = builder.isMatch;
        isChat = builder.isChat;
        wsUrl = builder.wsUrl;
        isNeedReconnect = builder.needReconnect;
        mOkHttpClient = builder.mOkHttpClient;
        this.mLock = new ReentrantLock();
    }

    private void initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        if (TextUtils.isEmpty(wsUrl)){return;}
        if (mRequest == null) {
            mRequest = new Request.Builder()
                    .url(wsUrl)
                    .build();
        }
        mOkHttpClient.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }


    @Override
    public synchronized boolean isWsConnected() {
        return mCurrentStatus == WsStatus.CONNECTED;
    }

    @Override
    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }

    @Override
    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }

    @Override
    public void startConnect() {
        isManualClose = false;
        buildConnect();
    }

    @Override
    public void stopConnect() {
        isManualClose = true;
        disconnect();
    }

    private void tryReconnect() {
        int maxReconnectCount = 15;
        if (reconnectCount > maxReconnectCount) {
            disconnect();
            return;
        }
        if (!isNeedReconnect | isManualClose) {
            return;
        }
        AppLog.e("websocket", "reconnectCount2222222[" + reconnectCount + "]");
        if (!isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
            AppLog.e("websocket", "[请您检查网络，未连接]");
//            return;
        }
        setCurrentStatus(WsStatus.RECONNECT);
        AppLog.e("websocket", "reconnectCount11111111[" + reconnectCount + "]");
        long delay = reconnectCount * RECONNECT_INTERVAL;
//        wsMainHandler.postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        wsMainHandler.postDelayed(reconnectRunnable, 10000);
        AppLog.e("websocket", "reconnectCount[" + reconnectCount + "]");
        reconnectCount++;

    }

    private void cancelReconnect() {
        wsMainHandler.removeCallbacks(reconnectRunnable);
        reconnectCount = 0;
    }

    private void connected() {
        cancelReconnect();
    }

    private void disconnect() {
        if (mCurrentStatus == WsStatus.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
        if (mWebSocket != null) {
            boolean isClosed = mWebSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE);
            //非正常关闭连接
            if (!isClosed) {
                AppLog.e("websocket", "服务器连接失败");
            }
        }
        mHandler.removeCallbacks(heartBeatRunnable);
        setCurrentStatus(WsStatus.DISCONNECTED);
    }

    private synchronized void buildConnect() {
        if (!isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
//            return;
        }
        switch (getCurrentStatus()) {
            case WsStatus.CONNECTED:
            case WsStatus.CONNECTING:
                break;
            default:
                setCurrentStatus(WsStatus.CONNECTING);
                initWebSocket();
        }
        if(isChat) {
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        }
    }
    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            buildConnect();
        }
    }
    // 发送心跳包
    private final Handler mHandler = new Handler();

    private final Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                if(mWebSocket!=null) {
                    boolean isSuccess = mWebSocket.send("{\"type\":\"heartbeat\"}");//发送一个消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                    if (!isSuccess) {//长连接已断开，
                        AppLog.e("TAG", "发送心跳包-------------长连接已断开");
                        mHandler.removeCallbacks(heartBeatRunnable);
                        mWebSocket.cancel();//取消掉以前的长连接
                        new InitSocketThread().start();//创建一个新的连接
                    } else {//长连接处于连接状态---
                        AppLog.e("TAG", "发送心跳包-------------长连接处于连接状态");
                    }
                }

                sendTime = System.currentTimeMillis();
            }

            mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
        }
    };


    //发送消息
    @Override
    public boolean sendMessage(String msg) {
        return send(msg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }

    private boolean send(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null && mCurrentStatus == WsStatus.CONNECTED) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            //发送消息失败，尝试重连
            if (!isSend) {
                tryReconnect();
            }
        }
        return isSend;
    }

    //检查网络是否连接
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static final class Builder {

        private final boolean isMatch;
        private final boolean isChat;
        private final Context mContext;
        private String wsUrl;
        private boolean needReconnect = true;
        private OkHttpClient mOkHttpClient;

        public Builder(Context val, boolean isChat, boolean isMatch) {
            mContext = val;
            this.isChat = isChat;
            this.isMatch = isMatch;
        }

        public Builder wsUrl(String val) {
            wsUrl = val;
            return this;
        }

        public Builder client(OkHttpClient val) {
            mOkHttpClient = val;
            return this;
        }

        public Builder needReconnect(boolean val) {
            needReconnect = val;
            return this;
        }

        public WsManager build() {
            return new WsManager(this);
        }

    }

}
