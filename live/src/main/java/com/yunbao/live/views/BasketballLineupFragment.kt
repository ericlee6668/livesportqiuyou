package com.yunbao.live.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yunbao.common.bean.Lineup
import com.yunbao.live.R
import com.yunbao.live.bean.LineHeader

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 14:25
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class BasketballLineupFragment : SimpleMatchFragment<MultiItemEntity>() {
    private lateinit var rootView: View
    private var name: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!this::rootView.isInitialized) {
            rootView = inflater.inflate(R.layout.basketball_line_fragment, container, false)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showManagerName(name)
    }

    override fun providerAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        return LineupAdapter().apply {
            setSpanSizeLookup { _, position ->
                val data = getItem(position)
                if (data is LineHeader) 2 else 1
            }
        }
    }

    fun showManagerName(name: String?) {
        this.name = name
        val txtManager = view?.findViewById<TextView>(R.id.txtManagerName)
        txtManager?.text = name
    }

    private class LineupAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(null) {
        init {
            addItemType(0, R.layout.item_basket_line_header)
            addItemType(1, R.layout.item_player)
            addItemType(2, R.layout.item_player_stop)
        }

        override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {
            if (item is LineHeader) {
                helper.setText(R.id.header, item.title)
            } else if (item is Lineup) {
                val imgAvatar = helper.getView<ImageView>(R.id.imgAvatar)
                Glide.with(imgAvatar).load(item.logo).error(R.mipmap.icon_default_logo).into(imgAvatar)
                helper.setText(R.id.txtName, item.short_name_zh)
                if (item.itemType == 1) {
                    helper.setText(R.id.txtPosition, helper.itemView.context.getString(R.string.player, item.shirt_number, getPositionName(item.position)))
                } else {
                    helper.setText(R.id.txtReason, item.reason)
                }
            }
        }

        private fun getPositionName(position: String?): String {
            return when (position) {
                "C" -> "中锋"
                "SF" -> "小前锋"
                "PF" -> "大前锋"
                "SG" -> "得分后卫"
                "PG" -> "组织后卫"
                "F" -> "前锋"
                "G" -> "后卫"
                else -> "未知"
            }
        }
    }
}