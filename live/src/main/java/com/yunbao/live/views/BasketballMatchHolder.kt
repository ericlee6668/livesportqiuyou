package com.yunbao.live.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.gson.JsonArray
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.http.V2Callback
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.adapter.TabNavigatorAdapter
import com.yunbao.live.bean.BasketPushData
import com.yunbao.live.bean.BasketballLive
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.utils.getAppCompatActivity
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/5 15:59
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class BasketballMatchHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {
    private var matchBean: MatchBean? = null

    private lateinit var listFragment: MutableList<Fragment>

    private lateinit var mFragmentContainerHelper: FragmentContainerHelper

    override fun getLayoutId() = R.layout.basketball_match_holder

    override fun init() {
        mFragmentContainerHelper = FragmentContainerHelper()
        listFragment = mutableListOf(BasketballStatisticsFragment(), IndividualFragment(), BasketLiveFragment.create(matchBean?.home_team?.nameZh, matchBean?.away_team?.nameZh))

        scoreHeader()
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
        requestBasketLive()

        EventBus.getDefault().register(this)
    }

    override fun processArguments(vararg args: Any?) {
        super.processArguments(*args)
        if (args.isNotEmpty()) {
            val firstIndex = args[0]
            if (firstIndex is Array<*>) {
                matchBean = firstIndex[0] as MatchBean?
            }
        }
    }

    @Subscribe
    fun onBasketLiveChange(pushData: BasketPushData) {
        if (pushData.id != matchBean?.id) {
            return
        }

        onScoreChange(pushData.score)
        (listFragment[2] as BasketLiveFragment).showLiveData(pushData.tlive)

        if (pushData.players.size == 4) {
            val stats = mutableListOf<String>()
            val homeStats = pushData.players[2]
            val awayStats = pushData.players[3]
            if (homeStats is String) {
                stats.add(homeStats)
            }
            if (awayStats is String) {
                stats.add(awayStats)
            }
            if (stats.size == 2) {
                (listFragment[0] as BasketballStatisticsFragment).showStatistics(stats)
            }

            (listFragment[1] as IndividualFragment).onIndividualChange(pushData.players[0] as List<List<String>>?, pushData.players[1] as List<List<String>>?)
        }
    }

    override fun release() {
        super.release()
        EventBus.getDefault().unregister(this)
    }

    private fun onScoreChange(array: JsonArray?) {
        eachSiteScores(array)
    }

    private fun requestBasketLive() {
        val gameId = matchBean?.id ?: return
        LiveHttpUtil.basketballLive(gameId.toString(), object : V2Callback<BasketballLive>() {
            override fun onSuccess(code: Int, msg: String?, data: BasketballLive?) {
                eachSiteScores(data?.score)
                (listFragment[0] as BasketballStatisticsFragment).showStatistics(data?.stat)
                (listFragment[1] as IndividualFragment).showIndividual(data?.best_player)
                (listFragment[2] as BasketLiveFragment).showLiveData(data?.tlive)
            }
        })
    }

    private fun initMagicIndicator() {
        val magicIndicator = findViewById<MagicIndicator>(R.id.basketballAnalysis)
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg)
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = true

        val titleList = mutableListOf("技术统计", "各项最高", "文字直播")
        commonNavigator.adapter = object : TabNavigatorAdapter(titleList) {
            override fun onTabSelect(index: Int) {
                mFragmentContainerHelper.handlePageSelected(index)
                switchPages(index)
            }
        }
        magicIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator)
    }

    private fun scoreHeader() {
        val headerView = findViewById<View>(R.id.scoreHeader)
        headerView.findViewById<TextView>(R.id.txtTeam).text = "球队"
        headerView.findViewById<TextView>(R.id.txtFirst).text = "Q1"
        headerView.findViewById<TextView>(R.id.txtSecond).text = "Q2"
        headerView.findViewById<TextView>(R.id.txtThird).text = "Q3"
        headerView.findViewById<TextView>(R.id.txtFourth).text = "Q4"
        headerView.findViewById<TextView>(R.id.txtExtra).text = "QT"
        headerView.findViewById<TextView>(R.id.txtScore).text = "总分"
        findViewById<View>(R.id.homeScores).findViewById<TextView>(R.id.txtTeam).text = matchBean?.home_team?.nameZh
        findViewById<View>(R.id.awayScores).findViewById<TextView>(R.id.txtTeam).text = matchBean?.away_team?.nameZh
        initScore(findViewById(R.id.homeScores))
        initScore(findViewById(R.id.awayScores))
    }

    private fun switchPages(index: Int) {
        val fragmentManager: FragmentManager = mContext.getAppCompatActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        for (i in listFragment.indices) {
            if (i == index) {
                continue
            }

            val fragment = listFragment[i]
            if (fragment.isAdded) {
                fragmentTransaction.hide(fragment)
            }
        }
        val fragment = listFragment[index]
        if (fragment.isAdded) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.add(R.id.frameAnalysis, fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun eachSiteScores(score: JsonArray?) {
        score?.let {
            val type = it[1].asInt
            matchScores(findViewById(R.id.homeScores), type, it[3].asJsonArray)
            matchScores(findViewById(R.id.awayScores), type, it[4].asJsonArray)
        }
    }

    private fun matchScores(scoreView: View, type: Int, score: JsonArray?) {
        scoreView.findViewById<TextView>(R.id.txtFirst).text = getScore(type, 0, score)
        scoreView.findViewById<TextView>(R.id.txtSecond).text = getScore(type, 1, score)
        scoreView.findViewById<TextView>(R.id.txtThird).text = getScore(type, 2, score)
        scoreView.findViewById<TextView>(R.id.txtFourth).text = getScore(type, 3, score)
        scoreView.findViewById<TextView>(R.id.txtExtra).text = getScore(type, 4, score)
        scoreView.findViewById<TextView>(R.id.txtScore).text = getTotalScore(type, score)
    }

    private fun initScore(scoreView: View) {
        scoreView.findViewById<TextView>(R.id.txtFirst).text = "-"
        scoreView.findViewById<TextView>(R.id.txtSecond).text = "-"
        scoreView.findViewById<TextView>(R.id.txtThird).text = "-"
        scoreView.findViewById<TextView>(R.id.txtFourth).text = "-"
        scoreView.findViewById<TextView>(R.id.txtExtra).text = "-"
        scoreView.findViewById<TextView>(R.id.txtScore).text = "-"
    }

    private fun getScore(type: Int, index: Int, score: JsonArray?): String? {
        return when {
            type <= 1 -> {
                return "-"
            }
            type >= 11 -> {
                return "-"
            }
            else -> score?.get(index)?.asString
        }
    }

    private fun getTotalScore(type: Int, score: JsonArray?): String {
        return when {
            type <= 1 -> {
                return "-"
            }
            type >= 11 -> {
                return "-"
            }
            else -> {
                score?.let {
                    var total = 0
                    for (item in it) {
                        total += item.asInt
                    }
                    total.toString()
                } ?: "0"
            }
        }
    }
}