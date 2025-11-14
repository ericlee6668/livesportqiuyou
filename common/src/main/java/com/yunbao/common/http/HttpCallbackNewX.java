package com.yunbao.common.http;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.yunbao.common.R;
import com.yunbao.common.event.LoginInvalidEvent;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * 针对后台新框架解析
 * Created by cxf on 2017/8/7.
 */

public abstract class HttpCallbackNewX extends AbsCallback<String> {

    private Dialog mLoadingDialog;

    @Override
    public String convertResponse(okhttp3.Response response) throws Throwable {
        return  response.body().string();
    }

    @Override
    public void onSuccess(Response<String> response) {

        if (!TextUtils.isEmpty(response.body())) {
            onSuccessALLData(response.body());
        } else {
            L.e("服务器返回值异常--->bean = null");
            onError(-1, "数据异常");
        }
    }

    @Override
    public void onError(Response<String> response) {
        Throwable t = response.getException();
        L.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
            ToastUtil.show(R.string.load_failure);
        }
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        onError(-1, "网络请求失败");
    }

//    public void onError() {
//
//    }

    public void onError(int ret, String msg) {

    }

    /**
     * 登录过期
     */
    public void onLoginInvalid() {

    }


    public abstract void onSuccessALLData(String data);


    @Override
    public void onStart(Request<String, ? extends Request> request) {
        onStart();
    }

    public void onStart() {
        try {
            if (showLoadingDialog()) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = createLoadingDialog();
                }
                mLoadingDialog.show();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onFinish() {
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public Dialog createLoadingDialog() {
        return null;
    }

    public boolean showLoadingDialog() {
        return false;
    }

    public boolean isUseLoginInvalid() {
        return false;
    }

}
