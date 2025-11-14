package com.yunbao.main.adapter.home;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.DataNewX;
import com.yunbao.main.R;

import java.util.List;

/**
 * 首页推荐
 */
public class HomeRecommendAdapter extends BaseQuickAdapter<DataNewX.DataDTO, BaseViewHolder> {

    //extends BaseQuickAdapter<MainRecommendBean, BaseViewHolder> {
    public HomeRecommendAdapter(@LayoutRes int layoutResId, @Nullable List<DataNewX.DataDTO> data) {
        super(layoutResId, data);
    }


//    public HomeRecommendAdapter(@LayoutRes int layoutResId, @Nullable List<MainRecommendBean> data) {
//      super(layoutResId, data);
//     }

    RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_image_loading)
            .error(R.mipmap.icon_load_failed);

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, DataNewX.DataDTO item) {

        RoundedImageView iv_main = helper.getView(R.id.iv_main);
        ImageView iv_palying = helper.getView(R.id.iv_palying);
        TextView tv_game_name = helper.getView(R.id.tv_game_name);
        TextView tv_game_des = helper.getView(R.id.tv_game_des);
        TextView tv_main_view_num = helper.getView(R.id.tv_main_view_num);

        if (!TextUtils.isEmpty(item.getPull())) {
            iv_palying.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.gif_main_live_play)
                    .placeholder(R.mipmap.icon_item_living)
                    .error(R.mipmap.icon_item_living)
                    .into(iv_palying);
        } else {
            iv_palying.setVisibility(View.GONE);
        }

        ImgLoader.display(mContext, item.getThumb(), iv_main);
        tv_game_name.setText(item.getTitle());
//            tv_game_des.setText(item.getUser_nicename());
//            tv_main_view_num.setText(String.valueOf(item.getViewnum()));
        tv_game_des.setText(item.getUser().getUserNicename());
//        tv_main_view_num.setText(item.getUser().getUserNicename());
        tv_main_view_num.setText(item.getUser().getViewnum());

    }


}
