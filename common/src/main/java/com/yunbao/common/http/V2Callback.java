package com.yunbao.common.http;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.lzy.okgo.callback.AbsCallback;
import com.yunbao.common.R;
import com.yunbao.common.event.LoginInvalidEvent;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/22 17:23
 *
 * @package: com.yunbao.common.http
 * Description：TODO
 */
public abstract class V2Callback<T> extends AbsCallback<V2Response<T>> {

    private Gson gson;

    public V2Callback() {
        gson = new Gson();
    }

    public V2Callback(Gson gson) {
        this.gson = gson;
    }

    @Override
    public V2Response<T> convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            throw new IOException("the api " + response.request() + " response is null");
        }

        try (JsonReader reader = gson.newJsonReader(body.charStream())) {
            Type rawType = getRawType();
            if (rawType == null) {
                throw new JsonParseException("unable parse response");
            }

            Type responseType = new V2ResponseTypeImpl(rawType);
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(responseType));
            V2Response<T> result = (V2Response<T>) adapter.read(reader);
            if (reader.peek() != JsonToken.END_DOCUMENT) {
                throw new IOException("JSON document was not fully consumed.");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("the api " + response.request() + " response error->" + e.getMessage());
        }
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<V2Response<T>> response) {
        V2Response<T> result = response.body();
        if (result != null) {
            if (200 == result.getCode()) {
                T data = result.getData();
                if (data != null) {
                    onSuccess(result.getCode(), result.getMsg(), result.getData());
                } else {
                    L.e("服务器返回值异常--->ret: " + result.getCode() + " msg: " + result.getMsg());
                    onError(result.getCode(), result.getMsg());
                }
            } else if (700 == result.getCode()) {
                if (isUseLoginInvalid()) {
                    onLoginInvalid();
                } else {
                    EventBus.getDefault().post(new LoginInvalidEvent());
                    RouteUtil.forwardLoginInvalid(result.getMsg());
                }

            } else {
                L.e("服务器返回值异常--->ret: " + result.getCode() + " msg: " + result.getMsg());
                onError(result.getCode(), result.getMsg());
            }

        } else {
            L.e("服务器返回值异常--->bean = null");
            onError(-1, "数据异常");
        }
    }

    @Override
    public void onFinish() {
        if (showLoadingDialog() && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<V2Response<T>> response) {
        super.onError(response);
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

    private Dialog mLoadingDialog;

    public Dialog createLoadingDialog() {
        return null;
    }

    public boolean showLoadingDialog() {
        return false;
    }

    public boolean isUseLoginInvalid() {
        return false;
    }

    /**
     * 登录过期
     */
    public void onLoginInvalid() {

    }

    public void onError(int ret, String msg) {

    }

    public abstract void onSuccess(int code, String msg, T data);

    private static class V2ResponseTypeImpl implements ParameterizedType {
        Type clazz;

        public V2ResponseTypeImpl(Type clz) {
            clazz = clz;
        }

        @NotNull
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @NotNull
        @Override
        public Type getRawType() {
            return V2Response.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    private Type getRawType() {
        Type classType = getClass().getGenericSuperclass();
        if (classType == null) {
            return null;
        }
        return ((ParameterizedType) classType).getActualTypeArguments()[0];
    }
}
