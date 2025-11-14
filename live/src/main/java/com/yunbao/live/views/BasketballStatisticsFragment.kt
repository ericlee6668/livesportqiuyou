package com.yunbao.live.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/5 17:01
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class BasketballStatisticsFragment : Fragment() {
    private var data: List<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_list_event_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = StatisticsAdapter(mutableListOf()).apply {
            emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty_view, null)
        }
        showData()
    }

    fun showStatistics(data: List<String>?) {
        this.data = data
        showData()
    }

    private fun showData() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView) ?: return
        val size = data?.size ?: 0
        if (size != 2) {
            return
        }
        data?.let {
            val homeTeam = it[0]
            val awayTeam = it[1]
            val homeArray = homeTeam.split("^")
            val awayArray = awayTeam.split("^")
            val itemList = mutableListOf<Statistics>()
            val homeShoot = homeArray[1].split("-")
            val awayShoot = awayArray[1].split("-")
            val homePointer = homeArray[2].split("-")
            val awayPointer = awayArray[2].split("-")
            val homeThrow = homeArray[3].split("-")
            val awayThrow = awayArray[3].split("-")
            itemList.add(Statistics("投篮", homeShoot[1].toInt(), awayShoot[1].toInt()))
            itemList.add(Statistics("三分球", homePointer[0].toInt(), awayPointer[0].toInt()))
            itemList.add(Statistics("罚球", homeThrow[0].toInt(), awayThrow[0].toInt()))
            itemList.add(Statistics("进攻篮板", homeArray[4].toInt(), awayArray[4].toInt()))
            itemList.add(Statistics("防守篮板", homeArray[5].toInt(), awayArray[5].toInt()))
            itemList.add(Statistics("助攻", homeArray[7].toInt(), awayArray[5].toInt()))
            itemList.add(Statistics("抢断", homeArray[8].toInt(), awayArray[8].toInt()))
            itemList.add(Statistics("盖帽", homeArray[9].toInt(), awayArray[9].toInt()))
            itemList.add(Statistics("失误", homeArray[10].toInt(), awayArray[10].toInt()))
            itemList.add(Statistics("犯规", homeArray[11].toInt(), awayArray[11].toInt()))

            (recyclerView.adapter as StatisticsAdapter).setNewData(itemList)
        }
    }

    private data class Statistics(val name: String, val home: Int, val away: Int)

    private class StatisticsAdapter(data: MutableList<Statistics>) : BaseQuickAdapter<Statistics, BaseViewHolder>(R.layout.item_basketball_statistics, data) {
        override fun convert(helper: BaseViewHolder, item: Statistics?) {
            item?.let {
                helper.setText(R.id.txtType, it.name)
                        .setText(R.id.txtHomeScore, it.home.toString())
                        .setText(R.id.txtAwayScore, it.away.toString())
                calculateRating(helper, it)
            }
        }

        private fun calculateRating(helper: BaseViewHolder, stats: Statistics) {
            val homeRating: ProgressBar = helper.getView(R.id.homeRating)
            val awayRating: ProgressBar = helper.getView(R.id.awayRating)
            val total = stats.away + stats.home
            when {
                stats.away > stats.home -> {
                    awayRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress)
                    homeRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basket_statistic_progress_left_gray)
                }
                stats.away == stats.home -> {
                    awayRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress)
                    homeRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress_left)
                }
                else -> {
                    awayRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basket_statistic_progress_right_gray)
                    homeRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress_left)
                }
            }
            if (total > 0) {
                homeRating.progress = (stats.home * 1F / total * 100).toInt()
                awayRating.progress = (stats.away * 1F / total * 100).toInt()
            } else {
                homeRating.progress = 0
                awayRating.progress = 0
            }
        }
    }
}