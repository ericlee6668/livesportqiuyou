package com.yunbao.main.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.main.R;
import com.yunbao.main.bean.SubscribeAnchorBean;

import java.util.List;

/**
 * 订阅主播Adapter
 */
public class SubscribeAnchorAdapter extends BaseQuickAdapter<SubscribeAnchorBean, BaseViewHolder> {

    public SubscribeAnchorAdapter(int layoutResId, @Nullable List<SubscribeAnchorBean> data) {
        super(layoutResId, data);
    }

    RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_default_logo)
            .error(R.mipmap.icon_default_logo);

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(final BaseViewHolder helper, final SubscribeAnchorBean item) {
        RoundedImageView iv_anchor = helper.getView(R.id.iv_rounded_anchor);
        TextView tv_anchor_name = helper.getView(R.id.tv_anchor_name);
        TextView tv_subscribe = helper.getView(R.id.tv_subscribe);
        ImageView iv_palying = helper.getView(R.id.iv_palying);

        Glide.with(mContext)
                .load(item.getAvatar())
                .apply(options)
                .into(iv_anchor);

        if ("1".equals(item.getIslive())) {
            iv_palying.setVisibility(View.VISIBLE);
            com.bumptech.glide.Glide.with(mContext).load(R.drawable.gif_main_live_play)
                    .placeholder(R.mipmap.icon_item_living)
                    .error(R.mipmap.icon_item_living)
                    .into(iv_palying);
        } else {
            iv_palying.setVisibility(View.GONE);
        }

        tv_anchor_name.setText(item.getUser_nicename());
        tv_subscribe.setSelected(item.getIsSubscribe() == 0);
        tv_subscribe.setText(item.getIsSubscribe() == 0 ? R.string.item_subscribe : R.string.item_subscribed);
        tv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.subscribe(item, helper.getLayoutPosition());
                }
            }
        });

        iv_anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.anchor(item);
                }
            }
        });

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void subscribe(SubscribeAnchorBean item, int position);

        void anchor(SubscribeAnchorBean item);
    }


}
