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
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveGamePlayerBean;
import com.yunbao.live.http.LiveHttpUtil;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juwan on 2018/10/9.
 * 观众聊天页
 */

public class LiveGamePlayerViewHolder extends AbsViewHolder implements View.OnClickListener {
    private ImageView ATeamIcon, BTeamIcon, ATeamPlayerIcon, BTeamPlayerIcon,
            ATeamHeroOneIcon, ATeamHeroTwoIcon, ATeamHeroThreeIcon, BTeamHeroOneIcon,
            BTeamHeroTwoIcon, BTeamHeroThreeIcon, ATeamAlternateOne, ATeamAlternateTwo, ATeamAlternateThree, BTeamAlternateOne, BTeamAlternateTwo, BTeamAlternateThree;
    private TextView ATeamName, BTeamName, ATeamPlayerName, BTeamPlayerName, ATeamPlayerWinningProbability,
            BTeamPlayerWinningProbability, ATeamPlayerKDANum, BTeamPlayerKDANum, ATeamParticipationRate,
            BTeamParticipationRate, ATeamPlayerOutput, BTeamPlayerOutput, ATeamPlayerEconomics, BTeamPlayerEconomics,
            ATeamHeroNumOne, ATeamHeroNumTwo, ATeamHeroNumThree, BTeamHeroNumOne, BTeamHeroNumTwo, BTeamHeroNumThree,
            ATeamHeroWinningOne, ATeamHeroWinningTwo, ATeamHeroWinningThree, BTeamHeroWinningOne, BTeamHeroWinningTwo,
            BTeamHeroWinningThree, TabUpper, TabCenterRoad, TabAdc, TabFightingWild, TabAuxiliary;
    private CircularProgressView mCPView;
    private View ATeamPlayerKDAView, BTeamPlayerKDAView, ATeamParticipationRateView, BTeamParticipationRateView,
            ATeamPlayerOutputView, BTeamPlayerOutputView, ATeamPlayerEconomicsView, BTeamPlayerEconomicsView;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);

    private List<LiveGamePlayerBean.TeamPlayerStatsBean> AUpperRoadBeanList = new ArrayList<>(); //A上路
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> BUpperRoadBeanList = new ArrayList<>(); //B上路
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> ACenterRoadBeanList = new ArrayList<>(); //A中单
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> BCenterRoadBeanList = new ArrayList<>(); //B中单
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> AADCBeanList = new ArrayList<>(); //A adc
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> BADCBeanList = new ArrayList<>(); //B adc
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> AFightingWildBeanList = new ArrayList<>(); //A 打野
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> BFightingWildBeanList = new ArrayList<>(); //B 打野
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> AAuxiliaryBeanList = new ArrayList<>(); //A辅助
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> BAuxiliaryBeanList = new ArrayList<>(); //B辅助

    private List<LiveGamePlayerBean.TeamPlayerStatsBean> AChooseBeanList = new ArrayList<>(); //选择 A list
    private List<LiveGamePlayerBean.TeamPlayerStatsBean> BChooseBeanList = new ArrayList<>(); //选择 B list

    private LiveGamePlayerBean.TeamPlayerStatsBean AChooseBean; //选择 A
    private LiveGamePlayerBean.TeamPlayerStatsBean BChooseBean;  //选择 B
    private GameLolMatchBean mMatchInfo;

    private SmartRefreshLayout mRefresh;
    private int index;

    public LiveGamePlayerViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_game_player;
    }

    @Override
    public void init() {
        initView();
        mRefresh = mContentView.findViewById(R.id.mRefresh);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh.isRefreshing();
                resetData();
                requestPlayerData();
            }
        });

        requestPlayerData();

    }

    public void loadData(GameLolMatchBean mMatchInfo) {
        this.mMatchInfo = mMatchInfo;
        resetData();
        requestPlayerData();
    }

    private void resetData() {
        AUpperRoadBeanList.clear();
        BUpperRoadBeanList.clear();
        ACenterRoadBeanList.clear();
        BCenterRoadBeanList.clear();
        AADCBeanList.clear();
        BADCBeanList.clear();
        AFightingWildBeanList.clear();
        BFightingWildBeanList.clear();
        AAuxiliaryBeanList.clear();
        BAuxiliaryBeanList.clear();

        AChooseBeanList.clear();
        BChooseBeanList.clear();

    }

    private void requestPlayerData() {
        if (mMatchInfo != null) {
            LiveHttpUtil.getLOLMatchPlayer(mMatchInfo.getId(), 0, new HttpCallbackObject() {
                @Override
                public void onSuccess(int code, String msg, Object info) {
                    mRefresh.finishRefresh();
                    if (code == 0) {
                        LiveGamePlayerBean liveGamePlayerBean = JSON.parseObject(info.toString(), LiveGamePlayerBean.class);
                        if (liveGamePlayerBean != null) {
                            setTeamData(liveGamePlayerBean);
                            splitHeroData(liveGamePlayerBean);
                            if (AUpperRoadBeanList.size() > 0 && BUpperRoadBeanList.size() > 0) {
                                if (index == 0) {
                                    setDefault();
                                }
                            }

                        }
                    }
                }
            });
        }

    }

    /**
     * 拆分数据
     *
     * @param liveGamePlayerBean
     */
    private void splitHeroData(LiveGamePlayerBean liveGamePlayerBean) {
        List<LiveGamePlayerBean.TeamPlayerStatsBean> teamAPlayerStats = liveGamePlayerBean.getTeam_a_player_stats();
        for (LiveGamePlayerBean.TeamPlayerStatsBean teamAPlayerStat : teamAPlayerStats) {
            if ("打野".equals(teamAPlayerStat.getPosition())) {
                AFightingWildBeanList.add(teamAPlayerStat);
            } else if ("中单".equals(teamAPlayerStat.getPosition())) {
                ACenterRoadBeanList.add(teamAPlayerStat);
            } else if ("上单".equals(teamAPlayerStat.getPosition())) {
                AUpperRoadBeanList.add(teamAPlayerStat);
            } else if ("ADC".equals(teamAPlayerStat.getPosition())) {
                AADCBeanList.add(teamAPlayerStat);
            } else if ("辅助".equals(teamAPlayerStat.getPosition())) {
                AAuxiliaryBeanList.add(teamAPlayerStat);
            }
        }

        List<LiveGamePlayerBean.TeamPlayerStatsBean> teamBPlayerStats = liveGamePlayerBean.getTeam_b_player_stats();
        for (LiveGamePlayerBean.TeamPlayerStatsBean teamBPlayerStat : teamBPlayerStats) {
            if ("打野".equals(teamBPlayerStat.getPosition())) {
                BFightingWildBeanList.add(teamBPlayerStat);
            } else if ("中单".equals(teamBPlayerStat.getPosition())) {
                BCenterRoadBeanList.add(teamBPlayerStat);
            } else if ("上单".equals(teamBPlayerStat.getPosition())) {
                BUpperRoadBeanList.add(teamBPlayerStat);
            } else if ("ADC".equals(teamBPlayerStat.getPosition())) {
                BADCBeanList.add(teamBPlayerStat);
            } else if ("辅助".equals(teamBPlayerStat.getPosition())) {
                BAuxiliaryBeanList.add(teamBPlayerStat);
            }
        }
    }

    /***
     * 设置玩家信息
     * @param aPlayer a玩家
     * @param bPlayer b玩家
     */
    private void setDetailData(LiveGamePlayerBean.TeamPlayerStatsBean aPlayer, LiveGamePlayerBean.TeamPlayerStatsBean bPlayer) {
        if (aPlayer != null && bPlayer != null) {

            setImage(ATeamPlayerIcon, aPlayer.getAvatar(), false);
            setImage(BTeamPlayerIcon, bPlayer.getAvatar(), false);
            ATeamPlayerName.setText(aPlayer.getPlayer_name());
            BTeamPlayerName.setText(bPlayer.getPlayer_name());
            ATeamPlayerWinningProbability.setText(String.format("%s%%", StringUtil.formatDouble2(aPlayer.getWin_rate() * 100)));
            BTeamPlayerWinningProbability.setText(String.format("%s%%", StringUtil.formatDouble2(bPlayer.getWin_rate() * 100)));
            mCPView.setProgress((int) (bPlayer.getWin_rate() / (aPlayer.getWin_rate() + bPlayer.getWin_rate()) * 100));
            mCPView.setBackColor(R.color.live_EE9E00);

            ATeamPlayerKDANum.setText(MessageFormat.format("{0}", aPlayer.getKda()));
            BTeamPlayerKDANum.setText(MessageFormat.format("{0}", bPlayer.getKda()));

            LinearLayout.LayoutParams aTeamPlayerKDAViewLayoutParams = (LinearLayout.LayoutParams) ATeamPlayerKDAView.getLayoutParams();
            aTeamPlayerKDAViewLayoutParams.weight = (float) aPlayer.getKda();
            ATeamPlayerKDAView.setLayoutParams(aTeamPlayerKDAViewLayoutParams);
            ATeamPlayerKDAView.setBackgroundResource(R.drawable.bg_live_battle_line_color_left);
            LinearLayout.LayoutParams bTeamPlayerKDAViewLayoutParams = (LinearLayout.LayoutParams) BTeamPlayerKDAView.getLayoutParams();
            bTeamPlayerKDAViewLayoutParams.weight = (float) bPlayer.getKda();
            BTeamPlayerKDAView.setLayoutParams(bTeamPlayerKDAViewLayoutParams);
            BTeamPlayerKDAView.setBackgroundResource(R.drawable.bg_live_battle_line_color_right);

            ATeamParticipationRate.setText(String.format("%s%%", StringUtil.formatDouble2(aPlayer.getOffered_rate() * 100)));
            BTeamParticipationRate.setText(String.format("%s%%", StringUtil.formatDouble2(bPlayer.getOffered_rate() * 100)));
            LinearLayout.LayoutParams aTeamParticipationRateViewLayoutParams = (LinearLayout.LayoutParams) ATeamParticipationRateView.getLayoutParams();
            aTeamParticipationRateViewLayoutParams.weight = (float) aPlayer.getOffered_rate();
            ATeamParticipationRateView.setLayoutParams(aTeamParticipationRateViewLayoutParams);
            ATeamParticipationRateView.setBackgroundResource(R.drawable.bg_live_battle_line_color_left);
            LinearLayout.LayoutParams bTeamParticipationRateViewLayoutParams = (LinearLayout.LayoutParams) BTeamParticipationRateView.getLayoutParams();
            bTeamParticipationRateViewLayoutParams.weight = (float) bPlayer.getOffered_rate();
            BTeamParticipationRateView.setLayoutParams(bTeamParticipationRateViewLayoutParams);
            BTeamParticipationRateView.setBackgroundResource(R.drawable.bg_live_battle_line_color_right);

            ATeamPlayerOutput.setText(MessageFormat.format("{0}", aPlayer.getDamage_per_min()));
            BTeamPlayerOutput.setText(MessageFormat.format("{0}", bPlayer.getDamage_per_min()));
            LinearLayout.LayoutParams aTeamPlayerOutputViewLayoutParams = (LinearLayout.LayoutParams) ATeamPlayerOutputView.getLayoutParams();
            aTeamPlayerOutputViewLayoutParams.weight = (float) aPlayer.getDamage_per_min();
            ATeamPlayerOutputView.setLayoutParams(aTeamPlayerOutputViewLayoutParams);
            ATeamPlayerOutputView.setBackgroundResource(R.drawable.bg_live_battle_line_color_left);
            LinearLayout.LayoutParams bTeamPlayerOutputViewLayoutParams = (LinearLayout.LayoutParams) BTeamPlayerOutputView.getLayoutParams();
            bTeamPlayerOutputViewLayoutParams.weight = (float) bPlayer.getDamage_per_min();
            BTeamPlayerOutputView.setLayoutParams(bTeamPlayerOutputViewLayoutParams);
            BTeamPlayerOutputView.setBackgroundResource(R.drawable.bg_live_battle_line_color_right);


            ATeamPlayerEconomics.setText(MessageFormat.format("{0}", aPlayer.getGolds_per_min()));
            BTeamPlayerEconomics.setText(MessageFormat.format("{0}", bPlayer.getGolds_per_min()));
            LinearLayout.LayoutParams aTeamPlayerEconomicsViewLayoutParams = (LinearLayout.LayoutParams) ATeamPlayerEconomicsView.getLayoutParams();
            aTeamPlayerEconomicsViewLayoutParams.weight = (float) aPlayer.getGolds_per_min();
            ATeamPlayerEconomicsView.setLayoutParams(aTeamPlayerEconomicsViewLayoutParams);
            ATeamPlayerEconomicsView.setBackgroundResource(R.drawable.bg_live_battle_line_color_left);
            LinearLayout.LayoutParams bTeamPlayerEconomicsViewLayoutParams = (LinearLayout.LayoutParams) BTeamPlayerEconomicsView.getLayoutParams();
            bTeamPlayerEconomicsViewLayoutParams.weight = (float) bPlayer.getGolds_per_min();
            BTeamPlayerEconomicsView.setLayoutParams(bTeamPlayerEconomicsViewLayoutParams);
            BTeamPlayerEconomicsView.setBackgroundResource(R.drawable.bg_live_battle_line_color_right);


            List<LiveGamePlayerBean.TeamPlayerStatsBean.HeroPickStatsBeanX> hero_pick_stats_a = aPlayer.getHero_pick_stats();
            for (int i = 0; i < hero_pick_stats_a.size(); i++) {
                LiveGamePlayerBean.TeamPlayerStatsBean.HeroPickStatsBeanX heroPickStatsBeanX = hero_pick_stats_a.get(i);
                if (i == 0) {
                    setImage(ATeamHeroOneIcon, heroPickStatsBeanX.getHero_logo(), false);
                    ATeamHeroNumOne.setText(MessageFormat.format("{0}局", heroPickStatsBeanX.getHero_game_count()));
                    ATeamHeroWinningOne.setText(MessageFormat.format("{0}%", getWinningProbability(heroPickStatsBeanX.getHero_win_count(), heroPickStatsBeanX.getHero_game_count())));
                } else if (i == 1) {
                    setImage(ATeamHeroTwoIcon, heroPickStatsBeanX.getHero_logo(), false);
                    ATeamHeroNumTwo.setText(MessageFormat.format("{0}局", heroPickStatsBeanX.getHero_game_count()));
                    ATeamHeroWinningTwo.setText(MessageFormat.format("{0}%", getWinningProbability(heroPickStatsBeanX.getHero_win_count(), heroPickStatsBeanX.getHero_game_count())));
                } else if (i == 2) {
                    setImage(ATeamHeroThreeIcon, heroPickStatsBeanX.getHero_logo(), false);
                    ATeamHeroNumThree.setText(MessageFormat.format("{0}局", heroPickStatsBeanX.getHero_game_count()));
                    ATeamHeroWinningThree.setText(MessageFormat.format("{0}%", getWinningProbability(heroPickStatsBeanX.getHero_win_count(), heroPickStatsBeanX.getHero_game_count())));
                }
            }

            List<LiveGamePlayerBean.TeamPlayerStatsBean.HeroPickStatsBeanX> hero_pick_stats_b = bPlayer.getHero_pick_stats();
            for (int i = 0; i < hero_pick_stats_b.size(); i++) {
                LiveGamePlayerBean.TeamPlayerStatsBean.HeroPickStatsBeanX heroPickStatsBeanX = hero_pick_stats_b.get(i);
                if (i == 0) {
                    setImage(BTeamHeroOneIcon, heroPickStatsBeanX.getHero_logo(), false);
                    BTeamHeroNumOne.setText(MessageFormat.format("{0}局", heroPickStatsBeanX.getHero_game_count()));
                    BTeamHeroWinningOne.setText(MessageFormat.format("{0}%", getWinningProbability(heroPickStatsBeanX.getHero_win_count(), heroPickStatsBeanX.getHero_game_count())));
                } else if (i == 1) {
                    setImage(BTeamHeroTwoIcon, heroPickStatsBeanX.getHero_logo(), false);
                    BTeamHeroNumTwo.setText(MessageFormat.format("{0}局", heroPickStatsBeanX.getHero_game_count()));
                    BTeamHeroWinningTwo.setText(MessageFormat.format("{0}%", getWinningProbability(heroPickStatsBeanX.getHero_win_count(), heroPickStatsBeanX.getHero_game_count())));
                } else if (i == 2) {
                    setImage(BTeamHeroThreeIcon, heroPickStatsBeanX.getHero_logo(), false);
                    BTeamHeroNumThree.setText(MessageFormat.format("{0}局", heroPickStatsBeanX.getHero_game_count()));
                    BTeamHeroWinningThree.setText(MessageFormat.format("{0}%", getWinningProbability(heroPickStatsBeanX.getHero_win_count(), heroPickStatsBeanX.getHero_game_count())));
                }
            }
        } else {

            setImage(ATeamPlayerIcon, "", false);
            setImage(BTeamPlayerIcon, "", false);
            ATeamPlayerName.setText("");
            BTeamPlayerName.setText("");
            ATeamPlayerWinningProbability.setText("0%");
            BTeamPlayerWinningProbability.setText("0%");
            mCPView.setProgress(0);
            mCPView.setBackColor(R.color.live_gary);

            ATeamPlayerKDANum.setText("0");
            BTeamPlayerKDANum.setText("0");

            ATeamPlayerKDAView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));
            BTeamPlayerKDAView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));

            ATeamParticipationRate.setText("0");
            BTeamParticipationRate.setText("0");
            ATeamParticipationRateView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));
            BTeamParticipationRateView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));


            ATeamPlayerOutput.setText("0");
            BTeamPlayerOutput.setText("0");
            ATeamPlayerOutputView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));
            BTeamPlayerOutputView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));

            ATeamPlayerEconomics.setText("0");
            BTeamPlayerEconomics.setText("0");
            ATeamPlayerEconomicsView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));
            BTeamPlayerEconomicsView.setBackgroundColor(mContext.getResources().getColor(R.color.live_f7f7f7));

            setImage(ATeamHeroOneIcon, "", false);
            ATeamHeroNumOne.setText("");
            ATeamHeroWinningOne.setText("0%");
            setImage(ATeamHeroTwoIcon, "", false);
            ATeamHeroNumTwo.setText("");
            ATeamHeroWinningTwo.setText("0%");
            setImage(ATeamHeroThreeIcon, "", false);
            ATeamHeroNumThree.setText("");
            ATeamHeroWinningThree.setText("0%");

            setImage(BTeamHeroOneIcon, "", false);
            BTeamHeroNumOne.setText("");
            BTeamHeroWinningOne.setText("0%");
            setImage(BTeamHeroTwoIcon, "", false);
            BTeamHeroNumTwo.setText("");
            BTeamHeroWinningTwo.setText("0%");
            setImage(BTeamHeroThreeIcon, "", false);
            BTeamHeroNumThree.setText("");
            BTeamHeroWinningThree.setText("0%");
        }
    }

    private String getWinningProbability(int numberOne, int numberTwo) {
        DecimalFormat df = new DecimalFormat("0");//格式化小数，不足的补0
        return df.format((double) numberOne / numberTwo * 100);
    }

    private void setTeamData(LiveGamePlayerBean liveGamePlayerBean) {
        setImage(ATeamIcon, liveGamePlayerBean.getTeam_a_logo(), false);
        setImage(BTeamIcon, liveGamePlayerBean.getTeam_b_logo(), false);
        ATeamName.setText(liveGamePlayerBean.getTeam_a_name());
        BTeamName.setText(liveGamePlayerBean.getTeam_b_name());
    }

    private void initView() {
        TabUpper = mContentView.findViewById(R.id.live_tv_upper_road);
        TabFightingWild = mContentView.findViewById(R.id.live_tv_fighting_wild);
        TabCenterRoad = mContentView.findViewById(R.id.live_tv_center_road);
        TabAdc = mContentView.findViewById(R.id.live_tv_adc);
        TabAuxiliary = mContentView.findViewById(R.id.live_tv_auxiliary);
        TabUpper.setOnClickListener(this);
        TabFightingWild.setOnClickListener(this);
        TabCenterRoad.setOnClickListener(this);
        TabAdc.setOnClickListener(this);
        TabAuxiliary.setOnClickListener(this);

        ATeamIcon = mContentView.findViewById(R.id.live_iv_team_a_icon);
        BTeamIcon = mContentView.findViewById(R.id.live_iv_team_b_icon);
        ATeamName = mContentView.findViewById(R.id.live_tv_team_a_name);
        BTeamName = mContentView.findViewById(R.id.live_tv_team_b_name);

        ATeamAlternateOne = mContentView.findViewById(R.id.live_iv_team_alternate_one_a);
        ATeamAlternateTwo = mContentView.findViewById(R.id.live_iv_team_alternate_two_a);
        ATeamAlternateThree = mContentView.findViewById(R.id.live_iv_team_alternate_three_a);
        ATeamAlternateOne.setOnClickListener(this);
        ATeamAlternateTwo.setOnClickListener(this);
        ATeamAlternateThree.setOnClickListener(this);

        BTeamAlternateOne = mContentView.findViewById(R.id.live_iv_team_alternate_one_b);
        BTeamAlternateTwo = mContentView.findViewById(R.id.live_iv_team_alternate_two_b);
        BTeamAlternateThree = mContentView.findViewById(R.id.live_iv_team_alternate_three_b);
        BTeamAlternateOne.setOnClickListener(this);
        BTeamAlternateTwo.setOnClickListener(this);
        BTeamAlternateThree.setOnClickListener(this);

        ATeamPlayerIcon = mContentView.findViewById(R.id.live_iv_player_one_avatar);
        BTeamPlayerIcon = mContentView.findViewById(R.id.live_iv_player_two_icon);
        ATeamPlayerName = mContentView.findViewById(R.id.live_tv_player_a_name);
        BTeamPlayerName = mContentView.findViewById(R.id.live_tv_player_b_name);
        ATeamPlayerWinningProbability = mContentView.findViewById(R.id.live_tv_a_winning_probability);
        BTeamPlayerWinningProbability = mContentView.findViewById(R.id.live_tv_b_winning_probability);
        mCPView = mContentView.findViewById(R.id.live_cp_view);
        ATeamPlayerKDANum = mContentView.findViewById(R.id.live_tv_kda_a_num);
        BTeamPlayerKDANum = mContentView.findViewById(R.id.live_tv_kda_b_num);
        ATeamPlayerKDAView = mContentView.findViewById(R.id.live_view_kda_a);
        BTeamPlayerKDAView = mContentView.findViewById(R.id.live_view_kda_b);

        ATeamParticipationRate = mContentView.findViewById(R.id.live_tv_a_participation_rate_num);
        BTeamParticipationRate = mContentView.findViewById(R.id.live_tv_b_participation_rate_num);
        ATeamParticipationRateView = mContentView.findViewById(R.id.live_view_a_participation_rate);
        BTeamParticipationRateView = mContentView.findViewById(R.id.live_view_b_participation_rate);

        ATeamPlayerOutput = mContentView.findViewById(R.id.live_tv_output_num_a);
        BTeamPlayerOutput = mContentView.findViewById(R.id.live_tv_output_num_b);
        ATeamPlayerOutputView = mContentView.findViewById(R.id.live_view_output_a);
        BTeamPlayerOutputView = mContentView.findViewById(R.id.live_view_output_b);

        ATeamPlayerEconomics = mContentView.findViewById(R.id.live_tv_economics_a);
        BTeamPlayerEconomics = mContentView.findViewById(R.id.live_tv_economics_b);
        ATeamPlayerEconomicsView = mContentView.findViewById(R.id.live_view_economics_a);
        BTeamPlayerEconomicsView = mContentView.findViewById(R.id.live_view_economics_b);

        ATeamHeroOneIcon = mContentView.findViewById(R.id.live_iv_hero_one_a);
        ATeamHeroNumOne = mContentView.findViewById(R.id.live_tv_hero_one_num_a);
        ATeamHeroWinningOne = mContentView.findViewById(R.id.live_tv_hero_one_winning_a);
        ATeamHeroTwoIcon = mContentView.findViewById(R.id.live_iv_hero_two_a);
        ATeamHeroNumTwo = mContentView.findViewById(R.id.live_tv_hero_two_num_a);
        ATeamHeroWinningTwo = mContentView.findViewById(R.id.live_tv_hero_two_winning_a);
        ATeamHeroThreeIcon = mContentView.findViewById(R.id.live_iv_hero_three_a);
        ATeamHeroNumThree = mContentView.findViewById(R.id.live_tv_hero_three_num_a);
        ATeamHeroWinningThree = mContentView.findViewById(R.id.live_tv_hero_three_winning_a);

        BTeamHeroOneIcon = mContentView.findViewById(R.id.live_iv_hero_one_b);
        BTeamHeroNumOne = mContentView.findViewById(R.id.live_tv_hero_one_num_b);
        BTeamHeroWinningOne = mContentView.findViewById(R.id.live_tv_hero_one_winning_b);
        BTeamHeroTwoIcon = mContentView.findViewById(R.id.live_iv_hero_two_b);
        BTeamHeroNumTwo = mContentView.findViewById(R.id.live_tv_hero_two_num_b);
        BTeamHeroWinningTwo = mContentView.findViewById(R.id.live_tv_hero_two_winning_b);
        BTeamHeroThreeIcon = mContentView.findViewById(R.id.live_iv_hero_three_b);
        BTeamHeroNumThree = mContentView.findViewById(R.id.live_tv_hero_three_num_b);
        BTeamHeroWinningThree = mContentView.findViewById(R.id.live_tv_hero_three_winning_b);
    }

    private void setImage(ImageView iv, String ivUrl, boolean isPerson) {
        Glide.with(mContext)
                .load((TextUtils.isEmpty(ivUrl)) ? (isPerson ? R.mipmap.live_player_alternate_avatar : R.mipmap.icon_default_logo) : ivUrl)
                .apply(options)
                .into(iv);
    }

    private void setAlternateData(List<LiveGamePlayerBean.TeamPlayerStatsBean> aBeanList, List<LiveGamePlayerBean.TeamPlayerStatsBean> bBeanList) {
        if (aBeanList != null && bBeanList != null) {

            AChooseBeanList = aBeanList;
            BChooseBeanList = bBeanList;
            if (aBeanList.size() == 0) {
                ATeamAlternateOne.setVisibility(View.GONE);
                ATeamAlternateTwo.setVisibility(View.GONE);
                ATeamAlternateThree.setVisibility(View.GONE);
            } else if (aBeanList.size() == 1) {
                ATeamAlternateOne.setVisibility(View.VISIBLE);
                ATeamAlternateTwo.setVisibility(View.GONE);
                ATeamAlternateThree.setVisibility(View.GONE);
            } else if (aBeanList.size() == 2) {
                ATeamAlternateOne.setVisibility(View.VISIBLE);
                ATeamAlternateTwo.setVisibility(View.VISIBLE);
                ATeamAlternateThree.setVisibility(View.GONE);
            } else if (aBeanList.size() == 3) {
                ATeamAlternateOne.setVisibility(View.VISIBLE);
                ATeamAlternateTwo.setVisibility(View.VISIBLE);
                ATeamAlternateThree.setVisibility(View.VISIBLE);
            }


            if (bBeanList.size() == 0) {
                BTeamAlternateOne.setVisibility(View.GONE);
                BTeamAlternateTwo.setVisibility(View.GONE);
                BTeamAlternateThree.setVisibility(View.GONE);
            } else if (bBeanList.size() == 1) {
                BTeamAlternateOne.setVisibility(View.VISIBLE);
                BTeamAlternateTwo.setVisibility(View.GONE);
                BTeamAlternateThree.setVisibility(View.GONE);
            } else if (bBeanList.size() == 2) {
                BTeamAlternateOne.setVisibility(View.VISIBLE);
                BTeamAlternateTwo.setVisibility(View.VISIBLE);
                BTeamAlternateThree.setVisibility(View.GONE);
            } else if (bBeanList.size() == 3) {
                BTeamAlternateOne.setVisibility(View.VISIBLE);
                BTeamAlternateTwo.setVisibility(View.VISIBLE);
                BTeamAlternateThree.setVisibility(View.VISIBLE);
            }


            for (int i = 0; i < aBeanList.size(); i++) {
                if (i == 0) {
                    setImage(ATeamAlternateOne, aBeanList.get(i).getAvatar(), true);
                }

                if (i == 1) {
                    setImage(ATeamAlternateTwo, aBeanList.get(i).getAvatar(), true);
                }

                if (i == 2) {
                    setImage(ATeamAlternateThree, aBeanList.get(i).getAvatar(), true);
                }

            }

            for (int i = 0; i < bBeanList.size(); i++) {
                if (i == 0) {
                    setImage(BTeamAlternateOne, bBeanList.get(i).getAvatar(), true);
                }

                if (i == 1) {
                    setImage(BTeamAlternateTwo, bBeanList.get(i).getAvatar(), true);
                }

                if (i == 2) {
                    setImage(BTeamAlternateThree, bBeanList.get(i).getAvatar(), true);
                }

            }
        } else {
//            ATeamAlternateOne.setVisibility(View.GONE);
//            ATeamAlternateTwo.setVisibility(View.GONE);
//            ATeamAlternateThree.setVisibility(View.GONE);
//            BTeamAlternateOne.setVisibility(View.GONE);
//            BTeamAlternateTwo.setVisibility(View.GONE);
//            BTeamAlternateThree.setVisibility(View.GONE);

            setImage(ATeamAlternateOne, "", true);
            setImage(ATeamAlternateTwo, "", true);
            setImage(ATeamAlternateThree, "", true);
            setImage(BTeamAlternateOne, "", true);
            setImage(BTeamAlternateTwo, "", true);
            setImage(BTeamAlternateThree, "", true);
        }
    }

    private void setDefault() {
        TabUpper.setBackgroundResource(R.drawable.bg_btn_common_02);
        TabUpper.setTextColor(mContentView.getResources().getColor(R.color.white));
        TabCenterRoad.setBackground(null);
        TabCenterRoad.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
        TabAdc.setBackground(null);
        TabAdc.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
        TabFightingWild.setBackground(null);
        TabFightingWild.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
        TabAuxiliary.setBackground(null);
        TabAuxiliary.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));

        if (AUpperRoadBeanList.size() > 0 && BUpperRoadBeanList.size() > 0) {
            AChooseBean = AUpperRoadBeanList.get(0);
            BChooseBean = BUpperRoadBeanList.get(0);
            setDetailData(AChooseBean, BChooseBean);
            setAlternateData(AUpperRoadBeanList, BUpperRoadBeanList);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        try {
            if (id == R.id.live_tv_upper_road) {
                index = 0;
                setDefault();
            } else if (id == R.id.live_tv_center_road) {
                index = 1;
                TabUpper.setBackground(null);
                TabUpper.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabCenterRoad.setBackgroundResource(R.drawable.bg_btn_common_02);
                TabCenterRoad.setTextColor(mContentView.getResources().getColor(R.color.white));
                TabAdc.setBackground(null);
                TabAdc.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabFightingWild.setBackground(null);
                TabFightingWild.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabAuxiliary.setBackground(null);
                TabAuxiliary.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));

                if (ACenterRoadBeanList.size() > 0 && BCenterRoadBeanList.size() > 0) {
                    AChooseBean = ACenterRoadBeanList.get(0);
                    BChooseBean = BCenterRoadBeanList.get(0);
                    setDetailData(AChooseBean, BChooseBean);
                    setAlternateData(ACenterRoadBeanList, BCenterRoadBeanList);
                }
            } else if (id == R.id.live_tv_fighting_wild) {
                index = 2;
                TabUpper.setBackground(null);
                TabUpper.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabCenterRoad.setBackground(null);
                TabCenterRoad.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabAdc.setBackground(null);
                TabAdc.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabFightingWild.setBackgroundResource(R.drawable.bg_btn_common_02);
                TabFightingWild.setTextColor(mContentView.getResources().getColor(R.color.white));
                TabAuxiliary.setBackground(null);
                TabAuxiliary.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));

                if (AFightingWildBeanList.size() > 0 && BFightingWildBeanList.size() > 0) {
                    AppLog.e("打野", "有数据");
                    AChooseBean = AFightingWildBeanList.get(0);
                    BChooseBean = BFightingWildBeanList.get(0);
                    setDetailData(AChooseBean, BChooseBean);
                    setAlternateData(AFightingWildBeanList, BFightingWildBeanList);
                } else {
                    //TODO: 2020/10/27  打野无数据
                    AppLog.e("打野", "打野无数据");
                    setDetailData(null, null);
                    setAlternateData(null, null);
                }
            } else if (id == R.id.live_tv_adc) {
                index = 3;
                TabUpper.setBackground(null);
                TabUpper.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabCenterRoad.setBackground(null);
                TabCenterRoad.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabAdc.setBackgroundResource(R.drawable.bg_btn_common_02);
                TabAdc.setTextColor(mContentView.getResources().getColor(R.color.white));
                TabFightingWild.setBackground(null);
                TabFightingWild.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabAuxiliary.setBackground(null);
                TabAuxiliary.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));

                if (AADCBeanList.size() > 0 && BADCBeanList.size() > 0) {
                    AChooseBean = AADCBeanList.get(0);
                    BChooseBean = BADCBeanList.get(0);
                    setDetailData(AChooseBean, BChooseBean);
                    setAlternateData(AADCBeanList, BADCBeanList);
                }
            } else if (id == R.id.live_tv_auxiliary) {
                index = 4;
                TabUpper.setBackground(null);
                TabUpper.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabCenterRoad.setBackground(null);
                TabCenterRoad.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabAdc.setBackground(null);
                TabAdc.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabFightingWild.setBackground(null);
                TabFightingWild.setTextColor(mContentView.getResources().getColor(R.color.live_ff5116));
                TabAuxiliary.setBackgroundResource(R.drawable.bg_btn_common_02);
                TabAuxiliary.setTextColor(mContentView.getResources().getColor(R.color.white));

                if (AAuxiliaryBeanList.size() > 0 && BAuxiliaryBeanList.size() > 0) {
                    AChooseBean = AAuxiliaryBeanList.get(0);
                    BChooseBean = BAuxiliaryBeanList.get(0);
                    setDetailData(AChooseBean, BChooseBean);
                    setAlternateData(AAuxiliaryBeanList, BAuxiliaryBeanList);
                }
            } else if (id == R.id.live_iv_team_alternate_one_a) {
                setDetailData(AChooseBeanList.get(0), BChooseBean);
            } else if (id == R.id.live_iv_team_alternate_two_a) {
                setDetailData(AChooseBeanList.get(1), BChooseBean);
            } else if (id == R.id.live_iv_team_alternate_three_a) {
                setDetailData(AChooseBeanList.get(2), BChooseBean);
            } else if (id == R.id.live_iv_team_alternate_one_b) {
                setDetailData(AChooseBean, BChooseBeanList.get(0));
            } else if (id == R.id.live_iv_team_alternate_two_b) {
                setDetailData(AChooseBean, BChooseBeanList.get(1));
            } else if (id == R.id.live_iv_team_alternate_three_b) {
                setDetailData(AChooseBean, BChooseBeanList.get(2));
            }
        } catch (Exception e) {
            AppLog.e("", e.toString());
        }
    }
}
