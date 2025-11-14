package com.yunbao.live.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.GameLolMatchBean;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.bean.MultiGroupHistogramChildData;
import com.yunbao.live.bean.MultiGroupHistogramGroupData;
import com.yunbao.live.http.LiveHttpUtil;
import com.yunbao.live.views.LiveGameHistogramView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiveBattleViewHolder extends AbsViewHolder implements OnRefreshListener {

    private LiveGameHistogramView multiGroupHistogramView;
    private GameLolMatchBean mMatchInfo;
    private SmartRefreshLayout mRefresh;
    private LinearLayout mLiveNoData;
    private LinearLayout mllBattleData;

    public LiveBattleViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_battle;
    }

    @Override
    public void init() {
        multiGroupHistogramView = mContentView.findViewById(R.id.live_lg_view);
        mRefresh = mContentView.findViewById(R.id.mRefresh);
        mllBattleData = mContentView.findViewById(R.id.ll_battle_data);
        mLiveNoData = mContentView.findViewById(R.id.view_live_no_data);
        mRefresh.setOnRefreshListener(this);
        // TODO: 2020/10/9
        showNoData(true);
        initMultiGroupHistogramView();
        initListener();
    }

    private void initListener() {
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefresh.finishRefresh();
            }
        });
    }

    public void loadData(GameLolMatchBean mMatchInfo) {
        this.mMatchInfo = mMatchInfo;
        // TODO: 2020/10/7  待接数据
        requestPlayerData();
    }

    private void requestPlayerData() {
        if (mMatchInfo != null) {
            LiveHttpUtil.getLOLBattleView(mMatchInfo.getId(), 0, new HttpCallbackObject() {
                @Override
                public void onSuccess(int code, String msg, Object info) {
                    Log.d("wj", info.toString());
                    mRefresh.finishRefresh();
                    if (code == 0) {

                    }
                }
            });
        }

    }

    private void initMultiGroupHistogramView() {
        Random random = new Random();
        int groupSize = 5;
        List<MultiGroupHistogramGroupData> groupDataList = new ArrayList<>();
        // 生成测试数据
        for (int i = 0; i < groupSize; i++) {
            List<MultiGroupHistogramChildData> childDataList = new ArrayList<>();
            MultiGroupHistogramGroupData groupData = new MultiGroupHistogramGroupData();
            if (i == 0) {
                groupData.setGroupName("一血次数");
            } else if (i == 1) {
                groupData.setGroupName("一塔次数");
            } else if (i == 2) {
                groupData.setGroupName("先五杀次数");
            } else if (i == 3) {
                groupData.setGroupName("首小龙次数");
            } else {
                groupData.setGroupName("首大龙次数");
            }

            MultiGroupHistogramChildData childData1 = new MultiGroupHistogramChildData();
            childData1.setValue(random.nextInt(10));
            childDataList.add(childData1);

            MultiGroupHistogramChildData childData2 = new MultiGroupHistogramChildData();
            childData2.setValue(random.nextInt(10));
            childDataList.add(childData2);
            groupData.setChildDataList(childDataList);
            groupDataList.add(groupData);
        }
        multiGroupHistogramView.setDataList(groupDataList);
        int[] color1 = new int[]{mContext.getResources().getColor(R.color.color_orange), mContext.getResources().getColor(R.color.color_orange)};

        int[] color2 = new int[]{mContext.getResources().getColor(R.color.color_supper_tip_normal), mContext.getResources().getColor(R.color.color_supper_tip_normal)};
        // 设置直方图颜色
        multiGroupHistogramView.setHistogramColor(color1, color2);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    /**
     * 是否有数据
     */
    private void showNoData(boolean noData) {
        mllBattleData.setVisibility(noData ? View.GONE : View.VISIBLE);
        mLiveNoData.setVisibility(noData ? View.VISIBLE : View.GONE);
    }
}
