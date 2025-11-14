package com.yunbao.main.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.HtmlConfig;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.bean.LevelBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.bean.UserItemBean;
import com.yunbao.common.event.UpdateAvatarEvent;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.CommonIconUtil;
import com.yunbao.common.utils.ProcessResultUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.VersionUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.activity.ChatActivity;
import com.yunbao.live.activity.LiveRecordActivity;
import com.yunbao.live.activity.RoomManageActivity;
import com.yunbao.main.R;
import com.yunbao.main.activity.EditProfileActivity;
import com.yunbao.main.activity.FansActivity;
import com.yunbao.main.activity.FollowActivity;
import com.yunbao.main.activity.LoginActivity;
import com.yunbao.main.activity.MyActiveActivity;
import com.yunbao.main.activity.MyProfitActivity;
import com.yunbao.main.activity.MyVideoActivity;
import com.yunbao.main.activity.SettingActivity;
import com.yunbao.main.activity.SubscribeAnchorActivity;
import com.yunbao.main.adapter.MainMeAdapter;
import com.yunbao.main.event.LogOutEvent;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的
 */

public class MainMeViewHolder extends AbsMainViewHolder implements OnItemClickListener<UserItemBean>, View.OnClickListener {

    private boolean mPaused;
    private RecyclerView mRecyclerView;
    private LinearLayout llSubscribeAnchor;
    private LinearLayout llInviteRewards;
    private MainMeAdapter mAdapter;
    private LinearLayout llUserNotLogin;
    private View btnEdit;
    private ImageView mAvatar;
    private TextView mName;
    private ImageView mSex;
    private ImageView mLevelAnchor;
    private ImageView mLevel;
    private TextView mID;
    private TextView mFollow;
    private TextView mFans;
    private List<UserItemBean> userItemBeanlist;
    private List<UserItemBean> tempList;
    private TextView tv_login_tltie=null;
    private TextView tv_user_type_tltie=null;

    public MainMeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_main_user;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {

        tv_user_type_tltie = findViewById(R.id.tv_user_type_tltie);
        tv_login_tltie = findViewById(R.id.tv_login_tltie);
        llInviteRewards = findViewById(R.id.ll_invite_rewards);
        llSubscribeAnchor = findViewById(R.id.ll_subscribe_anchor);
        mRecyclerView = findViewById(R.id.recyclerView);
        llUserNotLogin = findViewById(R.id.ll_user_not_login);
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mSex = (ImageView) findViewById(R.id.sex);
        mLevelAnchor = (ImageView) findViewById(R.id.level_anchor);
        mLevel = (ImageView) findViewById(R.id.level);
        mID = (TextView) findViewById(R.id.id_val);
        mFollow = (TextView) findViewById(R.id.btn_follow);
        mFans = (TextView) findViewById(R.id.btn_fans);
        mFollow.setOnClickListener(this);
        mFans.setOnClickListener(this);
        btnEdit = findViewById(R.id.btn_edit);
        EventBus.getDefault().register(this);
        tv_user_type_tltie.setText("您当前是访问身份");
        tv_login_tltie.setText("点击登录");
    }

    private void initData() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void initListener() {
        llInviteRewards.setOnClickListener(this);
        llSubscribeAnchor.setOnClickListener(this);
        llUserNotLogin.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    /**
     * 是否登陆UI
     *
     * @param isLogin
     */
    public void showLoginView(boolean isLogin) {
        llUserNotLogin.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        mRecyclerView.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        if (!isLogin) {
            mName.setText("");
            mSex.setVisibility(View.GONE);
            mLevelAnchor.setVisibility(View.INVISIBLE);
            mLevel.setVisibility(View.INVISIBLE);
            mID.setText("");
            mFollow.setText("");
            mFans.setText("");
        } else {
            mSex.setVisibility(View.VISIBLE);
            mLevelAnchor.setVisibility(View.VISIBLE);
            mLevel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
//        if (isShowed() && mPaused) {
//            loadData();
//        }
        mPaused = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainHttpUtil.cancel(MainHttpConsts.GET_BASE_INFO);
    }

    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            CommonAppConfig appConfig = CommonAppConfig.getInstance();
            UserBean u = appConfig.getUserBean();
            userItemBeanlist = appConfig.getUserItemList();
            if (u != null && userItemBeanlist != null) {
                showData(u, userItemBeanlist);
                showLoginView(true);
            } else {
                showLoginView(false);
            }
        }
        MainHttpUtil.getBaseInfo(mCallback);
    }

    private CommonCallback<UserBean> mCallback = new CommonCallback<UserBean>() {
        @Override
        public void callback(UserBean bean) {
            userItemBeanlist = CommonAppConfig.getInstance().getUserItemList();
            if (bean != null) {
                showData(bean, userItemBeanlist);
                showLoginView(true);
            } else {
                showLoginView(false);
            }
        }
    };


    @SuppressWarnings("SuspiciousListRemoveInLoop")
    private void showData(UserBean u, List<UserItemBean> list) {
        try {
            if (tempList == null) {
                tempList = new ArrayList<>(list);
            } else {
                tempList.clear();
                tempList.addAll(list);
            }
            ImgLoader.displayAvatar(mContext, u.getAvatar(), mAvatar);
            mName.setText(u.getUserNiceName());
            mSex.setImageResource(CommonIconUtil.getSexIcon(u.getSex()));
            CommonAppConfig appConfig = CommonAppConfig.getInstance();
            LevelBean anchorLevelBean = appConfig.getAnchorLevel(u.getLevelAnchor());
            if (anchorLevelBean != null) {
                ImgLoader.display(mContext, anchorLevelBean.getThumb(), mLevelAnchor);
            }
            LevelBean levelBean = appConfig.getLevel(u.getLevel());
            if (levelBean != null) {
                ImgLoader.display(mContext, levelBean.getThumb(), mLevel);
            }
            mID.setText(u.getLiangNameTip());
            mFollow.setText(StringUtil.contact(StringUtil.toWan(u.getFollows()), " ", WordUtil.getString(R.string.follow)));
            mFans.setText(StringUtil.contact(StringUtil.toWan(u.getFans()), " ", WordUtil.getString(R.string.fans)));
            if (tempList != null && tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getId() == 8) tempList.remove(i);
                }
                if (mAdapter == null) {
                    mAdapter = new MainMeAdapter(mContext, tempList);
                    mAdapter.setOnItemClickListener(this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.setList(tempList);
                }
            }
        } catch (Exception e) {
            AppLog.e(e.toString());
        }
    }

    @Override
    public void onItemClick(UserItemBean bean, int position) {
//        if (bean.getId() == 22) {//我的小店
//            forwardMall();
//            return;
//        } else if (bean.getId() == 24) {//付费内容
//            forwardPayContent();
//            return;
//        }
        String url = bean.getHref();
        if (TextUtils.isEmpty(url)) {
            switch (bean.getId()) {
                case 1:
                    //我的收益
                    forwardProfit();
                    break;
                case 2:
                    //我的钻石
                    forwardCoin();
                    break;
                case 13:
                    //设置
                    forwardSetting();
                    break;
                case 16:
                    //检查更新
                    checkVersion();
                    break;
                case 19:
                    //我的视频
                    forwardMyVideo();
                    break;
                case 20:
                    //房间管理
                    forwardRoomManage();
                    break;
                case 23:
                    //我的动态
                    mContext.startActivity(new Intent(mContext, MyActiveActivity.class));
                    break;
                case 25:
                    //我的消息
                    ChatActivity.forward(mContext);
                    break;
            }
        } else {
            if (!url.contains("?")) {
                url = StringUtil.contact(url, "?");
            }
//            if (bean.getId() == 8) {//三级分销
//                ThreeDistributActivity.forward(mContext, bean.getName(), url);
//            } else {
            WebViewActivity.forward(mContext, url);
//            }
        }
    }

    @Subscribe
    public void onLogOutEvent(LogOutEvent event) {
        showLoginView(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateAvatarEvent(UpdateAvatarEvent e) {
        ImgLoader.displayAvatar(mContext, e.getAvatar(), mAvatar);
    }

    /**
     * 我的小店 商城
     */
    private void forwardMall() {
        UserBean u = CommonAppConfig.getInstance().getUserBean();
        if (u != null) {
            if (u.getIsOpenShop() == 0) {
                RouteUtil.forward(RouteUtil.PATH_MALL_BUYER);
            } else {
                RouteUtil.forward(RouteUtil.PATH_MALL_SELLER);
            }
        }
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (v.getId() == R.id.ll_user_not_login) {
            LoginActivity.forward();
        } else if (v.getId() == R.id.ll_invite_rewards) {
            //邀请奖励
            if (userItemBeanlist == null || userItemBeanlist.isEmpty()) {
                LoginActivity.forward();
                return;
            }
            String url = "";
            for (int j = 0; j < userItemBeanlist.size(); j++) {
                if (userItemBeanlist.get(j).getId() == 8) {
                    url = userItemBeanlist.get(j).getHref();
                    break;
                }
            }
            if (!url.contains("?")) {
                url = StringUtil.contact(url, "?");
            }
            WebViewActivity.forward(mContext, url);
        } else if (v.getId() == R.id.ll_subscribe_anchor) {
            // 订阅主播
            if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                LoginActivity.forward();
                return;
            }
            SubscribeAnchorActivity.launch(mContext);
        } else if (i == R.id.btn_edit || i == R.id.avatar) {
            if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                return;
            }
            RouteUtil.forwardUserHome(mContext, CommonAppConfig.getInstance().getUid());
        } else if (i == R.id.btn_follow) {
            forwardFollow();

        } else if (i == R.id.btn_fans) {
            forwardFans();

        } else if (i == R.id.btn_msg) {
//            ChatActivity.forward(mContext);
        } else if (i == R.id.btn_wallet) {
            RouteUtil.forwardMyCoin(mContext);
        } else if (i == R.id.btn_detail) {
            WebViewActivity.forward(mContext, HtmlConfig.DETAIL);
        } else if (i == R.id.btn_shop) {
            WebViewActivity.forward(mContext, HtmlConfig.SHOP);
        }
    }

    /**
     * 编辑个人资料
     */
    private void forwardEditProfile() {
        mContext.startActivity(new Intent(mContext, EditProfileActivity.class));
    }

    /**
     * 我的关注
     */
    private void forwardFollow() {
        FollowActivity.forward(mContext, CommonAppConfig.getInstance().getUid());
    }

    /**
     * 我的粉丝
     */
    private void forwardFans() {
        FansActivity.forward(mContext, CommonAppConfig.getInstance().getUid());
    }

    /**
     * 直播记录
     */
    private void forwardLiveRecord() {
        LiveRecordActivity.forward(mContext, CommonAppConfig.getInstance().getUserBean());
    }

    /**
     * 我的收益
     */
    private void forwardProfit() {
        mContext.startActivity(new Intent(mContext, MyProfitActivity.class));
    }

    /**
     * 我的钻石
     */
    private void forwardCoin() {
        RouteUtil.forwardMyCoin(mContext);
    }

    /**
     * 设置
     */
    private void forwardSetting() {
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
    }

    /**
     * 我的视频
     */
    private void forwardMyVideo() {
        mContext.startActivity(new Intent(mContext, MyVideoActivity.class));
    }

    /**
     * 房间管理
     */
    private void forwardRoomManage() {
        mContext.startActivity(new Intent(mContext, RoomManageActivity.class));
    }

    /**
     * 检查更新
     */
    private void checkVersion() {
        CommonAppConfig.getInstance().getConfig(new CommonCallback<ConfigBean>() {
            @Override
            public void callback(ConfigBean configBean) {
                if (configBean != null) {
                    if (VersionUtil.isLatest(configBean.getVersion())) {
                        ToastUtil.show(R.string.version_latest);
                    } else {
                        ProcessResultUtil mProcessResultUtil = new ProcessResultUtil((FragmentActivity) mContext );
                        VersionUtil.showDialog(mProcessResultUtil, mContext, configBean, configBean.getDownloadApkUrl());
                    }
                }
            }
        });

    }

}

