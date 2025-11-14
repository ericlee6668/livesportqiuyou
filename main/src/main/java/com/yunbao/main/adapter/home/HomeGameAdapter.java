package com.yunbao.main.adapter.home;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.utils.DateFormatUtil;
import com.yunbao.common.utils.TimeUtil;
import com.yunbao.main.R;

import java.util.List;

/**
 * 首页游戏
 */
public class HomeGameAdapter extends BaseQuickAdapter<GameLolMatchBean, BaseViewHolder> {
    private Gson gson = new Gson();

    public HomeGameAdapter(@LayoutRes int layoutResId, @Nullable List<GameLolMatchBean> data) {
        super(layoutResId, data);
    }

    RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, GameLolMatchBean item) {

        TextView tv_item_home_game_title = helper.getView(R.id.tv_item_home_game_title);
        TextView tv_game_status = helper.getView(R.id.tv_game_status);
        TextView tv_game_result = helper.getView(R.id.tv_game_result);
        ImageView iv_left = helper.getView(R.id.iv_left);
        ImageView iv_right = helper.getView(R.id.iv_right);
        TextView tv_item_home_game_hint = helper.getView(R.id.tv_item_home_game_hint);
        TextView tv_team_a_name = helper.getView(R.id.tv_team_a_name);
        TextView tv_team_b_name = helper.getView(R.id.tv_team_b_name);

        tv_item_home_game_title.setText(item.getLive_class_id() == 1 ? "篮球" : "足球");
        if (item.getLeague() != null) {
            tv_item_home_game_hint.setText(item.getLeague().getNameZh());
        } else {
            tv_item_home_game_title.setText("");
        }
        String homeLogo;
        String awayLogo;
        if (item.getHome_team() != null) {
            tv_team_a_name.setText(item.getHome_team().getNameZh());
            homeLogo = item.getHome_team().getLogo();
        } else {
            tv_team_a_name.setText("");
            homeLogo = null;
        }
        if (item.getAway_team() != null) {
            awayLogo = item.getAway_team().getLogo();
            tv_team_b_name.setText(item.getAway_team().getNameZh());
        } else {
            awayLogo = null;
            tv_team_b_name.setText("");
        }
//        String dateDes = DateFormatUtil.getDateDes(item.getStarttime());

        //比赛状态 1:未开始 2:进行中 3:已结束
        if (item.getIs_playing() == 2) {
            tv_game_status.setText("比赛中");
            tv_game_status.setBackgroundResource(R.drawable.bg_home_game_status_in_play);
            tv_game_result.setText(String.format("%s : %s", getScore(item.getLive_class_id(), item.getHome_scores()), getScore(item.getLive_class_id(), item.getAway_scores())));
        } else if (item.getIs_playing() == 3) {
            tv_game_status.setText("完");
            tv_game_status.setBackgroundResource(R.drawable.bg_home_game_status_not_play);
            tv_game_result.setText(String.format("%s %s", getScore(item.getLive_class_id(), item.getHome_scores()), getScore(item.getLive_class_id(), item.getAway_scores())));
        } else {
            tv_game_status.setText("未开始");
            tv_game_status.setBackgroundResource(R.drawable.bg_home_game_status_not_play);
            tv_game_result.setText(DateFormatUtil.getMatchTime(item.getMatch_time() * 1000, "yyyy-MM-dd HH:mm"));
        }

        Glide.with(mContext)
                .load(homeLogo)
                .apply(options)
                .into(iv_left);

        Glide.with(mContext)
                .load(awayLogo)
                .apply(options)
                .into(iv_right);
    }

    private String getScore(int type, String scores) {
        if (type == 1) {
            return getBasketBallScore(scores);
        }
        return getFootballScore(scores);
    }

    private String getBasketBallScore(String scoreList) {
        if (TextUtils.isEmpty(scoreList)) {
            return "0";
        }

        JsonArray array = gson.fromJson(scoreList, JsonArray.class);
        int score = 0;
        try {
            for (int i = 0; i < array.size(); i++) {
                score += array.get(i).getAsInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(score);
    }

    private String getFootballScore(String scoreList) {
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
