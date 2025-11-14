package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.donkingliang.groupedadapter.widget.StickyHeaderLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.GameLOLMatchListBean;
import com.yunbao.common.bean.MatchListBean;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.http.V2Callback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.bean.BasketPushData;
import com.yunbao.live.bean.FootballLive;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MainGameBasketballAllListAdapter;
import com.yunbao.main.adapter.MainGameESportsAllListAdapter;
import com.yunbao.main.adapter.MainGameFootballAllListAdapter;
import com.yunbao.main.http.MainHttpUtil;

import java.util.List;

public class AllGameViewHolder extends AbsMainViewHolder {

    private SmartRefreshLayout mRefresh;
    private RecyclerView recyclerView;
    private LinearLayout llNoDate;
    private StickyHeaderLayout stickyLayout;
    private int currGameType;
    private MainGameFootballAllListAdapter gameFootBallAllListAdapter;
    private MainGameBasketballAllListAdapter gameBasketballAllListAdapter;
    private MainGameESportsAllListAdapter gameESportsAllListAdapter;
    private int page = 1;


    public AllGameViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_all_game_list;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();
    }

    @Override
    public void loadData(int type) {
        if (type != currGameType) {
            currGameType = type;
            page = 1;
            showNoData(true);
            initRecyclerView();
            mRefresh.setNoMoreData(false);
            mRefresh.setEnableLoadMore(currGameType != 2);
            getViewData();
        }
    }

    private void initListener() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh.setNoMoreData(false);
                page = 1;
                getViewData();
            }
        });
        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getViewData();
            }
        });
    }

    private void initView() {
        mRefresh = findViewById(R.id.mRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        stickyLayout = findViewById(R.id.sticky_layout);
        llNoDate = findViewById(R.id.ll_no_date);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initData() {
        initRecyclerView();
        getViewData();
    }

    /**
     * 获取数据
     */
    public void getViewData() {
        switch (currGameType) {
            //足球
            case 0:
                getFootballMatchListData();
                break;
            case 1:
                //篮球
                getBasketballMatchListData();
                break;
            case 2:
                //电竞
                getLolMatchListData();
                break;
            case 3:
                //其他
                showNoData(true);
                finishRefresh();
                break;
            default:
                break;
        }
    }

    /**
     * 足球全部赛事
     */
    private void getFootballMatchListData() {
        if (isFirstLoadData() || !mRefresh.isRefreshing()) {
//            mRefresh.autoRefresh();
        }
        MainHttpUtil.getFootballMatchAllList(page == 0 ? 1 : page, new V2Callback<List<MatchListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<MatchListBean> data) {
                finishRefresh();
                if (data != null && !data.isEmpty()) {
                    if (gameFootBallAllListAdapter != null) {
                        showNoData(false);
                        if (page == 1) {
                            gameFootBallAllListAdapter.setGroups(data);
                        } else {
                            gameFootBallAllListAdapter.addGroups(data);
                        }
                    }
                } else {
                    if (page == 1) {
                        showNoData(true);
                    }
                    mRefresh.setNoMoreData(true);
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                if (page == 1) {
                    showNoData(true);
                }
                finishRefresh();
                ToastUtil.show(msg);
            }
        });
    }

    /**
     * 篮球全部赛事
     */
    private void getBasketballMatchListData() {
        if (isFirstLoadData() || !mRefresh.isRefreshing()) {
            mRefresh.autoRefresh();
        }
        MainHttpUtil.getBasketballMatchAllList(page == 0 ? 1 : page, new V2Callback<List<MatchListBean>>() {

            @Override
            public void onSuccess(int code, String msg, List<MatchListBean> data) {
                finishRefresh();
                if (data != null && !data.isEmpty()) {
                    showNoData(false);
                    if (gameBasketballAllListAdapter != null) {
                        if (page == 1) {
                            gameBasketballAllListAdapter.setGroups(data);
                        } else {
                            gameBasketballAllListAdapter.addGroups(data);
                        }
                    }
                } else {
                    if (page == 1) {
                        showNoData(true);
                    }
                    mRefresh.setNoMoreData(true);
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                if (page == 1) {
                    showNoData(true);
                }
                finishRefresh();
                ToastUtil.show(msg);
            }
        });
    }


    /**
     * lol全部赛事
     */
    private void getLolMatchListData() {
        if (isFirstLoadData() || !mRefresh.isRefreshing()) {
            mRefresh.autoRefresh();
        }
        MainHttpUtil.getLOLMatchList(new HttpCallbackObject() {
            @Override
            public void onSuccess(int code, String msg, Object info) {
                finishRefresh();
                if (code == 0) {
                    GameLOLMatchListBean gameLOLMatchListBean = JSON.parseObject(info.toString(), GameLOLMatchListBean.class);
                    if (gameLOLMatchListBean != null) {
                        showNoData(false);
                        if (gameESportsAllListAdapter != null) {
                            gameESportsAllListAdapter.setGroups(gameLOLMatchListBean);
                        }
                    } else {
                        showNoData(true);
                    }
                } else {
                    showNoData(true);
                }
            }

            @Override
            public void onError() {
                showNoData(true);
                finishRefresh();
            }
        });
    }

    private void finishRefresh() {
        if (mRefresh.isRefreshing()) {
            mRefresh.finishRefresh();
        }
        if (mRefresh.isLoading()) {
            mRefresh.finishLoadMore();
        }
    }

    /**
     * 是否有数据
     */
    private void showNoData(boolean noData) {
        recyclerView.setVisibility(noData ? View.GONE : View.VISIBLE);
        llNoDate.setVisibility(noData ? View.VISIBLE : View.GONE);
    }

    private void initRecyclerView() {
        switch (currGameType) {
            case 0:
                //足球
                stickyLayout.setSticky(true);
                if (gameFootBallAllListAdapter == null) {
                    gameFootBallAllListAdapter = new MainGameFootballAllListAdapter(mContext, null);
                } else {
                    gameFootBallAllListAdapter.clear();
                }
                recyclerView.setAdapter(gameFootBallAllListAdapter);
                break;
            case 1:
                //篮球
                stickyLayout.setSticky(true);
                if (gameBasketballAllListAdapter == null) {
                    gameBasketballAllListAdapter = new MainGameBasketballAllListAdapter(mContext, null);
                } else {
                    gameBasketballAllListAdapter.clear();
                }
                recyclerView.setAdapter(gameBasketballAllListAdapter);
                break;
            case 2:
                //电竞
                stickyLayout.setSticky(true);
                if (gameESportsAllListAdapter == null) {
                    gameESportsAllListAdapter = new MainGameESportsAllListAdapter(mContext, null);
                } else {
                    gameESportsAllListAdapter.clear();
                }
                recyclerView.setAdapter(gameESportsAllListAdapter);
                break;
            case 3:
                //其他
                stickyLayout.setSticky(false);
                break;
            default:
                break;
        }
        stickyLayout.updateStickyView();
    }

    public void refreshFootballData(List<FootballLive> list) {
        if (gameFootBallAllListAdapter != null) {
            gameFootBallAllListAdapter.onFootballListDataRefresh(list);
        }
    }

    public void refreshBasketBallData(List<BasketPushData> list) {
        if (gameBasketballAllListAdapter != null)
            gameBasketballAllListAdapter.setUpdateData(list);
    }
}
