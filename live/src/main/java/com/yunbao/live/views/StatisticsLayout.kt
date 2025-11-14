package com.yunbao.live.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yunbao.common.bean.Stats
import com.yunbao.live.R
import com.yunbao.live.utils.getTypeName

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/27 18:39
 * @package: com.yunbao.live.views
 * Description：技术统计
 */
class StatisticsLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val txtType: TextView
    private val txtHomeScore: TextView
    private val txtAwayScore: TextView
    private val homeRating: ProgressBar
    private val awayRating: ProgressBar

    init {
        val view = inflate(context, R.layout.item_technical_statistics, this)
        txtType = view.findViewById(R.id.txtType)
        txtHomeScore = view.findViewById(R.id.txtHomeScore)
        txtAwayScore = view.findViewById(R.id.txtAwayScore)
        homeRating = view.findViewById(R.id.homeRating)
        awayRating = view.findViewById(R.id.awayRating)
    }

    fun statistics(stats: Stats) {
        txtType.text = stats.type.getTypeName()
        txtHomeScore.text = stats.home.toString()
        txtAwayScore.text = stats.away.toString()
        calculateRating(stats)
    }

    private fun calculateRating(stats: Stats) {
        val total = stats.away + stats.home
        if (total > 0) {
            val rating = stats.away * 1F / total * 100
            homeRating.progress = rating.toInt()
            awayRating.progress = rating.toInt()
        } else {
            homeRating.progress = 100
            awayRating.progress = 0
        }
    }
}