package com.yunbao.live.utils.mpchart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.yunbao.common.utils.AppLog;

public class LineChartInViewPager extends LineChart {


    private float x1;
    private float y1;
    private float y2;

    public LineChartInViewPager(Context context) {
        super(context);
    }

    public LineChartInViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartInViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = evt.getX();
                y1 = evt.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                y2 = evt.getY();
                AppLog.i("getScrollX ", getScrollX() + "");
                if (getScaleX() > 1 && Math.abs(evt.getX() - x1) > 5) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if(y1 - y2 > 100 ||y2 - y1 > 100) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

        }
        return super.onTouchEvent(evt);
    }
}
