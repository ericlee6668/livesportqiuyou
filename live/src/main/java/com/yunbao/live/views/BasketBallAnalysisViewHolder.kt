package com.yunbao.live.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.yunbao.common.bean.FightHistory
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.http.V2Callback
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.adapter.FightAdapter
import com.yunbao.live.adapter.TabNavigatorAdapter
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.utils.getAppCompatActivity
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 15:57
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class BasketBallAnalysisViewHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {
    private lateinit var fightHistory: RecyclerView
    private var matchBean: MatchBean? = null
    private lateinit var mFragmentContainerHelper: FragmentContainerHelper
    private lateinit var homeHistoryFragment: HistoryFragment
    private lateinit var awayHistoryFragment: HistoryFragment
    private lateinit var historyHomeAway: TextView
    private lateinit var filterHome: TextView
    private lateinit var sameMatch: TextView
    var vs: List<JsonArray>? = null

    override fun processArguments(vararg args: Any?) {
        super.processArguments(*args)
        if (args.isNotEmpty()) {
            val params = args[0]
            if (params is Array<*>) {
                if (params.size >= 1) {
                    matchBean = params[0] as MatchBean?
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.basketball_analysis_holder

    override fun init() {
        val homeId = matchBean?.home_team_id ?: 0
        val awayId = matchBean?.home_team_id ?: 0
        val matchId = matchBean?.competition_id ?: 0

        homeHistoryFragment = HistoryFragment.create(homeId, matchId, matchBean?.home_team?.nameZh)
        awayHistoryFragment = HistoryFragment.create(awayId, matchId, matchBean?.away_team?.nameZh)
        mFragmentContainerHelper = FragmentContainerHelper()
        fightHistory = findViewById(R.id.fightHistory)
        historyHomeAway = findViewById(R.id.historyHomeAway)
        historyHomeAway.setOnClickListener {
            val isSelect = it.isSelected
            val adapter = fightHistory.adapter as FightAdapter?
            if (adapter != null) {
                if (isSelect) {
                    adapter.setNewData(this.vs?.toMutableList())
                } else if (!this.vs.isNullOrEmpty()) {
                    val filter = vs?.filter { array ->
                        val homeTeam = array[5].asLong
                        homeTeam == homeId
                    }
                    adapter.setNewData(filter?.toMutableList())
                }
                showEmptyView(adapter.itemCount == 0)
            }
            it.isSelected = !isSelect
        }
        filterHome = findViewById(R.id.filterHome)
        sameMatch = findViewById(R.id.sameMatch)
        filterHome.setOnClickListener {
            filterData(!it.isSelected, sameMatch.isSelected)
            it.isSelected = !it.isSelected
        }
        sameMatch.setOnClickListener {
            filterData(filterHome.isSelected, !it.isSelected)
            it.isSelected = !it.isSelected
        }

        fightHistory.addItemDecoration(DividerItemDecoration(mContext, RecyclerView.VERTICAL))
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
        getBasketAnalysis()
    }

    private fun showEmptyView(show: Boolean) {
        mContentView.findViewById<View>(R.id.historyEmptyView).visibility = if (show) View.VISIBLE else View.GONE
        fightHistory.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun initMagicIndicator() {
        val magicIndicator = findViewById<MagicIndicator>(R.id.analysisIndicator)
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg)
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = true

        val homeName = matchBean?.home_team?.nameZh ?: ""
        val awayName = matchBean?.away_team?.nameZh ?: ""
        val titleList = mutableListOf(homeName, awayName)
        commonNavigator.adapter = object : TabNavigatorAdapter(titleList) {
            override fun onTabSelect(index: Int) {
                mFragmentContainerHelper.handlePageSelected(index)
                switchPages(index)
            }
        }
        magicIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator)
    }

    private fun filterData(filterHome: Boolean, filterMatch: Boolean) {
        homeHistoryFragment.filterHome(filterHome, filterMatch)
        awayHistoryFragment.filterHome(filterHome, filterMatch)
    }

    private fun getBasketAnalysis() {
        val matchId = matchBean?.id ?: return
        LiveHttpUtil.getBasketMatchAnalysis(matchId.toString(), object : V2Callback<FightHistory>() {
            override fun onSuccess(code: Int, msg: String?, data: FightHistory?) {
                data?.let {
                    this@BasketBallAnalysisViewHolder.vs = it.vs
                    val homeId = matchBean?.home_team_id
                    fightHistory.adapter = FightAdapter(it.vs, homeId ?: 0)
                    showEmptyView(it.vs.isNullOrEmpty())
                    homeHistoryFragment.providerData(it.home)
                    awayHistoryFragment.providerData(it.away)
                } ?: kotlin.run { showEmptyView(true) }
            }
        })
    }

    private fun switchPages(index: Int) {
        val fragmentManager: FragmentManager = mContext.getAppCompatActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (index == 0) {
            if (awayHistoryFragment.isAdded) {
                fragmentTransaction.hide(awayHistoryFragment)
            }
            showFragment(homeHistoryFragment, fragmentTransaction)
        } else {
            if (homeHistoryFragment.isAdded) {
                fragmentTransaction.hide(homeHistoryFragment)
            }
            showFragment(awayHistoryFragment, fragmentTransaction)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun showFragment(fragment: Fragment, fragmentTransaction: FragmentTransaction) {
        if (fragment.isAdded) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.add(R.id.analysisContainer, fragment)
        }
    }

}