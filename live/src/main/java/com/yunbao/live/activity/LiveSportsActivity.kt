package com.yunbao.live.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.yunbao.common.CommonAppConfig
import com.yunbao.common.Constants
import com.yunbao.common.adapter.ViewPagerAdapter
import com.yunbao.common.bean.GameBasketballMatchBean
import com.yunbao.common.bean.GameFootballMatchBean
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.glide.ImgLoader
import com.yunbao.common.http.V2Callback
import com.yunbao.common.utils.DateFormatUtil
import com.yunbao.common.utils.EasyAES
import com.yunbao.common.utils.MD5Util
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.bean.BasketPushData
import com.yunbao.live.bean.FootballLive
import com.yunbao.live.event.MatchEvent
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.http.WsManager
import com.yunbao.live.views.*
import kotlinx.android.synthetic.main.activity_live_sports.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 足球赛事
 */
class LiveSportsActivity : LiveActivity(), View.OnClickListener {

    private val mBottomTitles: ArrayList<String> = arrayListOf("主播")
    private var mAbsViewHolders: Array<AbsViewHolder?>? = null
    private var mFootballInfo: MatchBean? = null
    private var mBasketBallInfo: MatchBean? = null
    private var id: String? = null
    private var mLivePlayViewHolder: LivePlayTxViewHolder? = null
    private var mViewList = ArrayList<FrameLayout>()
    private var matchType = 0
    lateinit var mRlTitle: View

    private val gson by lazy { Gson() }
    private var eventWsManager: WsManager? = null
    private var matchWebSocket: WebSocket? = null

    private fun createEventService() {
        if (eventWsManager != null) {
            return
        }
        if (mFootballInfo != null || mBasketBallInfo != null) {
            eventWsManager = WsManager.Builder(baseContext, true, false).client(
                OkHttpClient().newBuilder()
                    .pingInterval(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            )
                .needReconnect(true)
                .wsUrl(CommonAppConfig.WEBSOCKET_MATCH).build()
            eventWsManager?.setOnReceiveListener(object : WsManager.OnReceiveListener {
                override fun onMessage(message: String) {
                    if ("alive" == message) {
                        Log.i("LiveEvent", "heart beat")
                        return
                    }
                    Log.i("LiveEvent", message)
                    try {
                        val isFootball = mFootballInfo != null
                        val data = JSONObject(message)
                        val code = data.optInt("code")
                        if (!isFootball) {
                            if (8003 == code) {
                                val type = object : TypeToken<List<BasketPushData>>() {}.type
                                val changeList: List<BasketPushData> =
                                    gson.fromJson(data.optString("data"), type)
                                broadBasketChange(changeList)
                            }
                        } else {
                            if (code == 8001) {
                                val type = object : TypeToken<List<FootballLive>>() {}.type
                                val changeList: List<FootballLive> =
                                    gson.fromJson(data.optString("data"), type)
                                broadEventChange(changeList)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onOpen() {
                    matchWebSocket = eventWsManager?.webSocket
                    val message = if (mFootballInfo == null) {
                        "{\"code\":8000,\"data\":\"BASKETBALL\"}"
                    } else {
                        "{\"code\":8000,\"data\":\"FOOTBALL\"}"
                    }
                    matchWebSocket?.send(message)
                }
            })
            eventWsManager?.startConnect()
        }
    }

    private fun broadEventChange(changeList: List<FootballLive>) {
        changeList.forEach {
            if (it.id == mFootballInfo?.id) {
                EventBus.getDefault().post(it)
                live_tv_score_one.text = getScore(it.score[3].asString)
                live_tv_score_two.text = getScore(it.score[4].asString)
                return@forEach
            }
        }
    }

    private fun broadBasketChange(changeList: List<BasketPushData>?) {
        changeList?.forEach {
            if (it.id == mBasketBallInfo?.id) {
                EventBus.getDefault().post(it)
                if (it.score != null && it.score.size() == 4) {
                    val homeScore =
                        it.score?.get(3)?.asJsonArray?.let { it1 -> getScoreFromArray(it1) }
                    val awayScore =
                        it.score?.get(4)?.asJsonArray?.let { it1 -> getScoreFromArray(it1) }
                    live_tv_score_one.text = homeScore
                    live_tv_score_two.text = awayScore
                }
                return@forEach
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_live_sports
    }

    override fun getInentParams() {
        super.getInentParams()
        val intent = intent
        mFootballInfo = intent.getParcelableExtra(Constants.LIVE_MATCH_FOOTBALL_INFO) as MatchBean?
        mBasketBallInfo =
            intent.getParcelableExtra(Constants.LIVE_MATCH_BASKETBALL_INFO) as MatchBean?

        id = intent.getStringExtra("id")
        matchType = intent.getIntExtra("match_type", -1)
        when {
            mFootballInfo != null -> {
                mBottomTitles.addAll(arrayOf("赛况", "阵容", "情报", "指数", "分析"))
            }
            mBasketBallInfo != null -> {
                mBottomTitles.addAll(arrayOf("赛况", "情报", "指数", "分析"))
            }
            matchType == 2 -> {
                mBottomTitles.addAll(arrayOf("赛况", "情报", "指数", "分析"))
            }
            matchType == 3 -> {
                mBottomTitles.addAll(arrayOf("赛况", "阵容", "情报", "指数", "分析"))
            }
        }
    }

    override fun backClick(v: View) {
        if (v.id == com.yunbao.common.R.id.btn_back) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                finish()
                return
            }
            showFullScreen()
        }
    }

    override fun main() {
        super.main()
        ImmersionBar.with(this).statusBarDarkFont(false).navigationBarColor(R.color.white)
            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR).navigationBarDarkIcon(true).init()
        mRlTitle = findViewById(R.id.ll_game_title)
        mAbsViewHolders = arrayOfNulls(mBottomTitles.size)
        mLivePlayViewHolder = LivePlayTxViewHolder(mContext, play_container as ViewGroup?)
        mLivePlayViewHolder!!.addToParent()
        mLivePlayViewHolder!!.subscribeActivityLifeCycle()
        live_tv_living.setOnClickListener(this)
        setData()
        requestData()
        setViewPage()
        loadPageData(0)
    }

    private fun requestData() {
        if (matchType == 2) {
            getBasketBallInfo()
        }
        if (matchType == 3) {
            getFootBallInfo()
        }
    }

    @Subscribe
    fun onReceiveMathEvent(event: MatchEvent) {
        when (event.type) {
            1 -> {
            }
            2 -> setBaseKetBallInfo(event.basketBallBeanList)
            3 -> setFootBallInfo(event.footballBallBeanList)
        }
    }

    override fun onResume() {
        super.onResume()
        createEventService()
    }

    override fun onPause() {
        super.onPause()
        eventWsManager?.stopConnect()
        matchWebSocket?.close(1001, "")
        eventWsManager = null
        matchWebSocket = null
    }

    private fun setFootBallInfo(footballBallBeanList: ArrayList<GameFootballMatchBean>?) {
        if (footballBallBeanList == null) return
        for (bean in footballBallBeanList) {
            if (bean.matchId == id) {
                val isPlaying = bean.isIs_playing
                val state = bean.state_str
                when (isPlaying) {
                    1 -> {
                        live_view_stats.setText(R.string.vs)
                        live_tv_txt_status.text = state
                    }
                    2 -> {
                        live_tv_score_one.text = bean.homeScore
                        live_tv_score_two.text = bean.awayScore
                        tv_half.text = state
                    }
                    else -> {
                        live_tv_score_one.text = bean.homeScore
                        live_tv_score_two.text = bean.awayScore
                        live_tv_txt_status.text = state
                    }
                }
            }
        }
    }

    private fun setBaseKetBallInfo(basketBallBeanList: ArrayList<GameBasketballMatchBean>?) {
        if (basketBallBeanList == null) return
        for (bean in basketBallBeanList) {
            if (bean.matchId == id) {
                val isPlaying = bean.isIs_playing
                val state = bean.state_str
                when (isPlaying) {
                    1 -> {
                        live_view_stats.setText(R.string.vs)
                        live_tv_txt_status.text = state
                    }
                    2 -> {
                        live_tv_score_one.text = bean.homeScore
                        live_tv_score_two.text = bean.awayScore
                        tv_half.text = state
                    }
                    else -> {
                        live_tv_score_one.text = bean.homeScore
                        live_tv_score_two.text = bean.awayScore
                        live_tv_txt_status.text = state
                    }
                }
            }
        }
    }

    private fun getScore(scoreList: String?): String {
        if (TextUtils.isEmpty(scoreList)) {
            return "0"
        }
        val array: JsonArray = Gson().fromJson(scoreList, JsonArray::class.java)
        var score = 0
        try {
            score = array[0].asInt
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return score.toString()
    }

    private fun getBasketBallInfo() {
        if (mBasketBallInfo == null) {
            LiveHttpUtil.getBasketBallMatch(id, object : V2Callback<MatchBean>() {
                override fun onSuccess(code: Int, msg: String?, data: MatchBean?) {
                    mBasketBallInfo = data
                    setData()
                }
            })
        }
    }

    private fun getFootBallInfo() {
        if (mFootballInfo == null) {
            LiveHttpUtil.getFooBallMatch(id, object : V2Callback<MatchBean>() {
                override fun onSuccess(code: Int, msg: String?, data: MatchBean?) {
                    mFootballInfo = data
                    setData()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        //比赛状态:-14:推迟，-13:中断，-12:腰斩，-11:待定，-10:取消，-1.完场，0:未开始，1:上半场，2:中场，3:下半场，4:加时，5:点球
        if (mFootballInfo != null) {
            val homeTeam = mFootballInfo!!.home_team
            val awayTeam = mFootballInfo!!.away_team
            if (homeTeam != null) {
                ImgLoader.displayWithError(
                    this,
                    homeTeam.logo,
                    live_iv_team_one_icon,
                    R.mipmap.icon_default_logo
                )
                live_iv_team_one_name.text = homeTeam.nameZh
            }
            if (awayTeam != null) {
                ImgLoader.displayWithError(
                    this,
                    mFootballInfo!!.away_team.logo,
                    live_iv_team_two_icon,
                    R.mipmap.icon_default_logo
                )
                live_iv_team_two_name.text = awayTeam.nameZh
            }
            tv_title.text =
                if (mFootballInfo!!.league == null) "" else mFootballInfo!!.league.nameZh
            val time =
                DateFormatUtil.getMatchTime(mFootballInfo!!.matchTime * 1000L, "yyyy-MM-dd HH:mm")
            live_tv_date_info.text = time
            live_ll_match_info.setBackgroundResource(R.mipmap.bg_default_live_football)
            //1未开赛，2比赛中，3已完赛
            val isPlaying = mFootballInfo!!.isPlaying
            if (isPlaying == 1) {
                live_view_stats.setText(R.string.vs)
                live_tv_txt_status.text = mFootballInfo!!.state_str
                live_tv_living.visibility = View.GONE
            } else if (isPlaying == 2) {
                live_tv_score_one.text = getScore(mFootballInfo?.homeScores)
                live_tv_score_two.text = getScore(mFootballInfo?.awayScores)
                tv_half.text = mFootballInfo!!.state_str
                val liveUrl = mFootballInfo!!.live_url_1
                if (liveUrl.isEmpty()) {
                    live_tv_living.visibility = View.GONE
                } else {
                    live_tv_living.visibility = View.VISIBLE
                }
            } else {
                live_tv_score_one.text = getScore(mFootballInfo?.homeScores)
                live_tv_score_two.text = getScore(mFootballInfo?.awayScores)
                live_tv_txt_status.text = mFootballInfo!!.state_str
                live_tv_living.visibility = View.GONE
            }
        }
        if (mBasketBallInfo != null) {
            val homeTeam = mBasketBallInfo!!.home_team
            val awayTeam = mBasketBallInfo!!.away_team
            if (homeTeam != null) {
                ImgLoader.display(this, homeTeam.logo, live_iv_team_one_icon)
                live_iv_team_one_name.text = homeTeam.nameZh
            }
            if (awayTeam != null) {
                ImgLoader.display(this, awayTeam.logo, live_iv_team_two_icon)
                live_iv_team_two_name.text = awayTeam.nameZh
            }
            tv_title.text =
                if (mBasketBallInfo!!.league == null) "" else mBasketBallInfo!!.league.nameZh
            val time =
                DateFormatUtil.getMatchTime(mBasketBallInfo!!.matchTime * 1000L, "yyyy-MM-dd HH:mm")
            live_tv_date_info.text = time
            live_ll_match_info.setBackgroundResource(R.mipmap.bg_default_live_basketball)
            //1未开赛，2比赛中，3已完赛
            val isPlaying = mBasketBallInfo!!.isPlaying
            if (isPlaying == 1) {
                live_view_stats.setText(R.string.vs)
                live_tv_txt_status.text = mBasketBallInfo!!.state_str
            } else if (isPlaying == 2) {
                live_tv_score_one.text = getBasketScore(mBasketBallInfo?.homeScores)
                live_tv_score_two.text = getBasketScore(mBasketBallInfo?.awayScores)
                tv_half.text = mBasketBallInfo!!.state_str
                val liveUrl = mBasketBallInfo!!.live_url_1
                if (liveUrl.isEmpty()) {
                    live_tv_living.visibility = View.GONE
                } else {
                    live_tv_living.visibility = View.VISIBLE
                }
            } else {
                live_tv_score_one.text = getBasketScore(mBasketBallInfo?.homeScores)
                live_tv_score_two.text = getBasketScore(mBasketBallInfo?.awayScores)
            }
        }
    }

    private fun getBasketScore(scoreList: String?): String {
        if (TextUtils.isEmpty(scoreList)) {
            return "0"
        }
        val array: JsonArray = Gson().fromJson(scoreList, JsonArray::class.java)
        return getScoreFromArray(array)
    }

    private fun getScoreFromArray(array: JsonArray): String {
        var score = 0
        try {
            for (i in 0 until array.size()) {
                score += array[i].asInt
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return score.toString()
    }

    private fun setViewPage() {
        for (i in mBottomTitles.indices) {
            val frameLayout = FrameLayout(mContext)
            frameLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mViewList.add(frameLayout)
        }
        viewPager.adapter = ViewPagerAdapter(mViewList)
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                loadPageData(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mBottomTitles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return getIndicatorTitleView(context, mBottomTitles, index)
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        indicator.navigator = commonNavigator
        ViewPagerHelper.bind(indicator, viewPager)
    }

    private fun getIndicatorTitleView(
        context: Context?,
        titles: ArrayList<String>,
        index: Int
    ): IPagerTitleView {
        val simplePagerTitleView = ColorTransitionPagerTitleView(context)
        simplePagerTitleView.let {
            it.normalColor = ContextCompat.getColor(mContext, R.color.live_2B2626)
            it.selectedColor = ContextCompat.getColor(mContext, R.color.live_ff5116)
            it.text = titles[index]
            it.textSize = 13f
            it.paint.isFakeBoldText = true
            it.setOnClickListener {
                viewPager?.currentItem = index
            }
        }

        return simplePagerTitleView
    }

    private fun loadPageData(position: Int) {
        if (mAbsViewHolders == null) {
            return
        }
        var vh = mAbsViewHolders!![position]
        if (vh == null) {
            if (position < mViewList.size) {
                val parent = mViewList[position]
                when {
                    position == 0 -> {
                        val gameId =
                            if (mFootballInfo != null) mFootballInfo?.id else mBasketBallInfo?.id
                        mLiveChatViewHolder = LiveGameChatViewHolder(
                            mContext,
                            parent,
                            if (mFootballInfo != null) LiveGameChatViewHolder.TYPE_FOOTBALL else LiveGameChatViewHolder.TYPE_BASKETBALL,
                            if (mFootballInfo != null) mFootballInfo else mBasketBallInfo
                        )
                        vh = mLiveChatViewHolder
                    }
                    mFootballInfo != null || matchType == 3 -> vh =
                        createFootballHolder(position, parent)
                    mBasketBallInfo != null || matchType == 2 -> vh =
                        createBasketballHolder(position, parent)
                }
                mAbsViewHolders!![position] = vh
                vh?.addToParent()
                vh?.subscribeActivityLifeCycle()
            }
        }
        if (vh is LiveGameChatViewHolder) {
            mLiveChatViewHolder.isMatch()
        }
    }

    private fun createFootballHolder(position: Int, parent: ViewGroup): AbsViewHolder {
        return when (position) {
            1 -> FootballMatchHolder(mContext, parent, mFootballInfo)
            2 -> FootballLineUpViewHolder(mContext, parent, mFootballInfo)
            3 -> IntelligenceViewHolder(mContext, parent, mFootballInfo, true)
            4 -> ExponentViewHolder(mContext, parent, mFootballInfo, true)
            else -> AnalysisViewHolder(mContext, parent, mFootballInfo, true)
        }
    }

    private fun createBasketballHolder(position: Int, parent: ViewGroup): AbsViewHolder {
        return when (position) {
            1 -> BasketballMatchHolder(mContext, parent, mBasketBallInfo, false)
            2 -> IntelligenceViewHolder(mContext, parent, mBasketBallInfo, false)
            3 -> ExponentViewHolder(mContext, parent, mBasketBallInfo, false)
            else -> BasketBallAnalysisViewHolder(mContext, parent, mBasketBallInfo)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.live_tv_living) {
            play_container.visibility = View.VISIBLE
            mFootballInfo?.let {
                val liveUrl = listOf(it.live_url_1, it.live_url_2, it.live_url_3)
                playLive(liveUrl, mFootballInfo!!.id.toString())
            } ?: kotlin.run {
                mBasketBallInfo?.let {
                    val liveUrl = listOf(it.live_url_1, it.live_url_2, it.live_url_3)
                    playLive(liveUrl, mBasketBallInfo!!.id.toString())
                }
            }
        }
    }

    //播放直播
    private fun playLive(liveUrl: List<String>, matchId: String) {
        mLivePlayViewHolder?.setPlayMode("1", "1")
        mLivePlayViewHolder?.setLiveId(matchId.toString())

        var playUrl: String? = null
        for (url in liveUrl) {
            if (!TextUtils.isEmpty(url)) {
                playUrl = url
                break
            }
        }

        if (playUrl.isNullOrBlank()) {
            return
        }
        val mUrl = EasyAES.decryptString(playUrl)
        val resultUrl = if (!TextUtils.isEmpty(mUrl)) {
            val stream =
                Uri.parse(mUrl).lastPathSegment?.split("\\.".toRegex())?.toTypedArray()?.get(0)
            "$mUrl?txSecret=" + MD5Util.getMD5(
                "02019d98d6084be641cbf67ce3aa65fc"
                        + stream + java.lang.Long.toHexString(System.currentTimeMillis() / 1000)
            ) + "&txTime=" + java.lang.Long.toHexString(System.currentTimeMillis() / 1000)
        } else {
            playUrl
        }

        mLivePlayViewHolder!!.play(resultUrl)
    }

    fun showFullScreen() {
        setConfigDisplay(play_container, false)
        mLivePlayViewHolder?.changeWaterMark()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                showFullScreen()
                return true
            }
            if (mLivePlayViewHolder != null) {
                mLivePlayViewHolder!!.stopPlay()
            }
            if (play_container!!.visibility == View.VISIBLE) {
                play_container!!.visibility = View.GONE
            } else {
                return super.onKeyDown(keyCode, event)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}