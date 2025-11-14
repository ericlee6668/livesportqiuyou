package com.yunbao.common.utils;
import com.tencent.cos.xml.utils.StringUtils;
import com.yunbao.common.CommonAppConfig;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by cxf on 2018/9/28.
 */

public class StringUtil {
    private static DecimalFormat sDecimalFormat;
    private static DecimalFormat sDecimalFormat2;
    private static DecimalFormat sDecimalFormat3;
    // private static Pattern sPattern;
    private static Pattern sIntPattern;
    private static Random sRandom;
    private static StringBuilder sStringBuilder;


    static {
        sDecimalFormat = new DecimalFormat("#.#");

        sDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        sDecimalFormat2 = new DecimalFormat("#.##");
        sDecimalFormat2.setRoundingMode(RoundingMode.DOWN);
        sDecimalFormat3 = new DecimalFormat("#");
        sDecimalFormat3.setRoundingMode(RoundingMode.HALF_UP);
        //sPattern = Pattern.compile("[\u4e00-\u9fa5]");
        sIntPattern = Pattern.compile("^[-\\+]?[\\d]*$");
        sRandom = new Random();
        sStringBuilder = new StringBuilder();
    }

    public static String format(double value) {
        return sDecimalFormat.format(value);
    }

    public static String formatDouble(double value) {
        return sDecimalFormat3.format(value);
    }

    public static String formatDouble2(double num) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
        //去除小数点后多余的0
        BigDecimal bigDecimal = new BigDecimal(df.format(num));
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    /**
     * 把数字转化成多少万
     */
    public static String toWan(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat.format(num / 10000d) + "W";
    }


    /**
     * 把数字转化成多少万
     */
    public static String toWan2(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat.format(num / 10000d);
    }

    /**
     * 把数字转化成多少万
     */
    public static String toWan3(long num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        return sDecimalFormat2.format(num / 10000d) + "w";
    }

//    /**
//     * 判断字符串中是否包含中文
//     */
//    public static boolean isContainChinese(String str) {
//        Matcher m = sPattern.matcher(str);
//        if (m.find()) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 判断一个字符串是否是数字
     */
    public static boolean isInt(String str) {
        return sIntPattern.matcher(str).matches();
    }


    /**
     * 把一个long类型的总毫秒数转成时长
     */
    public static String getDurationText(long mms) {
        int hours = (int) (mms / (1000 * 60 * 60));
        int minutes = (int) ((mms % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) ((mms % (1000 * 60)) / 1000);
        sStringBuilder.delete(0, sStringBuilder.length());
        if (hours > 0) {
            if (hours < 10) {
                sStringBuilder.append("0");
            }
            sStringBuilder.append(String.valueOf(hours));
            sStringBuilder.append(":");
        }
        if (minutes > 0) {
            if (minutes < 10) {
                sStringBuilder.append("0");
            }
            sStringBuilder.append(String.valueOf(minutes));
            sStringBuilder.append(":");
        } else {
            sStringBuilder.append("00:");
        }
        if (seconds > 0) {
            if (seconds < 10) {
                sStringBuilder.append("0");
            }
            sStringBuilder.append(String.valueOf(seconds));
        } else {
            sStringBuilder.append("00");
        }
        return sStringBuilder.toString();
    }


    /**
     * 设置视频输出路径
     */
    public static String generateVideoOutputPath() {
        String outputDir = CommonAppConfig.VIDEO_PATH;
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        String videoName = DateFormatUtil.getVideoCurTimeString() + sRandom.nextInt(9999);
        return contact(outputDir, "android_", CommonAppConfig.getInstance().getUid(), "_", videoName, ".mp4");
    }


    /**
     * 获取随机文件名
     */
    public static String generateFileName() {
        return contact("android_",
                CommonAppConfig.getInstance().getUid(),
                "_",
                DateFormatUtil.getVideoCurTimeString(),
                String.valueOf(sRandom.nextInt(9999)));
    }


    /**
     * 多个字符串拼接
     */
    public static String contact(String... args) {
        sStringBuilder.delete(0, sStringBuilder.length());
        for (String s : args) {
            sStringBuilder.append(s);
        }
        return sStringBuilder.toString();
    }


    /*比较字符串*/
    public static boolean compareString(String var1, String var2) {
        if (var1 == null && var2 == null) {
            return true;
        } else if (var1 != null && var2 != null) {
            return var1.equals(var2);
        } else {
            return false;
        }
    }


    public static String formatBigNum(String num) {
        if (StringUtils.isEmpty(num)) {
            // 数据为空直接返回0
            return "0";
        }
        try {
            StringBuffer sb = new StringBuffer();
            if (!StringUtil.isNumeric(num)) {
                // 如果数据不是数字则直接返回0
                return "0";
            }


            BigDecimal b0 = new BigDecimal("1000");
            BigDecimal b1 = new BigDecimal("10000");
            BigDecimal b2 = new BigDecimal("100000000");
            BigDecimal b3 = new BigDecimal(num);

            String formatedNum = "";//输出结果
            String unit = "";//单位

            if (b3.compareTo(b0) == -1) {
                sb.append(b3.toString());
            } else if ((b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1)
                    || b3.compareTo(b1) == -1) {
                formatedNum = b3.divide(b0).toString();
                unit = "k";
            } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
                    || b3.compareTo(b2) == -1) {
                formatedNum = b3.divide(b1).toString();
                unit = "w";
            } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
                formatedNum = b3.divide(b2).toString();
                unit = "亿";
            }
            if (!"".equals(formatedNum)) {
                sb.append(formatedNum).append(unit);
//                int i = formatedNum.indexOf(".");
//                if (i == -1) {
//                    sb.append(formatedNum).append(unit);
//                } else {
//                    i = i + 1;
//                    String v = formatedNum.substring(i, i + 1);
//                    if (!v.equals("0")) {
//                        sb.append(formatedNum.substring(0, i + 1)).append(unit);
//                    } else {
//                        sb.append(formatedNum.substring(0, i - 1)).append(unit);
//                    }
//                }
            }
            if (sb.length() == 0)
                return "0";
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return num;
        }
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
