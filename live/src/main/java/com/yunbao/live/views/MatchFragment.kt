package com.yunbao.live.views

import android.graphics.Color
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.JsonArray
import com.yunbao.common.bean.MatchIndex
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/2 14:40
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class MatchFragment : SimpleMatchFragment<MatchIndex>() {
    override fun providerAdapter(): BaseQuickAdapter<MatchIndex, BaseViewHolder> {
        return MatchAdapter()
    }

    override fun addItemDecoration(): RecyclerView.ItemDecoration {
        return DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
    }

    private class MatchAdapter : BaseQuickAdapter<MatchIndex, BaseViewHolder>(R.layout.item_match_index, null) {

        override fun convert(helper: BaseViewHolder, item: MatchIndex?) {
            if (item is MatchIndex) {
                helper.setText(R.id.txtName, item.companyName)
                showInitRate(helper, item.initialInfo)
                showTimelyRate(helper, item.timelyInfo)
                val position = helper.layoutPosition % 2
                if (position == 1) {
                    helper.itemView.setBackgroundColor(Color.WHITE)
                } else {
                    helper.itemView.setBackgroundColor(Color.parseColor("#F7FAFF"))
                }
            }
        }

        private fun showInitRate(helper: BaseViewHolder, initialInfo: JsonArray?) {
            var victory = "-"
            var tie = "-"
            var lose = "-"
            val size = initialInfo?.size() ?: 0
            if (size == 4) {
                val str = initialInfo!![2].asString?.split(",")
                val rateSize = str?.size ?: 0
                if (rateSize > 1) {
                    victory = str?.get(0).toString()
                }
                if (rateSize > 2) {
                    tie = str?.get(1).toString()
                }
                if (rateSize > 3) {
                    lose = str?.get(2).toString()
                }
            }

            helper.setText(R.id.txtInitVictory, victory)
                    .setText(R.id.txtInitTie, tie)
                    .setText(R.id.txtInitLose, lose)
        }

        private fun showTimelyRate(helper: BaseViewHolder, timelyInfo: JsonArray?) {
            var victory = "-"
            var tie = "-"
            var lose = "-"
            timelyInfo?.let {
                if (it.size() >= 4) {
                    val str = it[2].asString?.split(",")
                    val size = str?.size ?: 0
                    if (size > 1) {
                        victory = str?.get(0).toString()
                    }
                    if (size > 2) {
                        tie = str?.get(1).toString()
                    }
                    if (size > 3) {
                        lose = str?.get(2).toString()
                    }
                }
            }
            helper.setText(R.id.txtTimelyVictory, victory)
                    .setText(R.id.txtTimelyTie, tie)
                    .setText(R.id.txtTimelyLose, lose)
        }
    }
}