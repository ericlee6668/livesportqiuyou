package com.yunbao.live.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.bean.MatchBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.http.V2Callback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.BitmapUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;
import com.yunbao.live.activity.LiveGameActivity;
import com.yunbao.live.adapter.LiveNewChatAdapter;
import com.yunbao.live.adapter.LiveRecommendAdapter;
import com.yunbao.live.adapter.RelativeLiveAdapter;
import com.yunbao.live.bean.LiveNewChatBean;
import com.yunbao.live.bean.MatchLiveBean;
import com.yunbao.live.dialog.LiveRoomChatManagerDialog;
import com.yunbao.live.dialog.LiveUserDialogFragment;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Juwan on 2018/10/9.
 * 观众聊天页
 */

public class LiveGameChatViewHolder extends AbsViewHolder {

    private ScrollView mSVAnchor;
    private RecyclerView mRvRecommend;
    private LiveRecommendAdapter mAdapterRecommend;
    private RecyclerView mChatRecyclerView;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_load_failed);

    private LiveNewChatAdapter mLiveChatAdapter;
    private String mLiveUid;
    private String mStream;
    protected int mUserListInterval;//用户列表刷新时间的间隔
    private TextView mTvIsSubscribed;
    private MainRecommendBean mMainRecommendBean;
    private static final int SETTING_ACTION_SELF = 0;//设置 自己点自己
    private static final int SETTING_ACTION_AUD = 30;//设置 普通观众点普通观众 或所有人点超管
    private static final int SETTING_ACTION_MANAGE = 35;//设置 房间管理员点普通观众
    private static final int SETTING_ACTION_ADM = 40;//设置 房间管理员点普通观众
    private static final int SETTING_ACTION_SUP = 60;//设置 超管点主播
    private static final int SETTING_ACTION_ANC_SUP = 70;//对方是超管
    private static final int SETTING_ACTION_ANC_AUD = 501;//设置 主播点普通观众
    private static final int SETTING_ACTION_ANC_ADM = 502;//设置 主播点房间管理员
    public static final int TYPE_GAME = 1;//游戏
    public static final int TYPE_FOOTBALL = 2;//足球
    public static final int TYPE_BASKETBALL = 3;//篮球
    private int mAction;
    private LiveRoomChatManagerDialog fragment;
    private View mLLInput;
    private TextView mTvInputDes;
    private ImageView mIvIcon;
    private int type;
    private TextView mTvAnnounce;
    private View mEmptyView;

    private MatchBean matchBean;

    public LiveGameChatViewHolder(Context context, ViewGroup parentView, Object... type) {
        super(context, parentView, type);
    }

    @Override
    protected void processArguments(Object... args) {
        type = (int) args[0];
        if (args.length > 1) {
            matchBean = (MatchBean) args[1];
        }
    }

    private void setImage(ImageView iv, String ivUrl) {
        Glide.with(mContext)
                .load(ivUrl)
                .apply(options)
                .into(iv);
    }

    /**
     * 添加聊天消息到聊天栏 和进入直播间
     */
    public void insertChat(LiveNewChatBean bean) {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.insertItem(bean);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_audience_chat;
    }

    public void setLiveInfo(String liveUid, String stream) {
        mLiveUid = liveUid;
        mStream = stream;
    }

    @Override
    public void init() {
        setChatView();
        setRecommendList();
        mSVAnchor = mContentView.findViewById(R.id.live_scroll_anchor);
        mLLInput = mContentView.findViewById(R.id.ll_live_bottom_input);
        mTvInputDes = mContentView.findViewById(R.id.tv_input_des);
        mIvIcon = mContentView.findViewById(R.id.iv_icon);
        mTvAnnounce = mContentView.findViewById(R.id.tv_announce);
        mSVAnchor.setVisibility(View.GONE);
        mContentView.findViewById(R.id.live_iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSVAnchor.setVisibility(View.GONE);
                setAnimation(0, 1, mSVAnchor);
            }
        });

        mContentView.findViewById(R.id.live_iv_to_anchor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSVAnchor.setVisibility(View.VISIBLE);
                setAnimation(1, 0, mSVAnchor);
            }
        });
        mTvAnnounce.setText(CommonAppConfig.getInstance().getConfig().getChatAnnounce());
        mLLInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                    DialogUitl.showLoginDialog(mContext, new DialogUitl.LiveLoginCallback() {
                        @Override
                        public void onLoginOrRegister() {
                            RouteUtil.forward(RouteUtil.PATH_LOGIN);
                        }

                        @Override
                        public void onGoToLiving() {

                        }
                    });
                    return;
                }
                ((LiveActivity) mContext).openChatWindow(null);
            }
        });

        mContentView.findViewById(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomRecommend();
            }
        });

        getRandomRecommend();
    }

    /**
     * 获取推荐直播
     */
    private void getRandomRecommend() {
        if (type == TYPE_GAME) {
            requestRandomRecommend();
        } else {
            requestRandomRecommend2();
        }
    }

    public void closeInfo() {
        mSVAnchor.setVisibility(View.GONE);
    }

    /**
     * 聊天栏滚到最底部
     */
    public void chatScrollToBottom() {
        if (mLiveChatAdapter != null) {
            mLiveChatAdapter.scrollToBottom();
        }
    }


    private void setChatView() {
        //聊天栏
        mChatRecyclerView = (RecyclerView) findViewById(R.id.liv_rc_chat_view);
        mChatRecyclerView.setHasFixedSize(true);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
//        mChatRecyclerView.addItemDecoration(new TopGradual());
        mLiveChatAdapter = new LiveNewChatAdapter(mContext);
        mLiveChatAdapter.setOnItemClickListener(new OnItemClickListener<LiveNewChatBean>() {
            @Override
            public void onItemClick(LiveNewChatBean bean, int position) {
                checkPermission(bean);
            }
        });
        mChatRecyclerView.setAdapter(mLiveChatAdapter);

    }

    /**
     * 检查权限
     *
     * @param bean
     */
    private void checkPermission(final LiveNewChatBean bean) {
        if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
            RouteUtil.forwardLogin("");
            return;
        }
        if (mLiveUid != null && bean != null && bean.getUser() != null && (!TextUtils.isEmpty(bean.getUser().getUid()) || !TextUtils.isEmpty(bean.getUser().getTouid()))) {
            LiveHttpUtil.getLiveUser(bean.getUser().getUid() == null ? bean.getUser().getTouid() : bean.getUser().getUid(), mLiveUid, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0) {
                        showData(bean, info[0]);
                    }
                }
            });
        }
    }

    private void showData(LiveNewChatBean bean, String data) {
        JSONObject obj = JSON.parseObject(data);
//        UserBean mUserBean = JSON.toJavaObject(obj, UserBean.class);
        mAction = obj.getIntValue("action");
        String uid = bean.getUser().getUid() == null ? bean.getUser().getTouid() : bean.getUser().getUid();
        if (mAction == SETTING_ACTION_AUD //设置 普通观众点普通观众 或所有人点超管
        ) {
            // TODO: 2020/10/23
            clickAvatar(uid);
        } else if (mAction == SETTING_ACTION_ANC_AUD) {//设置 主播点普通观众
            showRoomManagerDialog(1, uid);
        } else if (mAction == SETTING_ACTION_ANC_ADM) {//设置 主播点房间管理员
            showRoomManagerDialog(2, uid);
        } else if (mAction == SETTING_ACTION_ADM || mAction == SETTING_ACTION_MANAGE || mAction == SETTING_ACTION_SUP) {//设置 房间管理员点普通观众
            showRoomManagerDialog(3, uid);
        } else if (mAction == SETTING_ACTION_ANC_SUP) { //对方是超管
//            ToastUtil.show("对方是超管");
            clickAvatar(uid);
        }
    }

    /**
     * 管理聊天弹窗
     *
     * @param type
     * @param toUid
     */
    private void showRoomManagerDialog(int type, String toUid) {
        if (!TextUtils.isEmpty(mLiveUid) && !TextUtils.isEmpty(toUid)) {
            if (fragment != null) {
                fragment.dismiss();
                fragment = null;
            }
            fragment = new LiveRoomChatManagerDialog();
            fragment.setLifeCycleListener((LiveActivity) mContext);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.LIVE_UID, mLiveUid);
            bundle.putString(Constants.STREAM, mStream);
            bundle.putString(Constants.TO_UID, toUid);
            bundle.putInt(Constants.LIVE_TYPE, type);
            fragment.setArguments(bundle);
            fragment.show(((LiveActivity) mContext).getSupportFragmentManager(), "LiveRoomChatManagerDialog");
        }
    }


    /**
     * 显示个人资料弹窗
     */
    private void showUserDialog(String toUid) {
        if (!TextUtils.isEmpty(mLiveUid) && !TextUtils.isEmpty(toUid)) {
            LiveUserDialogFragment fragment = new LiveUserDialogFragment();
            fragment.setLifeCycleListener((LiveActivity) mContext);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.LIVE_UID, mLiveUid);
            bundle.putString(Constants.STREAM, mStream);
            bundle.putString(Constants.TO_UID, toUid);
            fragment.setArguments(bundle);
            fragment.show(((LiveActivity) mContext).getSupportFragmentManager(), "LiveUserDialogFragment");
        }
    }

    /**
     * 是赛事
     */
    public void isMatch() {
        mContentView.findViewById(R.id.live_cc_chat_view).setVisibility(View.GONE);
        mSVAnchor.setVisibility(View.VISIBLE);
        mContentView.findViewById(R.id.live_cc_top_view).setVisibility(View.GONE);
    }

    /**
     * 直播
     */
    public void setData(final MainRecommendBean mainRecommendBean) {
        this.mMainRecommendBean = mainRecommendBean;
        ImageView ivAnchorAvatar = mContentView.findViewById(R.id.live_iv_anchor_avatar);
        ImageView ivAnchorAvatar2 = mContentView.findViewById(R.id.live_iv_to_anchor);
        TextView tvAnchorName = mContentView.findViewById(R.id.live_tv_anchor_name);
        TextView tvHotNum = mContentView.findViewById(R.id.live_tv_hot_num);

        mTvIsSubscribed = mContentView.findViewById(R.id.live_tv_is_subscribed);
        mTvIsSubscribed.setText(mainRecommendBean.getIssubscribed() == 0 ? "订阅" : "已订阅");
        mTvIsSubscribed.setSelected(mainRecommendBean.getIssubscribed() == 0);

        TextView tvNotice = mContentView.findViewById(R.id.live_tv_notice);
        ivAnchorAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAvatar(mainRecommendBean.getUid());
            }
        });

        tvAnchorName.setText(mainRecommendBean.getUser_nicename());
        tvHotNum.setText(mainRecommendBean.getViewnum());
        if (!TextUtils.isEmpty(mainRecommendBean.getNotice())) {
            tvNotice.setText(mainRecommendBean.getNotice());
        } else {
            tvNotice.setText("主播很低调什么也没留下");
        }
        ImgLoader.displayAvatar(mContext, mainRecommendBean.getAvatar_thumb(), ivAnchorAvatar);
        ImgLoader.displayAvatar(mContext, mainRecommendBean.getAvatar(), ivAnchorAvatar2);

        mTvIsSubscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                    //已经登陆
                    recommendFollo(mainRecommendBean.getUid(), mainRecommendBean.getIssubscribed());
                    mainRecommendBean.setIssubscribed(mainRecommendBean.getIssubscribed() == 0 ? 1 : 0);
                    mMainRecommendBean = mainRecommendBean;
                } else {
                    //未登陆，去注册
                    RouteUtil.forwardLogin("");
                }
            }
        });
        getRandomRecommend();
    }

    /**
     * 点击头像
     */
    public void clickAvatar(String id) {
        if (!TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                RouteUtil.forwardLogin("");
                return;
            }
            RouteUtil.forwardUserHome(mContext, id, true, "");
        } else {
            RouteUtil.forwardLogin("");
        }
    }


    /**
     * 关注推荐主播
     *
     * @param touid
     * @param isSubscribe
     */
    private void recommendFollo(String touid, final int isSubscribe) {
        //type  默认为1，1订阅主播 2取消订阅
        LiveHttpUtil.SubscribeAnchor(touid, isSubscribe == 0 ? "1" : "2", new HttpCallbackObject() {
            @Override
            public void onSuccess(int code, String msg, Object info) {
                if (code == 0) {
                    ToastUtil.show(isSubscribe == 0 ? "订阅成功" : "取消订阅成功");
                    mTvIsSubscribed.setText(mMainRecommendBean.getIssubscribed() == 0 ? "订阅" : "已订阅");
                    mTvIsSubscribed.setSelected(mMainRecommendBean.getIssubscribed() == 0);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void setRecommendList() {
        mRvRecommend = mContentView.findViewById(R.id.home_recommend);
        mEmptyView = mContentView.findViewById(R.id.layout_empty);
        mRvRecommend.setNestedScrollingEnabled(false);
        mRvRecommend.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRvRecommend.setLayoutManager(gridLayoutManager);
        mAdapterRecommend = new LiveRecommendAdapter(mContext);
        mAdapterRecommend.setOnItemClickListener(new OnItemClickListener<MainRecommendBean>() {
            @Override
            public void onItemClick(MainRecommendBean bean, int position) {
                Intent intent = new Intent(mContext, LiveGameActivity.class);
                intent.putExtra(Constants.LIVE_RECOMMEND_DATA, bean);
                mContext.startActivity(intent);
                if (mMainRecommendBean != null) {
                    ((Activity) mContext).finish();
                }
            }
        });
        mRvRecommend.setAdapter(mAdapterRecommend);
    }

    private void requestRandomRecommend() {
        LiveHttpUtil.getLOLRandomRecommend(mMainRecommendBean == null ? "" : mMainRecommendBean.getUid(), "random", 1, 4, new HttpCallbackObject() {
            @Override
            public void onSuccess(int code, String msg, Object info) {
                List<MainRecommendBean> mainRecommendBeanList = JSON.parseArray(info.toString(), MainRecommendBean.class);
                if (mainRecommendBeanList != null && mainRecommendBeanList.size() > 0) {
                    mAdapterRecommend.setNewList(mainRecommendBeanList);
                    mEmptyView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //直播间分类id：2篮球，3英雄联盟，4足球，5星秀，6其他
    private void requestRandomRecommend2() {
        int classId = 0;
        switch (type) {
            case TYPE_FOOTBALL:
                classId = 4;
                break;
            case TYPE_BASKETBALL:
                classId = 2;
                break;
            default:
                break;
        }
        LiveHttpUtil.getLiveSportsList(4, classId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                List<MainRecommendBean> mainRecommendBeanList = JSON.parseArray(Arrays.toString(info), MainRecommendBean.class);
                if (mainRecommendBeanList != null && mainRecommendBeanList.size() > 0) {
                    mAdapterRecommend.setNewList(mainRecommendBeanList);
                    mEmptyView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });

        if (type == TYPE_FOOTBALL || type == TYPE_BASKETBALL) {
            boolean isFootball = type == TYPE_FOOTBALL;
            if (matchBean == null) {
                return;
            }
            LiveHttpUtil.getRelatedLive(isFootball, String.valueOf(matchBean.getId()), new V2Callback<List<MatchLiveBean>>() {

                @Override
                public void onSuccess(int code, String msg, List<MatchLiveBean> data) {
                    if (data != null && !data.isEmpty()) {
                        RecyclerView relativeList = mContentView.findViewById(R.id.relativeLiveList);
                        RelativeLiveAdapter adapter = new RelativeLiveAdapter(data);
                        adapter.setOnItemClickListener((adapter1, view, position) -> {
                            MatchLiveBean item = (MatchLiveBean) adapter1.getItem(position);
                            MainRecommendBean mainRecommendBean = new MainRecommendBean();
                            mainRecommendBean.setAvatar(item.getUser().getAvatar());
                            mainRecommendBean.setAvatar_thumb(item.getUser().getAvatar_thumb());
                            mainRecommendBean.setUser_nicename(item.getUser().getUser_nicename());
                            mainRecommendBean.setViewnum(item.getUser().getViewnum());
                            mainRecommendBean.setTitle(item.getTitle());
                            mainRecommendBean.setLiveclassid(item.getLiveclassid());
                            mainRecommendBean.setUid(item.getUid());
                            mainRecommendBean.setMatch_id(item.getMatch_id());
                            mainRecommendBean.setPull(item.getPull());
                            mainRecommendBean.setStream(item.getStream());
                            mainRecommendBean.setViewnum( item.getUser().getViewnum());
                            Intent intent = new Intent(mContext, LiveGameActivity.class);
//                            intent.putExtra(Constants.LIVE_MATCH_SPORTS, item);
//                            intent.putExtra(Constants.LIVE_MATCH_DATA, matchBean);
                            intent.putExtra(Constants.LIVE_RECOMMEND_DATA, mainRecommendBean);
                            mContext.startActivity(intent);
                        });
                        relativeList.setAdapter(adapter);
                    }
                }
            });
        }
    }

    private void setAnimation(int fromYValue, int toYValue, final View view) {
        TranslateAnimation ctrlAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, fromYValue, TranslateAnimation.RELATIVE_TO_SELF, toYValue);
        ctrlAnimation.setDuration(400l);
        view.startAnimation(ctrlAnimation);
        ctrlAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onDestroy() {
        mSVAnchor.clearAnimation();
        super.onDestroy();
    }

    public void setInPutEnable(boolean isEnableChat) {
        if (isEnableChat) {
            mLLInput.setClickable(true);
            mTvInputDes.setText("发条友善的弹幕，参与互动");
            BitmapUtil.getInstance().setColorDrawable(mIvIcon, false);
        } else {
            mLLInput.setClickable(false);
            mTvInputDes.setText("聊天室已关闭");
            BitmapUtil.getInstance().setColorDrawable(mIvIcon, true);
        }
    }

    public void deleteChat(String id) {
        List<LiveNewChatBean> list = mLiveChatAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            LiveNewChatBean mData = list.get(i);
            if (!TextUtils.isEmpty(mData.getUser().getUid())) {
                if (mData.getUser().getUid().equals(id)) {
                    list.remove(i);
                    i--;
                }
            }
        }
        mLiveChatAdapter.notifyDataSetChanged();
    }

    public void clearChatRoom() {
        mLiveChatAdapter.clear();
    }
}
