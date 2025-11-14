package com.yunbao.live.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.yunbao.live.R
import com.yunbao.live.adapter.FightAdapter

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 19:14
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class HistoryFragment : Fragment() {

    private var data: MutableList<JsonArray>? = null
    private var filterHome: Boolean = false
    private var filterMatch: Boolean = false
    private lateinit var rootView: View
    private var teamId = 0L
    private var matchId = 0L
    private var name: String? = null
    private var isFootball = false

    companion object {
        fun create(teamId: Long, matchId: Long, teamName: String?, isFootball: Boolean = false): HistoryFragment {
            return HistoryFragment().apply {
                arguments = Bundle().apply {
                    putLong("team", teamId)
                    putLong("match", matchId)
                    putString("name", teamName)
                    putBoolean("isFootball", isFootball)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamId = arguments?.getLong("team") ?: 0
        matchId = arguments?.getLong("match") ?: 0
        name = arguments?.getString("name") ?: ""
        isFootball = arguments?.getBoolean("isFootball", false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (!this::rootView.isInitialized) {
            rootView = inflater.inflate(R.layout.basket_history_layout, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showData()
    }

    fun providerData(data: MutableList<JsonArray>?) {
        this.data = data
        showData()
    }

    private fun showData() {
        val listView = view?.findViewById<RecyclerView>(R.id.historyList) ?: return
        if (listView.itemDecorationCount == 0) {
            listView.addItemDecoration(DividerItemDecoration(listView.context, RecyclerView.VERTICAL))
        }
        val adapter = listView.adapter
        if (adapter == null) {
            val filterData = filterData()
            listView.adapter = FightAdapter(filterData?.toMutableList(), teamId)
        } else {
            if (adapter is FightAdapter) {
                val filterData = filterData()
                adapter.setNewData(filterData?.toMutableList())
            }
        }
    }

    fun filterHome(filterHome: Boolean, filterMatch: Boolean) {
        this.filterHome = filterHome
        this.filterMatch = filterMatch
        showData()
    }

    private fun filterData(): List<JsonArray>? {
        val array = when {
            filterHome && filterMatch -> data?.filter {
                val competition = it[0].asLong
                val homeId = it[5].asLong
                competition == matchId && homeId == teamId
            }
            filterHome -> data?.filter {
                val homeId = it[5].asLong
                homeId == teamId
            }
            filterMatch -> data?.filter {
                val competition = it[0].asLong
                competition == matchId
            }
            else -> data
        }

        val victoryRateText = view?.findViewById<TextView>(R.id.victoryRate)
        if (array.isNullOrEmpty()) {
            victoryRateText?.setText(R.string.intelligence_holder)
        } else {
            var victoryCount = 0
            var loseCount = 0
            var matchDraw = 0
            for (item in array) {
                val result = item[12].asInt
                val homeId = item[5].asLong
                if (homeId == teamId) {
                    when (result) {
                        0 -> {
                            loseCount++
                        }
                        1 -> {
                            victoryCount++
                        }
                        2 -> {
                            matchDraw++
                        }
                    }
                } else {
                    when (result) {
                        0 -> {
                            victoryCount++
                        }
                        1 -> {
                            loseCount++
                        }
                        2 -> {
                            matchDraw++
                        }
                    }
                }
            }
            if (isFootball) {
                victoryRateText?.text = HtmlCompat.fromHtml(getString(R.string.history_victory_football, array.size, name, victoryCount, loseCount, matchDraw), HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                victoryRateText?.text = HtmlCompat.fromHtml(getString(R.string.history_victory, array.size, name, victoryCount, loseCount), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
        return array
    }
}