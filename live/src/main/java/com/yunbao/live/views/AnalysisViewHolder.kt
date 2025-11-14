package com.yunbao.live.views

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.yunbao.common.bean.MatchAnalysis
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.http.V2Callback
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.adapter.TabNavigatorAdapter
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.utils.getAppCompatActivity
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/15 17:01
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class AnalysisViewHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {
    private var matchBean: MatchBean? = null
    private var isFootBall = false

    private lateinit var homeIntegral: IntegralLayout
    private lateinit var awayIntegral: IntegralLayout
    private lateinit var fightHistory: RecyclerView
    private lateinit var mFragmentContainerHelper: FragmentContainerHelper
    private lateinit var homeHistoryFragment: HistoryFragment
    private lateinit var awayHistoryFragment: HistoryFragment
    private var fightHistoryData: List<JsonArray>? = null

    override fun getLayoutId() = R.layout.analysis_view_holder

    override fun processArguments(vararg args: Any?) {
        super.processArguments(*args)
        if (args.isNotEmpty()) {
            val params = args[0]
            if (params is Array<*>) {
                if (params.size >= 1) {
                    matchBean = params[0] as MatchBean?
                }
                if (params.size >= 2) {
                    isFootBall = params[1] as Boolean
                }
            }
        }
    }

    override fun init() {
        mFragmentContainerHelper = FragmentContainerHelper()
        val homeId = matchBean?.home_team_id ?: 0
        val awayId = matchBean?.home_team_id ?: 0
        val matchId = matchBean?.competition_id ?: 0
        homeHistoryFragment = HistoryFragment.create(homeId, matchId, matchBean?.home_team?.nameZh, true)
        awayHistoryFragment = HistoryFragment.create(awayId, matchId, matchBean?.away_team?.nameZh, true)
        homeIntegral = findViewById(R.id.homeIntegral)
        awayIntegral = findViewById(R.id.awayIntegral)
        fightHistory = findViewById(R.id.fightHistory)
        mContentView.findViewById<View>(R.id.txtHome).setOnClickListener {
            if (fightHistory.adapter != null) {
                val adapter = fightHistory.adapter as HistoryAdapter
                if (!it.isSelected) {
                    val data = fightHistoryData?.filter { item ->
                        item[5].asLong == homeId
                    }
                    adapter.setNewData(data?.toMutableList())
                } else {
                    adapter.setNewData(fightHistoryData?.toMutableList())
                }
                it.isSelected = !it.isSelected
                showEmptyView(adapter.itemCount == 0)
            }
        }

        findViewById<View>(R.id.filterHome).setOnClickListener {
            filterData(!it.isSelected, findViewById<View>(R.id.sameMatch).isSelected)
            it.isSelected = !it.isSelected
        }
        findViewById<View>(R.id.sameMatch).setOnClickListener {
            filterData(findViewById<View>(R.id.filterHome).isSelected, !it.isSelected)
            it.isSelected = !it.isSelected
        }

        fightHistory.addItemDecoration(DividerItemDecoration(mContext, RecyclerView.VERTICAL))
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
        requestAnalysis()
    }

    private fun showEmptyView(show: Boolean) {
        mContentView.findViewById<View>(R.id.fightHistoryEmptyView).visibility = if (show) View.VISIBLE else View.GONE
        fightHistory.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun filterData(filterHome: Boolean, filterMatch: Boolean) {
        homeHistoryFragment.filterHome(filterHome, filterMatch)
        awayHistoryFragment.filterHome(filterHome, filterMatch)
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

    private fun requestAnalysis() {
        val homeId = matchBean?.id ?: return
        LiveHttpUtil.getFootballAnalysis(homeId.toString(), object : V2Callback<MatchAnalysis>() {
            override fun onSuccess(code: Int, msg: String?, data: MatchAnalysis?) {
                homeIntegral.displayIntegral(data?.table?.get(0)?.get(0), matchBean?.home_team?.nameZh)
                awayIntegral.displayIntegral(data?.table?.get(1)?.get(0), matchBean?.away_team?.nameZh)
                fightHistoryData = data?.history?.vs
                val teamId = matchBean?.home_team_id ?: 0
                val awayId = matchBean?.home_team_id ?: 0
                fightHistory.adapter = HistoryAdapter(data?.history?.vs, teamId, awayId)
                showEmptyView(fightHistoryData.isNullOrEmpty())
                homeHistoryFragment.providerData(data?.history?.home)
                awayHistoryFragment.providerData(data?.history?.away)
            }
        })
    }

    private class HistoryAdapter(private var array: MutableList<JsonArray>?, private val home: Long, private val away: Long) : RecyclerView.Adapter<HistoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_fight_layout, parent, false)
            return HistoryViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
            holder.bindData(array?.get(position), home, away)
        }

        override fun getItemCount(): Int {
            return array?.size ?: 0
        }

        fun setNewData(data: MutableList<JsonArray>?) {
            this.array = data
            notifyDataSetChanged()
        }
    }

    private class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(json: JsonArray?, home: Long, away: Long) {
            json?.let {
                val txtMatch = itemView.findViewById<TextView>(R.id.txtMatch)
                val txtDateTime = itemView.findViewById<TextView>(R.id.txtDateTime)
                val txtAgainst = itemView.findViewById<TextView>(R.id.txtAgainst)
                val txtGoalGo = itemView.findViewById<TextView>(R.id.txtGoalGo)
                val txtWin = itemView.findViewById<TextView>(R.id.txtWin)
                val txtBs = itemView.findViewById<TextView>(R.id.txtBs)
                val txtBsValue = itemView.findViewById<TextView>(R.id.txtBsValue)
                txtMatch.text = it[1].asString
                txtDateTime.text = it[4].asString
                txtAgainst.text = getFight(txtAgainst.context, it, home, away)
                val goal = it[11].asString
                val result = it[12].asString
                txtGoalGo.text = if (TextUtils.isEmpty(goal)) "0" else goal
                txtWin.text = if (TextUtils.equals(result, "0")) "输" else "赢"
                txtWin.setTextColor(Color.parseColor(if (TextUtils.equals(result, "0")) "#389E0D" else "#F5222D"))
                val bs = it[14].asString
                txtBs.text = it[13].asString
                txtBsValue.text = if (TextUtils.equals(bs, "0")) "小" else "大"
                txtBsValue.setTextColor(Color.parseColor(if (TextUtils.equals(bs, "0")) "#2F54EB" else "#F5222D"))
                val position = layoutPosition % 2
                if (position == 0) {
                    itemView.setBackgroundColor(Color.WHITE)
                } else {
                    itemView.setBackgroundColor(Color.parseColor("#F7FAFF"))
                }
            }
        }

        // [赛事ID,赛事名称,比赛ID,比赛时间戳,比赛日期,主队ID,主队名称,主队得分,客队ID,客队名称,客队得分,让球数,输赢(0输|1赢),大小比(2/2.5),大小(0小|1 大)]
        private fun getFight(context: Context, json: JsonArray, home: Long, away: Long): CharSequence {
            val homeId = json[5].asLong
            val homeName = json[6].asString
            val homeScore = json[7].asString
            val awayId = json[8].asLong
            val awayName = json[9].asString
            val awayScore = json[10].asString
            return when (homeId) {
                home -> {
                    HtmlCompat.fromHtml(context.getString(R.string.fight_home, homeName, homeScore, awayScore, awayName), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
                else -> HtmlCompat.fromHtml(context.getString(R.string.fight_away, homeName, homeScore, awayScore, awayName), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }
}