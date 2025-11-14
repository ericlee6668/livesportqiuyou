package com.yunbao.common.bean;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/27 18:13
 *
 * @package: com.yunbao.common.bean
 * Description：比赛统计字段说明
 * {
 * "type": 2,//类型，详见状态码->足球技术统计
 * "home": 9,//主队值
 * "away": 0//客队值
 * }
 * 状态码	描述
 * 1	进球
 * 2	角球
 * 3	黄牌
 * 4	红牌
 * 5	界外球
 * 6	任意球
 * 7	球门球
 * 8	点球
 * 9	换人
 * 10	比赛开始
 * 11	中场
 * 12	结束
 * 13	半场比分
 * 15	两黄变红
 * 16	点球未进
 * 17	乌龙球
 * 19	伤停补时
 * 21	射正
 * 22	射偏
 * 23	进攻
 * 24	危险进攻
 * 25	控球率
 * 26	加时赛结束
 * 27	点球大战结束
 * 28	VAR(视频助理裁判)
 * 29	点球(点球大战)(type_v2字段返回)
 * 30	点球未进(点球大战)(type_v2字段返回)
 */
public class Stats {
    private int type = 0;
    private int home = 0;
    private int away = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public int getAway() {
        return away;
    }

    public void setAway(int away) {
        this.away = away;
    }

}
