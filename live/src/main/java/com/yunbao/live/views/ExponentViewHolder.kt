package com.yunbao.live.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.yunbao.common.bean.GameOpening
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
 * Created by Swan on 2021/5/15 17:18
 * @package: com.yunbao.live.views
 * Description：比赛指数
 */
class ExponentViewHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {
    private var matchBean: MatchBean? = null
    private var isFootBall = false

    private lateinit var listFragment: MutableList<MatchFragment>
    private lateinit var mFragmentContainerHelper: FragmentContainerHelper

    override fun getLayoutId() = R.layout.exponent_view_holder

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
        listFragment = mutableListOf(MatchFragment(), MatchFragment(), MatchFragment())
        initMagicIndicator()
        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
        requestMatch()
    }

    private fun requestMatch() {
        val match = matchBean?.id ?: return
        LiveHttpUtil.getMatchIndex(match, isFootBall, object : V2Callback<GameOpening>() {
            override fun onSuccess(code: Int, msg: String?, data: GameOpening?) {
                data?.let { renderData(it) }
            }
        })
    }

    private fun renderData(data: GameOpening) {
        if (data.eu.isNullOrEmpty() && data.bs.isNullOrEmpty() && data.asia.isNullOrEmpty()) {
            val container: FrameLayout = mContentView.findViewById(R.id.exponentContainer)
            val emptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_contact, container, false)
            emptyView.findViewById<TextView>(R.id.message).text = "暂无数据"
            container.removeAllViews()
            container.addView(emptyView)
            return
        }

        listFragment[0].providerData(data.asia?.toMutableList())
        listFragment[1].providerData(data.eu?.toMutableList())
        listFragment[2].providerData(data.bs?.toMutableList())
    }

    private fun initMagicIndicator() {
        val magicIndicator = findViewById<MagicIndicator>(R.id.exponentTab)
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg)
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = true
        val title = mutableListOf("亚指", "欧指", "进球数")
        commonNavigator.adapter = object : TabNavigatorAdapter(title) {
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
            fragmentTransaction.add(R.id.exponentContainer, fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }
}