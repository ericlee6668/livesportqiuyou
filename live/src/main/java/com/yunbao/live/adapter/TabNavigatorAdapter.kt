package com.yunbao.live.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.yunbao.live.R
import com.yunbao.live.utils.DensityUtils
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/2 15:22
 * @package: com.yunbao.live.adapter
 * Description：TODO
 */
abstract class TabNavigatorAdapter(private val titleList: MutableList<String>) : CommonNavigatorAdapter() {
    override fun getCount() = titleList.size

    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        val clipPagerTitleView = ClipPagerTitleView(context)
        clipPagerTitleView.text = titleList[index]
        clipPagerTitleView.textColor = ContextCompat.getColor(context, R.color.match_tab_color)
        clipPagerTitleView.clipColor = Color.WHITE
        clipPagerTitleView.textSize = DensityUtils.sp2px(context,12f).toFloat()
        clipPagerTitleView.setOnClickListener { onTabSelect(index) }
        return clipPagerTitleView
    }

    override fun getIndicator(context: Context): IPagerIndicator {
        val indicator = LinePagerIndicator(context)
        val navigatorHeight = context.resources.getDimension(R.dimen.common_navigator_height)
        val borderWidth = UIUtil.dip2px(context, 1.0).toFloat()
        val lineHeight = navigatorHeight - 2 * borderWidth
        indicator.lineHeight = lineHeight
        indicator.roundRadius = lineHeight / 2
        indicator.yOffset = borderWidth
        indicator.setColors(ContextCompat.getColor(context, R.color.match_tab_color))
        return indicator
    }

    abstract fun onTabSelect(index: Int)
}