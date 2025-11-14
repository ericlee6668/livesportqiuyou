package com.yunbao.live.views

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yunbao.common.bean.GameIntelligence
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/1 14:14
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class IntelligenceFragment : Fragment() {
    private var gameIntelligence: GameIntelligence? = null
    private lateinit var rootView: View
    private var isHome: Boolean = false

    companion object {
        fun create(isHome: Boolean): IntelligenceFragment {
            return IntelligenceFragment().apply {
                arguments = Bundle().apply { putBoolean("isHome", isHome) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (!this::rootView.isInitialized) {
            rootView = inflater.inflate(R.layout.fragment_match_intelligence, container, false)
        }
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isHome = arguments?.getBoolean("isHome") ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayIntelligence()
    }

    fun attachData(gameIntelligence: GameIntelligence?) {
        this.gameIntelligence = gameIntelligence
        displayIntelligence()
    }

    private fun displayIntelligence() {
        val txtFavorable = view?.findViewById<TextView>(R.id.txtFavorable)
        val txtUnFavorable = view?.findViewById<TextView>(R.id.txtUnFavorable)
        val txtNeutral = view?.findViewById<TextView>(R.id.txtNeutral)
        var favorable = ""
        var unFavorable = ""
        var neutral = ""
        gameIntelligence?.let {
            favorable = if (isHome) it.good.home.joinToString(separator = "\n") { item ->
                if (item.size > 1) item[1] else ""
            } else it.good.away.joinToString(separator = "\n") { item ->
                if (item.size > 1) item[1] else ""
            }
            unFavorable = if (isHome) it.bad.home.joinToString(separator = "\n") { item ->
                if (item.size > 1) item[1] else ""
            } else it.bad.away.joinToString(separator = "\n") { item ->
                if (item.size > 1) item[1] else ""
            }
            neutral = it.neutral.joinToString(separator = "\n") { item ->
                if (item.size > 1) item[1] else ""
            }
        }

        val holder = view?.context?.getString(R.string.intelligence_holder)
        txtFavorable?.text = if (TextUtils.isEmpty(favorable)) holder else favorable
        txtUnFavorable?.text = if (TextUtils.isEmpty(unFavorable)) holder else unFavorable
        txtNeutral?.text = if (TextUtils.isEmpty(neutral)) holder else neutral
    }
}