package com.yunbao.common.views;

import android.app.Dialog;
import android.content.Context;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 可监听外部 阴影事件点击部分
 */
public abstract class DiyDialog extends Dialog {
    public DiyDialog(@NonNull Context context) {
        super(context);
    }

    public DiyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DiyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected abstract void onTouchOutside();


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        //点击弹窗外部区域
        if (isOutOfBounds(getContext(), event)) {
            onTouchOutside();
        }
        return super.onTouchEvent(event);
    }


    private boolean isOutOfBounds(Context context, MotionEvent event) {

        final int x = (int) event.getX();//对应弹窗左上角的 X 坐标
        final int y = (int) event.getY();//对应弹框左上角的 Y 坐标

        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();//最小识别距离
        final View decorView = getWindow().getDecorView();//弹窗对应的 View

        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth()) + slop) || (y > (decorView.getHeight() + slop));
    }


}
