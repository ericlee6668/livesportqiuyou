package com.yunbao.common.bean;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/3 16:13
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class FightHistory {
    //[1861, "澳大利亚新南威尔士联赛", 3540354, 1619859600, "2021-05-01", 28420, "黑镇斯巴达", 0, 30819, "希尤斯布伦贝斯", 3, "0.75", 0, "0.8/1.0", 0]
    // [赛事ID,赛事名称,比赛ID,比赛时间戳,比赛日期,主队ID,主队名称,主队得分,客队ID,客队名称,客队得分,让球数,输赢(0输|1赢),大小比(2/2.5),大小(0小|1 大)]
    List<JsonArray> vs;
    List<JsonArray> home;
    List<JsonArray> away;

    public List<JsonArray> getVs() {
        return vs;
    }

    public void setVs(List<JsonArray> vs) {
        this.vs = vs;
    }

    public List<JsonArray> getHome() {
        return home;
    }

    public void setHome(List<JsonArray> home) {
        this.home = home;
    }

    public List<JsonArray> getAway() {
        return away;
    }

    public void setAway(List<JsonArray> away) {
        this.away = away;
    }
}
