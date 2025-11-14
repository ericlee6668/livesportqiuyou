package com.yunbao.live.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/26 19:51
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class InjuryLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val txtName: TextView
    private val txtReason: TextView
    private val imgReason: ImageView

    init {
        orientation = HORIZONTAL
        val view = inflate(context, R.layout.item_injury_layout, this)
        txtName = view.findViewById(R.id.txtName)
        txtReason = view.findViewById(R.id.txtReason)
        imgReason = view.findViewById(R.id.imgReason)
    }

    fun displayInjury(name: String?, position: String) {
        txtName.text = name
        txtReason.text = position
    }
}