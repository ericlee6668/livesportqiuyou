package com.yunbao.main.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.bean.MatchBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.V2Callback;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.DateFormatUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MainGameBasketballListAdapter;
import com.yunbao.main.adapter.MainGameFootballListAdapter;
import com.yunbao.main.adapter.MainGameViewListAdapter;
import com.yunbao.main.bean.DateBean;
import com.yunbao.main.http.MainHttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GameTimeViewHolder extends AbsMainViewHolder {

    private TabLayout tabLayout;
    private ImageView iv_calendar;
    private SmartRefreshLayout mRefresh;
    private RecyclerView recyclerView;
    private LinearLayout llNoDate;
    private List<DateBean> dataBeans;
    private static final int PAGE_COUNT = 15;
    private String mDate;
    private int currGameType;
    private Dialog mDialog;
    private int page = 1;
    private MainGameViewListAdapter gameViewListAdapter;
    private MainGameFootballListAdapter gameFootBallListAdapter;
    private MainGameBasketballListAdapter gameBasketballListAdapter;


    public GameTimeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_game_time_list;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();
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

    private void initView() {
        tabLayout = findViewById(R.id.tab_layout);
        iv_calendar = findViewById(R.id.iv_calendar);
        mRefresh = findViewById(R.id.mRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        llNoDate = findViewById(R.id.ll_no_date);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
    }

    private void initData() {
        mDate = getFutureDate(Calendar.getInstance(), 0);
        dataBeans = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++) {
            dataBeans.add(new DateBean(getFutureDate(Calendar.getInstance(), i), getFutureWeek(Calendar.getInstance(), i)));
//            dataBeans.add(new DateBean(i == 0 ? mContext.getString(R.string.today) : getFutureDate(Calendar.getInstance(), i), getFutureWeek(Calendar.getInstance(), i)));
        }
        setTabView(dataBeans);
    }

    /**
     * 获取数据
     */
    public void getViewData() {
        switch (currGameType) {
            case 0:
                //足球
                getFootballMatchListByDate(mDate);
                break;
            case 1:
                //篮球
                getBasketballMatchListByDate(mDate);
                break;
            case 2:
                //电竞
                getLolMatchListByStart(mDate);
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

    private void initAdapter() {
        switch (currGameType) {
            case 0:
                //足球
                if (gameFootBallListAdapter == null) {
                    gameFootBallListAdapter = new MainGameFootballListAdapter(mContext);
                }
                recyclerView.setAdapter(gameFootBallListAdapter);
                break;
            case 1:
                //篮球
                if (gameBasketballListAdapter == null) {
                    gameBasketballListAdapter = new MainGameBasketballListAdapter(mContext);
                }
                recyclerView.setAdapter(gameBasketballListAdapter);
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
     * 设置tab数据
     */
    private void setTabView(List<DateBean> dataBeans) {
        for (int i = 0; i < dataBeans.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View inflate = View.inflate(mContext, R.layout.view_game_date_choose, null);
            TextView tv_date = inflate.findViewById(R.id.tv_date);
            TextView tv_week = inflate.findViewById(R.id.tv_week);
            if (i == 0) {
                tv_date.setText(R.string.today);
            } else {
                tv_date.setText(DateFormatUtil.formatTargetDate(dataBeans.get(i).getDate(), "yyyy-MM-dd", "MM-dd"));
            }
            tv_week.setText(dataBeans.get(i).getWeek());
            tab.setCustomView(inflate);
            tabLayout.addTab(tab);
        }
    }

    private void initListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(tab.getPosition())).getCustomView()).setSelected(true);
                    mDate = dataBeans.get(tab.getPosition()).getDate();
//                    mRefresh.autoRefresh();
                    if (mDialog == null) {
                        mDialog = DialogUitl.loadingDialog(mContext, true);
                    }
                    mDialog.show();
                    page = 1;
                    mRefresh.setNoMoreData(false);
                    getViewData();
                } catch (Exception e) {
                    AppLog.e("TAG", "run: " + e.toString());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(tab.getPosition())).getCustomView()).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

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

    /**
     * 日历
     */
    private void showDate() {
        final Calendar calendarChoose = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DateTimePicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarChoose.set(Calendar.YEAR, year);
                calendarChoose.set(Calendar.MONTH, monthOfYear);
                calendarChoose.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarChoose.set(Calendar.HOUR_OF_DAY, calendarChoose.get(Calendar.HOUR_OF_DAY));
                calendarChoose.set(Calendar.MINUTE, calendarChoose.get(Calendar.MINUTE));
                calendarChoose.set(Calendar.SECOND, 0);
                String date = getFutureDate(calendarChoose, 0);
                if (dataBeans != null) {
                    for (int i = 0; i < dataBeans.size(); i++) {
                        if (dataBeans.get(i).getDate().equals(date)) {
                            changeTab(i, dataBeans.get(i).getDate());
                            return;
                        }
                    }
                }
            }
        }, calendarChoose.get(Calendar.YEAR), calendarChoose.get(Calendar.MONTH), calendarChoose.get(Calendar.DAY_OF_MONTH));
        //设置起始日期和结束日期
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() + (PAGE_COUNT - 1) * 24 * 60 * 60 * 1000);
        datePickerDialog.show();
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                DatePicker dp = findDatePicker((ViewGroup) datePickerDialog.getWindow().getDecorView());
                if (dp != null) {
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    /**
     * 切换tab
     *
     * @param i
     * @param date
     */
    private void changeTab(int i, String date) {
        try {
            mDate = date;
            tabLayout.getTabAt(i).select();
            tabLayout.getTabAt(i).getCustomView().callOnClick();
            tabLayout.getTabAt(i).getCustomView().setSelected(true);
            getViewData();
        } catch (Exception e) {
            AppLog.e("TAG", "run: " + e.toString());
        }
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public String getFutureDate(Calendar calendar, int past) {
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取星期几
     *
     * @return
     */
    public String getFutureWeek(Calendar calendar, int past) {
        String week = "";
        calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK) + past);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekDay) {
            case 0:
                week = mContext.getString(R.string.sunday);
                break;
            case 1:
                week = mContext.getString(R.string.monday);
                break;
            case 2:
                week = mContext.getString(R.string.tuesday);
                break;
            case 3:
                week = mContext.getString(R.string.wednesday);
                break;
            case 4:
                week = mContext.getString(R.string.thursday);
                break;
            case 5:
                week = mContext.getString(R.string.friday);
                break;
            case 6:
                week = mContext.getString(R.string.saturday);
                break;
        }
        return week;
    }

    /**
     * 足球
     *
     * @param date
     */
    private void getFootballMatchListByDate(String date) {
        MainHttpUtil.getFootballMatchListByDate(date, 1, page == 0 ? 1 : page, new V2Callback<List<MatchBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<MatchBean> data) {
                finishRefresh();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (data != null && !data.isEmpty()) {
                    showNoData(false);
                    if (gameFootBallListAdapter == null) {
                        initAdapter();
                    }
                    if (page == 1) {
                        recyclerView.setAdapter(gameFootBallListAdapter);
                        gameFootBallListAdapter.setListData(data);
                    } else {
                        gameFootBallListAdapter.addListData(data);
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
                finishRefresh();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (page == 1) {
                    showNoData(true);
                }
            }
        });
    }

    /**
     * 篮球
     *
     * @param date
     */
    private void getBasketballMatchListByDate(String date) {
        MainHttpUtil.getBasketballMatchListByDate(date, 1, page == 0 ? 1 : page, new V2Callback<List<MatchBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<MatchBean> data) {
                finishRefresh();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (data != null && !data.isEmpty()) {
                    showNoData(false);
                    if (gameBasketballListAdapter == null) {
                        initAdapter();
                    }
                    if (page == 1) {
                        recyclerView.setAdapter(gameBasketballListAdapter);
                        gameBasketballListAdapter.setListData(data);
                    } else {
                        gameBasketballListAdapter.addListData(data);
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
                if (page == 1) {
                    showNoData(true);
                }
                finishRefresh();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
    }


    /**
     * LOL赛事列表查询,状态0，未开始
     */
    private void getLolMatchListByStart(String date) {
        MainHttpUtil.getLOLMatchListByStat(0, date, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                finishRefresh();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if (code == 0) {
                    final List<GameLolMatchBean> gameLolMatchBeans = JSON.parseArray(Arrays.toString(info), GameLolMatchBean.class);
                    if (gameLolMatchBeans != null && !gameLolMatchBeans.isEmpty()) {
                        if (gameViewListAdapter == null) {
                            initAdapter();
                        }
                        showNoData(false);
                        recyclerView.setAdapter(gameViewListAdapter);
                        gameViewListAdapter.setListData(gameLolMatchBeans);
                    } else {
                        showNoData(true);
                    }
                } else {
                    showNoData(true);
                }
            }

            @Override
            public void onError(int ret, String msg) {
                mRefresh.finishRefresh();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                showNoData(true);
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
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
