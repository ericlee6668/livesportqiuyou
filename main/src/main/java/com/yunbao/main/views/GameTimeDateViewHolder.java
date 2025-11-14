package com.yunbao.main.views;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yunbao.main.R;
import com.yunbao.main.adapter.MainGameViewListAdapter;

public class GameTimeDateViewHolder extends AbsMainViewHolder {


    private RecyclerView recyclerView;
    private MainGameViewListAdapter gameViewListAdapter;

    public GameTimeDateViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_game_time_date_list;
    }

    @Override
    public void init() {
        initView();
        initData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        gameViewListAdapter = new MainGameViewListAdapter(mContext);
        recyclerView.setAdapter(gameViewListAdapter);
    }
}
