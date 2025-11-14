package com.yunbao.live.bean;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/8 21:10
 *
 * @package: com.yunbao.live.bean
 * Description：TODO
 */
public class BasketballPushData {

    private int code;
    private int msg;
    private List<BasketPushData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public List<BasketPushData> getData() {
        return data;
    }

    public void setData(List<BasketPushData> data) {
        this.data = data;
    }
}
