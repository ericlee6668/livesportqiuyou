package com.yunbao.main.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.SpUtil;
import com.yunbao.common.utils.downmumusicutils.DownUtils;
import com.yunbao.main.R;
import com.yunbao.main.bean.SettingBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by cxf on 2018/9/30.
 */

public class SettingAdapter extends RecyclerView.Adapter {

    private static final int NORMAL = 0;
    private static final int VERSION = 1;
    private static final int LAST = 2;
    private static final int CHECK = 3;
    private Context mContext;
    private List<SettingBean> mList;
    private LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;
    private OnItemClickListener<SettingBean> mOnItemClickListener;
    private String mVersionString;
    private String mCacheString;
    private boolean mChecked;
    private Drawable mRadioCheckDrawable;
    private Drawable mRadioUnCheckDrawable;
    private View.OnClickListener mOnRadioBtnClickListener;


    public SettingAdapter(Context context, List<SettingBean> list, String version, String cache) {
        mContext = context;
        mList = list;
        mVersionString = version;
        mCacheString = cache;
        mInflater = LayoutInflater.from(context);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null) {
                    int position = (int) tag;
                    SettingBean bean = mList.get(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, position);
                    }
                }
            }
        };
        mChecked = SpUtil.getInstance().getBrightness() == 0.05f;
        mRadioCheckDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_btn_radio_1);
        mRadioUnCheckDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_btn_radio_0);
        mOnRadioBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                SpUtil.getInstance().setBrightness(mChecked ? -1f : 0.05f);
                mChecked = !mChecked;
                notifyItemChanged(position, Constants.PAYLOAD);
                if (mContext != null) {
                    ((AbsActivity) mContext).updateBrightness();
                }
            }
        };

    }

    public void setCacheString(String cacheString) {
        mCacheString = cacheString;
    }

    public void setOnItemClickListener(OnItemClickListener<SettingBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        SettingBean bean = mList.get(position);
        if (bean.getId() == Constants.SETTING_UPDATE_ID || bean.getId() == Constants.SETTING_CLEAR_CACHE) {
            return VERSION;
        } else if (bean.getId() == -1) {
            return CHECK;
        } else if (bean.isLast()) {
            return LAST;
        } else {
            return NORMAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VERSION) {
            return new Vh2(mInflater.inflate(R.layout.item_setting_1, parent, false));
        } else if (viewType == CHECK) {
            return new RadioButtonVh(mInflater.inflate(R.layout.item_setting_3, parent, false));
        } else if (viewType == LAST) {
            return new Vh(mInflater.inflate(R.layout.item_setting_2, parent, false));
        } else {
            return new Vh(mInflater.inflate(R.layout.item_setting, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        if (vh instanceof Vh) {
            ((Vh) vh).setData(mList.get(position), position);
        } else if (vh instanceof RadioButtonVh) {
            Object payload = payloads.size() > 0 ? payloads.get(0) : null;
            ((RadioButtonVh) vh).setData(mList.get(position), position, payload);
        } else {
            ((Vh2) vh).setData(mList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mName;

        public Vh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(SettingBean bean, int position) {
            itemView.setTag(position);
            mName.setText(bean.getName());
        }
    }

    class Vh2 extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mText;

        public Vh2(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mText = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(mOnClickListener);
        }
        void setData(SettingBean bean, int position) {
            itemView.setTag(position);
            mName.setText(bean.getName());
            if (bean.getId() == Constants.SETTING_CLEAR_CACHE) {// 显示缓存大小  XXMB

                try {
                    Log.i("=====================","=====================mCacheString:"+mCacheString);
//                    String numbx=mCacheString.substring(0,mCacheString.length()-2);
//                    Log.i("=====================","=====================numbx:"+numbx);
                    BigDecimal bd = new BigDecimal(mCacheString);
//                    double value = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

//                    String  value = bd.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                    double getFileTotalSize = DownUtils.getInstance().getFileTotalSize();
                    BigDecimal totalSizeDecimal = new BigDecimal(getFileTotalSize+"");
                    String totalSize = totalSizeDecimal.add(bd.setScale(2)).setScale(2).toPlainString();
//                    double allMusicNumb=Double.parseDouble(value)+getFileTotalSize;
                    Log.i("=====================","=====================getFileTotalSize:"+getFileTotalSize);
                    Log.i("=====================","=====================allMusicNumb:"+bd.setScale(2).toPlainString());
                    Log.i("=====================","=====================totalSize:"+totalSize);
                    if(totalSize.contains(".")){
                        int coinIndex = totalSize.indexOf(".");
                        int lastIndex = totalSize.length() - coinIndex;
                        if(lastIndex > 3){
                            totalSize = totalSize.substring(0,coinIndex + 3);
                        }
                    }else{
                        totalSize = totalSize + ".00";
                    }
                    mText.setText(totalSize+"MB");

                }catch (Exception e){

                    mText.setText(mCacheString+"MB");
                }


            } else {
                mText.setText(mVersionString);
            }
        }
    }


    class RadioButtonVh extends RecyclerView.ViewHolder {

        TextView mName;
        ImageView mBtnRadio;

        public RadioButtonVh(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mBtnRadio = itemView.findViewById(R.id.btn_radio);
            mBtnRadio.setOnClickListener(mOnRadioBtnClickListener);
        }

        void setData(SettingBean bean, int position, Object payload) {
            if (payload == null) {
                mName.setText(bean.getName());
                mBtnRadio.setTag(position);
            }
            mBtnRadio.setImageDrawable(mChecked ? mRadioCheckDrawable : mRadioUnCheckDrawable);
        }
    }


}
