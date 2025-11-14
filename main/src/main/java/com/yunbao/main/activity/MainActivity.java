package com.yunbao.main.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.bean.UserItemBean;
import com.yunbao.common.custom.TabButton;
import com.yunbao.common.custom.TabButtonGroup;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.LocationUtil;
import com.yunbao.common.utils.LogUtil;
import com.yunbao.common.utils.ProcessResultUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.VersionUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.LiveConfig;
import com.yunbao.live.MatchService;
import com.yunbao.live.activity.LiveAnchorActivity;
import com.yunbao.live.bean.BasketPushData;
import com.yunbao.live.bean.FootballLive;
import com.yunbao.live.bean.LiveBean;
import com.yunbao.live.bean.LiveConfigBean;
import com.yunbao.live.event.MatchData;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.http.WsManager;
import com.yunbao.live.utils.LiveStorge;
import com.yunbao.main.R;
import com.yunbao.main.bean.BonusBean;
import com.yunbao.main.dialog.MainStartDialogFragment;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.interfaces.MainAppBarLayoutListener;
import com.yunbao.main.interfaces.MainStartChooseCallback;
import com.yunbao.main.presenter.CheckLivePresenter;
import com.yunbao.main.views.AbsMainViewHolder;
import com.yunbao.main.views.BonusViewHolder;
import com.yunbao.main.views.MainActiveViewHolder;
import com.yunbao.main.views.MainAvtivityDialog;
import com.yunbao.main.views.MainGameViewHolder;
import com.yunbao.main.views.MainHomeVideoPlayHolder;
import com.yunbao.main.views.MainHomeViewHolder;
import com.yunbao.main.views.MainMeViewHolder;
import com.yunbao.main.views.MyConfigCustomerService;
import com.yunbao.video.dialog.VideoShareDialogFragment;
import com.yunbao.video.event.ShareClickEvent;
import com.yunbao.video.utils.VideoStorge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

import static com.yunbao.common.CommonAppContext.sInstance;


public class MainActivity extends AbsActivity implements MainAppBarLayoutListener {

    private ViewGroup mRootView;
    private TabButtonGroup mTabButtonGroup;
    private ViewPager mViewPager;
    private List<FrameLayout> mViewList;
    private MainHomeViewHolder mHomeViewHolder;
    private MainActiveViewHolder mActiveViewHolder;
    private MainHomeVideoPlayHolder mVideoViewHolder;
    private MainGameViewHolder mGameViewHolder;
    private MainMeViewHolder mUserViewHolder;
    private MyConfigCustomerService mMyConfigCustomerService = null;
    private AbsMainViewHolder[] mViewHolders;
    private View mBottom;
    private int mDp70;
    private ObjectAnimator mUpAnimator;//向上动画
    private ObjectAnimator mDownAnimator;//向下动画
    private boolean mAnimating;
    private boolean mShowed = true;
    private boolean mHided;
    private ProcessResultUtil mProcessResultUtil;
    private CheckLivePresenter mCheckLivePresenter;
    private boolean mFristLoad;
    private long mLastClickBackTime;//上次点击back键的时间
    private HttpCallback mGetLiveSdkCallback;
    private AudioManager mAudioManager;
    private Intent mIntent;
    private WsManager wsManager;
    private WebSocket mWebSocketClient;
    private int vp_service_show_gone = 0;

    private static final int TYPE_SHOW_EXTERNAL = 0;//TODO 显示跳外部  3页面 ， 但是 4个 控件
    private static final int TYPE_SHOW_INTERNAL = 1;//TODO 显示跳 内部 4个控件 4个页面
    private static final int TYPE_GONEL = 2;//TODO 不显示
    private boolean isSpecial = false;//TODO 控制是否  TYPE_SHOW_EXTERNAL

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

//android.permission.READ_PHONE_STATE
    @Override
    protected void main() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
        ,Manifest.permission.READ_PHONE_STATE
        ,Manifest.permission.READ_PHONE_NUMBERS}, 2);
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        boolean showInvite = getIntent().getBooleanExtra(Constants.SHOW_INVITE, false);
        mRootView = findViewById(R.id.rootView);
        mTabButtonGroup = findViewById(R.id.tab_group);
        mViewPager = findViewById(R.id.viewPager);
//        mViewPager.setOffscreenPageLimit(5);


        if (getViewVisibleAndGone() == TYPE_SHOW_EXTERNAL) {
            isSpecial = true;
            vp_service_show_gone = 3;
        } else if (getViewVisibleAndGone() == TYPE_SHOW_INTERNAL) {
            isSpecial = false;
            vp_service_show_gone = 4;
        } else if (getViewVisibleAndGone() == TYPE_GONEL) {
            isSpecial = false;
            vp_service_show_gone = 3;
        }

        vp_service_show_gone = 4;
        mViewPager.setOffscreenPageLimit(vp_service_show_gone);

        mViewList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
        for (int i = 0; i < vp_service_show_gone; i++) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        mViewPager.setAdapter(new ViewPagerAdapter(mViewList));


        TabButton chViewButoom = (TabButton) mTabButtonGroup.getChildAt(2);
        String btitle = SpUtil.getInstance().getStringValue("btitle");
        String switchType = SpUtil.getInstance().getStringValue("switch");
        if (TextUtils.isEmpty(switchType) || switchType.equals("0")) {
            isSpecial = false;
            chViewButoom.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(switchType) && switchType.equals("1")) {
            isSpecial = true;
            chViewButoom.setVisibility(View.VISIBLE);
            if (chViewButoom.getmText() != null) {
                if (TextUtils.isEmpty(btitle)) {
                    btitle = "客服";
                }
                chViewButoom.getmText().setText(btitle);
            }
        }


        //别名
//        JPushInterface.setAlias(this,123215,"fdsfvdsfds");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                showTabView(position);
                loadPageData(position, true);
//                if (position != 2) {
//                    if (mVideoViewHolder != null) {
//                        mVideoViewHolder.setVisible(false);
//                    }
//                } else {
//                    if (mVideoViewHolder != null) {
//                        mVideoViewHolder.setVisible(true);
//                    }
//                }
                if (mViewHolders != null) {
                    for (int i = 0, length = mViewHolders.length; i < length; i++) {
                        AbsMainViewHolder vh = mViewHolders[i];
                        if (vh != null) {
                            vh.setShowed(position == i);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabButtonGroup.setViewPager(mViewPager);
//        mViewHolders = new AbsMainViewHolder[5];
        mViewHolders = new AbsMainViewHolder[vp_service_show_gone];
        mDp70 = DpUtil.dp2px(70);
        mBottom = findViewById(R.id.bottom);
        mUpAnimator = ObjectAnimator.ofFloat(mBottom, "translationY", mDp70, 0);
        mDownAnimator = ObjectAnimator.ofFloat(mBottom, "translationY", 0, mDp70);
        mUpAnimator.setDuration(250);
        mDownAnimator.setDuration(250);
        mUpAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                mShowed = true;
                mHided = false;
            }
        });
        mDownAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                mShowed = false;
                mHided = true;
            }
        });
        mProcessResultUtil = new ProcessResultUtil(this);
        EventBus.getDefault().register(this);
        checkVersion();
        if (showInvite) {
            showInvitationCode();
        }
        loginIM();
//        CommonHttpUtil.updatePushId(ImPushUtil.getInstance().getPushID());
        CommonAppConfig.getInstance().setLaunched(true);
        mFristLoad = true;
//        startMatchService();
        startSocketConnect();


        //TODO 政策弹框
        showPolicyDialog();

    }

    private void showPolicyDialog() {
        MainAvtivityDialog mMainAvtivityDialog=new MainAvtivityDialog(this,this).builder();
        mMainAvtivityDialog.setDialogData();
        //
        mMainAvtivityDialog.setmIsetDiaLogShadowOnClick(new MainAvtivityDialog.IsetDiaLogShadowOnClick() {
            @Override
            public void setDiaLogShadowOnClick() {
                SpUtil.getInstance().setStringValue("ce","no");
                finish();
            }
        });
        String typeShow=SpUtil.getInstance().getStringValue("ce");
        if (typeShow.equals("yes")){
            mMainAvtivityDialog.dismiss();
        }else {
            mMainAvtivityDialog.show();
        }

    }

    @Override
    protected void main(Bundle savedInstanceState) {
        super.main(savedInstanceState);
        if (savedInstanceState != null) {
            String host = savedInstanceState.getString("host");
            CommonAppConfig.setHost(host);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("host", CommonAppConfig.HOST);
        super.onSaveInstanceState(outState);
    }

    //开启赛事推送服务
    private void startMatchService() {
        mIntent = new Intent(this, MatchService.class);
        startService(mIntent);
    }

    private void requestStoragePermissions(final boolean toVideo) {
        if (mProcessResultUtil == null) {
            mProcessResultUtil = new ProcessResultUtil(this);
        }
        mProcessResultUtil.requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new Runnable() {
                    @Override
                    public void run() {
                        if (toVideo && mVideoViewHolder != null) {
                            mVideoViewHolder.loadData();
                        }
                    }
                });
    }

    private void showTabView(int position) {
        if (position == 2) {
            mTabButtonGroup.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            mTabButtonGroup.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    public void mainClick(View v) {
        if (!canClick()) {
            return;
        }
        int i = v.getId();
        if (i == R.id.btn_start) {
            showStartDialog();
        } else if (i == R.id.btn_search) {
            SearchActivity.forward(mContext);
        } else if (i == R.id.btn_msg) {
//            ChatActivity.forward(mContext);
        } else if (i == R.id.btn_add_active) {
            if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                RouteUtil.forwardLogin("");
                return;
            }
            startActivity(new Intent(mContext, ActivePubActivity.class));

        }
    }

    private void showStartDialog() {
        MainStartDialogFragment dialogFragment = new MainStartDialogFragment();
        dialogFragment.setMainStartChooseCallback(mMainStartChooseCallback);
        dialogFragment.show(getSupportFragmentManager(), "MainStartDialogFragment");
    }

    private MainStartChooseCallback mMainStartChooseCallback = new MainStartChooseCallback() {
        @Override
        public void onLiveClick() {
            mProcessResultUtil.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,

            }, mStartLiveRunnable);
        }

        @Override
        public void onVideoClick() {
            mProcessResultUtil.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,

            }, mStartVideoRunnable);
        }
    };

    private Runnable mStartLiveRunnable = new Runnable() {
        @Override
        public void run() {
            if (mGetLiveSdkCallback == null) {
                mGetLiveSdkCallback = new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            JSONObject obj = JSON.parseObject(info[0]);
                            int haveStore = obj.getIntValue("isshop");
                            if (CommonAppConfig.LIVE_SDK_CHANGED) {
                                int sdk = obj.getIntValue("live_sdk");
                                LiveConfigBean configBean = null;
                                if (sdk == Constants.LIVE_SDK_KSY) {
                                    configBean = JSON.parseObject(obj.getString("android"), LiveConfigBean.class);
                                } else {
                                    configBean = JSON.parseObject(obj.getString("android_tx"), LiveConfigBean.class);
                                }
                                LiveAnchorActivity.forward(mContext, sdk, configBean, haveStore);
                            } else {
                                LiveConfigBean liveConfigBean = null;
                                if (CommonAppConfig.LIVE_SDK_USED == Constants.LIVE_SDK_KSY) {
                                    liveConfigBean = LiveConfig.getDefaultKsyConfig();
                                } else if (CommonAppConfig.LIVE_SDK_USED == Constants.LIVE_SDK_TX) {
                                    liveConfigBean = LiveConfig.getDefaultTxConfig();
                                }
                                LiveAnchorActivity.forward(mContext, CommonAppConfig.LIVE_SDK_USED, liveConfigBean, haveStore);
                            }
                        }
                    }
                };
            }
            LiveHttpUtil.getLiveSdk(mGetLiveSdkCallback);
        }
    };


    private Runnable mStartVideoRunnable = new Runnable() {
        @Override
        public void run() {
//            startActivity(new Intent(mContext, VideoRecordActivity.class));
        }
    };

    /**
     * 检查版本更新
     */
    private void checkVersion() {
        CommonAppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean configBean) {
                if (configBean != null) {
                    if (configBean.getMaintainSwitch() == 1) {//开启维护
                        DialogUitl.showSimpleTipDialog(mContext, WordUtil.getString(R.string.main_maintain_notice), configBean.getMaintainTips(),
                                true, false, false, false);
                    }
                    if (!VersionUtil.isLatest(configBean.getVersion())) {
                        if (mProcessResultUtil == null) {
                            mProcessResultUtil = new ProcessResultUtil(MainActivity.this);
                        }
                        VersionUtil.showDialog(mProcessResultUtil, mContext, configBean, configBean.getDownloadApkUrl());
                    }
                }
            }
        });
    }

    /**
     * 填写邀请码
     */
    private void showInvitationCode() {
        DialogUitl.showSimpleInputDialog(mContext, WordUtil.getString(R.string.main_input_invatation_code), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(final Dialog dialog, final String content) {
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show(R.string.main_input_invatation_code);
                    return;
                }
                MainHttpUtil.setDistribut(content, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            ToastUtil.show(JSON.parseObject(info[0]).getString("msg"));
                            dialog.dismiss();
                        } else {
                            ToastUtil.show(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 签到奖励
     */
    private void requestBonus() {
        MainHttpUtil.requestBonus(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (obj.getIntValue("bonus_switch") == 0) {
                        return;
                    }
                    int day = obj.getIntValue("bonus_day");
                    if (day <= 0) {
                        return;
                    }
                    List<BonusBean> list = JSON.parseArray(obj.getString("bonus_list"), BonusBean.class);
                    BonusViewHolder bonusViewHolder = new BonusViewHolder(mContext, mRootView);
                    bonusViewHolder.setData(list, day, obj.getString("count_day"));
                    bonusViewHolder.show();
                }
            }
        });
    }

    /**
     * 登录IM
     */
    private void loginIM() {
        String uid = CommonAppConfig.getInstance().getUid();
//        ImMessageUtil.getInstance().loginImClient(uid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFristLoad) {
            mFristLoad = false;
            getLocation();
//            requestStoragePermissions(false);
            loadPageData(0, false);
            if (mHomeViewHolder != null) {
                mHomeViewHolder.setShowed(true);
            }
//            if (ImPushUtil.getInstance().isClickNotification()) {//MainActivity是点击通知打开的
//                ImPushUtil.getInstance().setClickNotification(false);
//                int notificationType = ImPushUtil.getInstance().getNotificationType();
//                if (notificationType == Constants.JPUSH_TYPE_LIVE) {
//                } else if (notificationType == Constants.JPUSH_TYPE_MESSAGE) {
//                }
//                ImPushUtil.getInstance().setNotificationType(Constants.JPUSH_TYPE_NONE);
//            } else {
//            }
        } else {
            if (mViewPager==null||mTabButtonGroup==null||mTabButtonGroup.getmTabButtons()==null){
                return;
            }
            mViewPager.setCurrentItem(positionInDex);

            for (int i = 0; i < mTabButtonGroup.getmTabButtons().length; i++) {


                if (i == positionInDex) {
                    mTabButtonGroup.getmTabButtons()[positionInDex].setChecked(true, positionInDex);
                } else {

                    mTabButtonGroup.getmTabButtons()[i].setChecked(false, positionInDex);
                }

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void showViewHolder(int positionInDex) {
        if (mViewHolders == null) {
            return;
        }
        for (int i = 0, length = mViewHolders.length; i < length; i++) {
            AbsMainViewHolder vh = mViewHolders[i];
            if (vh != null) {
                vh.setShowed(positionInDex == i);
                if (vh.getContentView()==null){
                   break;
                }
                if (positionInDex == i){
                    vh.getContentView().setVisibility(View.VISIBLE);
                }else {
                    vh.getContentView().setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 获取所在位置
     */
    private void getLocation() {
        // TODO: 2020/10/3  去除定位
//        mProcessResultUtil.requestPermissions(new String[]{
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//        }, new Runnable() {
//            @Override
//            public void run() {
//                LocationUtil.getInstance().startLocation();
//            }
//        });
    }

    public static void forward(Context context) {
        forward(context, false);
    }

    public static void forward(Context context, boolean showInvite) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constants.SHOW_INVITE, showInvite);
        context.startActivity(intent);
    }

    /**
     * 观看直播
     */
    public void watchLive(LiveBean liveBean, String key, int position) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        if (CommonAppConfig.LIVE_ROOM_SCROLL) {
            mCheckLivePresenter.watchLive(liveBean, key, position);
        } else {
            mCheckLivePresenter.watchLive(liveBean);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onImUnReadCountEvent(ImUnReadCountEvent e) {
//        String unReadCount = e.getUnReadCount();
//        if (!TextUtils.isEmpty(unReadCount)) {
//            if (mHomeViewHolder != null) {
//            }
//            if (mActiveViewHolder != null) {
//                mActiveViewHolder.setUnReadCount(unReadCount);
//            }
//        }
//    }

    /**
     * 短视频分享
     *
     * @param shareClickEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareClickEvent(final ShareClickEvent shareClickEvent) {
        VideoShareDialogFragment fragment = new VideoShareDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.VIDEO_BEAN, shareClickEvent.getmVideoBean());
        fragment.setArguments(bundle);
        fragment.show(((MainActivity) mContext).getSupportFragmentManager(), "VideoShareDialogFragment");
        fragment.setOnItemClickListener(new VideoShareDialogFragment.OnItemClickListener() {
            @Override
            public void copyLink() {
                if (mVideoViewHolder != null) {
                    mVideoViewHolder.copyLink(shareClickEvent.getmVideoBean());
                }
            }

            @Override
            public void shareVideoPage(String type) {
                if (mVideoViewHolder != null) {
                    mVideoViewHolder.shareVideoPage(type, shareClickEvent.getmVideoBean());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - mLastClickBackTime > 2000) {
            mLastClickBackTime = curTime;
            ToastUtil.show(R.string.main_click_next_exit);
            return;
        }
        super.onBackPressed();
    }

    int positionInDex = 0;

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void loadPageData(int position, boolean needlLoadData) {
        if (mViewHolders == null) {
            return;
        }

//        if (position == 2) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            ImmersionBar.with(this).statusBarDarkFont(false).navigationBarColor(R.color.black).navigationBarDarkIcon(false).init();
//        } else {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();
//        }
        AbsMainViewHolder vh = mViewHolders[position];
        String jtype = SpUtil.getInstance().getStringValue("jtype");
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    positionInDex = 0;
                    mHomeViewHolder = new MainHomeViewHolder(mContext, parent);
                    vh = mHomeViewHolder;
                } else if (position == 1) {
                    positionInDex = 1;
                    mGameViewHolder = new MainGameViewHolder(mContext, parent);
//                    mGameViewHolder.setAppBarLayoutListener(this);
                    vh = mGameViewHolder;
                }

                    if (position == 2) {
                        if (!TextUtils.isEmpty(jtype) && jtype.equals("0")) {
                            positionInDex = 2;
                            mMyConfigCustomerService = new MyConfigCustomerService(mContext, parent);
                            vh = mMyConfigCustomerService;
                        } else {
                            String url = SpUtil.getInstance().getStringValue("cs");
                            openBrowser(MainActivity.this, url);
                        }
                    } else if (position == 3) {
                        positionInDex = 5;
                        mUserViewHolder = new MainMeViewHolder(mContext, parent);
                        vh = mUserViewHolder;
                    }




//                else if (position == 2) {
//                    mVideoViewHolder = new MainHomeVideoPlayHolder(mContext, parent);
//                    vh = mVideoViewHolder;
//                } else if (position == 3) {
//                    mActiveViewHolder = new MainActiveViewHolder(mContext, parent);
//                    vh = mActiveViewHolder;
//                }
//                else if (position == 4) {
//                    mUserViewHolder = new MainMeViewHolder(mContext, parent);
//                    vh = mUserViewHolder;
//                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.subscribeActivityLifeCycle();
            }
        }else {
            if (position==0){
                positionInDex = 0;
            }else if (position==1){
                positionInDex = 1;
            }else if (position==2){
                if (!TextUtils.isEmpty(jtype) && jtype.equals("0")) {
                    positionInDex = 2;
                }else {
                    //TODO 保留上一次 什么都不做
                }
            }else if (position==3){
//                CommonAppConfig appConfig = CommonAppConfig.getInstance();
//                UserBean u = appConfig.getUserBean();
//                List<UserItemBean> userItemBeanlist = appConfig.getUserItemList();
                positionInDex = 5;
//                if (mUserViewHolder!=null){
//                    mUserViewHolder.showLoginView(true);
//                }
            }
        }

        if (needlLoadData && vh != null) {
//            if (vh instanceof MainHomeVideoPlayHolder) {
//                requestStoragePermissions(true);
//            } else {
//                vh.loadData();
//            }
            vh.loadData();
        }

        if (!(vh instanceof MainHomeVideoPlayHolder) && mVideoViewHolder != null) {
            mVideoViewHolder.stopPlay();
        }

        //未登陆
        if (vh instanceof MainMeViewHolder && TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
            LoginActivity.forward();
        }
    }

    @Override
    public void onOffsetChangedDirection(boolean up) {
        if (!mAnimating) {
            if (up) {
                if (mShowed && mDownAnimator != null) {
                    mDownAnimator.start();
                }
            } else {
                if (mHided && mUpAnimator != null) {
                    mUpAnimator.start();
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (mAudioManager != null) {
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_RAISE,
                            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (mAudioManager != null) {
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER,
                            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        disConnect();
        if (mTabButtonGroup != null) {
            mTabButtonGroup.cancelAnim();
        }
        EventBus.getDefault().unregister(this);
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_SDK);
        MainHttpUtil.cancel(CommonHttpConsts.GET_CONFIG);
        MainHttpUtil.cancel(MainHttpConsts.REQUEST_BONUS);
        MainHttpUtil.cancel(MainHttpConsts.GET_BONUS);
        MainHttpUtil.cancel(MainHttpConsts.SET_DISTRIBUT);
        if (mCheckLivePresenter != null) {
            mCheckLivePresenter.cancel();
        }
        LocationUtil.getInstance().stopLocation();
        if (mProcessResultUtil != null) {
            mProcessResultUtil.release();
        }
        if (mIntent != null) {
            stopService(mIntent);
        }
        CommonAppConfig.getInstance().setGiftListJson(null);
        CommonAppConfig.getInstance().setLaunched(false);
        LiveStorge.getInstance().clear();
        VideoStorge.getInstance().clear();
        super.onDestroy();
    }

    /**
     * 赛事socket
     */
    private void startSocketConnect() {
        wsManager = new WsManager.Builder(this, false, true).client(
                new OkHttpClient().newBuilder()
                        .pingInterval(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true)
                .wsUrl(CommonAppConfig.WEBSOCKET_MATCH).build();
        wsManager.setOnReceiveListener(new WsManager.OnReceiveListener() {
            @Override
            public void onMessage(String message) {
                sendMsg(message);
            }

            @Override
            public void onOpen() {
                mWebSocketClient = wsManager.getWebSocket();
                mWebSocketClient.send("{\"code\":8000,\"data\":\"BASKETBALL\"}");
                mWebSocketClient.send("{\"code\":8000,\"data\":\"FOOTBALL\"}");
            }
        });
        wsManager.startConnect();
    }

    /**
     * 收到的socket数据
     *
     * @param message
     */
    private void sendMsg(String message) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(message);
            int code = jsonObject.optInt("code");
            if (code == 8001) {
                Type type = new TypeToken<List<FootballLive>>() {
                }.getType();
                List<FootballLive> changeList = new Gson().fromJson(jsonObject.optString("data"), type);
                EventBus.getDefault().post(new MatchData(1, changeList, null));
            } else if (code == 8003) {
                Type type = new TypeToken<List<BasketPushData>>() {
                }.getType();
                List<BasketPushData> changeList = new Gson().fromJson(jsonObject.optString("data"), type);
                EventBus.getDefault().post(new MatchData(2, null, changeList));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("LiveEvent", "the message " + message + " transfer exception");
        }
    }

    public void disConnect() {
        if (wsManager != null) {
            wsManager.stopConnect();
        }
        if (mWebSocketClient != null) {
            mWebSocketClient.cancel();
        }
    }

    //判断是否显示服务
    private int getViewVisibleAndGone() {
        String service_switch = SpUtil.getInstance().getStringValue("switch");
        String jtype = SpUtil.getInstance().getStringValue("jtype");
        if (!TextUtils.isEmpty(service_switch) && service_switch.equals("1")) {
            if (!TextUtils.isEmpty(jtype) && jtype.equals("1")) {
                //显示 跳外部
                return TYPE_SHOW_EXTERNAL;
            } else {
                //显示 跳内部
                return TYPE_SHOW_INTERNAL;
            }
        } else {
            //隐藏
            return TYPE_GONEL;
        }
    }


    //跳转外部浏览器
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
//            LogUtil.d("suyan = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
//            GlobalMethod.showToast(context, "链接错误或无浏览器");
            Toast.makeText(context, "链接错误或无浏览器", Toast.LENGTH_SHORT).show();
        }
    }




}
