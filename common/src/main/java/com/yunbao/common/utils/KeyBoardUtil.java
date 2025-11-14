package com.yunbao.common.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardUtil {

    public static void hiddenKeyBoard(Activity activity) {
        try {//处理 偶尔报错情况
            InputMethodManager imm = ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            AppLog.e("KeyBoardUtil", "hiddenKeyBoard: " + e.toString());
        }
    }

    public static void showKeyBoard(View view) {
        InputMethodManager imm = ((InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE));
        imm.showSoftInput(view, 0);
    }
}
