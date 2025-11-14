package com.yunbao.main.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by cxf on 2019/3/30.
 */

public class BannerBean {
    private String mId;
    private String mImageUrl;
    private String mLink;

    @JSONField(name = "id")
    public String getId() {
        return mId;
    }

    @JSONField(name = "id")
    public void setId(String id) {
        mId = id;
    }

    @JSONField(name = "image")
    public String getImageUrl() {
        return mImageUrl;
    }

    @JSONField(name = "image")
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @JSONField(name = "url")
    public String getLink() {
        return mLink;
    }

    @JSONField(name = "url")
    public void setLink(String link) {
        mLink = link;
    }
}
