package com.yunbao.live.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveChatBean;
import com.yunbao.live.utils.LiveTextRender;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/10.
 */

public class LiveChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<LiveChatBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<LiveChatBean> mOnItemClickListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public LiveChatAdapter(Context context) {
        mContext=context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    LiveChatBean bean = (LiveChatBean) tag;
                    if (bean.getType() != LiveChatBean.SYSTEM && mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<LiveChatBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LiveChatBean.RED_PACK) {
            return new RedPackVh(mInflater.inflate(R.layout.item_live_chat_red_pack, parent, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_live_chat, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position));
        } else if (vh instanceof RedPackVh) {
            ((RedPackVh) vh).setData(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    class RedPackVh extends RecyclerView.ViewHolder {

        TextView mTextView;

        public RedPackVh(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        void setData(LiveChatBean bean) {
            mTextView.setText(bean.getContent());
        }
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTvName, mTvContent;
        ImageView mIvChatAvatar;

        public Vh(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.live_tv_name);
            mTvContent = itemView.findViewById(R.id.live_tv_content);
            mIvChatAvatar = itemView.findViewById(R.id.live_iv_chat_avatar);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(LiveChatBean bean) {

            if(bean.getType() == LiveChatBean.ENTER_ROOM) {
                mTvName.setText(bean.getUserNiceName());
                mTvContent.setText("  进入聊天室");
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_959292));
            } else if (bean.getType() == LiveChatBean.SYSTEM){
                mTvName.setVisibility(View.GONE);
                mIvChatAvatar.setVisibility(View.GONE);
                mTvContent.setText(bean.getContent());
            } else {
                mTvName.setText(MessageFormat.format("{0}：", bean.getUserNiceName()));
                mTvContent.setText(bean.getContent());

            }
        }
    }

    public void insertItem(LiveChatBean bean) {
        if (bean == null) {
            return;
        }
        int size = mList.size();
        mList.add(bean);
        notifyItemInserted(size);
        int lastItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        if (lastItemPosition != size - 1) {
            mRecyclerView.smoothScrollToPosition(size);
        } else {
            mRecyclerView.scrollToPosition(size);
        }
    }

    public void scrollToBottom() {
        if (mList.size() > 0) {
            mRecyclerView.smoothScrollToPosition(mList.size() - 1);
        }
    }

    public void clear(){
        if(mList!=null){
            mList.clear();
        }
        notifyDataSetChanged();
    }
}
