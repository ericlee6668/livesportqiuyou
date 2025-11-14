package com.yunbao.live.bean;


public class LineChartDataBean {

    private String tradeDate;
    private float value;

    public LineChartDataBean(String tradeDate, float value) {
        this.tradeDate = tradeDate;
        this.value = value;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
