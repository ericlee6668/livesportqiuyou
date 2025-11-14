package com.yunbao.common.http;

/**
 * Created by cxf on 2017/8/5.
 */

public class DataObject {
    private int code;
    private String msg;
    private Object info;

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

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }
}
