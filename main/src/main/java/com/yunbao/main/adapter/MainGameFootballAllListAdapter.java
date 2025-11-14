package com.yunbao.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.League;
import com.yunbao.common.bean.MatchBean;
import com.yunbao.common.bean.MatchListBean;
import com.yunbao.common.bean.Team;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.live.activity.LiveSportsActivity;
import com.yunbao.live.bean.FootballLive;
import com.yunbao.main.R;

import java.util.ArrayList;
import java.util.List;


public class MainGameFootballAllListAdapter extends GroupedRecyclerViewAdapter {

    private Context mContext;
    private Gson gson = new Gson();
    private List<MatchListBean> matchListBeanList;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);

    public MainGameFootballAllListAdapter(Context context, ArrayList<MatchListBean> groups) {
        super(context);
        this.mContext = context;
        if (groups == null) {
            matchListBeanList = new ArrayList<>();
        } else {
            matchListBeanList = groups;
        }
        this.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                if (!ClickUtil.canClick()) return;
                Intent intent = new Intent(mContext, LiveSportsActivity.class);
                intent.putExtra(Constants.LIVE_MATCH_FOOTBALL_INFO, matchListBeanList.get(groupPosition).getList().get(childPosition));
                mContext.startActivity(intent);
            }
        });
    }

    public void clear() {
        if (matchListBeanList != null) {
            matchListBeanList.clear();
        }
        notifyDataChanged();
    }

    public void setGroups(List<MatchListBean> groups) {
        if (matchListBeanList != null) {
            this.matchListBeanList = groups;
            notifyDataChanged();
        }
    }

    public void addGroups(List<MatchListBean> groups) {
        if (this.matchListBeanList != null && groups != null && !groups.isEmpty()) {
            for (int i = 0; i < groups.size(); i++) {
                if (groups.get(i).getWeek_day_str().equals(matchListBeanList.get(matchListBeanList.size() - 1).getWeek_day_str())) {
                    matchListBeanList.get(matchListBeanList.size() - 1).getList().addAll(groups.get(i).getList());
                } else {
                    matchListBeanList.add(groups.get(i));
                }
            }
            notifyDataChanged();
        }
    }

    @Override
    public int getGroupCount() {
        //返回组的数量
        return matchListBeanList == null ? 0 : matchListBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //返回当前组的子项数量
        return matchListBeanList == null ? 0 : matchListBeanList.get(groupPosition).getList().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        //当前组是否有头部
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        //当前组是否有尾部
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        //返回头部的布局id。(如果hasHeader返回false，这个方法不会执行)
        return R.layout.layout_sticky_header_view;
    }

    @Override
    public int getFooterLayout(int viewType) {
        //返回尾部的布局id。(如果hasFooter返回false，这个方法不会执行)
        return R.layout.layout_sticky_header_view;
    }

    @Override
    public int getChildLayout(int viewType) {
        //返回子项的布局id。
        return R.layout.layout_game_football_list_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        //绑定头部布局数据。(如果hasHeader返回false，这个方法不会执行)
        MatchListBean gameFootballMatchBean = matchListBeanList.get(groupPosition);
        holder.setText(R.id.tv_sticky_header_view, gameFootballMatchBean.getWeek_day_str());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        //绑定尾部布局数据。(如果hasFooter返回false，这个方法不会执行)
//        GameFootballAllListBean gameFootballMatchBean = gameFootballAllListBeanList.get(groupPosition);
//        holder.setText(R.id.tv_footer, gameFootballMatchBean.getFooter());
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        //绑定子项布局数据。
        try {
            MatchBean gameFootballMatchBean = matchListBeanList.get(groupPosition).getList().get(childPosition);

            holder.setText(R.id.tv_time, TimeUtil.timeToHour(gameFootballMatchBean.getMatchTime() * 1000));
            League league = gameFootballMatchBean.getLeague();
            String leagueName = null;
            if (league != null) {
                leagueName = TextUtils.isEmpty(league.getShortNameZh()) ? league.getNameZh() : league.getShortNameZh();
            }
            holder.setText(R.id.tv_game_name, gameFootballMatchBean.getLeague() == null ? "" : leagueName);

            holder.setText(R.id.tv_a_score, TextUtils.isEmpty(gameFootballMatchBean.getHomeScoreStr()) ? getScore(gameFootballMatchBean.getHomeScores()) : gameFootballMatchBean.getHomeScoreStr());
            holder.setText(R.id.tv_b_score, TextUtils.isEmpty(gameFootballMatchBean.getAwayScoreStr()) ? getScore(gameFootballMatchBean.getAwayScores()) : gameFootballMatchBean.getAwayScoreStr());
//          1未开赛，2比赛中，3已完赛
            if (gameFootballMatchBean.getIsPlaying() == 2) {
                holder.setText(R.id.tv_game_des, gameFootballMatchBean.getState_str());
                holder.setTextColor(R.id.tv_game_des, mContext.getResources().getColor(R.color.red1));
                holder.setBackgroundRes(R.id.ll_bg_play_status, R.drawable.bg_shape_rounded_corners_red_normal);
                holder.setText(R.id.tv_play_status, R.string.is_playing);
                holder.setTextColor(R.id.tv_game_name, mContext.getResources().getColor(R.color.red1));
                holder.setImageResource(R.id.iv_play_status, R.mipmap.icon_ball_playing);
                holder.setImageResource(R.id.iv_ball, R.mipmap.icon_football_ing);
                holder.setTextColor(R.id.tv_a_score, mContext.getResources().getColor(R.color.red1));
                holder.setTextColor(R.id.tv_b_score, mContext.getResources().getColor(R.color.red1));
            } else if (gameFootballMatchBean.getIsPlaying() == 3) {
                holder.setText(R.id.tv_game_des, "-");
                holder.setTextColor(R.id.tv_game_des, mContext.getResources().getColor(R.color.black2));
                holder.setImageResource(R.id.iv_ball, R.mipmap.icon_football_gray);
                holder.setTextColor(R.id.tv_a_score, mContext.getResources().getColor(R.color.gray1));
                holder.setTextColor(R.id.tv_b_score, mContext.getResources().getColor(R.color.gray1));
                holder.setBackgroundRes(R.id.ll_bg_play_status, R.drawable.bg_shape_rounded_corners_gray_light);
                holder.setText(R.id.tv_play_status, R.string.game_over);
                holder.setTextColor(R.id.tv_game_name, mContext.getResources().getColor(R.color.black2));
                holder.setImageResource(R.id.iv_play_status, R.mipmap.icon_ball_over);
            } else {
                holder.setText(R.id.tv_game_des, "-");
                holder.setTextColor(R.id.tv_game_des, mContext.getResources().getColor(R.color.black2));
                holder.setImageResource(R.id.iv_ball, R.mipmap.icon_football_gray);
                holder.setTextColor(R.id.tv_a_score, mContext.getResources().getColor(R.color.gray1));
                holder.setTextColor(R.id.tv_b_score, mContext.getResources().getColor(R.color.gray1));
                holder.setBackgroundRes(R.id.ll_bg_play_status, R.drawable.bg_shape_rounded_corners_gray);
                holder.setText(R.id.tv_play_status, R.string.game_not_start);
                holder.setTextColor(R.id.tv_game_name, mContext.getResources().getColor(R.color.black2));
                holder.setImageResource(R.id.iv_play_status, R.mipmap.icon_ball_not_start);
            }

            Team home_team = gameFootballMatchBean.getHome_team();
            Team away_team = gameFootballMatchBean.getAway_team();
            holder.setText(R.id.tv_team_a_name, home_team == null ? "" : home_team.getNameZh());
            holder.setText(R.id.tv_team_b_name, away_team == null ? "" : away_team.getNameZh());
            ImageView viewA = holder.get(R.id.iv_team_a);
            ImageView viewB = holder.get(R.id.iv_team_b);
            Glide.with(mContext)
                    .load(home_team == null ? R.mipmap.icon_default_logo : home_team.getLogo())
                    .apply(options)
                    .into(viewA);
            Glide.with(mContext)
                    .load(away_team == null ? R.mipmap.icon_default_logo : away_team.getLogo())
                    .apply(options)
                    .into(viewB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFootballListDataRefresh(List<FootballLive> list) {
        try {
            if (list != null && !list.isEmpty()
                    && matchListBeanList != null && !matchListBeanList.isEmpty() && getGroupCount() > 0) {
                AppLog.e("赛事列表收到通知：刷新足球全部列表");
                for (int i = 0; i < matchListBeanList.size(); i++) {
                    for (int j = 0; j < matchListBeanList.get(i).getList().size(); j++) {
                        for (int k = 0; k < list.size(); k++) {
                            FootballLive bean = list.get(k);
                            if (bean.getId() == matchListBeanList.get(i).getList().get(j).getId()) {
                                MatchBean listMatchBean = matchListBeanList.get(i).getList().get(j);
                                AppLog.e("全部足球有相同的id：" + bean.getId() + ", " + listMatchBean.getHome_team().getNameZh());
                                getGameStatus(listMatchBean, bean.getScore());
                            }
                        }
                    }
                }
                notifyDataChanged();
            }
        } catch (Exception e) {
            AppLog.e(e.toString());
        }
    }

    private void getGameStatus(MatchBean listMatchBean, JsonArray array) {
        if (array.size() >= 5) {
            int status = array.get(1).getAsInt();
            int playing = 0;
            if (status >= 2 && status < 8) {
                playing = 2;
            } else if (status == 8) {
                playing = 3;
            } else {
                playing = 1;
            }
            long duration = System.currentTimeMillis() - array.get(4).getAsLong() * 1000;
            listMatchBean.setHomeScores(array.get(2).getAsJsonArray().toString());
            listMatchBean.setAwayScores(array.get(3).getAsJsonArray().toString());
            listMatchBean.setIsPlaying(playing);
            listMatchBean.setState_str(getStatusStr(status, duration));
        }
    }

    private String getStatusStr(int status, long duration) {
        String str = "";
        switch (status) {
            case 1:
                str = "未开赛";
                break;
            case 2:
                str = "上半场";
                break;
            case 3:
                str = "中场";
                break;
            case 4:
                str = "下半场";
                break;
            case 5:
                str = "加时赛";
                break;
            case 7:
                str = "点球决战";
                break;
            case 8:
                str = "已完赛";
                break;
            case 9:
                str = "推迟";
                break;
            case 10:
                str = "中断";
                break;
            case 11:
                str = "腰斩";
                break;
            case 12:
                str = "取消";
                break;
            case 13:
                str = "待定";
                break;
            default:
                str = "比赛异常";
        }
        return str;
    }

    private String getScore(String scoreList) {
        if (TextUtils.isEmpty(scoreList)) {
            return "0";
        }

        JsonArray array = gson.fromJson(scoreList, JsonArray.class);
        int score = 0;
        try {
            score = array.get(0).getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(score);
    }
}