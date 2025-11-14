package com.yunbao.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/2 14:28
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class MatchIndex implements MultiItemEntity {
    @SerializedName("initial_info")
    private JsonArray initialInfo;
    @SerializedName("timely_info")
    private JsonArray timelyInfo;
    @SerializedName("company_name")
    private String companyName;

    public JsonArray getInitialInfo() {
        return initialInfo;
    }

    public void setInitialInfo(JsonArray initialInfo) {
        this.initialInfo = initialInfo;
    }

    public JsonArray getTimelyInfo() {
        return timelyInfo;
    }

    public void setTimelyInfo(JsonArray timelyInfo) {
        this.timelyInfo = timelyInfo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
