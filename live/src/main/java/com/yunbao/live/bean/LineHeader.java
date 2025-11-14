package com.yunbao.live.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 14:49
 *
 * @package: com.yunbao.live.bean
 * Description：TODO
 */
public class LineHeader implements MultiItemEntity {
    private String title;

    public LineHeader(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
