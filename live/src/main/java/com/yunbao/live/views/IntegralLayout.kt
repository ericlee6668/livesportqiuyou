package com.yunbao.live.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/3 15:35
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class IntegralLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val txtRanking: TextView
    private val txtTeam: TextView
    private val txtResult: TextView
    private val txtGoal: TextView
    private val txtIntegral: TextView

    init {
        val view = inflate(context, R.layout.item_integral_layout, this)
        txtRanking = view.findViewById(R.id.txtRanking)
        txtTeam = view.findViewById(R.id.txtTeam)
        txtResult = view.findViewById(R.id.txtResult)
        txtGoal = view.findViewById(R.id.txtGoal)
        txtIntegral = view.findViewById(R.id.txtIntegral)

        val array = context.obtainStyledAttributes(attrs, R.styleable.IntegralLayout)
        val showData = array.getBoolean(R.styleable.IntegralLayout_displayData, true)
        if (showData) {
            txtRanking.text = "-"
            txtTeam.text = "-"
            txtResult.text = "-"
            txtGoal.text = "-"
            txtIntegral.text = "-"
        }
        array.recycle()
    }

    fun displayIntegral(integral: List<Int>?, team: String?) {
        txtTeam.text = team
        integral?.let {
            txtRanking.text = it[0].toString()
            txtResult.text = "${it[1]}/${it[2]}/${it[3]}"
            txtGoal.text = "${it[4]}/${it[5]}"
            txtIntegral.text = it[6].toString()
        }
    }
}