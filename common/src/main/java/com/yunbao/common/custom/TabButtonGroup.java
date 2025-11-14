package com.yunbao.common.custom;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by cxf on 2018/9/22.
 */

public class TabButtonGroup extends LinearLayout implements View.OnClickListener {

    public TabButton[] getmTabButtons() {
        return mTabButtons;
    }

    public void setmTabButtons(TabButton[] mTabButtons) {
        this.mTabButtons = mTabButtons;
    }

    private TabButton[] mTabButtons;
    private ViewPager mViewPager;
    private int mCurPosition;


    public TabButtonGroup(Context context) {
        this(context, null);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButtonGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount > 0) {
            mTabButtons = new TabButton[childCount];
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                v.setTag(i);
                v.setOnClickListener(this);
                mTabButtons[i] = (TabButton) v;
            }
        }
    }


    public void setCurPosition(int position) {
        if (position == mCurPosition) {
            return;
        }
        // TODO: 2020/10/15
//        mTabButtons[mCurPosition].setChecked(false, position);
//        mTabButtons[position].setChecked(true, position);

        for (int i = 0; i < mTabButtons.length; i++) {
            if (i == position) {
                mTabButtons[position].setChecked(true, position);
            } else {
                mTabButtons[i].setChecked(false, position);
            }
        }
        mCurPosition = position;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null) {
            setCurPosition((int) tag);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void cancelAnim() {
        if (mTabButtons != null) {
            for (TabButton tbn : mTabButtons) {
                if (tbn != null) {
                    tbn.cancelAnim();
                }
            }
        }
    }
}
