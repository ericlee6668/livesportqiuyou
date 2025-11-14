package com.yunbao.common.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.tencent.live.OkHttpBuilder;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;

import okhttp3.OkHttpClient;

/**
 * Created by cxf on 2018/9/17.
 */

public class HttpClient {

    private static HttpClient sInstance;
    private String mLanguage;//语言
    private static String mUrl;
    private static String mVideoUrl;
    private static String mSportsUrl;
    private final OkHttpClient mOkHttpClient;
    private final OkHttpBuilder mBuilder;

    private HttpClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);
        mBuilder = new OkHttpBuilder();
        mOkHttpClient = mBuilder
                .setHost(CommonAppConfig.HOST)
                .setTimeout(10000)
                .setLoggingInterceptor(loggingInterceptor)
                .build(CommonAppContext.sInstance);
    }

    public static HttpClient getInstance() {
        mUrl = CommonAppConfig.HOST + "/?service=";
        mVideoUrl = CommonAppConfig.VIDEO_HOST + "/?service=";
        mSportsUrl = CommonAppConfig.SPORTS_HOST;
        if (sInstance == null) {
            synchronized (HttpClient.class) {
                if (sInstance == null) {
                    sInstance = new HttpClient();
                }
            }
        }
        return sInstance;
    }


    public GetRequest<JsonObjectBean> getDataObject(String serviceName, String tag) {
        return mBuilder.req1(mUrl + serviceName, tag, JsonObjectBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonBean> get(String serviceName, String tag) {
        return mBuilder.req1(mUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);

    }

    public PostRequest<JsonBean> post(String serviceName, String tag) {
        return mBuilder.req2(mUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonObjectBean> getVideoDataObject(String serviceName, String tag) {
        return mBuilder.req1(mVideoUrl + serviceName, tag, JsonObjectBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonBean> getVideo(String serviceName, String tag) {
        return mBuilder.req1(mVideoUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public PostRequest<JsonBean> postVideo(String serviceName, String tag) {
        return mBuilder.req2(mVideoUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonBean> getSports(String serviceName, String tag) {
        return mBuilder.req1(mSportsUrl + serviceName, tag, JsonBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public GetRequest<JsonObjectBean> getSportsDataObject(String serviceName, String tag) {
        return mBuilder.req1(mSportsUrl + serviceName, tag, JsonObjectBean.class)
                .params(CommonHttpConsts.LANGUAGE, mLanguage);
    }

    public PostRequest<JsonBean> postSports(String serviceName, String tag) {
        return mBuilder.req2(mSportsUrl + serviceName, tag, JsonBean.class)
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
