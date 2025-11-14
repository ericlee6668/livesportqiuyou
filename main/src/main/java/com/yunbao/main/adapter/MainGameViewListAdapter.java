package com.yunbao.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.live.activity.LiveGameActivity;
import com.yunbao.main.R;

import java.util.ArrayList;
import java.util.List;

public class MainGameViewListAdapter extends RefreshAdapter<GameLolMatchBean> {

    private List<GameLolMatchBean> allList;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_lol_finished)
            .error(R.mipmap.icon_lol_finished);

    public MainGameViewListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameViewListVh(mInflater.inflate(R.layout.item_game_lol_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {

        if (vh instanceof GameViewListVh) {
            GameViewListVh gameViewListVh = (GameViewListVh) vh;
            gameViewListVh.bind(position);
        } else if (vh instanceof MiddleViewVh) {
            MiddleViewVh middleViewVh = (MiddleViewVh) vh;
            middleViewVh.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        if (allList != null && !allList.isEmpty()) {
            return allList.size();
        }
        return 0;
    }

    public void setListData(List<GameLolMatchBean> gameLOLMatchListBean) {
        allList = new ArrayList<>();
        if (gameLOLMatchListBean != null && !gameLOLMatchListBean.isEmpty()) {
            allList.addAll(gameLOLMatchListBean);
        }
        setList(allList);
        notifyDataSetChanged();
    }

    class GameViewListVh extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_game_name;
        private final TextView tv_time;
        private final TextView tv_times;
        private final TextView tv_users;
        private final TextView tv_a_score;
        private final TextView tv_b_score;
        private final ImageView iv_gam;
        private final ImageView iv_evs;
        private final TextView tv_gam;
        private final TextView tv_evs;
        private final TextView tv_gam_num;
        private final TextView tv_evs_num;
        private final TextView tv_gam_decimal_points;
        private final TextView tv_evs_decimal_points;
        private final LinearLayout ll_decimal_points;
        private final RelativeLayout rl_lock;
        private final ImageView iv_play;


        public GameViewListVh(View itemView) {
            super(itemView);
            tv_game_name = itemView.findViewById(R.id.tv_game_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_times = itemView.findViewById(R.id.tv_times);
            tv_users = itemView.findViewById(R.id.tv_users);
            tv_a_score = itemView.findViewById(R.id.tv_a_score);
            tv_b_score = itemView.findViewById(R.id.tv_b_score);
            iv_gam = itemView.findViewById(R.id.iv_gam);
            iv_evs = itemView.findViewById(R.id.iv_evs);
            tv_gam = itemView.findViewById(R.id.tv_gam);
            tv_evs = itemView.findViewById(R.id.tv_evs);
            tv_gam_num = itemView.findViewById(R.id.tv_gam_num);
            tv_evs_num = itemView.findViewById(R.id.tv_evs_num);
            tv_gam_decimal_points = itemView.findViewById(R.id.tv_gam_decimal_points);
            tv_evs_decimal_points = itemView.findViewById(R.id.tv_evs_decimal_points);
            ll_decimal_points = itemView.findViewById(R.id.ll_decimal_points);
            rl_lock = itemView.findViewById(R.id.rl_lock);
            iv_play = itemView.findViewById(R.id.iv_play);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            itemView.setTag(position);
            try {
                if (getList().get(position) != null) {
                    GameLolMatchBean gameLolMatchBean = (GameLolMatchBean) getList().get(position);
                    if (gameLolMatchBean != null) {
                        tv_game_name.setText(gameLolMatchBean.getLeague().getNameZh());
                        tv_time.setText(gameLolMatchBean.getMatch_date());
                        //比赛状态 0:未开始 1:进行中 2:已结束 3:已延期 4:已删除
                        if (gameLolMatchBean.getIs_playing() == 1) {
                            tv_times.setText(R.string.game_not_start);
                            tv_times.setTextColor(mContext.getResources().getColor(R.color.black));
                        } else if (gameLolMatchBean.getIs_playing() == 2) {
                            tv_times.setTextColor(Color.parseColor("#FF5116"));
                            tv_times.setText(gameLolMatchBean.getTrend());
                        } else if (gameLolMatchBean.getIs_playing() == 3) {
                            tv_times.setText(R.string.over);
                            tv_times.setTextColor(mContext.getResources().getColor(R.color.black));
                        } else {
                            tv_times.setText(R.string.game_not_start);
                            tv_times.setTextColor(mContext.getResources().getColor(R.color.black));
                        }
                        tv_users.setText("");
                        tv_a_score.setText("");
                        tv_b_score.setText("");
                        tv_gam.setText("");
                        tv_evs.setText("");
                        tv_gam_num.setText("");
                        tv_evs_num.setText("");
                        tv_gam_decimal_points.setText("");
                        tv_evs_decimal_points.setText("");
                        setPlayIcon(gameLolMatchBean, R.mipmap.icon_lol_not_start, R.mipmap.icon_lol_finished);
                        Glide.with(mContext)
                                .load(gameLolMatchBean.getHome_team().getLogo())
                                .apply(options)
                                .into(iv_gam);
                        Glide.with(mContext)
                                .load(gameLolMatchBean.getAway_team().getLogo())
                                .apply(options)
                                .into(iv_evs);
                    }
                }
            } catch (Exception e) {
                AppLog.e("", e.toString());
            }
        }

        //比赛状态 0:未开始 1:进行中 2:已结束 3:已延期 4:已删除
        private void setPlayIcon(GameLolMatchBean gameLolMatchBean, int iconNotStart, int iconFinished) {
//            if (gameLolMatchBean.getStatus() == 0) {
//                iv_play.setImageResource(iconNotStart);
//            } else if (gameLolMatchBean.getStatus() == 1) {
//                iv_play.setImageResource(R.mipmap.icon_play_normal);
//            } else if (gameLolMatchBean.getStatus() == 2) {
//                iv_play.setImageResource(iconFinished);
//            } else {
//                iv_play.setImageResource(iconFinished);
//            }
        }

        @Override
        public void onClick(View v) {
            if (!ClickUtil.canClick()) return;
            if (getList().get(getAdapterPosition()) instanceof GameLolMatchBean) {
                GameLolMatchBean gameLolMatchBean = (GameLolMatchBean) getList().get(getAdapterPosition());
                Intent intent = new Intent(mContext, LiveGameActivity.class);
                intent.putExtra(Constants.LIVE_MATCH_INFO, gameLolMatchBean);
                mContext.startActivity(intent);
            }
        }
    }

    private class MiddleViewVh extends RecyclerView.ViewHolder {

        private TextView tv_content;

        public MiddleViewVh(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
        }

        public void bind(int position) {
            itemView.setTag(position);
            tv_content.setText(allList.get(position).toString());
        }
    }

    public void setUpdateData(List<Object> gameLolMatchBean) {

    }
}
