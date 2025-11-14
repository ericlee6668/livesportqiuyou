package com.yunbao.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.R;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.dialog.NotCancelableDialog;
import com.yunbao.common.dialog.UpdateDialog;

/**
 * Created by cxf on 2017/10/9.
 */

public class VersionUtil {

    private static String sVersion;

    /**
     * 是否是最新版本
     */
    public static boolean isLatest(String version) {
        if (TextUtils.isEmpty(version)) {
            return true;
        }
        String curVersion = getVersion();
        int compareVersion = compareVersion(version, curVersion);
        //0代表相等，1代表version1大于version2，-1代表version1小于version2
        return compareVersion != 1;
    }


    public static void showDialog(final ProcessResultUtil mProcessResultUtil,Context context,  ConfigBean configBean, final String downloadUrl) {
        if (!TextUtils.isEmpty(downloadUrl)) {
            try {
                UpdateDialog  updateDialog =  new UpdateDialog(configBean,mProcessResultUtil);
                updateDialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"updateDialog");

            } catch (Exception e) {
                ToastUtil.show(R.string.version_download_url_error);
            }
        } else {
            ToastUtil.show(R.string.version_download_url_error);
        }

    }

    /**
     * 获取版本号
     */
    public static String getVersion() {
        if (TextUtils.isEmpty(sVersion)) {
            try {
                PackageManager manager = CommonAppContext.sInstance.getPackageManager();
                PackageInfo info = manager.getPackageInfo(CommonAppContext.sInstance.getPackageName(), 0);
                sVersion = info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sVersion;
    }

    /**
     * 版本号比较
     * 0代表相等，1代表version1大于version2，-1代表version1小于version2
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
