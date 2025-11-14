package com.yunbao.live.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveInfoContactBean;

import java.util.List;

public class LiveContactAdapter extends BaseQuickAdapter<LiveInfoContactBean.ContractBean, BaseViewHolder> {

    public LiveContactAdapter(@LayoutRes int layoutResId, @Nullable List<LiveInfoContactBean.ContractBean> data) {
        super(layoutResId, data);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, LiveInfoContactBean.ContractBean item) {

        RelativeLayout rl_root = helper.getView(R.id.rl_root);
        ImageView iv_type = helper.getView(R.id.iv_type);
        ImageView iv_copy = helper.getView(R.id.iv_copy);
        TextView tv_name = helper.getView(R.id.tv_name);
        final TextView tv_id_number = helper.getView(R.id.tv_id_number);

        if ("1".equals(item.getType())) {
            iv_type.setImageResource(R.mipmap.icon_contact_qq);
            tv_name.setText("主播QQ");
        } else {
            tv_name.setText("主播wechart");
            iv_type.setImageResource(R.mipmap.icon_contact_wechat);
        }
        tv_id_number.setText(item.getContract());
        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyLink(tv_id_number.getText().toString().trim());
            }
        });

        if (item.getStatus() == 1) {
            rl_root.setVisibility(View.VISIBLE);
            iv_copy.setVisibility(View.VISIBLE);
            tv_name.setVisibility(View.VISIBLE);
            tv_id_number.setVisibility(View.VISIBLE);
            iv_type.setVisibility(View.VISIBLE);
        } else if (item.getStatus() == 2) {
            rl_root.setVisibility(View.VISIBLE);
            iv_copy.setVisibility(View.VISIBLE);
            tv_name.setVisibility(View.GONE);
            tv_id_number.setVisibility(View.GONE);
            iv_type.setVisibility(View.GONE);
        } else {
            rl_root.setVisibility(View.GONE);
        }
    }

    /**
     * 复制
     *
     * @param text
     */
    private void copyLink(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        if (configBean == null) {
            return;
        }
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(R.string.copy_success);
    }
}