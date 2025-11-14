package com.yunbao.live.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.media.AudioManager
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.yunbao.common.CommonAppConfig
import com.yunbao.common.Constants
import com.yunbao.common.adapter.ViewPagerAdapter
import com.yunbao.common.bean.GameLolMatchBean
import com.yunbao.common.bean.MainRecommendBean
import com.yunbao.common.bean.MatchBean
import com.yunbao.common.event.CloseEvent
import com.yunbao.common.glide.ImgLoader
import com.yunbao.common.http.*
import com.yunbao.common.pay.PayPresenter
import com.yunbao.common.utils.*
import com.yunbao.common.utils.downmumusicutils.DownUtils
import com.yunbao.common.utils.downmumusicutils.MediaService
import com.yunbao.common.utils.downmumusicutils.MediaService.MediaBinder
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.adapter.LiveContactAdapter
import com.yunbao.live.adapter.LiveRoomScrollAdapter
import com.yunbao.live.bean.*
import com.yunbao.live.bean.LiveInfoContactBean.ContractBean
import com.yunbao.live.event.DeleteChatEvent
import com.yunbao.live.event.LiveRoomInfoEvent
import com.yunbao.live.event.MatchEvent
import com.yunbao.live.http.LiveHttpConsts
import com.yunbao.live.http.LiveHttpUtil
import com.yunbao.live.http.WsManager
import com.yunbao.live.http.WsManager.OnReceiveListener
import com.yunbao.live.views.*
import kotlinx.android.synthetic.main.activity_live_game_audience.*
import kotlinx.android.synthetic.main.activity_live_game_audience.indicator
import kotlinx.android.synthetic.main.activity_live_game_audience.live_iv_team_one_icon
import kotlinx.android.synthetic.main.activity_live_game_audience.live_iv_team_one_name
import kotlinx.android.synthetic.main.activity_live_game_audience.live_iv_team_two_icon
import kotlinx.android.synthetic.main.activity_live_game_audience.live_ll_match_info
import kotlinx.android.synthetic.main.activity_live_game_audience.live_tv_date_info
import kotlinx.android.synthetic.main.activity_live_game_audience.live_tv_living
import kotlinx.android.synthetic.main.activity_live_game_audience.live_tv_txt_status
import kotlinx.android.synthetic.main.activity_live_game_audience.live_view_stats
import kotlinx.android.synthetic.main.activity_live_game_audience.play_container
import kotlinx.android.synthetic.main.activity_live_game_audience.viewPager
import kotlinx.android.synthetic.main.activity_live_sports.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject
import java.text.MessageFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.net.NetworkCapabilities

import android.net.ConnectivityManager
import android.content.pm.PackageManager
import android.os.Build
import com.yunbao.live.table.TableLayoutManager
import javax.xml.transform.OutputKeys.VERSION


/**
 * 直播间
 */
class LiveGameActivity : LiveActivity() {
    private var mediaBinder: MediaService.MediaBinder? = null
    private var mMediaConnect: MediaConnect? = null;
    private var progressIndex: Int? = 0
    private var lastPosition = 0
    private var isChatOff = 0
    private var wsManager: WsManager? = null
    private var eventWsManager: WsManager? = null
    private var mRoomScrollAdapter: LiveRoomScrollAdapter? = null
    private var mLivePlayViewHolder: LivePlayTxViewHolder? = null
    private var mLiveAudienceViewHolder: LiveAudienceViewHolder? = null
    private var mLiveGameDataViewHolder: LiveGameDataViewHolder? = null
    private var mLiveExponentViewHolder: LiveGameExponentViewHolder? = null
    private var mLiveGamePlayerViewHolder: LiveGamePlayerViewHolder? = null
    private var mLiveGameAnalysisViewHolder: LiveGameAnalysisViewHolder? = null
    private var mLiveContactHostViewHolder: LiveContactHostViewHolder? = null
    private var mPrivateLetterViewHolder: PrivateLetterViewHolder? = null
    private var footballMatchHolder: FootballMatchHolder? = null
    private var footballLineupHolder: FootballLineUpViewHolder? = null
    private var intelligenceViewHolder: IntelligenceViewHolder? = null
    private var exponentViewHolder: ExponentViewHolder? = null
    private var analysisViewHolder: AnalysisViewHolder? = null
    private var basketballMatchHolder: BasketballMatchHolder? = null
    private var basketBallAnalysisViewHolder: BasketBallAnalysisViewHolder? = null
    private var mEnd = false
    private var mPayPresenter: PayPresenter? = null
    private var mViewList: ArrayList<FrameLayout> = ArrayList()
    private var mBottomTitles = arrayOf("聊天", "私信", "联系")
    private var mAbsViewHolders: Array<AbsViewHolder?>? = null
    private var mainRecommendBean: MainRecommendBean? = null
    private var mMatchInfo: GameLolMatchBean? = null
    private var sportMatchLiveBean: MatchLiveBean? = null
    private var matchBean: MatchBean? = null
    private var matchWebSocket: WebSocket? = null
    lateinit var mRlTitle: LinearLayout
    private val gson by lazy { Gson() }
    val REQUEST_READ_PHONE_STATE = 10000
    fun RequestPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
            val permissionCheck =
                this.applicationContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    REQUEST_READ_PHONE_STATE
                )
            }
        }
    }
    override fun getInentParams() {
        RequestPhoneStatePermission();



        val intent = intent
        mMatchInfo = intent.getSerializableExtra(Constants.LIVE_MATCH_INFO) as GameLolMatchBean?
        mainRecommendBean =
            intent.getSerializableExtra(Constants.LIVE_RECOMMEND_DATA) as MainRecommendBean?
        sportMatchLiveBean =
            intent.getParcelableExtra(Constants.LIVE_MATCH_SPORTS) as MatchLiveBean?
        matchBean = intent.getParcelableExtra(Constants.LIVE_MATCH_DATA) as MatchBean?
        mLiveType = intent.getIntExtra(Constants.LIVE_TYPE, Constants.LIVE_TYPE_NORMAL)
        mLiveTypeVal = intent.getIntExtra(Constants.LIVE_TYPE_VAL, 0)
        mLiveBean = intent.getParcelableExtra(Constants.LIVE_BEAN)

        if (mainRecommendBean != null && !TextUtils.isEmpty(mainRecommendBean?.match_id) && "0" != mainRecommendBean?.match_id) {  //不等于0 说明关联比赛

            mBottomTitles = when (mainRecommendBean?.liveclassid) {
                "2" -> {
                    mainRecommendBean?.match_id?.let { getFootBallInfo(it, "2") }
                    arrayOf("聊天", "私信", "联系", "赛况", "阵容", "情报", "指数", "分析")
                }
                "1" -> {
                    mainRecommendBean?.match_id?.let { getFootBallInfo(it, "1") }
                    arrayOf("聊天", "私信", "联系", "赛况", "情报", "指数", "分析")
                }
                else -> {
                    mainRecommendBean?.match_id?.let { getMatchData(it) }
                    arrayOf("主播", "数据", "指数", "选手", "分析")
                }
            }
        }
        if (mMatchInfo != null) {
            mBottomTitles = arrayOf("主播", "数据", "指数", "选手", "分析")
        }
        if (sportMatchLiveBean != null) {
            val classType = sportMatchLiveBean!!.liveclassid
            mBottomTitles = if (TextUtils.equals(classType, "1")) {
                arrayOf("聊天", "私信", "联系", "赛况", "情报", "指数", "分析")
            } else {
                arrayOf("聊天", "私信", "联系", "赛况", "阵容", "情报", "指数", "分析")
            }

        }
    }



    private fun getMatchData(match_id: String) {
        LiveHttpUtil.getMatchInfo(match_id, object : HttpCallbackObject() {
            override fun onSuccess(code: Int, msg: String, info: Any) {
                mMatchInfo = JSON.parseObject(info.toString(), GameLolMatchBean::class.java)
                matchStatus()
            }
        })
    }

    private fun initMatchView() {
        live_ll_match_info.visibility = View.VISIBLE
        mRlTitle = findViewById(R.id.rl_title)

        live_tv_living.setOnClickListener {
            live_ll_match_info.visibility = View.GONE
            if (mLivePlayViewHolder != null) {
                //TODO 视频播放
                mLivePlayViewHolder!!.setLiveId(mMatchInfo!!.id)
                mLivePlayViewHolder!!.play("http://tx2play1.douyucdn.cn/live/7596577rnqGac34w.flv?uuid=")
            }
        }
        live_tv_animation.setOnClickListener {
            //TODO 动画播放
        }

        tv_game_title.text = mMatchInfo?.league?.nameZh
        live_tv_date_info.text = MessageFormat.format(
            "{0} {1}",
            mMatchInfo?.match_date,
            mMatchInfo?.state_str
        )
        ImgLoader.displayAvatar(this, mMatchInfo?.home_team?.logo, live_iv_team_one_icon)
        ImgLoader.displayAvatar(this, mMatchInfo?.away_team?.logo, live_iv_team_two_icon)
        when (mMatchInfo?.is_playing) {
            1 -> {
                live_tv_txt_status.text = "未开赛"
                ll_live_status.visibility = View.GONE
                live_view_stats.text = "VS"
            }
            2 -> {
                live_tv_txt_status.text = ""
                ll_live_status.visibility = View.VISIBLE
                live_view_stats.text = MessageFormat.format(
                    "{0} - {1}",
                    getScore(mMatchInfo?.live_class_id ?: 0, mMatchInfo?.home_scores ?: ""),
                    getScore(mMatchInfo?.live_class_id ?: 0, mMatchInfo?.away_scores ?: "")
                )
            }
            3 -> {
                live_tv_txt_status.text = "比赛结束"
                ll_live_status.visibility = View.GONE
                live_view_stats.text = MessageFormat.format(
                    "{0} - {1}",
                    getScore(mMatchInfo?.live_class_id ?: 0, mMatchInfo?.home_scores ?: ""),
                    getScore(mMatchInfo?.live_class_id ?: 0, mMatchInfo?.away_scores ?: "")
                )
            }
        }
        live_iv_team_one_name.text = mMatchInfo?.home_team?.nameZh
        live_iv_team_one_name.text = mMatchInfo?.away_team?.nameZh
    }

    private fun getScore(type: Int, scores: String): String? {
        return if (type == 1) {
            getBasketBallScore(scores)
        } else getFootballScore(scores)
    }

    private fun getBasketBallScore(scoreList: String): String? {
        if (TextUtils.isEmpty(scoreList)) {
            return "0"
        }
        val array = gson.fromJson(scoreList, JsonArray::class.java)
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

    private fun getFootballScore(scoreList: String): String? {
        if (TextUtils.isEmpty(scoreList)) {
            return "0"
        }
        val array = gson.fromJson(scoreList, JsonArray::class.java)
        var score = 0
        try {
            score = array[0].asInt
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return score.toString()
    }

    private fun getFootBallInfo(match_id: String, classType: String) {
        if (matchBean != null) {
            return
        }

        if (TextUtils.equals(classType, "1")) {
            LiveHttpUtil.getBasketBallMatch(match_id, object : V2Callback<MatchBean>() {
                override fun onSuccess(code: Int, msg: String?, data: MatchBean?) {
                    matchBean = data
                }
            })
        } else {
            LiveHttpUtil.getFooBallMatch(match_id, object : V2Callback<MatchBean>() {
                override fun onSuccess(code: Int, msg: String?, data: MatchBean?) {
                    matchBean = data
                }
            })
        }
    }

    fun showFullScreen() {
        setConfigDisplay(play_container, false)
        mLivePlayViewHolder?.changeWaterMark()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_live_game_audience
    }

    override fun main() {
        super.main()
        val itenstMy = Intent(this, MediaService::class.java)
        mMediaConnect = MediaConnect(mediaBinder)

        bindService(
            itenstMy,
            mMediaConnect!!,
            BIND_AUTO_CREATE
        )

        ImmersionBar.with(this).statusBarDarkFont(false).titleBar(rl_title)
            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR).navigationBarColor(R.color.white)
            .navigationBarDarkIcon(true).init()
        mRlTitle = findViewById(R.id.rl_title)
        live_iv_back.setOnClickListener(View.OnClickListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                finish()
                return@OnClickListener
            }
            showFullScreen()
        })
        iv_game_back.setOnClickListener { finish() }
        if (mMatchInfo != null && mainRecommendBean == null) {
            initMatchView()
        }
        volumeControlStream = AudioManager.STREAM_MUSIC

        mLivePlayViewHolder = LivePlayTxViewHolder(mContext, play_container as ViewGroup?)
        mAbsViewHolders = arrayOfNulls(mBottomTitles.size)
        mLivePlayViewHolder?.addToParent()
        mLivePlayViewHolder?.subscribeActivityLifeCycle()
        matchStatus()
        setViewPage()
//        viewPager.currentItem = 0

        if (mainRecommendBean != null && mainRecommendBean?.pull != null) {
            mLiveUid = mainRecommendBean?.uid
            mStream = mainRecommendBean?.stream
            live_tv_live_title.text = mainRecommendBean?.title
            playLive()
            enterRoom()
        }
        if (mMatchInfo != null) {
            tv_game_title.text = mMatchInfo!!.league?.nameZh
            enterRoom()
        }

        if (sportMatchLiveBean != null) {
            mLiveUid = sportMatchLiveBean?.uid
            mStream = sportMatchLiveBean?.stream
            live_tv_live_title.text = sportMatchLiveBean?.title
            playMatchLive()
            enterRoom()
        }
        viewPager.currentItem = 0
        loadPageData(0)

        if (CameraUtils.checkTakePhotoPermission(this)) {//检查权限
            if (TextUtils.isEmpty(mLiveUid) || TextUtils.isEmpty(mStream)) {
                return;
            }
            getDataMu(mLiveUid, mStream, this);
        } else {
            //无权限，申请
            CameraUtils.requestTakePhotoPermissions(this);
        }
    }

    override fun onResume() {
        super.onResume()
        mMediaConnect?.mediaBinder?.resumePlay();
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)



        //权限申请回调
        //权限申请回调
        if (requestCode == CameraUtils.CAMERA_TAKE_PHOTO_PERMISSION) {
            if (CameraUtils.checkSelectPhotoPermission(this)) {
                if (TextUtils.isEmpty(mLiveUid) || TextUtils.isEmpty(mStream)) {
                    return;
                }
                getDataMu(mLiveUid, mStream, this);

            } else {
                //权限不足
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ),
//                    2
//                )

                Log.e("===", "===")
            }
        }

    }


    private fun matchStatus() {
//        todo 待处理
//        if (mainRecommendBean != null && mMatchInfo == null) {
//            live_ll_living_indicator.visibility = View.VISIBLE
//            indicator.visibility = View.GONE
//        } else {
//            live_ll_living_indicator.visibility = View.GONE
//            indicator.visibility = View.VISIBLE
//        }
    }

    private fun setViewPage() {
        if (mainRecommendBean != null && mainRecommendBean?.liveclassid != "1" && mainRecommendBean?.liveclassid != "2") {
            val frameLayout1 = FrameLayout(mContext)
            frameLayout1.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mViewList.add(frameLayout1)
            val frameLayout2 = FrameLayout(mContext)
            frameLayout2.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mViewList.add(frameLayout2)
            val frameLayout3 = FrameLayout(mContext)
            frameLayout3.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mViewList.add(frameLayout3)
        } else {
            for (i in mBottomTitles.indices) {
                val frameLayout = FrameLayout(mContext)
                frameLayout.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                mViewList.add(frameLayout)
            }
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
                if (position != 1) {
                    lastPosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.isAdjustMode = mBottomTitles.size < 4
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mBottomTitles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return getIndicatorTitleView(context, mBottomTitles, index)
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_MATCH_EDGE
                linePagerIndicator.xOffset = DpUtil.dp2px(5f).toFloat()
                linePagerIndicator.roundRadius = DpUtil.dp2px(2f).toFloat()
                linePagerIndicator.setColors(ContextCompat.getColor(mContext, R.color.live_ff5116))
                return linePagerIndicator
            }
        }
        indicator.navigator = commonNavigator
        ViewPagerHelper.bind(indicator, viewPager)
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
                    matchBean != null -> {
                        val classId =
                            if (mainRecommendBean != null) mainRecommendBean?.liveclassid else if (sportMatchLiveBean != null) sportMatchLiveBean?.liveclassid else null
                        if (classId != null) {
                            vh =
                                createMatchHolder(position, parent, mainRecommendBean!!.liveclassid)
                        } else {
                            when (position) {
                                0 -> {
                                    mLiveChatViewHolder = LiveGameChatViewHolder(
                                        mContext,
                                        parent,
                                        LiveGameChatViewHolder.TYPE_GAME
                                    )
                                    vh = mLiveChatViewHolder
                                }
                                1 -> {
                                    mPrivateLetterViewHolder =
                                        PrivateLetterViewHolder(mContext, parent)
                                    vh = mPrivateLetterViewHolder
                                }
                                2 -> { //联系主播
                                    mLiveContactHostViewHolder =
                                        LiveContactHostViewHolder(mContext, parent)
                                    vh = mLiveContactHostViewHolder
                                }
                            }
                        }
                    }
                    else -> {
                        when (position) {
                            0 -> {
                                mLiveChatViewHolder = LiveGameChatViewHolder(
                                    mContext,
                                    parent,
                                    LiveGameChatViewHolder.TYPE_GAME
                                )
                                vh = mLiveChatViewHolder
                            }
                            1 -> {
                                mPrivateLetterViewHolder =
                                    PrivateLetterViewHolder(mContext, parent)
                                vh = mPrivateLetterViewHolder
                            }
                            2 -> {
                                mLiveContactHostViewHolder =
                                    LiveContactHostViewHolder(mContext, parent)
                                vh = mLiveContactHostViewHolder
                            }
                        }

                    }
                }
                mAbsViewHolders!![position] = vh
                vh?.addToParent()
                vh?.subscribeActivityLifeCycle()
            }
        }
        if (vh is LiveGameDataViewHolder) {
            mLiveGameDataViewHolder?.loadData(mMatchInfo, 1)
        } else if (vh is LiveGameExponentViewHolder) {
            mLiveExponentViewHolder?.loadData(mMatchInfo)
        } else if (vh is LiveGamePlayerViewHolder) {
            mLiveGamePlayerViewHolder?.loadData(mMatchInfo)
        } else if (vh is LiveGameAnalysisViewHolder) {
            mLiveGameAnalysisViewHolder?.loadData(mMatchInfo)
        } else if (vh is LiveContactHostViewHolder) {
            if (mainRecommendBean != null) {
                mLiveContactHostViewHolder?.setLiveInfo(mLiveUid, mStream)
            }
            if (sportMatchLiveBean != null) {
                mLiveContactHostViewHolder?.setLiveInfo(mLiveUid, mStream)
            }
        } else if (vh is PrivateLetterViewHolder) {
            if (mainRecommendBean != null) {
                mPrivateLetterViewHolder?.setLiveInfo(mLiveUid, mStream)
            }
            if (sportMatchLiveBean != null) {
                mPrivateLetterViewHolder?.setLiveInfo(mLiveUid, mStream)
            }
        } else if (vh is LiveGameChatViewHolder) {
            if (mMatchInfo != null && mainRecommendBean == null) {
                mLiveChatViewHolder.isMatch()
            }
            if (mainRecommendBean != null) {
                mLiveChatViewHolder.setData(mainRecommendBean)
                mLiveChatViewHolder.setLiveInfo(mLiveUid, mStream)
            }

            if (sportMatchLiveBean != null) {
                mLiveChatViewHolder.setLiveInfo(mLiveUid, mStream)
            }
        }
    }

    private fun createMatchHolder(
        position: Int,
        parent: ViewGroup,
        classId: String
    ): AbsViewHolder? {
        return if (TextUtils.equals("1", classId)) {
            when (position) {
                0 -> {
                    mLiveChatViewHolder =
                        LiveGameChatViewHolder(mContext, parent, LiveGameChatViewHolder.TYPE_GAME)
                    mLiveChatViewHolder
                }
                1 -> {
                    mPrivateLetterViewHolder =
                        PrivateLetterViewHolder(mContext, parent)
                    mPrivateLetterViewHolder
                }
                2 -> {
                    mLiveContactHostViewHolder = LiveContactHostViewHolder(mContext, parent)
                    mLiveContactHostViewHolder
                }
                3 -> {
                    if (matchWebSocket == null) {
                        createEventService()
                    }
                    basketballMatchHolder = BasketballMatchHolder(mContext, parent, matchBean)
                    basketballMatchHolder
                }
                4 -> {
                    intelligenceViewHolder =
                        IntelligenceViewHolder(mContext, parent, matchBean, false)
                    intelligenceViewHolder
                }
                5 -> {
                    exponentViewHolder = ExponentViewHolder(mContext, parent, matchBean, false)
                    exponentViewHolder
                }
                else -> {
                    basketBallAnalysisViewHolder =
                        BasketBallAnalysisViewHolder(mContext, parent, matchBean)
                    basketBallAnalysisViewHolder
                }
            }
        } else {
            when (position) {
                0 -> {
                    mLiveChatViewHolder =
                        LiveGameChatViewHolder(mContext, parent, LiveGameChatViewHolder.TYPE_GAME)
                    mLiveChatViewHolder
                }
                1 -> {
                    mPrivateLetterViewHolder =
                        PrivateLetterViewHolder(mContext, parent)
                    mPrivateLetterViewHolder
                }
                2 -> {
                    mLiveContactHostViewHolder = LiveContactHostViewHolder(mContext, parent)
                    mLiveContactHostViewHolder
                }
                3 -> {
                    if (matchWebSocket == null) {
                        createEventService()
                    }
                    footballMatchHolder = FootballMatchHolder(mContext, parent, matchBean)
                    footballMatchHolder
                }
                4 -> {
                    footballLineupHolder = FootballLineUpViewHolder(mContext, parent, matchBean)
                    footballLineupHolder
                }
                5 -> {
                    intelligenceViewHolder =
                        IntelligenceViewHolder(mContext, parent, matchBean, true)
                    intelligenceViewHolder
                }
                6 -> {
                    exponentViewHolder = ExponentViewHolder(mContext, parent, matchBean, true)
                    exponentViewHolder
                }
                else -> {
                    analysisViewHolder = AnalysisViewHolder(mContext, parent, matchBean)
                    analysisViewHolder
                }
            }
        }
    }

    fun setInPutEnable(b: Boolean) {
        if (mLiveChatViewHolder == null) {
            loadPageData(0)
        }
        if (b) {
            live_tv_live_title.text =
                if (mainRecommendBean == null) "" else mainRecommendBean?.title
        } else {
            live_tv_live_title.text = ""
        }
        mLiveChatViewHolder.setInPutEnable(b)
    }

    private fun getIndicatorTitleView(
        context: Context?,
        titles: Array<String>,
        index: Int
    ): IPagerTitleView {
        val simplePagerTitleView = ColorTransitionPagerTitleView(context)
        simplePagerTitleView.let {
            it.normalColor = ContextCompat.getColor(mContext, R.color.black2)
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

    //播放直播
    private fun playMatchLive() {
        mLivePlayViewHolder?.setPlayMode(sportMatchLiveBean?.anyway, sportMatchLiveBean?.isvideo)
        mLivePlayViewHolder?.setLiveId(mLiveUid)
        mLivePlayViewHolder?.play(sportMatchLiveBean?.pull)
    }

    //播放视频
    private fun playLive() {
        if (mainRecommendBean == null) {
            return
        }
        if (TextUtils.isEmpty(mainRecommendBean!!.pull)){
            return
        }
        mLivePlayViewHolder?.setPlayMode(mainRecommendBean?.anyway, mainRecommendBean?.isvideo)
        mLivePlayViewHolder?.setLiveId(mLiveUid)

        var mUrl = mainRecommendBean!!.pull
        val l = System.currentTimeMillis() / 1000
        mUrl = if (mUrl.contains("?")) {
            "$mUrl&"
        } else {
            "$mUrl?"
        }
        val url = (mUrl + "txSecret=" + MD5Util.getMD5(
            "02019d98d6084be641cbf67ce3aa65fc"
                    + mStream + java.lang.Long.toHexString(l)
        )
                + "&txTime=" + java.lang.Long.toHexString(l))
        mLivePlayViewHolder?.play(url)
    }

    private fun clearRoomData() {
        if (mSocketClient != null) {
            mSocketClient.disConnect()
        }
        mSocketClient = null
        if (mLivePlayViewHolder != null) {
            mLivePlayViewHolder!!.stopPlay()
        }
        if (mLiveRoomViewHolder != null) {
            mLiveRoomViewHolder.clearData()
        }

        if (mLiveEndViewHolder != null) {
            mLiveEndViewHolder.removeFromParent()
        }
        if (mLiveLinkMicPresenter != null) {
            mLiveLinkMicPresenter.clearData()
        }
        if (mLiveLinkMicAnchorPresenter != null) {
            mLiveLinkMicAnchorPresenter.clearData()
        }
        if (mLiveLinkMicPkPresenter != null) {
            mLiveLinkMicPkPresenter.clearData()
        }

    }

    private fun createEventService() {
        if (eventWsManager != null) {
            return
        }
        val classId =
            if (mainRecommendBean != null) mainRecommendBean?.liveclassid else if (sportMatchLiveBean != null) sportMatchLiveBean!!.liveclassid else null
        if (classId != null) {
            val matchId = mainRecommendBean?.match_id ?: sportMatchLiveBean?.match_id
            eventWsManager = WsManager.Builder(baseContext, true, false).client(
                OkHttpClient().newBuilder()
                    .pingInterval(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            )
                .needReconnect(true)
                .wsUrl(CommonAppConfig.WEBSOCKET_MATCH).build()
            eventWsManager?.setOnReceiveListener(object : OnReceiveListener {
                override fun onMessage(message: String) {
                    if ("alive" == message) {
                        Log.i("LiveEvent", "heart beat")
                        return
                    }
                    Log.i("LiveEvent", message)
                    try {
                        val data = JSONObject(message)
                        val code = data.optInt("code")
                        if (TextUtils.equals(classId, "1")) {
                            if (8003 == code) {
                                val type = object : TypeToken<List<BasketPushData>>() {}.type
                                val changeList: List<BasketPushData> =
                                    gson.fromJson(data.optString("data"), type)
                                broadBasketChange(changeList, matchId)
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
                    val message = if (TextUtils.equals(sportMatchLiveBean?.liveclassid, "1")) {
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

    private fun broadBasketChange(changeList: List<BasketPushData>?, matchId: String?) {
        changeList?.forEach {
            if (it.id.toString() == matchId) {
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

    private fun broadEventChange(changeList: List<FootballLive>) {
        changeList.forEach {
            if (it.id == matchBean?.id) {
                EventBus.getDefault().post(it)
                return@forEach
            }
        }
    }

    /**
     * 进入主播或者赛事直播房间socket
     */
    private fun enterRoom() {
        setInPutEnable(true)
        if (mainRecommendBean != null || sportMatchLiveBean != null) {
            AppLog.e("enterRoom")
            isChatOff = if (mainRecommendBean != null) {
                mainRecommendBean?.is_chat_off ?: 0
            } else {
                sportMatchLiveBean?.is_chat_off ?: 0
            }
            if (!TextUtils.isEmpty(mLiveUid)) {
                LiveHttpUtil.enterRoom(mLiveUid, mStream, object : HttpCallback() {
                    override fun onSuccess(code: Int, msg: String, info: Array<String>) {
                        if (code == 0 && info.isNotEmpty()) {
                            val obj = JSON.parseObject(info[0])
                            isChatOff = obj.getIntValue("is_chat_off")
                            if (isChatOff == 1) {
                                setInPutEnable(false)
                                mLiveChatViewHolder.clearChatRoom()
                                AppLog.e("直播", "直播间已关闭")
                            }
                        }
                    }
                })

            }
            wsManager = WsManager.Builder(baseContext, true, false).client(
                OkHttpClient().newBuilder()
                    .pingInterval(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            )
                .needReconnect(true)
                .wsUrl(CommonAppConfig.WEBSOCKET_CHAT).build()
            AppLog.e("websocket", "链接地址:" + CommonAppConfig.WEBSOCKET_CHAT)
            if (mLiveUid != null) {
                wsManager?.setLiveId(mLiveUid)
            }
            wsManager?.setOnReceiveListener(object : OnReceiveListener {
                override fun onMessage(message: String) {

                    sendMsg(message)

                }

                override fun onOpen() {
                    mWebSocketClient = wsManager?.webSocket
                }
            })
            wsManager?.startConnect()
        }
    }

    /**
     * 收到的socket数据
     *
     * @param message
     */
    private fun sendMsg(message: String) {
        Log.i(TAG, "message is $message")
        if (mainRecommendBean != null || sportMatchLiveBean != null) {
            try {
                val `object` = JSONObject(message)
                val code = `object`.getInt("code")
                val msg = `object`.getString("msg")
                when (code) {
                    0 -> { //0 成功
                        val liveNewChatBean =
                            Gson().fromJson(`object`.getString("data"), LiveNewChatBean::class.java)
                        val logs = liveNewChatBean.logs
                        if (logs != null) {
                            for (log in logs) {
                                onNewChat(log)
                            }
                        }
                        if (isChatOff == 1) {
                            setInPutEnable(false)
                            mLiveChatViewHolder.clearChatRoom()
                        } else {
                            setInPutEnable(true)
                            onNewChat(liveNewChatBean)
                        }
                    }
                    2000 -> {
                        // 下播 {"code":2000,"msg":"","data":{}}
                        // ToastUtil.show("聊天室已关闭!");
                        mLivePlayViewHolder?.stopPlay()
                        musicBackOnStop()
                        mLivePlayViewHolder?.showCover(false)
                        setInPutEnable(false)
                    }
                    2001, 1004 -> {
                        // {"code":2001,"msg":"被禁言！","data":{"touid":"1","tousername":"xxx"}}
                        if (`object`.has("data")) {
                            val liveChatUserBean = Gson().fromJson(
                                `object`.getString("data"),
                                LiveChatUserBean::class.java
                            )
                            val uid =
                                if (liveChatUserBean.uid == null) liveChatUserBean.touid else liveChatUserBean.uid

                            //被禁言删除此人的聊天记录
                            EventBus.getDefault().post(DeleteChatEvent(uid))
                            //是自己才弹框提示
                            if (!TextUtils.isEmpty(uid)) {
                                if (uid == CommonAppConfig.getInstance().uid) {
                                    if (!isFinishing) {
                                        DialogUitl.showLogoTipDialog(
                                            this@LiveGameActivity,
                                            getString(R.string.dialog_tip),
                                            getString(R.string.live_you_are_shut_contact_customer_service),
                                            true,
                                            true,
                                            true,
                                            false
                                        )
                                    }
                                }
                                val liveNewChatBean = LiveNewChatBean()
                                liveNewChatBean.type = "2001"
                                liveNewChatBean.msg = msg
                                liveNewChatBean.user = liveChatUserBean
                                onNewChat(liveNewChatBean)
                            }
                        } else {
                            //  ToastUtil.show(msg);
                            DialogUitl.showLogoTipDialog(
                                this@LiveGameActivity, getString(R.string.dialog_tip), msg, true,
                                true, true, false
                            )
                        }
                    }
                    2002 -> {
                        //     {"code":2002,"msg":"被设为管理员！","data":{"touid":"1","tousername":"xxx"}}
                        val liveChatUserBean = Gson().fromJson(
                            `object`.getString("data"),
                            LiveChatUserBean::class.java
                        )
                        val uid =
                            if (liveChatUserBean.uid == null) liveChatUserBean.touid else liveChatUserBean.uid
                        if (!TextUtils.isEmpty(uid)) {
                            //是自己才弹框提示
                            if (uid == CommonAppConfig.getInstance().uid) {
                                if (!TextUtils.isEmpty(msg)) {
                                    ToastUtil.show(msg)
                                }
                            }
                            val liveNewChatBean = LiveNewChatBean()
                            liveNewChatBean.type = "2002"
                            liveNewChatBean.msg = msg
                            liveNewChatBean.user = liveChatUserBean
                            onNewChat(liveNewChatBean)
                        }
                    }
                    2003 -> { //清空聊天消息
                        mLiveChatViewHolder.clearChatRoom()
                    }
                    2004 -> { //关闭聊天室
                        setInPutEnable(false)
                        mLiveChatViewHolder.clearChatRoom()
                    }
                    2005 -> {//打开聊天室
                        isChatOff = 0
                        setInPutEnable(true)
                        wsManager?.stopConnect()
                        enterRoom()
                    }
                    else -> {
                        if (!TextUtils.isEmpty(msg)) {
                            // TODO: 2020/11/13
                            //                        ToastUtil.show(msg);
                            AppLog.e(msg)
                        }
                    }
                }
            } catch (e: JSONException) {
                AppLog.e(e.toString())
            }
        }
    }


    /**
     * 结束观看
     */
    private fun endPlay() {
        leaveRoom()
        if (mEnd) {
            return
        }
        mEnd = true
        //断开socket
        mSocketClient?.disConnect()
        mSocketClient = null
        //结束播放
        mLivePlayViewHolder?.release()
        mLivePlayViewHolder = null
        release()
    }

    override fun release() {
        mSocketClient?.disConnect()
        mPayPresenter?.release()
        mPayPresenter = null
        eventWsManager?.stopConnect()
        LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE)
        LiveHttpUtil.cancel(LiveHttpConsts.ENTER_ROOM)
        LiveHttpUtil.cancel(LiveHttpConsts.ROOM_CHARGE)
        CommonHttpUtil.cancel(CommonHttpConsts.GET_BALANCE)
        super.release()
        mRoomScrollAdapter?.release()
        mRoomScrollAdapter = null
    }

    /**
     * 观众收到直播结束消息
     */
    override fun onLiveEnd() {
        super.onLiveEnd()
        endPlay()
        if (viewPager?.currentItem != 1) {
            viewPager?.setCurrentItem(1, false)
        }
        mLiveEndViewHolder.showData(mLiveBean, mStream)

    }

    /**
     * 观众收到踢人消息
     */
    override fun onKick(touid: String) {
        if (!TextUtils.isEmpty(touid) && touid == CommonAppConfig.getInstance().uid) { //被踢的是自己
            exitLiveRoom()
            ToastUtil.show(WordUtil.getString(R.string.live_kicked_2))
        }
    }

    /**
     * 观众收到禁言消息
     */
    override fun onShutUp(touid: String, content: String) {
        if (!TextUtils.isEmpty(touid) && touid == CommonAppConfig.getInstance().uid) {
            DialogUitl.showSimpleTipDialog(mContext, content)
        }
    }

    override fun onBackPressed() {
        if (!mEnd && !canBackPressed()) {
            return
        }
        exitLiveRoom()
    }

    /**
     * 退出直播间
     */
    fun exitLiveRoom() {
        endPlay()
        super.onBackPressed()
    }

    override fun onDestroy() {

        //离开直播间
        leaveRoom()
        mLiveAudienceViewHolder?.clearAnim()
        wsManager?.stopConnect()
        wsManager = null
        mWebSocketClient?.cancel()
        mWebSocketClient = null
        Log.i("=====================", "onDestroy 走播放器 钱 ABC");
        musicBackOnDestroy()
        AppLog.e("LiveAudienceActivity-------onDestroy------->")
        super.onDestroy()
        Log.i("=====================", "----onDestroy什么都不做");
    }

    fun musicBackOnDestroy() {
        mMediaConnect?.mediaBinder?.stop()
        if (progressIndex != 100) {
            Log.i("=====================", "----------------musicBackOnDestroy" + progressIndex);
            DownUtils.getInstance().stopDown()
            DownUtils.getInstance().stopCancelDown()
        }
        Log.i("=====================", "----------------musicBackOnDestroy出来后" + progressIndex);

//        mMediaConnect?.mediaBinder?.pausePlay();

        mMediaConnect?.mediaBinder = null
        mMediaConnect = null

    }

    override fun onStop() {
        musicBackOnStop()
        super.onStop()
    }

    fun musicBackOnStop() {
        mMediaConnect?.mediaBinder?.pausePlay();
        if (progressIndex != 100) {
            DownUtils.getInstance().stopDown()
            DownUtils.getInstance().stopCancelDown()
        }
//        mMediaConnect?.mediaBinder?.stop()
    }

    /**
     * 计时收费更新主播映票数
     */
    fun roomChargeUpdateVotes() {
        sendUpdateVotesMessage(mLiveTypeVal)
    }

    /**
     * 暂停播放
     */
    fun pausePlay() {
        mLivePlayViewHolder?.pausePlay()
    }

    /**
     * 恢复播放
     */
    fun resumePlay() {
        mLivePlayViewHolder?.resumePlay()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClosePrivateChatEvent(e: CloseEvent) {
        viewPager.setCurrentItem(lastPosition)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLiveRoomInfoEvent(e: LiveRoomInfoEvent?) {
        mLiveChatViewHolder?.closeInfo()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveMathEvent(event: MatchEvent) {
        val type = event.type
        AppLog.e("详情收到赛事通知：$type")
        try {
            when (type) {
                1 -> {
                    val lolBeanList = event.lolBeanList
                    if (lolBeanList != null && lolBeanList.isNotEmpty()) {
                        AppLog.e("详情收到赛事通知：刷新数据")
                        for (gameLolMatchBean in lolBeanList) {
                            if (mMatchInfo?.id == gameLolMatchBean.id) {
                                mLiveGameDataViewHolder?.loadData(mMatchInfo, 1)
                                mLiveExponentViewHolder?.loadData(mMatchInfo)
                            }
                        }
                    }
                }

                2 -> {
                }
            }
        } catch (e: Exception) {
            AppLog.e(e.toString())
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                showFullScreen()
                return true
            }
            if (mMatchInfo != null) {
                mLivePlayViewHolder?.stopPlay()
                musicBackOnStop()
                if (live_ll_match_info.visibility == View.GONE) {
                    live_ll_match_info.visibility = View.VISIBLE
                } else {
                    return super.onKeyDown(keyCode, event)
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 离开直播间
     */
    private fun leaveRoom() {
        val stream = mainRecommendBean?.stream ?: sportMatchLiveBean?.stream
        LiveHttpUtil.leaveRoom(stream, object : HttpCallback() {
            override fun onSuccess(code: Int, msg: String, info: Array<String>) {}
        })
    }

    companion object {
        private const val TAG = "LiveAudienceActivity"
    }


    fun getDataMu(mLiveUids: String, mStreams: String, content: Context) {

        LiveHttpUtil.getLiveInfoContact(mLiveUids, mStreams, object : HttpCallback() {
            override fun onSuccess(code: Int, msg: String, info: Array<String>) {
//                mRefresh.finishRefresh()
                AppLog.e("code:" + code + "info:" + Arrays.toString(info))
                if (code == 0) {
                    val list = JSON.parseArray(
                        Arrays.toString(info),
                        LiveInfoContactBean::class.java
                    )
                    if (list == null || list.size == 0 || TextUtils.isEmpty(list.get(0).audio_path)) {
                        return
                    }
                    DownUtils.getInstance().addDownload(list[0].audio_path)

                    Log.e("===", "===");

                }
            }

            override fun onFinish() {
                super.onFinish()
                if (mLivePlayViewHolder != null) {
                    mLivePlayViewHolder?.setmMediaConnect(mMediaConnect)
                }
                DownUtils.getInstance().setOnDownListener(object : DownUtils.OnDownListener {
                    override fun onDownloadStart(path: String?) {
                        Log.e("===", "====");
                    }

                    override fun onDownFinish(path: String?) {
                        if (TextUtils.isEmpty(path)) {
                            return
                        }
                        Log.i("=====================", "----------------onDownFinish");
                        progressIndex = 100;
//                        mMediaConnect?.mediaBinder?.play(path)
                        mMediaConnect?.mediaBinder?.countPlay(-1, path);
                        mLivePlayViewHolder?.musicPath = path
                    }

                    override fun onDowning(progress: Int) {
                        Log.e("===", "====");
                        Log.i("=====================", "----------------onDowning" + progress);
                        progressIndex = progress;
                    }

                    //存在
                    override fun onDownExist(path: String?) {
                        if (TextUtils.isEmpty(path)) {
                            return
                        }
                        Log.i("=====================", "----------------onDownExist" + path);
                        progressIndex = 100;
//                        mMediaConnect?.mediaBinder?.play(path)
                        mMediaConnect?.mediaBinder?.countPlay(-1, path);
                        mLivePlayViewHolder?.musicPath = path
                    }
                })
            }


            override fun onError(ret: Int, msg: String) {
                super.onError(ret, msg)
                DownUtils.getInstance().stopCancelDown()
            }
        })
    }

    class MediaConnect(mediaBinder: MediaService.MediaBinder?) : ServiceConnection {
        var mediaBinder: MediaService.MediaBinder? = null

        init {
            this.mediaBinder = mediaBinder;
            Log.e("===", "====")
        }

        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            this.mediaBinder = iBinder as MediaService.MediaBinder
            Log.i("connect", "connect")
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            this.mediaBinder = null
        }
    }
}