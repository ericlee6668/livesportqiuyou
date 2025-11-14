package com.yunbao.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 14:20
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class Lineup implements MultiItemEntity {

    private String short_name_zh;
    private String logo;
    private String position;
    private String shirt_number;
    private String id;
    private int type = -1;
    private String reason;

    public String getShort_name_zh() {
        return short_name_zh;
    }

    public void setShort_name_zh(String short_name_zh) {
        this.short_name_zh = short_name_zh;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getShirt_number() {
        return shirt_number;
    }

    public void setShirt_number(String shirt_number) {
        this.shirt_number = shirt_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int getItemType() {
        if (type == -1) {
            return 1;
        }
        return 2;
    }
}
