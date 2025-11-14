package com.yunbao.common.http;

/**
 * Created by cxf on 2017/8/5.
 */

public class JsonObjectBean {
    private int ret;
    private String msg;
    private DataObject data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataObject getData() {
        return data;
    }

    public void setData(DataObject data) {
        this.data = data;
    }
}
