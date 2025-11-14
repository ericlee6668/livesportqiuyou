package com.yunbao.common.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.tencent.live.OkHttpBuilder;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppConfigNewX;
import com.yunbao.common.CommonAppContext;

import okhttp3.OkHttpClient;

/**针对 后台新框架 管理类
 * Created by cxf on 2018/9/17.
 */

public class HttpClientNewX {

    private static HttpClientNewX sInstance;
    private String mLanguage;//语言
    private static String mUrl;
    private static String mVideoUrl;
    private static String mSportsUrl;
    private final OkHttpClient mOkHttpClient;
    private final OkHttpBuilder mBuilder;
//    /api/v1/live
    private static final String API1="/api";
    private static final String V1="/v1";
    private static final String LIVE="/live";

    private HttpClientNewX() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);
        mBuilder = new OkHttpBuilder();
        mOkHttpClient = mBuilder
                .setHost(CommonAppConfigNewX.listURL.get(0))
                .setTimeout(10000)
                .setLoggingInterceptor(loggingInterceptor)
                .build(CommonAppContext.sInstance);
    }

    public static HttpClientNewX getInstanceNewX() {
        mUrl = CommonAppConfigNewX.getInstanceX().listURL.get(0) + API1+V1+LIVE+"/";
        //TODO 这个只针对后台新框架 后面可能会用到
//        mVideoUrl = CommonAppConfig.VIDEO_HOST + "/?service=";
//        mSportsUrl = CommonAppConfig.SPORTS_HOST;
        if (sInstance == null) {
            synchronized (HttpClientNewX.class) {
                if (sInstance == null) {
                    sInstance = new HttpClientNewX();
                }
            }
        }
        return sInstance;
    }


    public GetRequest<JsonBeanNewX> getDataObject(String serviceName, String tag) {
        return mBuilder.req1(mUrl + serviceName, tag, JsonBeanNewX.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<String> get(String serviceName, String tag) {
        return mBuilder.req1(mUrl + serviceName, tag, String.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);

    }

    public PostRequest<JsonBeanNewX> post(String serviceName, String tag) {
        return mBuilder.req2(mUrl + serviceName, tag, JsonBeanNewX.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonObjectBean> getVideoDataObject(String serviceName, String tag) {
        return mBuilder.req1(mVideoUrl + serviceName, tag, JsonObjectBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonBeanNewX> getVideo(String serviceName, String tag) {
        return mBuilder.req1(mVideoUrl + serviceName, tag, JsonBeanNewX.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public PostRequest<JsonBeanNewX> postVideo(String serviceName, String tag) {
        return mBuilder.req2(mVideoUrl + serviceName, tag, JsonBeanNewX.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonBeanNewX> getSports(String serviceName, String tag) {
        return mBuilder.req1(mSportsUrl + serviceName, tag, JsonBeanNewX.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonObjectBean> getSportsDataObject(String serviceName, String tag) {
        return mBuilder.req1(mSportsUrl + serviceName, tag, JsonObjectBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public PostRequest<JsonBeanNewX> postSports(String serviceName, String tag) {
        return mBuilder.req2(mSportsUrl + serviceName, tag, JsonBeanNewX.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public void cancel(String tag) {
        mBuilder.cancel(mOkHttpClient, tag);
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public <T> GetRequest<T> getV2Request(String serviceName, String tag) {
        GetRequest<T> request = OkGo.get(mSportsUrl + serviceName);
        request.params(CommonHttpConsts.LANGUAGE, mLanguage);
        request.tag(tag);
        return request;
    }
}
