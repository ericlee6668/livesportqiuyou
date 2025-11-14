package com.yunbao.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveGameDataBean;

import java.util.ArrayList;
import java.util.List;


public class LiveGameDataAdapter extends RefreshAdapter<Object> {

    private RequestOptions optionsLogo = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);

    RequestOptions optionsGame = new RequestOptions()
            .placeholder(R.mipmap.icon_lol_default)
            .error(R.mipmap.icon_lol_default);
    private String teamId = "";
    private int count = 5;

    private List<LiveGameDataBean.DataBean.PlayerStatsBean> player_stats = new ArrayList<>();

    public LiveGameDataAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameDataViewListVh(mInflater.inflate(R.layout.item_live_game_data_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof GameDataViewListVh) {
            GameDataViewListVh gameViewListVh = (GameDataViewListVh) vh;
            gameViewListVh.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void setListData(String teamId, List<LiveGameDataBean.DataBean.PlayerStatsBean> player_stats) {
        try {
            this.teamId = teamId;
            this.player_stats.clear();
            if (player_stats != null && !player_stats.isEmpty()) {
                for (int i = 0; i < player_stats.size(); i++) {
                    if (teamId.equals(player_stats.get(i).getTeam_id())) {
                        this.player_stats.add(player_stats.get(i));
                    }
                }
                count = this.player_stats.size();
            } else {
                count = 0;
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            AppLog.e("", "" + e.toString());
        }
    }

    class GameDataViewListVh extends RecyclerView.ViewHolder {

        private final ImageView iv_team_logo;
        private final TextView tv_name;
        private final TextView tv_num;
        private final TextView tv_game_data;
        private final ImageView iv_1;
        private final ImageView iv_2;
        private final ImageView iv_3;
        private final ImageView iv_4;
        private final ImageView iv_5;
        private final ImageView iv_6;
        private final ImageView iv_7;

        public GameDataViewListVh(View itemView) {
            super(itemView);
            iv_team_logo = itemView.findViewById(R.id.iv_team_logo);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_num = itemView.findViewById(R.id.tv_num);
            tv_game_data = itemView.findViewById(R.id.tv_game_data);
            iv_1 = itemView.findViewById(R.id.iv_1);
            iv_2 = itemView.findViewById(R.id.iv_2);
            iv_3 = itemView.findViewById(R.id.iv_3);
            iv_4 = itemView.findViewById(R.id.iv_4);
            iv_5 = itemView.findViewById(R.id.iv_5);
            iv_6 = itemView.findViewById(R.id.iv_6);
            iv_7 = itemView.findViewById(R.id.iv_7);
        }

        public void bind(int position) {
            itemView.setTag(position);
            try {
                if (player_stats != null && !player_stats.isEmpty()) {
                    LiveGameDataBean.DataBean.PlayerStatsBean playerStatsBean = player_stats.get(position);
                    tv_game_data.setText(String.format("%s/%s/%s", playerStatsBean.getKill_count(), playerStatsBean.getDeath_count(), playerStatsBean.getAssist_count()));
                    tv_num.setText(StringUtil.formatBigNum(playerStatsBean.getMoney_count()));
                    tv_name.setText(playerStatsBean.getPlayer_name());
                    setImage(iv_team_logo, playerStatsBean.getPlayer_avatar(), optionsLogo);
                    setImage(iv_1, playerStatsBean.getEquip_ids().get(0), optionsGame);
                    setImage(iv_2, playerStatsBean.getEquip_ids().get(1), optionsGame);
                    setImage(iv_3, playerStatsBean.getEquip_ids().get(2), optionsGame);
                    setImage(iv_4, playerStatsBean.getEquip_ids().get(3), optionsGame);
                    setImage(iv_5, playerStatsBean.getEquip_ids().get(4), optionsGame);
                    setImage(iv_6, playerStatsBean.getEquip_ids().get(5), optionsGame);
                    setImage(iv_7, playerStatsBean.getEquip_ids().get(6), optionsGame);
                }
            } catch (Exception e) {
                AppLog.e("Exception", e.toString());
            }
        }

        private void setImage(ImageView iv, String ivUrl, RequestOptions options) {
            Glide.with(mContext)
                    .load(ivUrl)
                    .apply(options)
                    .into(iv);
        }

    }
}

