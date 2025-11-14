package com.yunbao.common.http;

import android.app.Dialog;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.yunbao.common.R;
import com.yunbao.common.event.LoginInvalidEvent;
import com.yunbao.common.http.Data;
import com.yunbao.common.http.JsonBean;
import com.yunbao.common.utils.AppLog;
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
 * Created by cxf on 2017/8/7.
 */

public abstract class HttpCallbackObject extends AbsCallback<JsonObjectBean> {

    private Dialog mLoadingDialog;

    @Override
    public JsonObjectBean convertResponse(okhttp3.Response response) throws Throwable {
        return JSON.parseObject(response.body().string(), JsonObjectBean.class);
    }

    @Override
    public void onSuccess(Response<JsonObjectBean> response) {
        JsonObjectBean bean = response.body();
        try {
            if (bean != null) {
                if (200 == bean.getRet()) {
                    DataObject data = bean.getData();
                    if (data != null) {
                        if (700 == data.getCode()) {
                            //token过期，重新登录
                            if (isUseLoginInvalid()) {
                                onLoginInvalid();
                            } else {
                                EventBus.getDefault().post(new LoginInvalidEvent());
                                RouteUtil.forwardLoginInvalid(data.getMsg());
                            }
                        } else {
                            onSuccess(data.getCode(), data.getMsg(), data.getInfo());
                        }
                    } else {
                        AppLog.e("服务器返回值异常--->ret: " + bean.getRet() + " msg: " + bean.getMsg());
                    }
                } else {
                    AppLog.e("服务器返回值异常--->ret: " + bean.getRet() + " msg: " + bean.getMsg());
                }

            } else {
                AppLog.e("服务器返回值异常--->bean = null");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Response<JsonObjectBean> response) {
        Throwable t = response.getException();
        AppLog.e("网络请求错误---->" + t.getClass() + " : " + t.getMessage());
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException || t instanceof UnknownServiceException || t instanceof SocketException) {
            ToastUtil.show(R.string.load_failure);
        }
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        onError();
    }

    public void onError() {

    }

    /**
     * 登录过期
     */
    public void onLoginInvalid() {

    }


    public abstract void onSuccess(int code, String msg, Object info);

    @Override
    public void onStart(Request<JsonObjectBean, ? extends Request> request) {
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
