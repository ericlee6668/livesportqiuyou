package com.yunbao.live.views

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yunbao.common.bean.BasketballLineup
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.http.V2Callback
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.adapter.TabNavigatorAdapter
import com.yunbao.live.bean.LineHeader
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.utils.FragmentContainerHelper
import com.yunbao.live.utils.getAppCompatActivity
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 13:59
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class BasketballViewHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {
    private lateinit var mFragmentContainerHelper: FragmentContainerHelper
    private var matchBean: MatchBean? = null
    private lateinit var homeLineup: BasketballLineupFragment
    private lateinit var awayLineup: BasketballLineupFragment

    override fun processArguments(vararg args: Any?) {
        super.processArguments(*args)
        if (args.isNotEmpty()) {
            val firstIndex = args[0]
            if (firstIndex is Array<*>) {
                matchBean = firstIndex[0] as MatchBean
            }
        }
    }

    override fun getLayoutId() = R.layout.basketball_line_up

    override fun init() {
        homeLineup = BasketballLineupFragment()
        awayLineup = BasketballLineupFragment()
        mFragmentContainerHelper = FragmentContainerHelper()
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
        getLineUp()
    }

    private fun initMagicIndicator() {
        val magicIndicator = findViewById<MagicIndicator>(R.id.basketLineup)
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg)
        val commonNavigator = CommonNavigator(mContext)
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

    private fun getLineUp() {
        val gameId = matchBean?.id ?: return
        LiveHttpUtil.getBasketballLineUp(gameId.toString(), object : V2Callback<BasketballLineup>() {
            override fun onSuccess(code: Int, msg: String?, data: BasketballLineup?) {
                data?.let {
                    homeLineup.showManagerName(it.lineup?.home_manager_name)
                    awayLineup.showManagerName(it.lineup?.away_manager_name)
                    val homeLines = mutableListOf<MultiItemEntity>()
                    if (!it.lineup.home.isNullOrEmpty()) {
                        homeLines.add(LineHeader("首发阵容"))
                        homeLines.addAll(it.lineup.home)
                    }
                    if (!it.injury.home.isNullOrEmpty()) {
                        homeLines.add(LineHeader("伤停情况"))
                        homeLines.addAll(it.injury.home)
                    }
                    val awayLines = mutableListOf<MultiItemEntity>()
                    if (!it.lineup.away.isNullOrEmpty()) {
                        awayLines.add(LineHeader("首发阵容"))
                        awayLines.addAll(it.lineup.away)
                    }
                    if (!it.injury.away.isNullOrEmpty()) {
                        awayLines.add(LineHeader("伤停情况"))
                        awayLines.addAll(it.injury.away)
                    }
                    homeLineup.providerData(homeLines)
                    awayLineup.providerData(awayLines)
                }
            }
        })
    }

    private fun switchPages(index: Int) {
        val fragmentManager: FragmentManager = mContext.getAppCompatActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (index == 0) {
            if (awayLineup.isAdded) {
                fragmentTransaction.hide(awayLineup)
            }
            showFragment(homeLineup, fragmentTransaction)
        } else {
            if (homeLineup.isAdded) {
                fragmentTransaction.hide(homeLineup)
            }
            showFragment(awayLineup, fragmentTransaction)
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
}