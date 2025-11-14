package com.yunbao.live.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/26 20:03
 * @package: com.yunbao.live.utils
 * Description：TODO
 */

/**
 *球员位置，F前锋、M中场、D后卫、G守门员、其他为未知
 */
fun String.getPosition(): String {
    return when (this) {
        "F" -> "前锋"
        "M" -> "中场"
        "D" -> "后卫"
        "G" -> "守门员"
        else -> "未知"
    }
}

/**
 * 状态码	描述
 * 1	    进球
 * 2	    角球
 * 3	    黄牌
 * 4	    红牌
 * 5	    界外球
 * 6	    任意球
 * 7	    球门球
 * 8	    点球
 * 9	    换人
 * 10	    比赛开始
 * 11	    中场
 * 12	    结束
 * 13	    半场比分
 * 15	    两黄变红
 * 16	    点球未进
 * 17	    乌龙球
 * 19	    伤停补时
 * 21	    射正
 * 22	    射偏
 * 23	    进攻
 * 24	    危险进攻
 * 25	    控球率
 * 26	    加时赛结束
 * 27	    点球大战结束
 * 28	    VAR(视频助理裁判)
 * 29	    点球(点球大战)(type_v2字段返回)
 * 30	    点球未进(点球大战)(type_v2字段返回)
 */
fun Int.getTypeName(): String {
    return when (this) {
        1 -> "进球"
        2 -> "角球"
        3 -> "黄牌"
        4 -> "红牌"
        5 -> "界外球"
        6 -> "任意球"
        7 -> "球门球"
        8 -> "点球"
        9 -> "换人"
        10 -> "比赛开始"
        11 -> "中场"
        12 -> "结束"
        13 -> "半场比分"
        15 -> "两黄变红"
        16 -> "点球未进"
        17 -> "乌龙球"
        19 -> "伤停补时"
        21 -> "射正"
        22 -> "射偏"
        23 -> "进攻"
        24 -> "危险进攻"
        25 -> "控球率"
        26 -> "加时赛结束"
        27 -> "点球大战结束"
        28 -> "VAR(视频助理裁判)"
        29 -> "点球(点球大战)"
        30 -> "点球未进(点球大战)"
        else -> ""
    }
}

fun Context.getAppCompatActivity(): AppCompatActivity {
    return if (this is AppCompatActivity) {
        this
    } else {
        if (this is ContextThemeWrapper) baseContext.getAppCompatActivity() else throw IllegalArgumentException("context must be activity or ContextThemeWrapper")
    }
}