package com.yunbao.live.bean

import com.google.gson.JsonArray

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/5 16:05
 * @package: com.yunbao.live.bean
 * Description：TODO
 */
class BasketballLive {
    var status_id: Int = 0
    var tlive: List<List<String>>? = null
    var stat: List<String>? = null
    var best_player: List<List<JsonArray>>? = null
    var score: JsonArray? = null
}