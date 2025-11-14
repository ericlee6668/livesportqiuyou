package com.yunbao.live.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.utils.AppLog;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveExponentDataBean;

import java.util.List;


public class LiveGameExponentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<LiveExponentDataBean.DataBean> dataList;
    private String team_a_id;
    private boolean isNoData = true;

    public LiveGameExponentAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_live_exponent_item, parent, false);
        return new GameDataViewListVh(view);
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
        if (isNoData) {
            return 1;
        } else {
            if (dataList != null && !dataList.isEmpty()) {
                return dataList.size();
            } else {
                return 1;
            }
        }
    }

    public void setDataList(String team_a_id, List<LiveExponentDataBean.DataBean> data) {
        this.team_a_id = team_a_id;
        this.dataList = data;
        isNoData = data == null || data.isEmpty();
        notifyDataSetChanged();
    }

    public void setNoData() {
        isNoData = true;
        this.dataList = null;
        notifyDataSetChanged();
    }

    class GameDataViewListVh extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView source;
        private final ImageView ivHandicapOne;
        private final TextView tvHandicapOne;
        private final TextView tvHandicapTwo;
        private final ImageView ivHandicapTwo;
        private final TextView tvOddsOne;
        private final ImageView ivOddsOne;
        private final TextView tvOddsTwo;
        private final ImageView ivOddsTwo;

        public GameDataViewListVh(View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.live_tv_exponent_source);
            ivHandicapOne = itemView.findViewById(R.id.live_iv_init_handicap_one);
            tvHandicapOne = itemView.findViewById(R.id.live_tv_init_handicap_one);
            tvHandicapTwo = itemView.findViewById(R.id.live_tv_init_handicap_two);
            ivHandicapTwo = itemView.findViewById(R.id.live_iv_init_handicap_two);
            tvOddsOne = itemView.findViewById(R.id.live_tv_odds_one);
            ivOddsOne = itemView.findViewById(R.id.live_iv_odds_one);
            tvOddsTwo = itemView.findViewById(R.id.live_tv_odds_two);
            ivOddsTwo = itemView.findViewById(R.id.live_iv_odds_two);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            itemView.setTag(position);
            try {
                if (!isNoData) {
                    LiveExponentDataBean.DataBean dataBean = dataList.get(position);
                    source.setText(dataBean.getSource());
                    LiveExponentDataBean.DataBean.OptionsBean optionsBean0 = dataBean.getOptions().get(0);
                    LiveExponentDataBean.DataBean.OptionsBean optionsBean1 = dataBean.getOptions().get(1);
                    if (optionsBean0.getTeam_id().equals(team_a_id)) {
                        //队伍一致
                        if (optionsBean0.getInit_handicap() == 0) {
                            tvHandicapOne.setVisibility(View.GONE);
                            ivHandicapOne.setVisibility(View.VISIBLE);
                        } else {
                            tvHandicapOne.setVisibility(View.VISIBLE);
                            ivHandicapOne.setVisibility(View.GONE);
                            tvHandicapOne.setText(String.format("%s", optionsBean0.getInit_handicap()));
                        }

                        if (optionsBean0.getOdds() == 0) {
                            tvHandicapTwo.setVisibility(View.GONE);
                            ivHandicapTwo.setVisibility(View.VISIBLE);
                        } else {
                            tvHandicapTwo.setVisibility(View.VISIBLE);
                            ivHandicapTwo.setVisibility(View.GONE);
                            tvHandicapTwo.setText(String.format("%s", optionsBean0.getOdds()));
                        }

                        if (optionsBean1.getInit_handicap() == 0) {
                            tvOddsOne.setVisibility(View.GONE);
                            ivOddsOne.setVisibility(View.VISIBLE);
                        } else {
                            tvOddsOne.setVisibility(View.VISIBLE);
                            ivOddsOne.setVisibility(View.GONE);
                            tvOddsOne.setText(String.format("%s", optionsBean1.getInit_handicap()));
                        }

                        if (optionsBean1.getOdds() == 0) {
                            tvOddsTwo.setVisibility(View.GONE);
                            ivOddsTwo.setVisibility(View.VISIBLE);
                        } else {
                            tvOddsTwo.setVisibility(View.VISIBLE);
                            ivOddsTwo.setVisibility(View.GONE);
                            tvOddsTwo.setText(String.format("%s", optionsBean1.getOdds()));
                        }

                    } else {
                        //队伍不一致
                        if (optionsBean1.getInit_handicap() == 0) {
                            tvHandicapOne.setVisibility(View.GONE);
                            ivHandicapOne.setVisibility(View.VISIBLE);
                        } else {
                            tvHandicapOne.setVisibility(View.VISIBLE);
                            ivHandicapOne.setVisibility(View.GONE);
                            tvHandicapOne.setText(String.format("%s", optionsBean1.getInit_handicap()));
                        }

                        if (optionsBean1.getOdds() == 0) {
                            tvHandicapTwo.setVisibility(View.GONE);
                            ivHandicapTwo.setVisibility(View.VISIBLE);
                        } else {
                            tvHandicapTwo.setVisibility(View.VISIBLE);
                            ivHandicapTwo.setVisibility(View.GONE);
                            tvHandicapTwo.setText(String.format("%s", optionsBean1.getOdds()));
                        }

                        if (optionsBean0.getInit_handicap() == 0) {
                            tvOddsOne.setVisibility(View.GONE);
                            ivOddsOne.setVisibility(View.VISIBLE);
                        } else {
                            tvOddsOne.setVisibility(View.VISIBLE);
                            ivOddsOne.setVisibility(View.GONE);
                            tvOddsOne.setText(String.format("%s", optionsBean0.getInit_handicap()));
                        }

                        if (optionsBean0.getOdds() == 0) {
                            tvOddsTwo.setVisibility(View.GONE);
                            ivOddsTwo.setVisibility(View.VISIBLE);
                        } else {
                            tvOddsTwo.setVisibility(View.VISIBLE);
                            ivOddsTwo.setVisibility(View.GONE);
                            tvOddsTwo.setText(String.format("%s", optionsBean0.getOdds()));
                        }
                    }
                } else {
                    source.setText("指数");
                    tvHandicapOne.setVisibility(View.GONE);
                    ivHandicapOne.setVisibility(View.VISIBLE);
                    tvHandicapTwo.setVisibility(View.GONE);
                    ivHandicapTwo.setVisibility(View.VISIBLE);
                    tvOddsOne.setVisibility(View.GONE);
                    ivOddsOne.setVisibility(View.VISIBLE);
                    tvOddsTwo.setVisibility(View.GONE);
                    ivOddsTwo.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                AppLog.e("Exception", e.toString());
            }
        }

        @Override
        public void onClick(View v) {
            if (dataList != null
                    && !dataList.isEmpty()
                    && dataList.get(getAdapterPosition()) != null
                    && !TextUtils.isEmpty(dataList.get(getAdapterPosition()).getJumpurl())) {
                WebViewActivity.forward(mContext, dataList.get(getAdapterPosition()).getJumpurl(), false);
            }
        }
    }
}

