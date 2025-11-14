package com.yunbao.live.bean;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/8 21:47
 *
 * @package: com.yunbao.live.bean
 * Description：TODO
 */
public class BasketPushData {
    private long id;
    private JsonArray score;
    private List<JsonArray> stats;
    List<List<String>> tlive;
    List<Object> players;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public JsonArray getScore() {
        return score;
    }

    public void setScore(JsonArray score) {
        this.score = score;
    }

    public List<JsonArray> getStats() {
        return stats;
    }

    public void setStats(List<JsonArray> stats) {
        this.stats = stats;
    }

    public List<List<String>> getTlive() {
        return tlive;
    }

    public void setTlive(List<List<String>> tlive) {
        this.tlive = tlive;
    }

    public List<Object> getPlayers() {
        return players;
    }

    public void setPlayers(List<Object> players) {
        this.players = players;
    }
}
