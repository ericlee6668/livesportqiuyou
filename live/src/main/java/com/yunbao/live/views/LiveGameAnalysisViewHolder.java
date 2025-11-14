package com.yunbao.live.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.bean.match.MatchLolAnalysisBean;
import com.yunbao.live.bean.match.MatchTeamBattleRecordBean;
import com.yunbao.live.bean.match.MatchTeamBattleStatsBean;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.List;

/**
 * Created by Juwan on 2018/10/9.
 * 观众聊天页
 */
public class LiveGameAnalysisViewHolder extends AbsViewHolder {
    private static final int MOST_SHOW_DATA = 10; // 最多展示10条数据
    private static final int DATE_SCORE_HEIGHT = 45;

    //    private final static int ANALYSIS_MATCH_ID = 66898;
    private final static int ANALYSIS_RETURN_GAME_NUMBER = 0;
    private TextView tv_team_a_name, tv_team_b_name;
    private ImageView iv_team_a_logo, iv_team_b_logo;

    private TextView tv_team_a_win_count, tv_team_b_win_count;
    private TextView tv_team_a_win_percent, tv_team_b_win_percent;
    private View v_team_a_win_bg, v_team_b_win_bg;
    private LinearLayout ll_team_win_bg;
    private View v_team_win_bg;

    private TextView tv_team_a_blood_percent, tv_team_b_blood_percent;
    private TextView tv_team_a_first_blood_count, tv_team_b_first_blood_count;
    private View v_team_a_blood_bg, v_team_b_blood_bg;
    private LinearLayout ll_team_blood_bg;
    private View v_team_blood_bg;

    private TextView tv_team_a_tower_percent, tv_team_b_tower_percent;
    private TextView tv_team_a_first_tower_count, tv_team_b_first_tower_count;
    private View v_team_a_tower_bg, v_team_b_tower_bg;
    private LinearLayout ll_team_tower_bg;
    private View v_team_tower_bg;

    private TextView tv_team_a_five_kill_percent, tv_team_b_five_kill_percent;
    private TextView tv_team_a_five_kill_count, tv_team_b_five_kill_count;
    private View v_team_a_five_kill_bg, v_team_b_five_kill_bg;
    private LinearLayout ll_team_five_kill_bg;
    private View v_team_five_kill_bg;

    private TextView tv_team_a_dragon_percent, tv_team_b_dragon_percent;
    private TextView tv_team_a_first_dragon_count, tv_team_b_first_dragon_count;
    private View v_team_a_dragon_bg, v_team_b_dragon_bg;
    private LinearLayout ll_team_dragon_bg;
    private View v_team_dragon_bg;

    private TextView tv_team_a_nash_percent, tv_team_b_nash_percent;
    private TextView tv_team_a_first_nash_count, tv_team_b_first_nash_count;
    private View v_team_a_nash_bg, v_team_b_nash_bg;
    private LinearLayout ll_team_nash_bg;
    private View v_team_nash_bg;

    private TextView tv_history_win_count;
    private TextView tv_history_lose_count;
    private ImageView iv_recent_team_a_logo;
    private TextView tv_recent_team_a_name;
    private ImageView iv_recent_team_b_logo;
    private TextView tv_recent_team_b_name;
    private TextView tv_recent_team_a_win_count;
    private TextView tv_recent_team_b_win_count;
    private TextView tv_recent_team_a_lose_count;
    private TextView tv_recent_team_b_lose_count;

    private LinearLayout ll_add_history_battle;
    private LinearLayout ll_add_a_recent_battle_data;
    private LinearLayout ll_add_b_recent_battle_data;

    private GameLolMatchBean mMatchInfo;
    private SmartRefreshLayout mAnalysisRefresh;
    private MatchLolAnalysisBean matchLolAnalysisBean;
//    private List<MatchTeamBattleRecordBean> records;
//    private List<MatchTeamBattleRecordBean> aRecentData;

    // 设置Team默认logo
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_lol_default)
            .error(R.mipmap.icon_default_logo);

    public LiveGameAnalysisViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_game_analysis;
    }

    @Override
    public void init() {
        intViewWithId();
        initRefresh();
        initAnalysis();
    }

    private void intViewWithId() {
        tv_team_a_name = findViewById(R.id.tv_team_a_name);
        tv_team_b_name = findViewById(R.id.tv_team_b_name);
        iv_team_a_logo = findViewById(R.id.iv_team_a_logo);
        iv_team_b_logo = findViewById(R.id.iv_team_b_logo);

        tv_team_a_win_percent = findViewById(R.id.tv_team_a_win_percent);
        tv_team_b_win_percent = findViewById(R.id.tv_team_b_win_percent);
        tv_team_a_win_count = findViewById(R.id.tv_team_a_win_count);
        tv_team_b_win_count = findViewById(R.id.tv_team_b_win_count);
        v_team_a_win_bg = findViewById(R.id.v_team_a_win_bg);
        v_team_b_win_bg = findViewById(R.id.v_team_b_win_bg);
        ll_team_win_bg = findViewById(R.id.ll_team_win_bg);
        v_team_win_bg = findViewById(R.id.v_team_win_bg);

        tv_team_a_blood_percent = findViewById(R.id.tv_team_a_blood_percent);
        tv_team_b_blood_percent = findViewById(R.id.tv_team_b_blood_percent);
        tv_team_a_first_blood_count = findViewById(R.id.tv_team_a_first_blood_count);
        tv_team_b_first_blood_count = findViewById(R.id.tv_team_b_first_blood_count);
        v_team_a_blood_bg = findViewById(R.id.v_team_a_blood_bg);
        v_team_b_blood_bg = findViewById(R.id.v_team_b_blood_bg);
        ll_team_blood_bg = findViewById(R.id.ll_team_blood_bg);
        v_team_blood_bg = findViewById(R.id.v_team_blood_bg);

        tv_team_a_tower_percent = findViewById(R.id.tv_team_a_tower_percent);
        tv_team_b_tower_percent = findViewById(R.id.tv_team_b_tower_percent);
        tv_team_a_first_tower_count = findViewById(R.id.tv_team_a_first_tower_count);
        tv_team_b_first_tower_count = findViewById(R.id.tv_team_b_first_tower_count);
        v_team_a_tower_bg = findViewById(R.id.v_team_a_tower_bg);
        v_team_b_tower_bg = findViewById(R.id.v_team_b_tower_bg);
        ll_team_tower_bg = findViewById(R.id.ll_team_tower_bg);
        v_team_tower_bg = findViewById(R.id.v_team_tower_bg);

        tv_team_a_five_kill_percent = findViewById(R.id.tv_team_a_five_kill_percent);
        tv_team_b_five_kill_percent = findViewById(R.id.tv_team_b_five_kill_percent);
        tv_team_a_five_kill_count = findViewById(R.id.tv_team_a_five_kill_count);
        tv_team_b_five_kill_count = findViewById(R.id.tv_team_b_five_kill_count);
        v_team_a_five_kill_bg = findViewById(R.id.v_team_a_five_kill_bg);
        v_team_b_five_kill_bg = findViewById(R.id.v_team_b_five_kill_bg);
        ll_team_five_kill_bg = findViewById(R.id.ll_team_five_kill_bg);
        v_team_five_kill_bg = findViewById(R.id.v_team_five_kill_bg);

        tv_team_a_dragon_percent = findViewById(R.id.tv_team_a_dragon_percent);
        tv_team_b_dragon_percent = findViewById(R.id.tv_team_b_dragon_percent);
        tv_team_a_first_dragon_count = findViewById(R.id.tv_team_a_first_dragon_count);
        tv_team_b_first_dragon_count = findViewById(R.id.tv_team_b_first_dragon_count);
        v_team_a_dragon_bg = findViewById(R.id.v_team_a_dragon_bg);
        v_team_b_dragon_bg = findViewById(R.id.v_team_b_dragon_bg);
        ll_team_dragon_bg = findViewById(R.id.ll_team_dragon_bg);
        v_team_dragon_bg = findViewById(R.id.v_team_dragon_bg);

        tv_team_a_nash_percent = findViewById(R.id.tv_team_a_nash_percent);
        tv_team_b_nash_percent = findViewById(R.id.tv_team_b_nash_percent);
        tv_team_a_first_nash_count = findViewById(R.id.tv_team_a_first_nash_count);
        tv_team_b_first_nash_count = findViewById(R.id.tv_team_b_first_nash_count);
        v_team_a_nash_bg = findViewById(R.id.v_team_a_nash_bg);
        v_team_b_nash_bg = findViewById(R.id.v_team_b_nash_bg);
        ll_team_nash_bg = findViewById(R.id.ll_team_nash_bg);
        v_team_nash_bg = findViewById(R.id.v_team_nash_bg);

        tv_history_win_count = findViewById(R.id.tv_history_win_count);
        tv_history_lose_count = findViewById(R.id.tv_history_lose_count);
        iv_recent_team_a_logo = findViewById(R.id.iv_recent_team_a_logo);
        tv_recent_team_a_name = findViewById(R.id.tv_recent_team_a_name);
        iv_recent_team_b_logo = findViewById(R.id.iv_recent_team_b_logo);
        tv_recent_team_b_name = findViewById(R.id.tv_recent_team_b_name);
        tv_recent_team_a_win_count = findViewById(R.id.tv_recent_team_a_win_count);
        tv_recent_team_b_win_count = findViewById(R.id.tv_recent_team_b_win_count);
        tv_recent_team_a_lose_count = findViewById(R.id.tv_recent_team_a_lose_count);
        tv_recent_team_b_lose_count = findViewById(R.id.tv_recent_team_b_lose_count);

        // 添加Item容器
        ll_add_history_battle = findViewById(R.id.ll_add_history_battle);
        ll_add_a_recent_battle_data = findViewById(R.id.ll_add_a_recent_battle_data);
        ll_add_b_recent_battle_data = findViewById(R.id.ll_add_b_recent_battle_data);

    }

    private void initRefresh() {
        mAnalysisRefresh = mContentView.findViewById(R.id.mAnalysisRefresh);
        mAnalysisRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAnalysisRefresh.isRefreshing();
                clearData();
                initAnalysis();
            }
        });
    }

    /**
     * 获取分析数据 W.
     */
    private void initAnalysis() {
        if (mMatchInfo != null) {
            LiveHttpUtil.getLOLMatchListByAnalysis(mMatchInfo.getId(), ANALYSIS_RETURN_GAME_NUMBER, new HttpCallbackObject() {
                @Override
                public void onSuccess(int code, String msg, Object info) {
                    try {
                        setMatchLolAnalysisResult(matchLolAnalysisBean);
                        if (mAnalysisRefresh != null) mAnalysisRefresh.finishRefresh();
                        if (code == 0) {
                            matchLolAnalysisBean = JSON.parseObject(info.toString(), MatchLolAnalysisBean.class);
                        }
                    } catch (Exception e) {
                        AppLog.e(e.toString());
                    }
                }
            });
        }
    }

    /**
     * 设置Lol分析界面数据 W.
     */
    private void setMatchLolAnalysisResult(MatchLolAnalysisBean matchLolAnalysisBean) throws Exception {
        if (matchLolAnalysisBean == null) return;
        try {
            // 历史交手数据
            setMatchTeamHistoryBattle(matchLolAnalysisBean);
            // 历史交手详情
            setMatchTeamHistoryBattleDetail(matchLolAnalysisBean);
            // A队近期战绩
            setMatchARecentBattle(matchLolAnalysisBean);
            // B队近期战绩
            setMatchBRecentBattle(matchLolAnalysisBean);
        } catch (Exception e) {
            AppLog.e(e.toString());
        }
    }

    /**
     * 历史交手数据 W.
     */
    private void setMatchTeamHistoryBattle(MatchLolAnalysisBean matchLolAnalysisBean) throws Exception {
        // 设置Team的名字和logo
        tv_team_a_name.setText(matchLolAnalysisBean.getTeam_a_name());
        tv_team_b_name.setText(matchLolAnalysisBean.getTeam_b_name());
        setImage(iv_team_a_logo, matchLolAnalysisBean.getTeam_a_logo());
        setImage(iv_team_b_logo, matchLolAnalysisBean.getTeam_b_logo());

        if (matchLolAnalysisBean.getData() == null ||
                matchLolAnalysisBean.getData().size() == 0 ||
                matchLolAnalysisBean.getData().get(0).getBattle_stats() == null ||
                matchLolAnalysisBean.getData().get(0).getBattle_stats().size() == 0) {
            setMatchTeamHistoryBattleNoData();
            return;
        }

        // 设置胜场数
        MatchTeamBattleStatsBean matchTeamBattleStatsBean = matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0);
        double aWinCount = matchTeamBattleStatsBean.getTeam_a_win_count();
        double bWinCount = matchTeamBattleStatsBean.getTeam_b_win_count();
        if (aWinCount == 0.0 && bWinCount == 0.0) {
            ll_team_win_bg.setVisibility(View.GONE);
            v_team_win_bg.setVisibility(View.VISIBLE);
        } else {
            tv_team_a_win_percent.setText(String.format("%s%%", StringUtil.formatDouble(aWinCount / (aWinCount + bWinCount) * 100)));
            tv_team_b_win_percent.setText(String.format("%s%%", StringUtil.formatDouble(bWinCount / (aWinCount + bWinCount) * 100)));
            tv_team_a_win_count.setText(String.valueOf(StringUtil.formatDouble(aWinCount)));
            tv_team_b_win_count.setText(String.valueOf(StringUtil.formatDouble(bWinCount)));
            LinearLayout.LayoutParams aWinCountLayoutParams = (LinearLayout.LayoutParams) v_team_a_win_bg.getLayoutParams();
            aWinCountLayoutParams.weight = (float) (aWinCount / (aWinCount + bWinCount));
            v_team_a_win_bg.setLayoutParams(aWinCountLayoutParams);
            LinearLayout.LayoutParams bWinCountLayoutParams = (LinearLayout.LayoutParams) v_team_b_win_bg.getLayoutParams();
            bWinCountLayoutParams.weight = (float) (bWinCount / (aWinCount + bWinCount));
            v_team_b_win_bg.setLayoutParams(bWinCountLayoutParams);
        }

        // 设置一血数
        double aBloodCount = matchTeamBattleStatsBean.getTeam_a_first_blood_count();
        double bBloodCount = matchTeamBattleStatsBean.getTeam_b_first_blood_count();
        if (aBloodCount == 0.0 && bBloodCount == 0.0) {
            ll_team_blood_bg.setVisibility(View.GONE);
            v_team_blood_bg.setVisibility(View.VISIBLE);
        } else {
            tv_team_a_blood_percent.setText(String.format("%s%%", StringUtil.formatDouble(aBloodCount / (aBloodCount + bBloodCount) * 100)));
            tv_team_b_blood_percent.setText(String.format("%s%%", StringUtil.formatDouble(bBloodCount / (aBloodCount + bBloodCount) * 100)));
            tv_team_a_first_blood_count.setText(String.valueOf(StringUtil.formatDouble(aBloodCount)));
            tv_team_b_first_blood_count.setText(String.valueOf(StringUtil.formatDouble(bBloodCount)));
            LinearLayout.LayoutParams aBloodCountLayoutParams = (LinearLayout.LayoutParams) v_team_a_blood_bg.getLayoutParams();
            aBloodCountLayoutParams.weight = (float) (aBloodCount / (aBloodCount + bBloodCount));
            v_team_a_blood_bg.setLayoutParams(aBloodCountLayoutParams);
            LinearLayout.LayoutParams bBloodCountLayoutParams = (LinearLayout.LayoutParams) v_team_b_blood_bg.getLayoutParams();
            bBloodCountLayoutParams.weight = (float) (bBloodCount / (aBloodCount + bBloodCount));
            v_team_b_blood_bg.setLayoutParams(bBloodCountLayoutParams);
        }

        // 设置一塔数
        double aTowerCount = matchTeamBattleStatsBean.getTeam_a_first_tower_count();
        double bTowerCount = matchTeamBattleStatsBean.getTeam_b_first_tower_count();
        if (aTowerCount == 0.0 && bTowerCount == 0.0) {
            ll_team_tower_bg.setVisibility(View.GONE);
            v_team_tower_bg.setVisibility(View.VISIBLE);
        } else {
            tv_team_a_tower_percent.setText(String.format("%s%%", StringUtil.formatDouble(aTowerCount / (aTowerCount + bTowerCount) * 100)));
            tv_team_b_tower_percent.setText(String.format("%s%%", StringUtil.formatDouble(bTowerCount / (aTowerCount + bTowerCount) * 100)));
            tv_team_a_first_tower_count.setText(String.valueOf(StringUtil.formatDouble(aTowerCount)));
            tv_team_b_first_tower_count.setText(String.valueOf(StringUtil.formatDouble(bTowerCount)));
            LinearLayout.LayoutParams aTowerCountLayoutParams = (LinearLayout.LayoutParams) v_team_a_tower_bg.getLayoutParams();
            aTowerCountLayoutParams.weight = (float) (aTowerCount / (aTowerCount + bTowerCount));
            v_team_a_tower_bg.setLayoutParams(aTowerCountLayoutParams);
            LinearLayout.LayoutParams bTowerCountLayoutParams = (LinearLayout.LayoutParams) v_team_b_tower_bg.getLayoutParams();
            bTowerCountLayoutParams.weight = (float) (bTowerCount / (aTowerCount + bTowerCount));
            v_team_b_tower_bg.setLayoutParams(bTowerCountLayoutParams);
        }

        // 设置五杀数
        double aKillCount = matchTeamBattleStatsBean.getTeam_a_five_kill_count();
        double bKillCount = matchTeamBattleStatsBean.getTeam_b_five_kill_count();
        if (aKillCount == 0.0 && bKillCount == 0.0) {
            ll_team_five_kill_bg.setVisibility(View.GONE);
            v_team_five_kill_bg.setVisibility(View.VISIBLE);
        } else {
            tv_team_a_five_kill_percent.setText(String.format("%s%%", StringUtil.formatDouble(aKillCount / (aKillCount + bKillCount) * 100)));
            tv_team_b_five_kill_percent.setText(String.format("%s%%", StringUtil.formatDouble(bKillCount / (aKillCount + bKillCount) * 100)));
            tv_team_a_five_kill_count.setText(String.valueOf(StringUtil.formatDouble(aKillCount)));
            tv_team_b_five_kill_count.setText(String.valueOf(StringUtil.formatDouble(bKillCount)));
            LinearLayout.LayoutParams aKillCountLayoutParams = (LinearLayout.LayoutParams) v_team_a_five_kill_bg.getLayoutParams();
            aKillCountLayoutParams.weight = (float) (aKillCount / (aKillCount + bKillCount));
            v_team_a_five_kill_bg.setLayoutParams(aKillCountLayoutParams);
            LinearLayout.LayoutParams bKillCountLayoutParams = (LinearLayout.LayoutParams) v_team_b_five_kill_bg.getLayoutParams();
            bKillCountLayoutParams.weight = (float) (bKillCount / (aKillCount + bKillCount));
            v_team_b_five_kill_bg.setLayoutParams(bKillCountLayoutParams);
        }

        // 设置首次小龙
        double aDragonCount = matchTeamBattleStatsBean.getTeam_a_first_dragon_count();
        double bDragonCount = matchTeamBattleStatsBean.getTeam_b_first_dragon_count();
        if (aDragonCount == 0.0 && bDragonCount == 0.0) {
            ll_team_dragon_bg.setVisibility(View.GONE);
            v_team_dragon_bg.setVisibility(View.VISIBLE);
        } else {
            tv_team_a_dragon_percent.setText(String.format("%s%%", StringUtil.formatDouble(aDragonCount / (aDragonCount + bDragonCount) * 100)));
            tv_team_b_dragon_percent.setText(String.format("%s%%", StringUtil.formatDouble(bDragonCount / (aDragonCount + bDragonCount) * 100)));
            tv_team_a_first_dragon_count.setText(StringUtil.formatDouble(aDragonCount));
            tv_team_b_first_dragon_count.setText(StringUtil.formatDouble(bDragonCount));
            LinearLayout.LayoutParams aDragonCountLayoutParams = (LinearLayout.LayoutParams) v_team_a_dragon_bg.getLayoutParams();
            aDragonCountLayoutParams.weight = (float) (aDragonCount / (aDragonCount + bDragonCount));
            v_team_a_dragon_bg.setLayoutParams(aDragonCountLayoutParams);
            LinearLayout.LayoutParams bDragonCountLayoutParams = (LinearLayout.LayoutParams) v_team_b_dragon_bg.getLayoutParams();
            bDragonCountLayoutParams.weight = (float) (bDragonCount / (aDragonCount + bDragonCount));
            v_team_b_dragon_bg.setLayoutParams(bDragonCountLayoutParams);
        }

        // 设置首次大龙
        double aNashCount = matchTeamBattleStatsBean.getTeam_a_first_nash_count();
        double bNashCount = matchTeamBattleStatsBean.getTeam_b_first_nash_count();
        if (aNashCount == 0.0 && bNashCount == 0.0) {
            ll_team_nash_bg.setVisibility(View.GONE);
//            v_team_nash_bg.setVisibility(View.VISIBLE);
        } else {
            tv_team_a_nash_percent.setText(String.format("%s%%", StringUtil.formatDouble(aNashCount / (aNashCount + bNashCount) * 100)));
            tv_team_b_nash_percent.setText(String.format("%s%%", StringUtil.formatDouble(bNashCount / (aNashCount + bNashCount) * 100)));
            tv_team_a_first_nash_count.setText(StringUtil.formatDouble(aNashCount));
            tv_team_b_first_nash_count.setText(StringUtil.formatDouble(bNashCount));
            LinearLayout.LayoutParams aNashCountLayoutParams = (LinearLayout.LayoutParams) v_team_a_nash_bg.getLayoutParams();
            aNashCountLayoutParams.weight = (float) (aNashCount / (aNashCount + bNashCount));
            v_team_a_nash_bg.setLayoutParams(aNashCountLayoutParams);
            LinearLayout.LayoutParams bNashCountLayoutParams = (LinearLayout.LayoutParams) v_team_b_nash_bg.getLayoutParams();
            bNashCountLayoutParams.weight = (float) (bNashCount / (aNashCount + bNashCount));
            v_team_b_nash_bg.setLayoutParams(bNashCountLayoutParams);
        }
    }

    private void setMatchTeamHistoryBattleNoData() {
        ll_team_win_bg.setVisibility(View.GONE);
        v_team_win_bg.setVisibility(View.VISIBLE);
        ll_team_blood_bg.setVisibility(View.GONE);
        v_team_blood_bg.setVisibility(View.VISIBLE);
        ll_team_tower_bg.setVisibility(View.GONE);
        v_team_tower_bg.setVisibility(View.VISIBLE);
        ll_team_five_kill_bg.setVisibility(View.GONE);
        v_team_five_kill_bg.setVisibility(View.VISIBLE);
        ll_team_dragon_bg.setVisibility(View.GONE);
        v_team_dragon_bg.setVisibility(View.VISIBLE);
        ll_team_nash_bg.setVisibility(View.GONE);
//            v_team_nash_bg.setVisibility(View.VISIBLE);
    }

    /**
     * 历史交手详情 W.
     */
    private void setMatchTeamHistoryBattleDetail(MatchLolAnalysisBean matchLolAnalysisBean) throws Exception {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(DATE_SCORE_HEIGHT));
        if (matchLolAnalysisBean.getData() != null &&
                matchLolAnalysisBean.getData().size() > 0 &&
                matchLolAnalysisBean.getData().get(0).getBattle_stats() != null &&
                matchLolAnalysisBean.getData().get(0).getBattle_stats().size() > 0 &&
                matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getTeam_a_win_count() != 0 &&
                matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getTeam_b_lose_count() != 0) {
            tv_history_win_count.setText(String.format("%s胜", matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getTeam_a_win_count()));
            tv_history_lose_count.setText(String.format("%s负", matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getTeam_b_win_count()));
        }
        if (matchLolAnalysisBean.getData() == null ||
                matchLolAnalysisBean.getData().size() == 0 ||
                matchLolAnalysisBean.getData().get(0).getBattle_stats() == null ||
                matchLolAnalysisBean.getData().get(0).getBattle_stats().size() == 0 ||
                matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getRecords() == null ||
                matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getRecords().size() == 0) {
            LinearLayout itemEmptyView = (LinearLayout) View.inflate(mContext, R.layout.view_live_game_analysis_no_data, null);
            itemEmptyView.setLayoutParams(lp);
            TextView tv_analysis_no_data = itemEmptyView.findViewById(R.id.tv_analysis_no_data);
            tv_analysis_no_data.setText(R.string.live_game_analysis_no_data);
            ll_add_history_battle.addView(itemEmptyView);
            return;
        }

        // 获取历史数据
        List<MatchTeamBattleRecordBean> records = matchLolAnalysisBean.getData().get(0).getBattle_stats().get(0).getRecords();
        for (int i = 0; i < records.size() && i < MOST_SHOW_DATA; i++) {
            LinearLayout itemDataView = (LinearLayout) View.inflate(mContext, R.layout.view_live_game_analysis_history_battle, null);
            itemDataView.setLayoutParams(lp);
            // 初始化控件
            TextView tv_history_date = itemDataView.findViewById(R.id.tv_history_date);
            ImageView iv_history_a_logo = itemDataView.findViewById(R.id.iv_history_a_logo);
            TextView tv_history_a_name = itemDataView.findViewById(R.id.tv_history_a_name);
            ImageView iv_history_b_logo = itemDataView.findViewById(R.id.iv_history_b_logo);
            TextView tv_history_b_name = itemDataView.findViewById(R.id.tv_history_b_name);
            TextView tv_history_battle_point = itemDataView.findViewById(R.id.tv_history_battle_point);
            // 设置数据
            MatchTeamBattleRecordBean recordBean = records.get(i);
            tv_history_date.setText(recordBean.getMatch_time());
            setImage(iv_history_a_logo, recordBean.getTeam_a_logo());
            tv_history_a_name.setText(recordBean.getTeam_a_name());
            setImage(iv_history_b_logo, recordBean.getTeam_b_logo());
            tv_history_b_name.setText(recordBean.getTeam_b_name());
            String battlePoint = recordBean.getTeam_a_score() + "-" + recordBean.getTeam_b_score();
            tv_history_battle_point.setText(battlePoint);
            // 添加Item
            ll_add_history_battle.addView(itemDataView);
        }
    }

    /**
     * A队近期战绩 W.
     */
    private void setMatchARecentBattle(MatchLolAnalysisBean matchLolAnalysisBean) throws Exception {
        // 设置A队标题数据
        setImage(iv_recent_team_a_logo, matchLolAnalysisBean.getTeam_a_logo());
        tv_recent_team_a_name.setText(matchLolAnalysisBean.getTeam_a_name());
        // 判断集合
        if (matchLolAnalysisBean.getData() == null ||
                matchLolAnalysisBean.getData().size() == 0 ||
                matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats() == null ||
                matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats().size() == 0) {
            setMatchARecentBattleNoData();
            return;
        }
        // 设置胜负数量
        tv_recent_team_a_win_count.setText(String.format("%s胜", matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats().get(0).getWin_rate()));
        tv_recent_team_a_lose_count.setText(String.format("%s负", matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats().get(0).getLost_rate()));
        // 设置A队无数据页
        if (matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats().get(0).getMatches() == null ||
                matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats().get(0).getMatches().size() == 0) {
            setMatchARecentBattleNoData();
            return;
        }
        // 设置A队数据
        List<MatchTeamBattleRecordBean> aRecentData = matchLolAnalysisBean.getData().get(0).getTeam_a_recent_stats().get(0).getMatches();
        for (int i = 0; i < aRecentData.size() && i < MOST_SHOW_DATA; i++) {
            LinearLayout itemDataView = (LinearLayout) View.inflate(mContext, R.layout.view_live_game_analysis_history_battle, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(DATE_SCORE_HEIGHT));
            itemDataView.setLayoutParams(lp);
            // 初始化控件
            TextView tv_history_date = itemDataView.findViewById(R.id.tv_history_date);
            ImageView iv_history_a_logo = itemDataView.findViewById(R.id.iv_history_a_logo);
            TextView tv_history_a_name = itemDataView.findViewById(R.id.tv_history_a_name);
            ImageView iv_history_b_logo = itemDataView.findViewById(R.id.iv_history_b_logo);
            TextView tv_history_b_name = itemDataView.findViewById(R.id.tv_history_b_name);
            TextView tv_history_battle_point = itemDataView.findViewById(R.id.tv_history_battle_point);
            // 设置数据
            MatchTeamBattleRecordBean recentBean = aRecentData.get(i);
            tv_history_date.setText(recentBean.getMatch_time());
            setImage(iv_history_a_logo, recentBean.getTeam_a_logo());
            tv_history_a_name.setText(recentBean.getTeam_a_name());
            setImage(iv_history_b_logo, recentBean.getTeam_b_logo());
            tv_history_b_name.setText(recentBean.getTeam_b_name());
            String battlePoint = recentBean.getTeam_a_score() + "-" + recentBean.getTeam_b_score();
            tv_history_battle_point.setText(battlePoint);
            // 添加Item
            ll_add_a_recent_battle_data.addView(itemDataView);
        }

    }

    private void setMatchARecentBattleNoData() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(DATE_SCORE_HEIGHT));
        LinearLayout aItemEmptyView = (LinearLayout) View.inflate(mContext, R.layout.view_live_game_analysis_no_data, null);
        aItemEmptyView.setLayoutParams(lp);
        TextView tv_a_analysis_no_data = aItemEmptyView.findViewById(R.id.tv_analysis_no_data);
        tv_a_analysis_no_data.setText(R.string.live_game_analysis_recent_no_data);
        ll_add_a_recent_battle_data.addView(aItemEmptyView);
    }

    /**
     * B队近期战绩 W.
     */
    private void setMatchBRecentBattle(MatchLolAnalysisBean matchLolAnalysisBean) throws Exception {
        // 设置B队标题数据
        setImage(iv_recent_team_b_logo, matchLolAnalysisBean.getTeam_b_logo());
        tv_recent_team_b_name.setText(matchLolAnalysisBean.getTeam_b_name());
        // 判断集合
        if (matchLolAnalysisBean.getData() == null ||
                matchLolAnalysisBean.getData().size() == 0 ||
                matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats() == null ||
                matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats().size() == 0) {
            setMatchBRecentBattle();
            return;
        }
        // 设置胜负数量
        tv_recent_team_b_win_count.setText(String.format("%s胜", matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats().get(0).getWin_rate()));
        tv_recent_team_b_lose_count.setText(String.format("%s负", matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats().get(0).getLost_rate()));
        // 设置B队无数据页
        if (matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats().get(0).getMatches() == null ||
                matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats().get(0).getMatches().size() == 0) {
            setMatchBRecentBattle();
            return;
        }
        // 设置B队数据
        List<MatchTeamBattleRecordBean> bRecentData = matchLolAnalysisBean.getData().get(0).getTeam_b_recent_stats().get(0).getMatches();
        for (int i = 0; i < bRecentData.size() && i < MOST_SHOW_DATA; i++) {
            LinearLayout itemDataView = (LinearLayout) View.inflate(mContext, R.layout.view_live_game_analysis_history_battle, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(DATE_SCORE_HEIGHT));
            itemDataView.setLayoutParams(lp);
            // 初始化控件
            TextView tv_history_date = itemDataView.findViewById(R.id.tv_history_date);
            ImageView iv_history_a_logo = itemDataView.findViewById(R.id.iv_history_a_logo);
            TextView tv_history_a_name = itemDataView.findViewById(R.id.tv_history_a_name);
            ImageView iv_history_b_logo = itemDataView.findViewById(R.id.iv_history_b_logo);
            TextView tv_history_b_name = itemDataView.findViewById(R.id.tv_history_b_name);
            TextView tv_history_battle_point = itemDataView.findViewById(R.id.tv_history_battle_point);
            // 设置数据
            MatchTeamBattleRecordBean recentBean = bRecentData.get(i);
            tv_history_date.setText(recentBean.getMatch_time());
            setImage(iv_history_a_logo, recentBean.getTeam_a_logo());
            tv_history_a_name.setText(recentBean.getTeam_a_name());
            setImage(iv_history_b_logo, recentBean.getTeam_b_logo());
            tv_history_b_name.setText(recentBean.getTeam_b_name());
            String battlePoint = recentBean.getTeam_a_score() + "-" + recentBean.getTeam_b_score();
            tv_history_battle_point.setText(battlePoint);
            // 添加Item
            ll_add_b_recent_battle_data.addView(itemDataView);
        }
    }

    private void setMatchBRecentBattle() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(DATE_SCORE_HEIGHT));
        LinearLayout bItemEmptyView = (LinearLayout) View.inflate(mContext, R.layout.view_live_game_analysis_no_data, null);
        bItemEmptyView.setLayoutParams(lp);
        TextView tv_b_analysis_no_data = bItemEmptyView.findViewById(R.id.tv_analysis_no_data);
        tv_b_analysis_no_data.setText(R.string.live_game_analysis_recent_no_data);
        ll_add_b_recent_battle_data.addView(bItemEmptyView);
    }

    /**
     * 设置图片 W.
     */
    private void setImage(ImageView iv, String ivUrl) {
        Glide.with(mContext)
                .load(ivUrl)
                .apply(options)
                .into(iv);
    }

    public void loadData(GameLolMatchBean mMatchInfo) {
        this.mMatchInfo = mMatchInfo;
        clearData();
        initAnalysis();
    }

    private void clearData() {
        matchLolAnalysisBean = null;
        ll_add_history_battle.removeAllViews();
        ll_add_a_recent_battle_data.removeAllViews();
        ll_add_b_recent_battle_data.removeAllViews();
//        if(records != null && records.size() > 0) records.clear();
//        if(aRecentData != null && aRecentData.size() > 0) aRecentData.clear();
    }

}
