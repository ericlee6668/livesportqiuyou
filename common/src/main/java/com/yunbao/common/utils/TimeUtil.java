package com.yunbao.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final long SECOND_IN_MILLIS = 1000;
    private static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    private static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;

    private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

    //将时间转换为时间戳
    public static String dateToStamp(String s) throws Exception {
        String res;//设置时间格式，将该时间格式的时间转换为时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long time = date.getTime();
        res = String.valueOf(time);
        return res;
    }

    //将时间戳转换为时间
    public static String stampToTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = Long.parseLong(s);//将时间戳转换为时间
        Date date = new Date(lt); //将时间调整为yyyy-MM-dd HH:mm:ss时间样式
        res = simpleDateFormat.format(date);
        return res;
    }

    //将时间戳转换为时间
    public static String stampToTimeMM(String s) throws Exception {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long lt = new Long(s);//将时间戳转换为时间
        Date date = new Date(lt); //将时间调整为yyyy-MM-dd HH:mm:ss时间样式
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String timeToHour(long timeStamp) {
        return HOUR_FORMAT.format(new Date(timeStamp));
    }

    public static CharSequence formatDuration(long millis, boolean needHour) {
        int hours = 0;
        int minutes = 0;
        long remainTimes = millis;
        if (remainTimes >= HOUR_IN_MILLIS) {
            hours = (int) (remainTimes / HOUR_IN_MILLIS);
            remainTimes %= HOUR_IN_MILLIS;
        }
        if (remainTimes >= MINUTE_IN_MILLIS) {
            minutes = (int) (remainTimes / MINUTE_IN_MILLIS);
            remainTimes %= MINUTE_IN_MILLIS;
        }

        int seconds = (int) (remainTimes / SECOND_IN_MILLIS);
        if (hours == 0 && !needHour) {
            return (minutes >= 10 ? minutes : "0" + minutes) + ":" +
                    (seconds > 10 ? seconds : "0" + seconds);
        }
        return (hours >= 10 ? hours : "0" + hours) + ":" + (minutes >= 10 ? minutes : "0" + minutes) + ":" +
                (seconds >= 10 ? seconds : "0" + seconds);
    }
}
