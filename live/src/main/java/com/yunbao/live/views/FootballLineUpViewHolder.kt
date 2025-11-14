package com.yunbao.live.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.yunbao.common.bean.FootballLineup
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.glide.ImgLoader
import com.yunbao.common.http.V2Callback
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.utils.getPosition

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/5/15 16:53
 * @package: com.yunbao.live.views
 * Description：足球阵容
 */
class FootballLineUpViewHolder(context: Context, parentView: ViewGroup, vararg args: Any?) : AbsViewHolder(context, parentView, args) {

    private var mFootballInfo: MatchBean? = null
    private lateinit var homeSubstituteLayout: LinearLayoutCompat
    private lateinit var awaySubstituteLayout: LinearLayoutCompat
    private lateinit var homeInjuryLayout: LinearLayoutCompat
    private lateinit var awayInjuryLayout: LinearLayoutCompat

    override fun getLayoutId() = R.layout.football_line_up

    override fun init() {
        val imgHomeLine = findViewById<ImageView>(R.id.imgHomeIcon)
        val imgAwayLine = findViewById<ImageView>(R.id.imgAwayIcon)
        val imgHomeInjury = findViewById<ImageView>(R.id.imgHomeInjury)
        val imgAwayInjury = findViewById<ImageView>(R.id.imgAwayInjury)
        val txtHomeName = findViewById<TextView>(R.id.txtHomeName)
        val txtAwayName = findViewById<TextView>(R.id.txtAwayName)
        val txtHomeInjury = findViewById<TextView>(R.id.txtHomeInjury)
        val txtAwayInjury = findViewById<TextView>(R.id.txtAwayInjury)
        homeSubstituteLayout = findViewById(R.id.homeSubstitute)
        awaySubstituteLayout = findViewById(R.id.awaySubstitute)
        homeInjuryLayout = findViewById(R.id.homeInjury)
        awayInjuryLayout = findViewById(R.id.awayInjury)
        mFootballInfo?.let {
            ImgLoader.displayWithError(mContext, it.home_team?.logo, imgHomeLine, R.mipmap.icon_default_logo)
            ImgLoader.displayWithError(mContext, it.home_team?.logo, imgHomeInjury, R.mipmap.icon_default_logo)
            ImgLoader.displayWithError(mContext, it.away_team?.logo, imgAwayLine, R.mipmap.icon_default_logo)
            ImgLoader.displayWithError(mContext, it.away_team?.logo, imgAwayInjury, R.mipmap.icon_default_logo)
            txtHomeName.text = it.home_team?.nameZh
            txtHomeInjury.text = it.home_team?.nameZh
            txtAwayName.text = it.away_team?.nameZh
            txtAwayInjury.text = it.away_team?.nameZh
        }

        if (mFootballInfo?.isPlaying != 1) {
            getLineUp()
        }
    }

    override fun processArguments(vararg args: Any?) {
        super.processArguments(*args)
        if (args.isNotEmpty()) {
            val firstIndex = args[0]
            if (firstIndex is Array<*>) {
                mFootballInfo = firstIndex[0] as MatchBean?
            }
        }
    }

    private fun getLineUp() {
        val gameId = mFootballInfo?.id ?: return
        LiveHttpUtil.getFootballLineUp(gameId.toString(), object : V2Callback<FootballLineup>() {
            override fun onSuccess(code: Int, msg: String?, data: FootballLineup?) {
                data?.let {
                    showInjury(it)
                    showSubstitute(it)
                    showLineUp(it.lineup)
                }
            }
        })
    }

    private fun showLineUp(lineup: FootballLineup.Lineups?) {
        if (lineup == null) return
        val lineupView: LineUpView = mContentView.findViewById(R.id.imgLineUp)
        lineupView.playPosition(lineup, mFootballInfo?.home_team?.nameZh, mFootballInfo?.away_team?.nameZh)
        findViewById<View>(R.id.teamLineUpImages).visibility = View.VISIBLE
        val homeLogo = mContentView.findViewById<ImageView>(R.id.homeTeamLogo)
        val awayTeamLogo = mContentView.findViewById<ImageView>(R.id.awayTeamLogo)
        val homeTeamName = mContentView.findViewById<TextView>(R.id.homeTeamName)
        val awayTeamName = mContentView.findViewById<TextView>(R.id.awayTeamName)
        val homeTeamFormation = mContentView.findViewById<TextView>(R.id.homeTeamFormation)
        val awayTeamFormation = mContentView.findViewById<TextView>(R.id.awayTeamFormation)
        ImgLoader.displayWithError(mContext, mFootballInfo?.home_team?.logo, homeLogo, R.mipmap.icon_default_logo)
        ImgLoader.displayWithError(mContext, mFootballInfo?.away_team?.logo, awayTeamLogo, R.mipmap.icon_default_logo)
        homeTeamName.text = mFootballInfo?.home_team?.nameZh
        awayTeamName.text = mFootballInfo?.away_team?.nameZh
        homeTeamFormation.text = lineup.homeFormation
        awayTeamFormation.text = lineup.awayFormation
    }

    /**
     * 伤停情况
     */
    private fun showInjury(data: FootballLineup) {
        val homeInjury = data.injury?.get("home")
        val awayInjury = data.injury?.get("away")
        homeInjuryLayout.removeAllViews()
        awayInjuryLayout.removeAllViews()
        homeInjury?.forEach {
            val item = InjuryLayout(mContext)
            item.displayInjury(it.name, it.reason)
            homeInjuryLayout.addView(item)
        }
        awayInjury?.forEach {
            val item = InjuryLayout(mContext)
            item.displayInjury(it.name, it.reason)
            awayInjuryLayout.addView(item)
        }
    }

    /**
     * 替补阵容
     */
    private fun showSubstitute(data: FootballLineup) {
        val homeLineup = data.lineup?.home
        val awayLineup = data.lineup?.away

        val homeSubstitute = homeLineup?.filter { it.first == 0 }
        val awaySubstitute = awayLineup?.filter { it.first == 0 }

        homeSubstituteLayout.removeAllViews()
        awaySubstituteLayout.removeAllViews()
        homeSubstitute?.forEach {
            val item = SubstituteLayout(mContext)
            item.displaySubstitute(true, it.shirtNumber.toString(), it.name, it.position?.getPosition())
            homeSubstituteLayout.addView(item)
        }
        awaySubstitute?.forEach {
            val item = SubstituteLayout(mContext)
            item.displaySubstitute(false, it.shirtNumber.toString(), it.name, it.position?.getPosition())
            awaySubstituteLayout.addView(item)
        }
    }
}