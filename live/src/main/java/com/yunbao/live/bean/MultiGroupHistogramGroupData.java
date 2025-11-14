package com.yunbao.live.bean;

import java.util.List;

/**
 * @author wangjiang
 * @date 2020/9/24
 * Description:
 */
public class MultiGroupHistogramGroupData {
    private String groupName;
    private List<MultiGroupHistogramChildData> childDataList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<MultiGroupHistogramChildData> getChildDataList() {
        return childDataList;
    }

    public void setChildDataList(List<MultiGroupHistogramChildData> childDataList) {
        this.childDataList = childDataList;
    }
}
