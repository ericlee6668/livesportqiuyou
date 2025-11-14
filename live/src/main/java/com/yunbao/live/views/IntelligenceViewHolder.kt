package com.yunbao.live.views

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.yunbao.common.bean.GameIntelligence
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
 * Created by Swan on 2021/5/15 17:15
 * @package: com.yunbao.live.views
 * Description：比赛情报
 */
class IntelligenceViewHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {

    private lateinit var mFragmentContainerHelper: FragmentContainerHelper
    private lateinit var homeIntelligenceFragment: IntelligenceFragment
    private lateinit var awayIntelligenceFragment: IntelligenceFragment

    private var matchBean: MatchBean? = null
    private var isFootBall = false

    override fun getLayoutId() = R.layout.intelligence_view_holder

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
        homeIntelligenceFragment = IntelligenceFragment.create(true)
        awayIntelligenceFragment = IntelligenceFragment.create(false)
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
        getIntelligence()
    }

    private fun showIntelligence(gameIntelligence: GameIntelligence?) {
        homeIntelligenceFragment.attachData(gameIntelligence)
        awayIntelligenceFragment.attachData(gameIntelligence)
    }

    private fun getIntelligence() {
        val match = matchBean?.id ?: return
        LiveHttpUtil.getIntelligence(isFootBall, match.toString(), object : V2Callback<GameIntelligence>() {
            override fun onSuccess(code: Int, msg: String?, data: GameIntelligence?) {
                showIntelligence(data)
            }
        })
    }

    private fun initMagicIndicator() {
        val magicIndicator = findViewById<MagicIndicator>(R.id.intelligenceTab)
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

    private fun getSupportFragmentManager(): FragmentManager {
        return mContext.getAppCompatActivity().supportFragmentManager
    }

    private fun switchPages(index: Int) {
        val fragmentManager: FragmentManager = getSupportFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (index == 0) {
            if (awayIntelligenceFragment.isAdded) {
                fragmentTransaction.hide(awayIntelligenceFragment)
            }
            showFragment(homeIntelligenceFragment, fragmentTransaction)
        } else {
            if (homeIntelligenceFragment.isAdded) {
                fragmentTransaction.hide(homeIntelligenceFragment)
            }
            showFragment(awayIntelligenceFragment, fragmentTransaction)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun showFragment(fragment: Fragment, fragmentTransaction: FragmentTransaction) {
        if (fragment.isAdded) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.add(R.id.intelligenceContainer, fragment)
        }
    }
}