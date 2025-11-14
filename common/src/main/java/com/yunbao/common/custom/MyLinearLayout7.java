package com.yunbao.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yunbao.common.R;


public class MyLinearLayout7 extends LinearLayout {

    private int mScreenWidth;
    private float mCount;
    private int withClear;

    public MyLinearLayout7(Context context) {
        this(context, null);
    }

    public MyLinearLayout7(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout7(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayout4);
        mCount = ta.getFloat(R.styleable.MyLinearLayout4_mll4_count, 1);
        float withDp = ta.getFloat(R.styleable.MyLinearLayout4_mll4_count_dp, 0);
        withClear = dp2px(context, withDp);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) ((mScreenWidth - withClear) / mCount), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
