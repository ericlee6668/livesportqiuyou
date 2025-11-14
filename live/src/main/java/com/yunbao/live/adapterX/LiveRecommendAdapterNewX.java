package com.yunbao.live.adapterX;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.MainRecommendBean;
import com.yunbao.common.http.DataNewX;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.live.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cxf on 2018/10/16.
 */
public class LiveRecommendAdapterNewX extends RefreshAdapter<DataNewX.DataDTO> {


    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<DataNewX.DataDTO> mOnItemClickListener;

    RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.icon_image_loading)
            .error(R.mipmap.icon_load_failed);

    public LiveRecommendAdapterNewX(Context context) {
        super(context);
        mContext=context;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mList.get(position), position);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<DataNewX.DataDTO> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setNewList(List<DataNewX.DataDTO> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_home_recommend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh)holder).setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        private RoundedImageView iv_main;
        private ImageView iv_playing;
        private TextView tv_game_name;
        private TextView tv_game_des;
        private TextView tv_main_view_num;

        public Vh(View itemView) {
            super(itemView);

            iv_main = itemView.findViewById(R.id.iv_main);
            iv_playing = itemView.findViewById(R.id.iv_palying);
            tv_game_name = itemView.findViewById(R.id.tv_game_name);
            tv_game_des = itemView.findViewById(R.id.tv_game_des);
            tv_main_view_num = itemView.findViewById(R.id.tv_main_view_num);

            itemView.setOnClickListener(mOnClickListener);

        }

        void setData(DataNewX.DataDTO item, int position, Object payload) {

            itemView.setTag(position);
            if(!TextUtils.isEmpty(item.getPull())){
                iv_playing.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.drawable.gif_main_live_play)
                        .placeholder(R.mipmap.icon_item_living)
                        .error(R.mipmap.icon_item_living)
                        .into(iv_playing);
            }else {
                iv_playing.setVisibility(View.GONE);
            }


            Glide.with(mContext)
                    .load(item.getThumb())
                    .apply(options)
                    .into(iv_main);

            tv_game_name.setText(item.getTitle());
            tv_game_des.setText(item.getUser().getUserNicename());
            tv_main_view_num.setText(item.getUser().getViewnum());
        }
    }

    public void removeItem(String uid) {

    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

    public void release(){
        if(mList!=null){
            mList.clear();
        }
        mOnClickListener=null;
        mOnItemClickListener=null;
    }
}
