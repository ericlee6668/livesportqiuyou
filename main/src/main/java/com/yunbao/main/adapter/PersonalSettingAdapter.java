package com.yunbao.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.main.R;

public class PersonalSettingAdapter extends RefreshAdapter {

    private static final int PERSONAL_SETTING_COUNT = 5;

    public PersonalSettingAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PersonalSettingVh(mInflater.inflate(R.layout.item_personal_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonalSettingVh) {
            PersonalSettingVh personalSettingVh = (PersonalSettingVh) holder;
            personalSettingVh.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return PERSONAL_SETTING_COUNT;
    }

    class PersonalSettingVh extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvPersonalSettingContent;

        public PersonalSettingVh(View itemView) {
            super(itemView);
            tvPersonalSettingContent = itemView.findViewById(R.id.tvPersonalSettingContent);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            itemView.setTag(position);
            switch (position) {
                case 0:
                    tvPersonalSettingContent.setText(R.string.night_model);
                    break;
                case 1:
                    tvPersonalSettingContent.setText(R.string.private_policy);
                    break;
                case 2:
                    tvPersonalSettingContent.setText(R.string.server_protocol);
                    break;
                case 3:
                    tvPersonalSettingContent.setText(R.string.change_pwd);
                    break;
                case 4:
                    tvPersonalSettingContent.setText(R.string.clear_cache);
                    break;
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
