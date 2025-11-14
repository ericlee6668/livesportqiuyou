package com.yunbao.live.views

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.google.gson.JsonArray
import com.yunbao.live.R
import kotlinx.coroutines.*

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/5 17:02
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class IndividualFragment : Fragment() {
    private val job by lazy { Job() }
    private val dispatchers by lazy { Dispatchers.Main }

    private var data: List<List<JsonArray>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_list_event_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = StatisticsAdapter(null).apply {
            emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty_view, null)
        }
        showData()
    }

    fun showIndividual(data: List<List<JsonArray>>?) {
        this.data = data
        showData()
    }

    fun onIndividualChange(homeArray: List<List<String>>?, awayArray: List<List<String>>?) {
        if (homeArray.isNullOrEmpty() && awayArray.isNullOrEmpty()) {
            return
        }
        view?.findViewById<RecyclerView>(R.id.recyclerView) ?: return
        convertArray(homeArray, awayArray)
    }

    private fun convertArray(homeArray: List<List<String>>?, awayArray: List<List<String>>?) {

        val coroutineScope = CoroutineScope(dispatchers + job)
        coroutineScope.launch {
            val list = withContext(Dispatchers.IO) {
                val homeSize = homeArray?.size ?: -1
                val awaySize = awayArray?.size ?: -1
                val dataList = mutableListOf<Individual>()
                if (homeSize > 0 || awaySize > 0) {
                    convertPlayer(0, "得分", dataList, homeArray, awayArray)
                }
                if (homeSize > 1 || awaySize > 1) {
                    convertPlayer(1, "篮板", dataList, homeArray, awayArray)
                }
                if (homeSize > 2 || awaySize > 2) {
                    convertPlayer(2, "助攻", dataList, homeArray, awayArray)
                }
                if (homeSize > 3 || awaySize > 3) {
                    convertPlayer(3, "抢断", dataList, homeArray, awayArray)
                }
                if (homeSize > 4 || awaySize > 4) {
                    convertPlayer(4, "盖帽", dataList, homeArray, awayArray)
                }
                dataList
            }

            val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView?.adapter as StatisticsAdapter?
            if (adapter != null) {
                adapter.setNewDiffData(DiffCallback(list))
            } else {
                recyclerView?.adapter = StatisticsAdapter(list)
            }
        }
    }

    private fun convertPlayer(index: Int, name: String, dataList: MutableList<Individual>, homeArray: List<List<String>>?, awayArray: List<List<String>>?) {
        val homePlayer = if (homeArray?.size ?: 0 > index) convertPush(homeArray!![index]) else Player.EMPTY_PLAYER
        val awayPlayer = if (awayArray?.size ?: 0 > index) convertPush(awayArray!![index]) else Player.EMPTY_PLAYER
        dataList.add(Individual(name, homePlayer, awayPlayer))
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
            val dataList = mutableListOf<Individual>()
            dataList.add(Individual("得分", convert(homeTeam[0]), convert(awayTeam[0])))
            dataList.add(Individual("篮板", convert(homeTeam[1]), convert(awayTeam[1])))
            dataList.add(Individual("助攻", convert(homeTeam[2]), convert(awayTeam[2])))
            dataList.add(Individual("抢断", convert(homeTeam[3]), convert(awayTeam[3])))
            dataList.add(Individual("盖帽", convert(homeTeam[4]), convert(awayTeam[4])))

            if (recyclerView.adapter == null) {
                recyclerView.adapter = StatisticsAdapter(dataList)
            } else {
                (recyclerView.adapter as StatisticsAdapter).setNewData(dataList)
            }
        }
    }

    private fun convert(array: JsonArray): Player {
        return Player(array[1].asString, array[2].asString, getScore(array[3].asString))
    }

    private fun convertPush(array: List<String>): Player {
        return Player(array[1], array[4], getScore(array[5]))
    }

    private fun getScore(value: String?): Int {
        return if (TextUtils.isEmpty(value)) 0 else value!!.toInt()
    }

    private data class Player(val name: String?, val avatar: String?, val score: Int = 0) {
        companion object {
            val EMPTY_PLAYER = Player("-", "", 0)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Player

            if (name != other.name) return false
            if (avatar != other.avatar) return false
            if (score != other.score) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name?.hashCode() ?: 0
            result = 31 * result + (avatar?.hashCode() ?: 0)
            result = 31 * result + score
            return result
        }

    }

    private data class Individual(val typeName: String, val home: Player, val away: Player)

    private class DiffCallback(data: MutableList<Individual>) : BaseQuickDiffCallback<Individual>(data) {
        override fun areItemsTheSame(oldItem: Individual, newItem: Individual): Boolean {
            return oldItem.typeName == newItem.typeName
        }

        override fun areContentsTheSame(oldItem: Individual, newItem: Individual): Boolean {
            return oldItem.away != newItem.away || oldItem.home != newItem.home
        }
    }

    private class StatisticsAdapter(data: MutableList<Individual>?) : BaseQuickAdapter<Individual, BaseViewHolder>(R.layout.item_individual_layout, data) {
        override fun convert(helper: BaseViewHolder, item: Individual?) {
            item?.let {
                helper.setText(R.id.txtType, it.typeName)
                        .setText(R.id.homeScore, it.home.score.toString())
                        .setText(R.id.homeName, it.home.name)
                        .setText(R.id.awayName, it.away.name)
                        .setText(R.id.awayScore, it.away.score.toString())
                calculateRating(helper, it)
                val homeAvatar = helper.getView<ImageView>(R.id.homeAvatar)
                val awayAvatar = helper.getView<ImageView>(R.id.awayAvatar)
                Glide.with(homeAvatar).load(it.home.avatar).placeholder(R.mipmap.icon_default_logo).into(homeAvatar)
                Glide.with(awayAvatar).load(it.away.avatar).placeholder(R.mipmap.icon_default_logo).into(awayAvatar)
            }
        }

        private fun calculateRating(helper: BaseViewHolder, stats: Individual) {
            val homeRating: ProgressBar = helper.getView(R.id.homeProgress)
            val awayRating: ProgressBar = helper.getView(R.id.awayProgress)
            val total = stats.away.score + stats.home.score

            when {
                stats.away.score > stats.home.score -> {
                    awayRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress_left)
                    homeRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basket_statistic_progress_right_gray)
                }
                stats.away.score == stats.home.score -> {
                    awayRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress_left)
                    homeRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress)
                }
                else -> {
                    awayRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basket_statistic_progress_left_gray)
                    homeRating.progressDrawable = ContextCompat.getDrawable(mContext, R.drawable.basketball_statistic_progress)
                }
            }

            if (total > 0) {
                homeRating.progress = (stats.home.score * 1F / total * 100).toInt()
                awayRating.progress = (stats.away.score * 1F / total * 100).toInt()
            } else {
                homeRating.progress = 0
                awayRating.progress = 0
            }
        }
    }
}