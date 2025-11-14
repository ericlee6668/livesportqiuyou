package com.yunbao.live.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/4 19:05
 * @package: com.yunbao.live.adapter
 * Description：TODO
 */
class FightAdapter(private var array: MutableList<JsonArray>?, private val home: Long) : RecyclerView.Adapter<HistoryViewHolder>() {

    fun setNewData(array: MutableList<JsonArray>?) {
        this.array = array
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_fight_layout, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindData(array?.get(position), home)
    }

    override fun getItemCount(): Int {
        return array?.size ?: 0
    }
}

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindData(json: JsonArray?, home: Long) {
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
            txtAgainst.text = getFight(txtAgainst.context, it, home)
            val goal = it[11].asString
            val result = it[12].asInt
            txtGoalGo.text = if (TextUtils.isEmpty(goal)) "0" else goal
            txtWin.text = getResultStr(result)
            txtWin.setTextColor(Color.parseColor(if (result == 0) "#389E0D" else "#F5222D"))
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
    private fun getFight(context: Context, json: JsonArray, home: Long): CharSequence {
        val homeId = json[5].asLong
        val homeName = json[6].asString
        val homeScore = json[7].asString
        val awayName = json[9].asString
        val awayScore = json[10].asString
        return when (homeId) {
            home -> {
                HtmlCompat.fromHtml(context.getString(R.string.fight_home, homeName, homeScore, awayScore, awayName), HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            else -> HtmlCompat.fromHtml(context.getString(R.string.fight_away, homeName, homeScore, awayScore, awayName), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun getResultStr(result: Int): String {
        return when (result) {
            0 -> "输"
            1 -> "赢"
            2 -> "平"
            else -> "未知"
        }
    }
}