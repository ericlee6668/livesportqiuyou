package com.yunbao.common.http;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/22 17:24
 *
 * @package: com.yunbao.common.http
 * Description：TODO
 */
public class V2Response<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
