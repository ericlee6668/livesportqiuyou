package com.yunbao.main.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.http.HttpCallbackObject;
import com.yunbao.common.utils.RouteUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.main.R;
import com.yunbao.main.adapter.SubscribeAnchorAdapter;
import com.yunbao.main.bean.SubscribeAnchorBean;
import com.yunbao.main.http.MainHttpUtil;

import java.util.Arrays;
import java.util.List;

public class SubscribeAnchorActivity extends AbsActivity {

    private TextView titleView;
    private ImageView btn_back;
    private RecyclerView recyclerView;
    private LinearLayout llNoDate;
    private SubscribeAnchorAdapter subscribeAnchorAdapter;
    private List<SubscribeAnchorBean> anchorBeanList;
    private SmartRefreshLayout mRefresh;
    private int p = 1;

    public static void launch(Context mContext) {
        Intent intent = new Intent(mContext, SubscribeAnchorActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_subscribe_anchor;
    }


    @Override
    protected void main() {
        initView();
        initData();
        getRecommend();
        initListener();
        getRecommend();
    }

    private void initView() {
        titleView = findViewById(R.id.titleView);
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerView);
        mRefresh = findViewById(R.id.mRefresh);
        llNoDate = findViewById(R.id.ll_no_date);
    }

    private void initListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                p = 1;
                getRecommend();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                p++;
                getRecommend();
            }
        });
    }

    private void initData() {
        titleView.setText(R.string.subscribe_anchor);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    /**
     * 推荐主播数据
     */
    private void getRecommend() {
        MainHttpUtil.getSubscribe(p, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                if (code == 0) {
                    anchorBeanList = JSON.parseArray(Arrays.toString(info), SubscribeAnchorBean.class);
                    if (subscribeAnchorAdapter == null) {
                        subscribeAnchorAdapter = new SubscribeAnchorAdapter(R.layout.item_subscribe_anchor, anchorBeanList);
                        recyclerView.setAdapter(subscribeAnchorAdapter);
                        subscribeAnchorAdapter.setOnItemClickListener(new SubscribeAnchorAdapter.OnItemClickListener() {
                            @Override
                            public void subscribe(SubscribeAnchorBean item, int position) {
                                if (!TextUtils.isEmpty(CommonAppConfig.getInstance().getUid())) {
                                    //已经登陆
                                    recommendFollo(item.getId(), item.getIsSubscribe());
                                    item.setIsSubscribe(item.getIsSubscribe() == 0 ? 1 : 0);
                                    subscribeAnchorAdapter.notifyItemChanged(position);
                                } else {
                                    //未登陆，去注册
                                    LoginActivity.forward();
                                }
                            }

                            @Override
                            public void anchor(SubscribeAnchorBean item) {
                                //主播
                                clickAvatar(item.getId());
                            }
                        });
                    } else {
                        if (anchorBeanList != null) {
                            if (p > 1) {
                                subscribeAnchorAdapter.addData(anchorBeanList);
                            } else {
                                subscribeAnchorAdapter.setNewData(anchorBeanList);
                            }
                        }
                    }

                    if (subscribeAnchorAdapter == null || subscribeAnchorAdapter.getData().isEmpty()) {
                        showNoData(true);
                    } else {
                        showNoData(false);
                    }
                }
            }

            @Override
            public void onError(int ret,String msg) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                showNoData(true);
            }
        });
    }

    /**
     * 点击头像
     */
    public void clickAvatar(String id) {
        if (id != null && !id.isEmpty()) {
            RouteUtil.forwardUserHome(mContext, id);
        }
    }

    /**
     * 关注推荐主播
     *
     * @param touid
     * @param isSubscribe
     */
    private void recommendFollo(String touid, final int isSubscribe) {
        //type  默认为1，1订阅主播 2取消订阅
        MainHttpUtil.SubscribeAnchor(touid, isSubscribe == 0 ? "1" : "2", new HttpCallbackObject() {
            @Override
            public void onSuccess(int code, String msg, Object info) {
                if (code == 0) {
                    ToastUtil.show(isSubscribe == 0 ? "订阅成功" : "取消订阅成功");
                } else {
                    ToastUtil.show(msg);
                }
                //刷新
//                getRecommend();
            }
        });
    }

    /**
     * 是否有数据
     */
    private void showNoData(boolean noData) {
        recyclerView.setVisibility(noData ? View.GONE : View.VISIBLE);
        llNoDate.setVisibility(noData ? View.VISIBLE : View.GONE);
    }

}
