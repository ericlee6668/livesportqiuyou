package com.yunbao.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {
    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }


    private View mClidOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams mFrameLayoutLayoutParams;
    private int contentHeight;
    private boolean isFirst = true;
    private Activity activity;
    private int stausBarHeight;

    private AndroidBug5497Workaround(Activity activity) {
        //获取状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        stausBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        this.activity = activity;
        FrameLayout conten = activity.findViewById(android.R.id.content);

        mClidOfContent = conten.getChildAt(0);
        //界面变动都会调用这个监听事件


        mClidOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    contentHeight = mClidOfContent.getHeight();//兼容华为
                    isFirst = false;
                }
                possibResizeChildOfContent();
            }
        });
        mFrameLayoutLayoutParams = (FrameLayout.LayoutParams) mClidOfContent.getLayoutParams();
    }


    //重新调整跟布局的高度
    private void possibResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        //当前课件高度和上一次 不一致 布局变动
        if (usableHeightNow != usableHeightPrevious) {
//            int usableHeighSansKeyboard2 = mClidOfContent.getHeight();//兼容华为等机型
            int usableHeighSansKeyboard = mClidOfContent.getRootView().getHeight();
            int heightDifference = usableHeighSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeighSansKeyboard / 4)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    // mFrameLayoutLayoutParams.height =usableHeighSansKeyboard-heightDifference;
                    mFrameLayoutLayoutParams.height = usableHeighSansKeyboard - heightDifference + stausBarHeight;
                } else {
                    mFrameLayoutLayoutParams.height = usableHeighSansKeyboard - heightDifference;
                }

            } else {
                mFrameLayoutLayoutParams.height = contentHeight;
            }
            mClidOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;

        }
    }

    /**
     * 计算 mClidOfContent 课件高度
     * @return
     */
    private int computeUsableHeight() {
        Rect rect=new Rect();
        mClidOfContent.getWindowVisibleDisplayFrame(rect);
        return (rect.bottom- rect.top);
    }


}
