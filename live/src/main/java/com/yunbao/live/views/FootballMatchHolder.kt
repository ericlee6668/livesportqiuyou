package com.yunbao.live.views

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.yunbao.common.bean.FootballHistory
import com.yunbao.common.bean.Incidents
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.bean.Stats
import com.yunbao.common.glide.ImgLoader
import com.yunbao.common.http.V2Callback
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.adapter.TabNavigatorAdapter
import com.yunbao.live.bean.FootballLive
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.utils.FragmentContainerHelper
import com.yunbao.live.utils.getTypeName
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/27 17:52
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class FootballMatchHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {

    private var mFootballInfo: MatchBean? = null
    private var footballHistory: FootballHistory? = null

    private lateinit var statisticsList: RecyclerView

    private lateinit var eventFragment: EventFragment
    private lateinit var tLiveFragment: TLiveFragment

    private lateinit var mFragmentContainerHelper: FragmentContainerHelper

    override fun getLayoutId() = R.layout.game_match_holder

    override fun processArguments(vararg args: Any?) {
        super.processArguments(*args)
        if (args.isNotEmpty()) {
            val firstIndex = args[0]
            if (firstIndex is Array<*>) {
                mFootballInfo = firstIndex[0] as MatchBean?
            }
        }
    }

    override fun init() {
        statisticsList = findViewById(R.id.statisticsList)
        val iconHome = findViewById<ImageView>(R.id.imgHome)
        val iconAway = findViewById<ImageView>(R.id.imgAway)
        mFootballInfo?.let {
            ImgLoader.displayWithError(mContext, it.home_team?.logo, iconHome, R.mipmap.icon_default_logo)
            ImgLoader.displayWithError(mContext, it.away_team?.logo, iconAway, R.mipmap.icon_default_logo)
            mContentView.findViewById<TextView>(R.id.txtFootballMatchHomeName).text = it.home_team?.nameZh
            mContentView.findViewById<TextView>(R.id.txtFootballMatchAwayName).text = it.away_team?.nameZh
            mContentView.findViewById<TextView>(R.id.txtMatchHomeScore).text = getScore(it.homeScores)
            mContentView.findViewById<TextView>(R.id.txtMatchAwayScore).text = getScore(it.awayScores)
        }

        eventFragment = EventFragment()
        tLiveFragment = TLiveFragment.create(mFootballInfo?.home_team?.nameZh, mFootballInfo?.away_team?.nameZh)
        mFragmentContainerHelper = FragmentContainerHelper()
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)

        if (mFootballInfo?.isPlaying != 1) {
            getMatchLive()
        }

        EventBus.getDefault().register(this)
    }

    override fun release() {
        super.release()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun observerLiveChange(footballLive: FootballLive) {
        if (footballLive.id != mFootballInfo?.id) {
            return
        }

        footballLive.stats?.let { observerStatsChange(it) }
        footballLive.incidents?.let { observerIncidentChange(it) }
        footballLive.score?.let { onMatchScoreChange(it) }
        tLiveFragment.insertLive(footballLive.tlive)
    }

    private fun observerIncidentChange(incidentList: List<Incidents>?) {
        eventFragment.onInsertIncident(incidentList)
    }

    private fun onMatchScoreChange(score: JsonArray) {
        val matchId = score[0].asLong
        if (matchId != mFootballInfo?.id) {
            return
        }
        val homeScore = score[2].asJsonArray
        val awayScore = score[3].asJsonArray
        val homeRealScore = homeScore[0].asInt + homeScore[5].asInt + homeScore[6].asInt
        val awayRealScore = awayScore[0].asInt + awayScore[5].asInt + awayScore[6].asInt
        mContentView.findViewById<TextView>(R.id.txtMatchHomeScore).text = homeRealScore.toString()
        mContentView.findViewById<TextView>(R.id.txtMatchAwayScore).text = awayRealScore.toString()
    }

    private fun observerStatsChange(statsList: List<Stats>) {
        val adapter = statisticsList.adapter as MatchAdapter?
        val dataList = adapter?.data
        val refreshList = mutableListOf<Stats>()
        if (dataList != null) {
            refreshList.addAll(refreshList)
            for (item in statsList) {
                val stats = refreshList.find { it.type == item.type }
                stats?.let {
                    it.away = item.away
                    it.home = item.home
                } ?: kotlin.run { refreshList.add(item) }
            }
        } else {
            refreshList.addAll(statsList)
        }
        adapter?.setNewDiffData(StatsCallBack(refreshList))
        showEmptyView(adapter?.itemCount == 0)
    }

    private fun getScore(scoreList: String?): String {
        if (TextUtils.isEmpty(scoreList)) {
            return "0"
        }
        val array: JsonArray = Gson().fromJson(scoreList, JsonArray::class.java)
        var score = 0
        try {
            score = array[0].asInt
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return score.toString()
    }

    private fun initMagicIndicator() {
        val magicIndicator = findViewById<MagicIndicator>(R.id.matchMagicTab)
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg)
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = true
        val titleList = arrayListOf(mContext.getString(R.string.important_events), mContext.getString(R.string.text_live))

        commonNavigator.adapter = object : TabNavigatorAdapter(titleList) {
            override fun onTabSelect(index: Int) {
                mFragmentContainerHelper.handlePageSelected(index)
                switchPages(index)
            }
        }
        magicIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator)
    }

    private fun getMatchLive() {
        val gameId = mFootballInfo?.id ?: return
        LiveHttpUtil.getFootballLiveData(gameId.toString(), object : V2Callback<FootballHistory>() {
            override fun onSuccess(code: Int, msg: String?, data: FootballHistory?) {
                footballHistory = data
                matchStats()
            }
        })
    }

    private fun matchStats() {
        val adapter = MatchAdapter().apply {
            setNewDiffData(StatsCallBack(footballHistory?.stats ?: mutableListOf()))
        }
        statisticsList.adapter = adapter
        showEmptyView(adapter.itemCount == 0)
        eventFragment.attachEventData(footballHistory?.incidents)
        if (mFootballInfo?.isPlaying == 2 || mFootballInfo?.isPlaying == 3) {
            tLiveFragment.attachLiveData(footballHistory?.tlive)
        }
    }

    private fun showEmptyView(shown: Boolean) {
        mContentView.findViewById<View>(R.id.statisticsGroup).visibility = if (shown) View.GONE else View.VISIBLE
        mContentView.findViewById<View>(R.id.staticEmptyView).visibility = if (shown) View.VISIBLE else View.GONE
    }

    private class MatchAdapter : BaseQuickAdapter<Stats, BaseViewHolder>(R.layout.item_basketball_statistics, null) {

        override fun convert(helper: BaseViewHolder, item: Stats?) {
            item?.let {
                helper.setText(R.id.txtType, it.type.getTypeName())
                        .setText(R.id.txtHomeScore, it.home.toString())
                        .setText(R.id.txtAwayScore, it.away.toString())
                calculateRating(helper, it)
            }
        }

        private fun calculateRating(helper: BaseViewHolder, stats: Stats) {
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

    private fun switchPages(index: Int) {
        val fragmentManager: FragmentManager = getAppCompatActivity(mContext).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (index == 0) {
            if (tLiveFragment.isAdded) {
                fragmentTransaction.hide(tLiveFragment)
            }
            showFragment(eventFragment, fragmentTransaction)
        } else {
            if (eventFragment.isAdded) {
                fragmentTransaction.hide(eventFragment)
            }
            showFragment(tLiveFragment, fragmentTransaction)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun showFragment(fragment: Fragment, fragmentTransaction: FragmentTransaction) {
        if (fragment.isAdded) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.add(R.id.matchContainer, fragment)
        }
    }

    private fun getAppCompatActivity(context: Context): AppCompatActivity {
        return if (context is AppCompatActivity) {
            context
        } else {
            if (context is ContextThemeWrapper) getAppCompatActivity(context.baseContext) else throw IllegalArgumentException("context must be activity or ContextThemeWrapper")
        }
    }

    private class StatsCallBack(newList: MutableList<Stats>) : BaseQuickDiffCallback<Stats>(newList) {
        override fun areItemsTheSame(oldItem: Stats, newItem: Stats): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: Stats, newItem: Stats): Boolean {
            return oldItem.home == newItem.home && oldItem.away == newItem.away
        }
    }
}