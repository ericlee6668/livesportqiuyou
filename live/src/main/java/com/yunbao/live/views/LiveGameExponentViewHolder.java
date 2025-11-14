package com.yunbao.live.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.ExponentNumBean;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.adapter.LiveAudienceExponentAdapter;
import com.yunbao.live.adapter.LiveGameExponentAdapter;
import com.yunbao.live.bean.LiveExponentDataBean;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juwan
 * @date 2020/9/23
 * Description:指数
 */
public class LiveGameExponentViewHolder extends AbsViewHolder implements OnRefreshListener {

    private GameLolMatchBean mMatchInfo;
    private SmartRefreshLayout mRefresh;
    private List<ExponentNumBean> list;
    private LiveAudienceExponentAdapter adapter;
    private int boardNum;
    private RecyclerView mMatchRecyclerView;
    private RecyclerView mContentRecyclerView;
    private LiveGameExponentAdapter mLiveGameExponentAdapter;

    public LiveGameExponentViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public LiveGameExponentViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_audience_exponent;
    }

    @Override
    public void init() {
        initGameNum();
        requestData();
    }

    public void loadData(GameLolMatchBean mMatchInfo) {
        this.mMatchInfo = mMatchInfo;
        requestData();
    }

    private void requestData() {
        if (mMatchInfo != null) {
            LiveHttpUtil.getMatchIndex(mMatchInfo.getId(), boardNum, 0, new HttpCallbackObject() {
                @Override
                public void onSuccess(int code, String msg, Object info) {
                    mRefresh.finishRefresh();
                    if (code == 0) {
                        LiveExponentDataBean liveGameDataBean = JSON.parseObject(info.toString(), LiveExponentDataBean.class);
                        if (liveGameDataBean != null) {
                            setTeamData(liveGameDataBean);
                            List<LiveExponentDataBean.DataBean> data = liveGameDataBean.getData();
                            if (data != null && data.size() > 0) {
                                mLiveGameExponentAdapter.setDataList(liveGameDataBean.getTeam_a_id(),data);
                            } else {
                                mLiveGameExponentAdapter.setNoData();
                            }
                        }
                    } else {
                        mLiveGameExponentAdapter.setNoData();
                    }
                }
            });
        }
    }

    private void setTeamData(LiveExponentDataBean liveGameDataBean) {
        int count = liveGameDataBean.getBattle_current_index();
        TextView mTeamA = mContentView.findViewById(R.id.live_tv_team_a);
        TextView mTeamB = mContentView.findViewById(R.id.live_tv_team_b);
        mTeamA.setText(liveGameDataBean.getTeam_a_name());
        mTeamB.setText(liveGameDataBean.getTeam_b_name());
        setCount(count);
    }

    private void setCount(int count) {
        if (list.size() != (count + 1)) {
            list.clear();
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, count + 1);
            mMatchRecyclerView.setLayoutManager(layoutManager);
            ExponentNumBean bean = new ExponentNumBean();
            bean.setMatchNumber("全局");
            bean.setChoose(true);
            ExponentNumBean bean1 = new ExponentNumBean();
            bean1.setMatchNumber("第一局");
            bean1.setChoose(false);
            ExponentNumBean bean2 = new ExponentNumBean();
            bean2.setMatchNumber("第二局");
            bean2.setChoose(false);
            ExponentNumBean bean3 = new ExponentNumBean();
            bean3.setMatchNumber("第三局");
            bean3.setChoose(false);
            ExponentNumBean bean4 = new ExponentNumBean();
            bean4.setMatchNumber("第四局");
            bean4.setChoose(false);
            ExponentNumBean bean5 = new ExponentNumBean();
            bean5.setMatchNumber("第五局");
            bean5.setChoose(false);
            switch (count) {
                case 1:
                    list.add(bean);
                    list.add(bean1);
                    break;
                case 2:
                    list.add(bean);
                    list.add(bean1);
                    list.add(bean2);
                    break;
                case 3:
                    list.add(bean);
                    list.add(bean1);
                    list.add(bean2);
                    list.add(bean3);
                    break;
                case 4:
                    list.add(bean);
                    list.add(bean1);
                    list.add(bean2);
                    list.add(bean3);
                    list.add(bean4);
                    break;
                case 5:
                    list.add(bean);
                    list.add(bean1);
                    list.add(bean2);
                    list.add(bean3);
                    list.add(bean4);
                    list.add(bean5);
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void initGameNum() {
        list = new ArrayList<>();
        mMatchRecyclerView = mContentView.findViewById(R.id.live_rv_games_num);
        mContentRecyclerView = mContentView.findViewById(R.id.recyclerView);
        mRefresh = mContentView.findViewById(R.id.mRefresh);
        mRefresh.setOnRefreshListener(this);
        mMatchRecyclerView.setNestedScrollingEnabled(false);
        mMatchRecyclerView.setHasFixedSize(true);

        adapter = new LiveAudienceExponentAdapter(mContext, list);
        adapter.setOnItemClickListener(new OnItemClickListener<ExponentNumBean>() {
            @Override
            public void onItemClick(ExponentNumBean bean, int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setChoose(false);

                }
                bean.setChoose(true);
                adapter.notifyDataSetChanged();
                boardNum = position;
                requestData();
            }

        });

        mMatchRecyclerView.setAdapter(adapter);

        mContentRecyclerView.setNestedScrollingEnabled(false);
        mContentRecyclerView.setHasFixedSize(true);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mLiveGameExponentAdapter = new LiveGameExponentAdapter(mContext);
        mContentRecyclerView.setAdapter(mLiveGameExponentAdapter);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        requestData();
    }
}
