package com.yunbao.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.yunbao.common.R;

import java.io.File;

public class SystemUtil {

    public static String getDeviceId(){
        String deviceId= "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits

        return deviceId;
    }
    public static void installApk(Activity activity, File file) {
        //有权限，开始安装应用程序
        if (getPermission(activity)) openInstallApk(activity, file);
    }

    public static boolean getPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            boolean haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                new AlertDialog.Builder(activity)
                        .setMessage("安装应用需要打开未知来源权限，请去设置中开启权限")
                        .setTitle("请打开安装权限")
                        .setPositiveButton(activity.getString(R.string.confirm), (dialog, which) -> {
                            startInstallPermissionSettingActivity(activity);
                            dialog.dismiss();
                        }).setCancelable(false).setNegativeButton(activity.getString(R.string.cancel), null).show();
                return false;
            }
        }
        return true;
    }

    public static void openInstallApk(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Activity activity) {
        Uri packageUri = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, 1);
    }
}
