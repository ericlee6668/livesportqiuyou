package com.yunbao.common.utils;


import android.util.Log;

public final class AppLog {

    public static boolean isPrint = true;
    private static String defaultTag = "appLog";

    public static void d(String msg) {
        if (isPrint && msg != null)
            Log.d(defaultTag, msg);
    }

    public static void i(String msg) {
        if (isPrint && msg != null)
            Log.i(defaultTag, msg);
    }

    public static void w(String msg) {
        if (isPrint && msg != null)
            Log.w(defaultTag, msg);
    }

    public static void e(String msg) {
        if (isPrint && msg != null)
            Log.e(defaultTag, msg);
    }

    public static void d(String tag, String msg) {
        if (isPrint && msg != null)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isPrint && msg != null)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isPrint && msg != null)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isPrint && msg != null)
            Log.e(tag, msg);
    }

}
