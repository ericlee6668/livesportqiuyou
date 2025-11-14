package com.yunbao.main.adapter.home;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.main.R;
import com.yunbao.main.bean.AnchorAnchorBean;

import java.util.List;

/**
 * 首页推荐
 */
public class HomeAnchorAdapter extends BaseQuickAdapter<AnchorAnchorBean, BaseViewHolder> {

    public HomeAnchorAdapter(@LayoutRes int layoutResId, @Nullable List<AnchorAnchorBean> data) {
        super(layoutResId, data);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(final BaseViewHolder helper, final AnchorAnchorBean item) {
        RoundedImageView iv_anchor = helper.getView(R.id.iv_anchor);
        TextView tv_anchor_name = helper.getView(R.id.tv_anchor_name);
        TextView tv_subscribe = helper.getView(R.id.tv_subscribe);


        ImgLoader.displayAvatar(mContext,item.getAvatar(),iv_anchor);
        tv_anchor_name.setText(item.getUser_nicename());
        tv_subscribe.setSelected(item.getIsSubscribe() == 0);
        tv_subscribe.setText(item.getIsSubscribe() == 0 ? R.string.item_subscribe : R.string.item_subscribed);
        tv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {
                    onItemClickListener.subscribe(item,helper.getLayoutPosition());
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
        void subscribe(AnchorAnchorBean item,int position);

        void anchor(AnchorAnchorBean item);
    }
}
