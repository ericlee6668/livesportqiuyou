package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.bean.MatchListBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.V2Callback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.bean.BasketPushData;
import com.yunbao.live.bean.FootballLive;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MainGameBasketballAllListAdapter;
import com.yunbao.main.adapter.MainGameFootballAllListAdapter;
import com.yunbao.main.adapter.MainGameViewListAdapter;
import com.yunbao.main.http.MainHttpUtil;

import java.util.Arrays;
import java.util.List;

public class GameIngViewHolder extends AbsMainViewHolder {


    private SmartRefreshLayout mRefresh;
    private RecyclerView recyclerView;
    private LinearLayout llNoDate;
    private int currGameType;
    private MainGameViewListAdapter gameViewListAdapter;
    private MainGameFootballAllListAdapter gameFootBallListAdapter;
    private MainGameBasketballAllListAdapter gameBasketballAllListAdapter;
    private int page = 1;

    public GameIngViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_game_ing_list;
    }

    @Override
    public void init() {
        initView();
        initListener();
    }

    private void initView() {
        mRefresh = findViewById(R.id.mRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        llNoDate = findViewById(R.id.ll_no_date);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
    }

    @Override
    public void loadData(int type) {
        if (isFirstLoadData() || type != currGameType) {
            currGameType = type;
            page = 1;
            mRefresh.setNoMoreData(false);
            mRefresh.setEnableLoadMore(currGameType != 2);
            showNoData(true);
            initAdapter();
            getViewData();
        }
    }

    private void initListener() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mRefresh.setNoMoreData(false);
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

    private void initAdapter() {
        switch (currGameType) {
            case 0:
                //足球
                if (gameFootBallListAdapter == null) {
                    gameFootBallListAdapter = new MainGameFootballAllListAdapter(mContext, null);
                } else {
                    gameFootBallListAdapter.clear();
                }
                recyclerView.setAdapter(gameFootBallListAdapter);
                break;
            case 1:
                //篮球
                if (gameBasketballAllListAdapter == null) {
                    gameBasketballAllListAdapter = new MainGameBasketballAllListAdapter(mContext, null);
                } else {
                    gameBasketballAllListAdapter.clear();
                }
                recyclerView.setAdapter(gameBasketballAllListAdapter);
                break;
            case 2:
                //电竞
                if (gameViewListAdapter == null) {
                    gameViewListAdapter = new MainGameViewListAdapter(mContext);
                }
                recyclerView.setAdapter(gameViewListAdapter);
                break;
            case 3:
                //其他
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据
     */
    public void getViewData() {
        switch (currGameType) {
            case 0:
                //足球
                getFootballMatchListByStart();
                break;
            case 1:
                //篮球
                getBasketballMatchListByStart();
                break;
            case 2:
                //电竞
                getLolMatchListByStart();
                break;
            case 3:
                //其他
                showNoData(true);
                mRefresh.finishRefreshWithNoMoreData();
                break;
            default:
                break;
        }
    }

    /**
     * 足球赛事
     */
    private void getFootballMatchListByStart() {
        if (isFirstLoadData()) {
            mRefresh.autoRefresh();
        }
        MainHttpUtil.getFootballMatchPlayingList(page == 0 ? 1 : page, new V2Callback<List<MatchListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<MatchListBean> data) {
                finishRefresh();
                if (data != null && !data.isEmpty()) {
                    if (gameFootBallListAdapter != null) {
                        showNoData(false);
                        if (page == 1) {
                            gameFootBallListAdapter.setGroups(data);
                        } else {
                            gameFootBallListAdapter.addGroups(data);
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
     * 篮球赛事
     */
    private void getBasketballMatchListByStart() {
        showNoData(false);
        if (isFirstLoadData()) {
//            mRefresh.autoRefresh();
        }
        MainHttpUtil.getBasketballMatchPlayingList(page == 0 ? 1 : page, new V2Callback<List<MatchListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<MatchListBean> data) {
                finishRefresh();
                if (data != null && !data.isEmpty()) {
                    showNoData(false);
                    if (gameBasketballAllListAdapter == null) {
                        initAdapter();
                    }
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
                finishRefresh();
                ToastUtil.show(msg);
                if (page == 1) {
                    showNoData(true);
                }
            }
        });
    }

    /**
     * LOL赛事列表查询,状态1，进行中
     */
    private void getLolMatchListByStart() {
        if (isFirstLoadData()) {
            mRefresh.autoRefresh();
        }
        MainHttpUtil.getLOLMatchListByStat(1, "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mRefresh.finishRefresh();
                if (code == 0) {
                    final List<GameLolMatchBean> gameLolMatchBeans = JSON.parseArray(Arrays.toString(info), GameLolMatchBean.class);
                    if (gameLolMatchBeans != null && !gameLolMatchBeans.isEmpty()) {
                        showNoData(false);
                        if (gameViewListAdapter == null) {
                            initAdapter();
                        }
                        if (gameViewListAdapter != null) {
                            gameViewListAdapter.setListData(gameLolMatchBeans);
                        }
                    } else {
                        showNoData(true);
                    }
                } else {
                    showNoData(true);
                }
            }

            @Override
            public void onError(int ret, String msg) {
                finishRefresh();
                showNoData(true);
            }
        });
    }

    /**
     * 是否有数据
     */
    private void showNoData(boolean noData) {
        recyclerView.setVisibility(noData ? View.GONE : View.VISIBLE);
        llNoDate.setVisibility(noData ? View.VISIBLE : View.GONE);
    }

    public void refreshFootballData(List<FootballLive> list) {
        if (gameFootBallListAdapter != null) {
            gameFootBallListAdapter.onFootballListDataRefresh(list);
        }
    }

    public void refreshBasketballData(List<BasketPushData> changeList) {
        if (gameBasketballAllListAdapter != null) {
            gameBasketballAllListAdapter.setUpdateData(changeList);
        }
    }

    private void finishRefresh() {
        if (mRefresh.isRefreshing()) {
            mRefresh.finishRefresh();
        }
        if (mRefresh.isLoading()) {
            mRefresh.finishLoadMore();
        }
    }
}
