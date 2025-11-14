package com.yunbao.common.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cxf on 2018/7/19.
 */

public class DateFormatUtil {

    private static SimpleDateFormat sFormat;
    private static SimpleDateFormat sFormat2;
    private static SimpleDateFormat sFormat3;
    private static SimpleDateFormat sFormat4;
    private static SimpleDateFormat hourMinuteFormatter;

    static {
        sFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sFormat2 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        sFormat3 = new SimpleDateFormat("MM.dd-HH:mm:ss");
        sFormat4 = new SimpleDateFormat("yyyyMMddHHmm");
        hourMinuteFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }


    public static String getCurTimeString() {
        return sFormat.format(new Date());
    }

    public static Long getCurTimeString4() {
        return Long.parseLong(sFormat4.format(new Date()));
    }

    public static String getVideoCurTimeString() {
        return sFormat2.format(new Date());
    }

    public static String getCurTimeString2() {
        return sFormat3.format(new Date());
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

    public static String formatTargetDate(String time, String currentPattern, String targetPattern) {
        SimpleDateFormat simpleDF = new SimpleDateFormat(currentPattern, Locale.getDefault());
        try {
            long milliseconds = simpleDF.parse(time).getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(targetPattern, Locale.getDefault());
            return sdf.format(milliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateDes(String time) {
        SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            long milliseconds = simpleDF.parse(time).getTime();
            SimpleDateFormat df = new SimpleDateFormat("HH");
            String str = df.format(milliseconds);
            int a = Integer.parseInt(str);
            if (a >= 0 && a <= 6) {
                System.out.println("凌晨");
                return "凌晨";
            }
            if (a > 6 && a <= 12) {
                System.out.println("上午");
                return "上午";
            }
            if (a > 12 && a <= 13) {
                System.out.println("中午");
                return "中午";
            }
            if (a > 13 && a <= 18) {
                System.out.println("下午");
                return "下午";
            }
            if (a > 18 && a <= 24) {
                System.out.println("晚上");
                return "今晚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMatchTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pattern, Locale.getDefault());

        String desTime = hourMinuteFormatter.format(date);
        long intervalMilli = System.currentTimeMillis() - time;

        if (DateUtils.isToday(time)) {
            return "今天" + desTime;
        }
        if (intervalMilli >= TimeUtil.DAY_IN_MILLIS && intervalMilli < TimeUtil.DAY_IN_MILLIS * 2) {
            return "明天" + desTime;
        }
        return sdf2.format(date);
    }

    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }


}
