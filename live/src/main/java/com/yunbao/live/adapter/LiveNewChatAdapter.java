package com.yunbao.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.WordFilterUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveNewChatBean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2018/10/10.
 */

public class LiveNewChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<LiveNewChatBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<LiveNewChatBean> mOnItemClickListener;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public LiveNewChatAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    LiveNewChatBean bean = (LiveNewChatBean) tag;
                    if (/*bean.getType() != LiveChatBean.SYSTEM &&*/ mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, 0);
                    }
                }
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener<LiveNewChatBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_live_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position));
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

    public List<LiveNewChatBean> getList() {
        return mList;
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

        void setData(LiveNewChatBean bean) {
            itemView.setTag(bean);
            if (bean.getType().equals("come")) {
                mTvName.setText(bean.getUser().getUser_nicename());
                mTvContent.setText("  进入聊天室");
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_959292));
            } else if (bean.getType().equals("")) {
                mTvName.setVisibility(View.GONE);
                mIvChatAvatar.setVisibility(View.GONE);
                mTvContent.setText(bean.getMsg());
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_2B2626));
            } else if (bean.getType().equals("chat")) {
                mTvName.setText(MessageFormat.format("{0}：", bean.getUser().getUser_nicename()));
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_2B2626));
                String content = WordFilterUtil.getInstance().filter(bean.getMsg());
                mTvContent.setText(content);
            } else if (bean.getType().equals("2000")) {
                //表示成功下播
                mTvName.setText("");
                mTvContent.setText("主播下播啦");
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_FAAD14));
            } else if (bean.getType().equals("2001")) {
                //被禁言
                mTvName.setText(bean.getUser().getTousername());
                mTvContent.setText(bean.getMsg());
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_FAAD14));
            } else if (bean.getType().equals("2002")) {
                //被设为管理员
                mTvName.setText(bean.getUser().getTousername());
                mTvContent.setText(bean.getMsg());
                mTvContent.setTextColor(mContext.getResources().getColor(R.color.live_FAAD14));
            }
        }
    }

    public void insertItem(LiveNewChatBean bean) {
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

    public void clear() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }
}
