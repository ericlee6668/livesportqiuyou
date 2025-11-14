package com.yunbao.live.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yunbao.common.bean.Integral
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/3 16:38
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class FightLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val txtDateTime: TextView
    private val txtAgainst: TextView
    private val txtGoalGo: TextView
    private val txtBs: TextView

    init {
        val view = inflate(context, R.layout.item_fight_header, this)
        txtDateTime = view.findViewById(R.id.txtDateTime)
        txtAgainst = view.findViewById(R.id.txtAgainst)
        txtGoalGo = view.findViewById(R.id.txtGoalGo)
        txtBs = view.findViewById(R.id.txtBs)

        val array = context.obtainStyledAttributes(attrs, R.styleable.FightLayout)
        val showData = array.getBoolean(R.styleable.FightLayout_fightData, true)
        if (showData) {
            txtDateTime.text = ""
            txtAgainst.text = ""
            txtGoalGo.text = ""
            txtBs.text = ""
        }
        array.recycle()
    }

    fun displayIntegral(integral: Integral?, team: String?) {
        txtAgainst.text = team
        integral?.let {
            txtDateTime.text = it.position.toString()
            txtGoalGo.text = "${it.won}/${it.drawn}/${it.lost}"
            txtBs.text = "${it.goals}/${it.against}"
        }
    }
}