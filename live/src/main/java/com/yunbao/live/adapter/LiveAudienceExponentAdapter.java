package com.yunbao.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yunbao.common.bean.ExponentNumBean;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.live.R;

import java.util.List;

/**
 * Created by cxf on 2018/10/16.
 */

public class LiveAudienceExponentAdapter extends RecyclerView.Adapter<LiveAudienceExponentAdapter.Vh> {

    private Context mContext;
    private List<ExponentNumBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<ExponentNumBean> mOnItemClickListener;

    public LiveAudienceExponentAdapter(Context context, List<ExponentNumBean> list) {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        mList = list;
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

    public void setOnItemClickListener(OnItemClickListener<ExponentNumBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<ExponentNumBean> list){
        mList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_audience_exponent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull Vh vh, int position, @NonNull List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        vh.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView tvExponent;

        public Vh(View itemView) {
            super(itemView);
            tvExponent = itemView.findViewById(R.id.live_tv_exponent);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ExponentNumBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (bean.isChoose()) {
                tvExponent.setBackgroundResource(R.drawable.bg_btn_common_02);
                tvExponent.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                tvExponent.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                tvExponent.setTextColor(mContext.getResources().getColor(R.color.live_2B2626));
            }

            tvExponent.setText(bean.getMatchNumber());
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
