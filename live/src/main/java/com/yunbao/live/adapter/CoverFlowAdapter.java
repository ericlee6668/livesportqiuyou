package com.yunbao.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveGameActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sensyang.
 * on 2018/11/16 0016.
 * Email:532245792@qq.com
 * desc:
 */

public class CoverFlowAdapter extends RecyclerView.Adapter<CoverFlowAdapter.ViewHolder> {

    private Context mContext;
    List<MainRecommendBean> list = new ArrayList<>();
    private OnCoverClickListener listener;

    public CoverFlowAdapter(Context c, List<MainRecommendBean> list) {
        mContext = c;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_live_cover_flow, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mContext != null) {

                if (position == 0) {
                    holder.llContainer.setVisibility(View.VISIBLE);
                    holder.img.setVisibility(View.GONE);
                    holder.llContainer.getLayoutParams().width = getScreenWidth(mContext) - DpUtil.dp2px(80);
                } else {
                    holder.img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LiveGameActivity.class);
                            intent.putExtra(Constants.LIVE_RECOMMEND_DATA, list.get(position));
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        }
                    });
                    holder.llContainer.setVisibility(View.GONE);
                    holder.img.setVisibility(View.VISIBLE);
                    holder.img.getLayoutParams().width = getScreenWidth(mContext) - DpUtil.dp2px(80);
                    Glide.with(mContext)
                            .load(list.get(position).getThumb())
                            .placeholder(R.mipmap.icon_default_logo)
                            .error(R.mipmap.icon_default_logo)
                            .centerCrop()
                            .into(holder.img);

                }
                if(list.get(position).isNetError()){
                    holder.title.setText("网络连接失败，点击重试");
                    holder.content.setVisibility(View.GONE);
                    holder.llContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              if(listener!=null){
                                  listener.onClick();
                              }
                        }
                    });
                }else{
                    holder.content.setVisibility(View.VISIBLE);
                }

        }
    }
    public void setOnCoverClickListener(OnCoverClickListener listener){
        this.listener = listener;
    }
    public interface OnCoverClickListener{
        void onClick();
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        FrameLayout llContainer;
        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            llContainer = itemView.findViewById(R.id.ll_container);
            title = itemView.findViewById(R.id.mTitle);
            content = itemView.findViewById(R.id.tv_content);
        }
    }
}

