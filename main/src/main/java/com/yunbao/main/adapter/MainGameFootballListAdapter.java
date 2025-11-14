package com.yunbao.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.MatchBean;
import com.yunbao.common.bean.Team;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.live.activity.LiveSportsActivity;
import com.yunbao.main.R;

import java.util.List;


public class MainGameFootballListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Gson gson = new Gson();
    private Context mContext;
    private List<MatchBean> gameFootBallMatchBeanList;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);


    public MainGameFootballListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_game_football_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
            recyclerViewHolder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return gameFootBallMatchBeanList == null ? 0 : gameFootBallMatchBeanList.size();
    }

    public void setListData(List<MatchBean> gameFootBallMatchBeanList) {
        this.gameFootBallMatchBeanList = gameFootBallMatchBeanList;
        notifyDataSetChanged();
    }

    public void addListData(List<MatchBean> gameFootBallMatchBeanList) {
        if (this.gameFootBallMatchBeanList == null) {
            this.gameFootBallMatchBeanList = gameFootBallMatchBeanList;
        } else {
            this.gameFootBallMatchBeanList.addAll(gameFootBallMatchBeanList);
        }
        notifyDataSetChanged();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_time;
        private TextView tv_game_name;
        private TextView tv_game_wheel;
        private TextView tv_a_score;
        private TextView tv_b_score;
        private TextView tv_game_des;
        private ImageView iv_team_a;
        private ImageView iv_team_b;
        private TextView tv_team_a_name;
        private TextView tv_team_b_name;
        private LinearLayout ll_bg_play_status;
        private TextView tv_play_status;
        private ImageView iv_play_status;
        private ImageView iv_ball;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_game_name = itemView.findViewById(R.id.tv_game_name);
            tv_game_wheel = itemView.findViewById(R.id.tv_game_wheel);
            tv_a_score = itemView.findViewById(R.id.tv_a_score);
            tv_b_score = itemView.findViewById(R.id.tv_b_score);
            tv_game_des = itemView.findViewById(R.id.tv_game_des);
            iv_team_a = itemView.findViewById(R.id.iv_team_a);
            iv_team_b = itemView.findViewById(R.id.iv_team_b);
            tv_team_a_name = itemView.findViewById(R.id.tv_team_a_name);
            tv_team_b_name = itemView.findViewById(R.id.tv_team_b_name);
            ll_bg_play_status = itemView.findViewById(R.id.ll_bg_play_status);
            tv_play_status = itemView.findViewById(R.id.tv_play_status);
            iv_play_status = itemView.findViewById(R.id.iv_play_status);
            iv_ball = itemView.findViewById(R.id.iv_ball);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            itemView.setTag(position);
            try {
                if (gameFootBallMatchBeanList != null && !gameFootBallMatchBeanList.isEmpty()) {
                    MatchBean listBean = gameFootBallMatchBeanList.get(position);
                    tv_time.setText(TimeUtil.timeToHour(listBean.getMatchTime() * 1000));
                    tv_game_name.setText(listBean.getLeague() == null ? "" : listBean.getLeague().getNameZh());
//                    tv_game_wheel.setText(gameFootBallMatchBean.get);
                    tv_a_score.setText(getScore(listBean.getHomeScores()));
                    tv_b_score.setText(getScore(listBean.getAwayScores()));
                    //1未开赛，2比赛中，3已完赛
                    if (listBean.getIsPlaying() == 2) {
                        // TODO: 2020/11/5
                        tv_game_des.setText(listBean.getState_str());
                        tv_game_des.setTextColor(mContext.getResources().getColor(R.color.red1));
                        ll_bg_play_status.setBackgroundResource(R.drawable.bg_shape_rounded_corners_red_normal);
                        tv_play_status.setText(R.string.is_playing);
                        tv_game_name.setTextColor(mContext.getResources().getColor(R.color.red1));
                        iv_play_status.setImageResource(R.mipmap.icon_ball_playing);
                        iv_ball.setImageResource(R.mipmap.icon_football_ing);
                        tv_a_score.setTextColor(mContext.getResources().getColor(R.color.red1));
                        tv_b_score.setTextColor(mContext.getResources().getColor(R.color.red1));
                    } else if (listBean.getIsPlaying() == 3) {
                        tv_game_des.setText("-");
                        tv_game_des.setTextColor(mContext.getResources().getColor(R.color.black2));
                        iv_ball.setImageResource(R.mipmap.icon_football_gray);
                        tv_a_score.setTextColor(mContext.getResources().getColor(R.color.gray1));
                        tv_b_score.setTextColor(mContext.getResources().getColor(R.color.gray1));
                        ll_bg_play_status.setBackgroundResource(R.drawable.bg_shape_rounded_corners_gray_light);
                        tv_play_status.setText(R.string.game_over);
                        tv_game_name.setTextColor(mContext.getResources().getColor(R.color.black2));
                        iv_play_status.setImageResource(R.mipmap.icon_ball_over);
                    } else {
                        tv_game_des.setText("-");
                        tv_game_des.setTextColor(mContext.getResources().getColor(R.color.black2));
                        iv_ball.setImageResource(R.mipmap.icon_football_gray);
                        tv_a_score.setTextColor(mContext.getResources().getColor(R.color.gray1));
                        tv_b_score.setTextColor(mContext.getResources().getColor(R.color.gray1));
                        ll_bg_play_status.setBackgroundResource(R.drawable.bg_shape_rounded_corners_gray);
                        tv_play_status.setText(TextUtils.isEmpty(listBean.getState_str()) ? itemView.getContext().getString(R.string.game_not_start) : listBean.getState_str());
                        tv_game_name.setTextColor(mContext.getResources().getColor(R.color.black2));
                        iv_play_status.setImageResource(R.mipmap.icon_ball_not_start);
                    }

                    Team home_team = listBean.getHome_team();
                    Team away_team = listBean.getAway_team();
                    tv_team_a_name.setText(home_team == null ? "" : home_team.getNameZh());
                    tv_team_b_name.setText(away_team == null ? "" : away_team.getNameZh());
                    Glide.with(mContext)
                            .load(home_team == null ? R.mipmap.icon_default_logo : home_team.getLogo())
                            .apply(options)
                            .into(iv_team_a);
                    Glide.with(mContext)
                            .load(away_team == null ? R.mipmap.icon_default_logo : away_team.getLogo())
                            .apply(options)
                            .into(iv_team_b);
                }

            } catch (Exception e) {
                AppLog.e("", e.toString());
            }
        }

        @Override
        public void onClick(View v) {
            if (gameFootBallMatchBeanList != null && !gameFootBallMatchBeanList.isEmpty()) {
                if (!ClickUtil.canClick()) return;
                Intent intent = new Intent(mContext, LiveSportsActivity.class);
                intent.putExtra(Constants.LIVE_MATCH_FOOTBALL_INFO, gameFootBallMatchBeanList.get(getAdapterPosition()));
                mContext.startActivity(intent);
            }
        }
    }

    public void setUpdateData(List<Object> gameFootballMatchBean) {
        try {
            if (gameFootballMatchBean != null && !gameFootballMatchBean.isEmpty()
                    && gameFootBallMatchBeanList != null && !gameFootBallMatchBeanList.isEmpty()) {
                AppLog.e("赛事列表收到通知：刷新足球列表");
                for (int i = 0; i < gameFootBallMatchBeanList.size(); i++) {
                    for (int k = 0; k < gameFootballMatchBean.size(); k++) {
                        MatchBean gameFootballMatch = (MatchBean) gameFootballMatchBean.get(k);
                        if (gameFootballMatch.getId() == gameFootBallMatchBeanList.get(i).getId()) {
                            AppLog.e("赛事列表收到通知：刷新足球列表，匹配上ID：" + gameFootballMatch.getId());
                            gameFootBallMatchBeanList.get(i).setIsPlaying(gameFootballMatch.getIsPlaying());
                            gameFootBallMatchBeanList.get(i).setHomeScores(gameFootballMatch.getHomeScores());
                            gameFootBallMatchBeanList.get(i).setAwayScores(gameFootballMatch.getAwayScores());
                        }
                    }
                }
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            AppLog.e(e.toString());
        }
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