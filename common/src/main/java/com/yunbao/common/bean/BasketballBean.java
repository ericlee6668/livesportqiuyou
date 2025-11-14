package com.yunbao.common.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/28 20:31
 *
 * @package: com.yunbao.common.bean
 * Description：TODO
 */
public class BasketballBean {
    private long id = 0;
    private long season_id = 0;
    private long competition_id = 0;
    private long home_team_id = 0;
    private long away_team_id = 0;
    private int status_id = 0;
    @SerializedName("match_time")
    private long matchTime = 0;
    private int neutral = 0;
    private String note = null;

    /**
     * [
     * 0,//主队比分(常规时间)
     * 0,//主队半场比分
     * 0,//主队红牌
     * 0,//主队黄牌
     * -1,//主队角球，-1表示没有角球数据
     * 0,//主队加时比分(120分钟)，加时赛才有
     * 0,//主队点球大战比分，点球大战才有
     * ],
     */
    @SerializedName("home_scores")
    private String homeScores = null;
    @SerializedName("away_scores")
    private String awayScores = null;
    private String home_position = null;
    private String away_position = null;
    private String coverage = null;
    private long venue_id = 0;
    private String referee_id = null;
    private String round = null;

    private String environment = null;
    private String trend = null;
    private String live_url_1 = null;
    private String live_url_2 = null;
    private String live_url_3 = null;
    private String pc_link = null;
    private String mobile_link = null;
    private int is_deleted = 0;
    private long updated_time = 0;
    private String match_date = null;
    private String state_str = null;
    private int period_count = 0;
    @SerializedName("is_playing")
    private int isPlaying = 0;
    private int kind = 0;
}
