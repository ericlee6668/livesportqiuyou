package com.yunbao.live.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/26 20:42
 * @package: com.yunbao.live.views
 * Description：替补阵容
 */
class SubstituteLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val txtName: TextView
    private val txtNumber: TextView
    private val txtPosition: TextView

    init {
        val view = inflate(context, R.layout.item_substitute_layout, this)
        txtName = view.findViewById(R.id.txtName)
        txtNumber = view.findViewById(R.id.txtNumber)
        txtPosition = view.findViewById(R.id.txtPosition)
    }

    fun displaySubstitute(home: Boolean, number: String?, name: String?, position: String?) {
        if (home) {
            txtNumber.setBackgroundResource(R.drawable.football_red_number)
        } else {
            txtNumber.setBackgroundResource(R.drawable.football_blue_number)
        }

        txtNumber.text = number
        txtName.text = name
        txtPosition.text = position
    }
}