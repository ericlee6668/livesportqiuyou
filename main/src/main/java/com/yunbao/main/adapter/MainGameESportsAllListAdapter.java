package com.yunbao.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.GameLOLMatchListBean;
import com.yunbao.common.bean.GameLolMarchDateBean;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.ClickUtil;
import com.yunbao.live.activity.LiveGameActivity;
import com.yunbao.main.R;

import java.util.ArrayList;
import java.util.List;

public class MainGameESportsAllListAdapter extends GroupedRecyclerViewAdapter {

    private Context mContext;
    private List<GameLolMarchDateBean> allList;
    private List<GameLolMatchBean> todayBeans;
    private List<GameLolMatchBean> tomorrowBeans;
    private List<GameLolMatchBean> overBeans;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_lol_finished)
            .error(R.mipmap.icon_lol_finished);

    public MainGameESportsAllListAdapter(Context context, List<GameLolMarchDateBean> gameLolMarchDateBeanList) {
        super(context);
        this.mContext = context;
        if (gameLolMarchDateBeanList != null) {
            allList = gameLolMarchDateBeanList;
        }
        this.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                if (allList != null && !allList.isEmpty()) {
                    if (!ClickUtil.canClick()) return;
                    GameLolMatchBean gameLolMatchBean = allList.get(groupPosition).getList().get(childPosition);
                    Intent intent = new Intent(mContext, LiveGameActivity.class);
                    intent.putExtra(Constants.LIVE_MATCH_INFO, gameLolMatchBean);
                    mContext.startActivity(intent);
                }

            }
        });
    }

    public void clear() {
        if (allList != null) {
            allList.clear();
        }
        notifyDataChanged();
    }

    public void setGroups(GameLOLMatchListBean gameLOLMatchListBean) {
        try {
            AppLog.e("setGroups:");
            if (gameLOLMatchListBean != null) {
                todayBeans = gameLOLMatchListBean.getToday();
                tomorrowBeans = gameLOLMatchListBean.getTomorrow();
                overBeans = gameLOLMatchListBean.getOver();
                if (allList == null) {
                    allList = new ArrayList<>();
                } else {
                    allList.clear();
                }
                if (todayBeans != null && !todayBeans.isEmpty()) {
                    allList.add(new GameLolMarchDateBean());
                    allList.get(0).setDate(todayBeans.get(0).getMatch_date() + " " + mContext.getString(R.string.today));
                    allList.get(0).setList(todayBeans);
                }
                if (tomorrowBeans != null && !tomorrowBeans.isEmpty()) {
                    allList.add(new GameLolMarchDateBean());
                    allList.get(1).setDate(tomorrowBeans.get(0).getMatch_date() + mContext.getString(R.string.tomorrow));
                    allList.get(1).setList(tomorrowBeans);
                }
                if (overBeans != null && !overBeans.isEmpty()) {
                    for (int i = 0; i < overBeans.size(); i++) {
                        if (i == 0) {
                            GameLolMarchDateBean gameLolMarchDateBean = new GameLolMarchDateBean();
                            gameLolMarchDateBean.setDate("");
                            gameLolMarchDateBean.setList(new ArrayList<GameLolMatchBean>());
                            allList.add(gameLolMarchDateBean);
                            allList.get(allList.isEmpty() ? 0 : allList.size() - 1).setDate(overBeans.get(i).getMatch_date());
                            allList.get(allList.isEmpty() ? 0 : allList.size() - 1).getList().add(overBeans.get(0));
                        } else {
                            if (!overBeans.get(i).getMatch_date().equals(overBeans.get(i - 1).getMatch_date())) {
                                GameLolMarchDateBean gameLolMarchDateBean = new GameLolMarchDateBean();
                                gameLolMarchDateBean.setDate("");
                                gameLolMarchDateBean.setList(new ArrayList<GameLolMatchBean>());
                                allList.add(gameLolMarchDateBean);
                            }
                            allList.get(allList.isEmpty() ? 0 : allList.size() - 1).setDate(overBeans.get(i).getMatch_date());
                            allList.get(allList.isEmpty() ? 0 : allList.size() - 1).getList().add(overBeans.get(i));
                        }
                    }
                }
            }
            notifyDataChanged();
        } catch (Exception e) {
            AppLog.e(e.toString());
        }
    }

    @Override
    public int getGroupCount() {
        //返回组的数量
        return allList == null ? 0 : allList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //返回当前组的子项数量
        return allList == null ? 0 : allList.get(groupPosition).getList().size();
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
        return R.layout.layout_game_lol_list_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        //绑定头部布局数据。(如果hasHeader返回false，这个方法不会执行)
        GameLolMarchDateBean gameLolMarchDateBean = allList.get(groupPosition);
        holder.setText(R.id.tv_sticky_header_view, gameLolMarchDateBean.getDate());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        //绑定尾部布局数据。(如果hasFooter返回false，这个方法不会执行)
        GameLolMarchDateBean gameLolMarchDateBean = allList.get(groupPosition);
//        holder.setText(R.id.tv_footer, gameFootballMatchBean.getFooter());
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        //绑定子项布局数据。
        try {
            GameLolMatchBean gameLolMatchBean = allList.get(groupPosition).getList().get(childPosition);
            if (gameLolMatchBean != null) {
                holder.setText(R.id.tv_game_name, gameLolMatchBean.getLeague().getNameZh());
                holder.setText(R.id.tv_time, gameLolMatchBean.getMatch_date());
                //比赛状态 1:未开始 2:进行中 3:已结束

                if (gameLolMatchBean.getIs_playing() == 2) {
                    holder.setText(R.id.tv_times, gameLolMatchBean.getTrend());
                    holder.setText(R.id.tv_times, "");
                    holder.setTextColor(R.id.tv_times, Color.parseColor("#FF5116"));
                } else if (gameLolMatchBean.getIs_playing() == 3) {
                    holder.setText(R.id.tv_times, R.string.over);
                    holder.setTextColor(R.id.tv_times, mContext.getResources().getColor(R.color.black));
                } else {
                    holder.setText(R.id.tv_times, R.string.game_not_start);
                    holder.setTextColor(R.id.tv_times, mContext.getResources().getColor(R.color.black));
                }
                holder.setText(R.id.tv_users, "");
                holder.setText(R.id.tv_a_score, "");
                holder.setText(R.id.tv_b_score, "");
                holder.setText(R.id.tv_gam, "");
                holder.setText(R.id.tv_evs, "");
                holder.setText(R.id.tv_gam_num, "");
                holder.setText(R.id.tv_evs_num, "");
                holder.setText(R.id.tv_gam_decimal_points, "");
                holder.setText(R.id.tv_evs_decimal_points, "");

                ImageView iv_gam = holder.get(R.id.iv_gam);
                ImageView iv_evs = holder.get(R.id.iv_evs);
                Glide.with(mContext)
                        .load(gameLolMatchBean.getLeague().getLogo())
                        .apply(options)
                        .into(iv_gam);
                Glide.with(mContext)
                        .load(gameLolMatchBean.getLeague().getLogo())
                        .apply(options)
                        .into(iv_evs);

                ImageView iv_play = holder.get(R.id.iv_play);
                //比赛状态 0:未开始 1:进行中 2:已结束 3:已延期 4:已删除
                if (gameLolMatchBean.getIs_playing() == 1) {
                    iv_play.setImageResource(R.mipmap.icon_lol_not_start);
                } else if (gameLolMatchBean.getIs_playing() == 2) {
                    iv_play.setImageResource(R.mipmap.icon_play_normal);
                } else if (gameLolMatchBean.getIs_playing() == 3) {
                    iv_play.setImageResource(R.mipmap.icon_lol_finished);
                } else {
                    iv_play.setImageResource(R.mipmap.icon_lol_finished);
                }
            }
        } catch (Exception e) {
            AppLog.e(e.toString());
        }
    }
}
