package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.yunbao.main.R;
import com.yunbao.main.adapter.SubscribeAnchorAdapter;

/**
 * 订阅主播 W.
 */
// TODO titleView
public class SubscribeAnchorViewHolder extends AbsMainViewHolder {
    private RecyclerView recyclerView;
    private SubscribeAnchorAdapter adapter;


    public SubscribeAnchorViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_subscribe_anchor;
    }

    @Override
    public void init() {
        recyclerView = findViewById(R.id.recyclerView);
//        adapter = new SubscribeAnchorAdapter(mContext, null, null);
        recyclerView.setAdapter(adapter);

    }


}
