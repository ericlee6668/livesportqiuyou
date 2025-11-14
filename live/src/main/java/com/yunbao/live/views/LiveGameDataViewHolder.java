package com.yunbao.live.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.BitmapUtil;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.adapter.LiveGameDataAdapter;
import com.yunbao.live.bean.LineChartDataBean;
import com.yunbao.live.bean.LiveGameDataBean;
import com.yunbao.live.custom.LiveGameDataBarView;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.utils.mpchart.LineChartHelper;
import com.yunbao.live.utils.mpchart.LineChartInViewPager;

import java.util.ArrayList;
import java.util.List;


public class LiveGameDataViewHolder extends AbsViewHolder implements View.OnClickListener {
    private SmartRefreshLayout mRefresh;
    private LiveGameDataBarView mLiveGameDataBarView;
    private LineChartInViewPager lineChart;
    private LinearLayout ll_team_a_view;
    private LinearLayout ll_team_b_view;
    private LineChartHelper lineChartHelper;
    private ImageView iv_team_logo1;
    private TextView tv_team_name1;
    private RecyclerView recyclerView1;
    private ImageView iv_team_logo2;
    private TextView tv_team_name2;
    private RecyclerView recyclerView2;
    private LiveGameDataAdapter liveGameDataAdapter1;
    private LiveGameDataAdapter liveGameDataAdapter2;
    private TextView tv_round1;
    private TextView tv_round2;
    private TextView tv_round3;
    private TextView tv_round4;
    private TextView tv_round5;
    private ImageView iv_team_a;
    private TextView tv_team_a_name1;
    private ImageView iv_team_a_cup;
    private TextView tv_team_a_score;
    private TextView tv_game_odds;
    private TextView tv_team_b_score;
    private ImageView iv_team_b_cup;
    private TextView tv_team_b_name1;
    private ImageView iv_team_b;
    private TextView tv_team_a_name2;
    private TextView tv_team_b_name2;
    private LinearLayout ll_content;
    private LinearLayout view_live_no_data;
    private GameLolMatchBean gameLolMatchBean;
    private int index = 1;
    private View ll_data;
    private LinearLayout ll_ban;
    private LinearLayout ll_ban2;
    private LinearLayout ll_pick;
    private LinearLayout ll_pick2;
    private List<LineChartDataBean> lineChartDataBeanList;

    public LiveGameDataViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_game_data;
    }

    @Override
    public void init() {
        initView();
//        showContent(false);
        initData();
        initListener();
    }

    private void initView() {
        mRefresh = mContentView.findViewById(R.id.mRefresh);
        tv_round1 = mContentView.findViewById(R.id.tv_round1);
        tv_round2 = mContentView.findViewById(R.id.tv_round2);
        tv_round3 = mContentView.findViewById(R.id.tv_round3);
        tv_round4 = mContentView.findViewById(R.id.tv_round4);
        tv_round5 = mContentView.findViewById(R.id.tv_round5);
        iv_team_a = mContentView.findViewById(R.id.iv_team_a);
        tv_team_a_name1 = mContentView.findViewById(R.id.tv_team_a_name1);
        iv_team_a_cup = mContentView.findViewById(R.id.iv_team_a_cup);
        tv_team_a_score = mContentView.findViewById(R.id.tv_team_a_score);
        tv_game_odds = mContentView.findViewById(R.id.tv_game_odds);
        tv_team_b_score = mContentView.findViewById(R.id.tv_team_b_score);
        iv_team_b_cup = mContentView.findViewById(R.id.iv_team_b_cup);
        tv_team_b_name1 = mContentView.findViewById(R.id.tv_team_b_name1);
        iv_team_b = mContentView.findViewById(R.id.iv_team_b);
        tv_team_a_name2 = mContentView.findViewById(R.id.tv_team_a_name2);
        tv_team_b_name2 = mContentView.findViewById(R.id.tv_team_b_name2);
        mLiveGameDataBarView = mContentView.findViewById(R.id.live_game_data_bar_view);
        lineChart = mContentView.findViewById(R.id.line_chart);
        ll_team_a_view = mContentView.findViewById(R.id.ll_team_a_view);
        ll_team_b_view = mContentView.findViewById(R.id.ll_team_b_view);
        iv_team_logo1 = ll_team_a_view.findViewById(R.id.iv_team_logo);
        tv_team_name1 = ll_team_a_view.findViewById(R.id.tv_team_name);
        recyclerView1 = ll_team_a_view.findViewById(R.id.recyclerView);
        iv_team_logo2 = ll_team_b_view.findViewById(R.id.iv_team_logo);
        tv_team_name2 = ll_team_b_view.findViewById(R.id.tv_team_name);
        recyclerView2 = ll_team_b_view.findViewById(R.id.recyclerView);
        ll_content = mContentView.findViewById(R.id.ll_content);
        ll_data = mContentView.findViewById(R.id.ll_data);
        view_live_no_data = mContentView.findViewById(R.id.view_live_no_data);
        ll_ban = mContentView.findViewById(R.id.ll_ban);
        ll_ban2 = mContentView.findViewById(R.id.ll_ban2);
        ll_pick = mContentView.findViewById(R.id.ll_pick);
        ll_pick2 = mContentView.findViewById(R.id.ll_pick2);

    }

    private void initData() {
        lineChartHelper = LineChartHelper.init(mContext, lineChart);
        initChartData(null);
        initTeamData();
    }

    private void initListener() {
        tv_round1.setOnClickListener(this);
        tv_round2.setOnClickListener(this);
        tv_round3.setOnClickListener(this);
        tv_round4.setOnClickListener(this);
        tv_round5.setOnClickListener(this);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh.isRefreshing();
                loadData(gameLolMatchBean, index);
            }
        });
    }

    /**
     * 团队数据
     */
    private void initTeamData() {
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        liveGameDataAdapter1 = new LiveGameDataAdapter(mContext);
        recyclerView1.setAdapter(liveGameDataAdapter1);

        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        liveGameDataAdapter2 = new LiveGameDataAdapter(mContext);
        recyclerView2.setAdapter(liveGameDataAdapter2);

    }

    /**
     * 图表数据
     *
     * @param dataBean
     */

    private void initChartData(LiveGameDataBean dataBean) {
        try {
            if (lineChartDataBeanList == null) {
                lineChartDataBeanList = new ArrayList<>();
            } else {
                lineChartDataBeanList.clear();
            }
            if (dataBean != null) {
                List<LiveGameDataBean.DataBean.EconomicDiffBean> economic_diff = dataBean.getData().getEconomic_diff();
                for (int i = 0; i < economic_diff.size(); i++) {
                    lineChartDataBeanList.add(new LineChartDataBean(economic_diff.get(i).getTime(), economic_diff.get(i).getDiff()));
                }
                lineChartHelper.showLineChart(lineChartDataBeanList, dataBean.getTeam_a_name(), mContext.getResources().getColor(R.color.live_FAAD14));
                @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.fade_chart_line1);
                lineChartHelper.setChartFillDrawable(0, drawable1);
                lineChartHelper.setMarkerView(dataBean);
            } else {
                lineChartDataBeanList.add(new LineChartDataBean("00:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("05:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("10:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("15:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("20:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("25:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("30:00", 0));
                lineChartDataBeanList.add(new LineChartDataBean("35:00", 0));
                lineChartHelper.showLineChart(lineChartDataBeanList, "", mContext.getResources().getColor(R.color.white));
                @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.fade_chart_line2);
                lineChartHelper.setChartFillDrawable(0, drawable1);
//                lineChartNoData();
            }
        } catch (Exception e) {
            lineChartNoData();
        }

    }

    //曲线图无数据
    private void lineChartNoData() {
        lineChart.setNoDataText(mContext.getString(R.string.no_data));
        lineChart.setNoDataTextColor(mContext.getResources().getColor(R.color.black2));
        lineChart.invalidate();
    }

    public void loadData(GameLolMatchBean gameLolMatchBean, int index) {
        if (gameLolMatchBean != null) {
            this.gameLolMatchBean = gameLolMatchBean;
            mRefresh.isRefreshing();
            LiveHttpUtil.getMatchData(gameLolMatchBean.getId(), index, new HttpCallbackObject() {
                @Override
                public void onSuccess(int code, String msg, Object info) {
                    mRefresh.finishRefresh();
                    if (code == 0) {
                        showContent(true);
                        LiveGameDataBean liveGameDataBean = JSON.parseObject(info.toString(), LiveGameDataBean.class);
                        refreshData(liveGameDataBean);

                    } else {
                        showContent(false);
                    }
                }
            });
        }
    }


    private void showContent(boolean show) {
        ll_content.setVisibility(show ? View.VISIBLE : View.GONE);
        view_live_no_data.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);

    /**
     * 刷新数据
     *
     * @param dataBean
     */
    private void refreshData(final LiveGameDataBean dataBean) {
        try {
            if (dataBean != null) {
                int battleCount = dataBean.getBattle_current_index();
                setRoundCount(battleCount);
                setImage(iv_team_a, dataBean.getTeam_a_logo());
                setImage(iv_team_b, dataBean.getTeam_b_logo());
                tv_team_a_name1.setText(dataBean.getTeam_a_name());
                tv_team_b_name1.setText(dataBean.getTeam_b_name());
                tv_team_a_name2.setText(dataBean.getTeam_a_name());
                tv_team_b_name2.setText(dataBean.getTeam_b_name());
                tv_team_name1.setText(dataBean.getTeam_a_name());
                tv_team_name2.setText(dataBean.getTeam_b_name());
                setImage(iv_team_logo1, dataBean.getTeam_a_logo());
                setImage(iv_team_logo2, dataBean.getTeam_b_logo());
                if (dataBean.getData() != null) {
                    tv_game_odds.setText(dataBean.getData().getDuration() == null ? "" : dataBean.getData().getDuration());
                    String team_a_id = dataBean.getData().getTeam_stats().get(0).getTeam_id();
                    if (dataBean.getTeam_a_id().equals(team_a_id)) {
                        tv_team_a_score.setText(dataBean.getData().getTeam_stats().get(0).getKill_count());
                        tv_team_b_score.setText(dataBean.getData().getTeam_stats().get(1).getKill_count());
                        iv_team_a_cup.setVisibility(dataBean.getData().getTeam_stats().get(0).isIs_win() ? View.VISIBLE : View.GONE);
                        iv_team_b_cup.setVisibility(dataBean.getData().getTeam_stats().get(1).isIs_win() ? View.VISIBLE : View.GONE);
                    } else {
                        tv_team_a_score.setText(dataBean.getData().getTeam_stats().get(1).getKill_count());
                        tv_team_b_score.setText(dataBean.getData().getTeam_stats().get(0).getKill_count());
                        iv_team_a_cup.setVisibility(dataBean.getData().getTeam_stats().get(1).isIs_win() ? View.VISIBLE : View.GONE);
                        iv_team_b_cup.setVisibility(dataBean.getData().getTeam_stats().get(0).isIs_win() ? View.VISIBLE : View.GONE);
                    }

                    List<LiveGameDataBean.DataBean.TeamStatsBean.BanListBean> banList = dataBean.getData().getTeam_stats().get(0).getBan_list();
                    List<LiveGameDataBean.DataBean.TeamStatsBean.PickListBean> pickList = dataBean.getData().getTeam_stats().get(0).getPick_list();

                    List<LiveGameDataBean.DataBean.TeamStatsBean.BanListBean> banList2 = dataBean.getData().getTeam_stats().get(1).getBan_list();
                    List<LiveGameDataBean.DataBean.TeamStatsBean.PickListBean> pickList2 = dataBean.getData().getTeam_stats().get(1).getPick_list();
                    ll_ban.removeAllViews();
                    ll_ban2.removeAllViews();
                    ll_pick.removeAllViews();
                    ll_pick2.removeAllViews();
                    for (LiveGameDataBean.DataBean.TeamStatsBean.BanListBean banListBean : banList) {
                        ImageView iv = new ImageView(mContext);
                        ll_ban.addView(iv);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                        params.width = DpUtil.dp2px(26);
                        params.height = DpUtil.dp2px(26);
                        params.leftMargin = DpUtil.dp2px(3);
                        iv.setLayoutParams(params);
                        BitmapUtil.getInstance().setColorDrawable(iv,true);
                        ImgLoader.display(mContext, banListBean.getAvatar(), iv);
                    }
                    for (LiveGameDataBean.DataBean.TeamStatsBean.PickListBean pickListBean : pickList) {
                        ImageView iv = new ImageView(mContext);
                        ll_pick.addView(iv);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                        params.width = DpUtil.dp2px(26);
                        params.height = DpUtil.dp2px(26);
                        params.leftMargin = DpUtil.dp2px(3);
                        iv.setLayoutParams(params);
                        ImgLoader.display(mContext, pickListBean.getAvatar(), iv);
                    }
                    for (LiveGameDataBean.DataBean.TeamStatsBean.BanListBean banListBean : banList2) {
                        ImageView iv = new ImageView(mContext);
                        ll_ban2.addView(iv);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                        params.width = DpUtil.dp2px(26);
                        params.height = DpUtil.dp2px(26);
                        params.leftMargin = DpUtil.dp2px(3);
                        iv.setLayoutParams(params);
                        BitmapUtil.getInstance().setColorDrawable(iv,true);
                        ImgLoader.display(mContext, banListBean.getAvatar(), iv);
                    }
                    for (LiveGameDataBean.DataBean.TeamStatsBean.PickListBean pickListBean : pickList2) {
                        ImageView iv = new ImageView(mContext);
                        ll_pick2.addView(iv);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                        params.width = DpUtil.dp2px(26);
                        params.height = DpUtil.dp2px(26);
                        params.leftMargin = DpUtil.dp2px(3);
                        iv.setLayoutParams(params);
                        ImgLoader.display(mContext, pickListBean.getAvatar(), iv);
                    }
                    mLiveGameDataBarView.reFreshMultiGroupHistogramView(dataBean);

                    initChartData(dataBean);
                    liveGameDataAdapter1.setListData(dataBean.getTeam_a_id(), dataBean.getData().getPlayer_stats());
                    liveGameDataAdapter2.setListData(dataBean.getTeam_b_id(), dataBean.getData().getPlayer_stats());
                } else {
                    ll_data.setVisibility(View.GONE);
                    ll_team_a_view.setVisibility(View.GONE);
                    ll_team_b_view.setVisibility(View.GONE);
                }

            } else {
                setImage(iv_team_a, "");
                setImage(iv_team_b, "");
                tv_team_a_name1.setText("");
                tv_team_b_name1.setText("");
                tv_team_a_score.setText("");
                tv_team_b_score.setText("");
                tv_team_a_name2.setText("");
                tv_team_b_name2.setText("");
                tv_team_name1.setText("");
                tv_team_name2.setText("");
                setImage(iv_team_logo1, "");
                setImage(iv_team_logo2, "");
                tv_game_odds.setText("");
                iv_team_a_cup.setVisibility(View.GONE);
                iv_team_b_cup.setVisibility(View.GONE);

                mLiveGameDataBarView.reFreshMultiGroupHistogramView(null);

                initChartData(null);

                liveGameDataAdapter1.setListData("", null);
                liveGameDataAdapter2.setListData("", null);

            }

        } catch (Exception e) {
            AppLog.e("赛事数据：", "" + e.toString());
        }
    }

    private void setRoundCount(int battleCount) {
        switch (battleCount) {
            case 2:
                tv_round1.setVisibility(View.VISIBLE);
                tv_round2.setVisibility(View.VISIBLE);
                tv_round3.setVisibility(View.GONE);
                tv_round4.setVisibility(View.GONE);
                tv_round5.setVisibility(View.GONE);
                break;
            case 3:
                tv_round1.setVisibility(View.VISIBLE);
                tv_round2.setVisibility(View.VISIBLE);
                tv_round3.setVisibility(View.VISIBLE);
                tv_round4.setVisibility(View.GONE);
                tv_round5.setVisibility(View.GONE);
                break;
            case 4:
                tv_round1.setVisibility(View.VISIBLE);
                tv_round2.setVisibility(View.VISIBLE);
                tv_round3.setVisibility(View.VISIBLE);
                tv_round4.setVisibility(View.VISIBLE);
                tv_round5.setVisibility(View.GONE);
                break;
            case 5:
                tv_round1.setVisibility(View.VISIBLE);
                tv_round2.setVisibility(View.VISIBLE);
                tv_round3.setVisibility(View.VISIBLE);
                tv_round4.setVisibility(View.VISIBLE);
                tv_round5.setVisibility(View.VISIBLE);
                break;
            default:
                tv_round1.setVisibility(View.VISIBLE);
                tv_round2.setVisibility(View.GONE);
                tv_round3.setVisibility(View.GONE);
                tv_round4.setVisibility(View.GONE);
                tv_round5.setVisibility(View.GONE);
                break;

        }
    }

    private void setImage(ImageView iv, String ivUrl) {
        Glide.with(mContext)
                .load(ivUrl)
                .apply(options)
                .into(iv);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_round1) {
            showRundBnt(1);
        } else if (id == R.id.tv_round2) {
            showRundBnt(2);
        } else if (id == R.id.tv_round3) {
            showRundBnt(3);
        } else if (id == R.id.tv_round4) {
            showRundBnt(4);
        } else if (id == R.id.tv_round5) {
            showRundBnt(5);
        }
    }

    private void showRundBnt(int i) {
        index = i;
        tv_round1.setTextColor(i == 1 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tv_round2.setTextColor(i == 2 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tv_round3.setTextColor(i == 3 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tv_round4.setTextColor(i == 4 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tv_round5.setTextColor(i == 5 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.black2));
        tv_round1.setBackgroundResource(i == 1 ? R.drawable.bg_btn_common_02 : R.color.white);
        tv_round2.setBackgroundResource(i == 2 ? R.drawable.bg_btn_common_02 : R.color.white);
        tv_round3.setBackgroundResource(i == 3 ? R.drawable.bg_btn_common_02 : R.color.white);
        tv_round4.setBackgroundResource(i == 4 ? R.drawable.bg_btn_common_02 : R.color.white);
        tv_round5.setBackgroundResource(i == 5 ? R.drawable.bg_btn_common_02 : R.color.white);
        refreshData(null);
        loadData(gameLolMatchBean, i);
    }
}
