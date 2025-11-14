package com.yunbao.live.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.adapter.LiveContactAdapter;
import com.yunbao.live.bean.LiveInfoContactBean;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.Arrays;
import java.util.List;


public class LiveContactHostViewHolder extends AbsViewHolder {

    private String mLiveUid;
    private String mStream;
    private RecyclerView recyclerView;
    private SmartRefreshLayout mRefresh;
    private LinearLayout view_live_no_data;
    private LiveContactAdapter mAdapter;

    public LiveContactHostViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public LiveContactHostViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_contact_host;
    }

    @Override
    public void init() {
        mRefresh = mContentView.findViewById(R.id.mRefresh);
        view_live_no_data = mContentView.findViewById(R.id.view_live_no_data);
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });

    }

    public void setLiveInfo(String liveUid, String stream) {
        this.mLiveUid = liveUid;
        this.mStream = stream;
        getData();
    }

    private void getData() {
        LiveHttpUtil.getLiveInfoContact(mLiveUid, mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mRefresh.finishRefresh();
                AppLog.e("code:" + code + "info:" + Arrays.toString(info));
                if (code == 0) {
                    List<LiveInfoContactBean> list = JSON.parseArray(Arrays.toString(info), LiveInfoContactBean.class);
                    if (list != null && !list.isEmpty() && list.get(0).getContract() != null && !list.get(0).getContract().isEmpty()) {
                        showData(true);
                        List<LiveInfoContactBean.ContractBean> contract = list.get(0).getContract();
                        for (int i = 0; i < contract.size(); i++) {
                            if (contract.get(i).getStatus() == 0) {
                                contract.remove(i);
                            }
                        }
                        if (contract.isEmpty()) {
                            showData(false);
                        } else {
                            if (mAdapter == null) {
                                mAdapter = new LiveContactAdapter(R.layout.item_live_contact_host, list.get(0).getContract());
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                mAdapter.setNewData(list.get(0).getContract());
                            }
                        }
                    } else {
                        mRefresh.finishRefresh();
                        showData(false);
                    }
                } else {
                    mRefresh.finishRefresh();
                    showData(false);
                }
            }

            @Override
            public void onError(int ret, String msg) {
                super.onError(ret, msg);
                mRefresh.finishRefresh();
                showData(false);
            }
        });
    }

    public void showData(boolean has) {
        view_live_no_data.setVisibility(has ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(has ? View.VISIBLE : View.GONE);
    }
}
