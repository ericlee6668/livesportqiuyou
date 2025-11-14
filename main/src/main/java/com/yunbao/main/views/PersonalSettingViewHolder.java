package com.yunbao.main.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.yunbao.main.R;
import com.yunbao.main.adapter.PersonalSettingAdapter;

/**
 * 个人设置 W.
 */
public class PersonalSettingViewHolder extends AbsMainViewHolder {
    private TextView titleView;
    private RecyclerView mRecyclerView;
    private PersonalSettingAdapter mAdapter;

    public PersonalSettingViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_personal_setting;
    }

    @Override
    public void init() {
        titleView = findViewById(R.id.titleView);
        mRecyclerView = findViewById(R.id.recyclerView);
        titleView.setText(R.string.personal_setting);
        mAdapter = new PersonalSettingAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }
}
