package com.yunbao.main.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.Data;
import com.yunbao.common.http.DataNewX;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.HttpCallbackNewX;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.http.JsonBean;
import com.yunbao.common.http.JsonBeanNewX;
import com.yunbao.common.http.V2Callback;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.activity.LiveGameActivity;
import com.yunbao.live.activity.LiveRecommendActivity;
import com.yunbao.live.activity.LiveSportsActivity;
import com.yunbao.main.R;
import com.yunbao.main.activity.LoginActivity;
import com.yunbao.main.adapter.home.HomeAnchorAdapter;
import com.yunbao.main.adapter.home.HomeGameAdapter;
import com.yunbao.main.adapter.home.HomeRecommendAdapter;
import com.yunbao.main.bean.AnchorAnchorBean;
import com.yunbao.main.bean.BannerBean;
import com.yunbao.main.bean.SlideBannerBean;
import com.yunbao.main.bean.SlideEventsBean;
import com.yunbao.main.http.MainHttpUtil;
import com.yunbao.main.presenter.CheckLivePresenter;
import com.yunbao.main.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 首页
 */
public class MainHomeViewHolder extends AbsMainViewHolder {

    private SmartRefreshLayout mRefresh;
    private Banner mBanner;
    private TextView tvChange;
    private TextView tvMore;
    private HomeGameAdapter mAdapterGame;
    private HomeRecommendAdapter mAdapterRecommend;
    private HomeAnchorAdapter mHomeAnchorAdapter;
    private RecyclerView recyclerViewRecommend;
    private RecyclerView mHomeAnchorRecyclerView;
    private RecyclerView recyclerViewGame;
    private List<BannerBean> slideBeanList;
    private List<GameLolMatchBean> listBeans;
    private List<AnchorAnchorBean> anchorBeanList;
    private RecyclerViewSkeletonScreen skeletonAnchor;
    private RecyclerViewSkeletonScreen skeletonGames;
    private RecyclerViewSkeletonScreen skeletonRecommend;
    private NestedScrollView scrollView;
    private ViewSkeletonScreen skeletonBanner;
    private CheckLivePresenter mCheckLivePresenter;
    private View mllAnchor;

    public MainHomeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {
        initView();
//        setBanner();
        skeletonBanner = Skeleton.bind(mBanner)
                .load(R.layout.ietm_banner_skeleton)
                .shimmer(false)
                .show();
        setGameView();
        setRecommendView();
        setAnchorView();
        initData();
        initListener();
    }

    private void initView() {
        mRefresh = mContentView.findViewById(R.id.mRefresh);
        mBanner = mContentView.findViewById(R.id.banner);
        tvChange = mContentView.findViewById(R.id.tv_change);
        tvMore = mContentView.findViewById(R.id.tv_more);
        scrollView = mContentView.findViewById(R.id.scrollView);
    }

    private void initData() {
        getSlide();
        GetSlideEventsData();
        getRecommend();
//        getHomeHotData();
        getHomeHotData();
    }

    private void initListener() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh.isRefreshing();
                initData();
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecommend();
            }
        });
//查看更多
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看更多
                Intent intent = new Intent(mContext, LiveRecommendActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoadData() && isShowed()) {
            initData();
        }
    }

    /**
     * banner
     */
    private void setBanner() {
        if (slideBeanList == null || slideBeanList.isEmpty()) {
            return;
        }

        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                if (!Utils.isDestroy((Activity) mContext)) {
                    ImgLoader.display(mContext, ((BannerBean) path).getImageUrl(), imageView);
                }
            }
        });
        mBanner.setImages(slideBeanList);
        mBanner.start();
        mBanner.setOnBannerListener(p -> {
            if (slideBeanList != null) {
                if (p >= 0 && p < slideBeanList.size()) {
                    BannerBean bean = slideBeanList.get(p);
                    if (bean != null) {
                        String link = bean.getLink();
                        if (!TextUtils.isEmpty(link)) {
                            WebViewActivity.forward(mContext, link, false);
                            getSlide(bean.getId());
                        }
                    }
                }
            }
        });
        mBanner.setOnClickListener(v -> {
            if (slideBeanList != null && slideBeanList.size() == 1) {
                BannerBean bannerBean = slideBeanList.get(0);
                String link = bannerBean.getLink();
                if (!TextUtils.isEmpty(link)) {
                    WebViewActivity.forward(mContext, link, false);
                    getSlide(bannerBean.getId());
                }
            }
        });
    }

    /**
     * 获取幻灯片
     */
    public void getSlide() {
        MainHttpUtil.getSlide(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                skeletonBanner.hide();
                if (code == 0) {
                    List<SlideBannerBean> slideBannerBeans = JSON.parseArray(Arrays.toString(info), SlideBannerBean.class);
                    if (slideBannerBeans != null && !slideBannerBeans.isEmpty()) {
                        if (slideBeanList == null) {
                            slideBeanList = new ArrayList<>();
                        } else {
                            slideBeanList.clear();
                        }
                        for (int i = 0; i < slideBannerBeans.size(); i++) {
                            if (slideBannerBeans.get(i) != null
                                    && slideBannerBeans.get(i).getPosition() == 5
                                    && slideBannerBeans.get(i).getItems() != null
                                    && !slideBannerBeans.get(i).getItems().isEmpty()
                                    && null != slideBannerBeans.get(i).getItems().get(0)) {
                                for (int j = 0; j < slideBannerBeans.get(i).getItems().size(); j++) {
                                    SlideBannerBean.ItemsBean itemsBean = slideBannerBeans.get(i).getItems().get(j);
                                    BannerBean bannerBean = new BannerBean();
                                    bannerBean.setId(itemsBean.getId());
                                    bannerBean.setImageUrl(itemsBean.getImage());
                                    bannerBean.setLink(itemsBean.getUrl());
                                    slideBeanList.add(bannerBean);
                                }
                            }
                        }
                    }
                    if (slideBeanList != null && !slideBeanList.isEmpty()) {
                        setBanner();
                    }
                } else {
                    mRefresh.finishRefresh();
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                mRefresh.finishRefresh();
                if (TextUtils.isEmpty(msg)) {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 获取幻灯片
     */
    public void getSlide(String id) {
        MainHttpUtil.clickSlideItem(id, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                AppLog.e("code", "code:" + code);
            }
        });
    }

    /**
     * 推荐赛事
     */
    private void setGameView() {
        recyclerViewGame = mContentView.findViewById(R.id.home_game);
        recyclerViewGame.setNestedScrollingEnabled(false);
        recyclerViewGame.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewGame.setLayoutManager(layoutManager);
        mAdapterGame = new HomeGameAdapter(R.layout.item_home_game, listBeans);
        skeletonGames = Skeleton.bind(recyclerViewGame)
                .adapter(mAdapterGame)
                .load(R.layout.item_home_game_sketon)
                .shimmer(false)
                .frozen(false)
                .color(R.color.gray5)
                .show();
        mAdapterGame.setOnItemClickListener((adapter, view, position) -> {
            if (!ClickUtil.canClick()) return;
            if (listBeans.get(position).getLive_class_id() == 1) {
                Intent intent = new Intent(mContext, LiveSportsActivity.class);
                intent.putExtra("id", listBeans.get(position).getId());
                intent.putExtra("match_type", 2);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, LiveSportsActivity.class);
                intent.putExtra("id", listBeans.get(position).getId());
                intent.putExtra("match_type", 3);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 推荐赛事
     */
    private void GetSlideEventsData() {
        MainHttpUtil.getSlideEvents(new V2Callback<SlideEventsBean>() {

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                skeletonGames.hide();
                mRefresh.finishRefresh();
                if (TextUtils.isEmpty(msg)) {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onSuccess(int code, String msg, SlideEventsBean data) {
                mRefresh.finishRefresh();
                skeletonGames.hide();
                listBeans = data.getList();
                if (listBeans != null && !listBeans.isEmpty()) {
                    mAdapterGame.setNewData(listBeans);
                }
            }
        });
    }


    /**
     * 推荐直播
     */
    private void setRecommendView() {
        recyclerViewRecommend = mContentView.findViewById(R.id.home_recommend);
        recyclerViewRecommend.setNestedScrollingEnabled(false);
        recyclerViewRecommend.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerViewRecommend.setLayoutManager(gridLayoutManager);
//        mAdapterRecommend = new HomeRecommendAdapter(R.layout.item_home_recommend, new ArrayList<MainRecommendBean>());
        mAdapterRecommend = new HomeRecommendAdapter(R.layout.item_home_recommend, new ArrayList<DataNewX.DataDTO>());
        skeletonRecommend = Skeleton.bind(recyclerViewRecommend)
                .adapter(mAdapterRecommend)
                .load(R.layout.item_home_recommend_sketon)
                .shimmer(false)
                .count(6)
                .color(R.color.gray4)
                .show();

        mAdapterRecommend.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!ClickUtil.canClick()) return;
//                 MainRecommendBean mainRecommendBean = mAdapterRecommend.getData().get(position);
//                DataNewX.DataDTO mDataDTO = mAdapterRecommend.getData().get(position);
////                if (TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
//                Intent intent = new Intent(mContext, LiveGameActivity.class);
//                intent.putExtra(Constants.LIVE_RECOMMEND_DATA, mainRecommendBean);
//                mContext.startActivity(intent);
////                } else {
////                    watchLive(mainRecommendBean);
////                }


                DataNewX.DataDTO mDataDTO = mAdapterRecommend.getData().get(position);
                MainRecommendBean mainRecommendBean = new MainRecommendBean();

                mainRecommendBean.setAvatar(mDataDTO.getUser().getAvatar());
                mainRecommendBean.setAvatar_thumb(mDataDTO.getUser().getAvatarThumb());
                mainRecommendBean.setUser_nicename(mDataDTO.getUser().getUserNicename());
                //TODO 必传字段
                mainRecommendBean.setLiveclassid(mDataDTO.getLiveclassid());
                mainRecommendBean.setUid(mDataDTO.getUid());
                mainRecommendBean.setTitle(mDataDTO.getTitle());
                mainRecommendBean.setStream(mDataDTO.getStream());
                mainRecommendBean.setPull(mDataDTO.getPull());
                mainRecommendBean.setIsvideo(mDataDTO.getIsvideo());
                mainRecommendBean.setAnyway(mDataDTO.getAnyway());
                mainRecommendBean.setMatch_id(mDataDTO.getMatchId());
                mainRecommendBean.setIs_chat_off(Integer.parseInt(mDataDTO.getIsChatOff()));
                mainRecommendBean.setViewnum( mDataDTO.getUser().getViewnum());
                Intent intent = new Intent(mContext, LiveGameActivity.class);
                intent.putExtra(Constants.LIVE_RECOMMEND_DATA, mainRecommendBean);
                mContext.startActivity(intent);
            }
        });
    }

    //    /**
//     * 推荐直播
//     */
    private void getHomeHotData() {
        MainHttpUtil.getHomeHotNewX(1, 12, new HttpCallbackNewX() {

            @Override
            public void onSuccessALLData(String data) {
                mRefresh.finishRefresh();
                skeletonRecommend.hide();
                mAdapterRecommend.setEmptyView(R.layout.layout_empty_view, recyclerViewRecommend);
                Gson gson = new GsonBuilder().create();
                DataNewX mDataNewX = gson.fromJson(data, DataNewX.class);
                if (TextUtils.isEmpty(data)
                        || mDataNewX.getData() == null
                        || mDataNewX.getData().size() == 0) {
                    tvMore.setVisibility(View.GONE);
                    return;
                }
                mAdapterRecommend.setNewData(mDataNewX.getData());
            }


            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                mRefresh.finishRefresh();
                skeletonRecommend.hide();
                if (TextUtils.isEmpty(msg)) {
                    ToastUtil.show(msg);
                }
            }


            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("===", "===");
            }
        });
    }


    /**
     * 观看直播
     *
     * @param liveBean
     */
    public void watchLive(MainRecommendBean liveBean) {
        if (mCheckLivePresenter == null) {
            mCheckLivePresenter = new CheckLivePresenter(mContext);
        }
        mCheckLivePresenter.watchLive(liveBean);
    }

    /**
     * 推荐主播
     */
    private void setAnchorView() {
        mHomeAnchorRecyclerView = mContentView.findViewById(R.id.home_anchor);
        mllAnchor = mContentView.findViewById(R.id.ll_anchor);
        mHomeAnchorRecyclerView.setNestedScrollingEnabled(false);
        mHomeAnchorRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mHomeAnchorRecyclerView.setLayoutManager(layoutManager);
        mHomeAnchorAdapter = new HomeAnchorAdapter(R.layout.item_home_anchor, anchorBeanList);
        mHomeAnchorRecyclerView.setAdapter(mHomeAnchorAdapter);
        mHomeAnchorAdapter.setOnItemClickListener(new HomeAnchorAdapter.OnItemClickListener() {
            @Override
            public void subscribe(AnchorAnchorBean item, int position) {
                if (!ClickUtil.canClick()) return;
                if (!TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                    //已经登陆
                    recommendFollo(position, item);
                } else {
                    //未登陆，去注册
                    LoginActivity.forward();
                }
            }

            @Override
            public void anchor(AnchorAnchorBean item) {
                //主播
                clickAvatar(item.getId());
            }

        });
        skeletonAnchor = Skeleton.bind(mHomeAnchorRecyclerView)
                .adapter(mHomeAnchorAdapter)
                .load(R.layout.item_home_anchor_sketon)
                .shimmer(false)
                .count(8)
                .show();
    }

    /**
     * 推荐主播数据加载
     */
    private void getRecommend() {
        MainHttpUtil.GetRecommendAnchor(1, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, final String[] info) {
                mRefresh.finishRefresh();
                skeletonAnchor.hide();
                if (code == 0) {
                    anchorBeanList = JSON.parseArray(Arrays.toString(info), AnchorAnchorBean.class);
                    mHomeAnchorAdapter.setNewData(anchorBeanList);
                    if (anchorBeanList.isEmpty()) {
                        mllAnchor.setVisibility(View.GONE);
                    } else {
                        mllAnchor.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (TextUtils.isEmpty(msg)) {
                        ToastUtil.show(msg);
                    }
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                mRefresh.finishRefresh();
                skeletonAnchor.hide();
                if (TextUtils.isEmpty(msg)) {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    /**
     * 点击头像
     */
    public void clickAvatar(String id) {
        if (id != null && !id.isEmpty()) {
            RouteUtil.forwardUserHome(mContext, id);
        }
    }

    /**
     * 关注推荐主播
     */
    private void recommendFollo(final int position, final AnchorAnchorBean anchorAnchorBean) {
        //type  默认为1，1订阅主播 2取消订阅
        MainHttpUtil.SubscribeAnchor(anchorAnchorBean.getId(), anchorAnchorBean.getIsSubscribe() == 0 ? "1" : "2", new HttpCallbackObject() {
            @Override
            public void onSuccess(int code, String msg, Object info) {
                if (code == 0) {
                    ToastUtil.show(anchorAnchorBean.getIsSubscribe() == 0 ? "订阅成功" : "取消订阅成功");
                    anchorAnchorBean.setIsSubscribe(anchorAnchorBean.getIsSubscribe() == 0 ? 1 : 0);
                    mHomeAnchorAdapter.notifyItemChanged(position);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }
}
