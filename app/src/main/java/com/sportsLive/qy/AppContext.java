package com.sportsLive.qy;

import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastBlackStyle;
import com.mob.MobSDK;
import com.tencent.live.TXLiveBase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.CommonAppContext;
import com.yunbao.common.bean.MeiyanConfig;
import com.yunbao.common.http.NetLegacy;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.ChannelUtil;
import com.yunbao.common.utils.DecryptUtil;
import com.yunbao.common.utils.L;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by cxf on 2017/8/3.
 */

public class AppContext extends CommonAppContext {

    public static AppContext sInstance;
    private boolean mBeautyInited;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        NetLegacy.initNet(this);
        //腾讯云鉴权url
        String ugcLicenceUrl = "http://license.vod2.myqcloud.com/license/v1/26aa72debb8cf861fa26e80ca417516c/TXUgcSDK.licence";
        //腾讯云鉴权key
        String ugcKey = "512e9149eccf4e7fbf2e9d21531ed2c8";
        TXLiveBase.getInstance().setLicence(this, ugcLicenceUrl, ugcKey);
        L.setDeBug(BuildConfig.DEBUG);
        AppLog.isPrint = BuildConfig.DEBUG;

        //初始化友盟
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
//6108eb17864a9558e6dacc38  :???不知道是那个友盟用到
        UMConfigure.init(this, "5fb0dadb5f5ec3628817bc3c", ChannelUtil.getChannel(this, "Umeng"), UMConfigure.DEVICE_TYPE_PHONE, "");
        //选择AUTO页面采集模式，统计SDK基础指标无需手动埋点可自动采集。
        //建议在宿主App的Application.onCreate函数中调用此函数。
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        //初始化ShareSdk
        MobSDK.init(this);
        //初始化极光推送
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        //初始化 ARouter
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);

        // 初始化吐司工具类
        ToastUtils.init(this, new ToastBlackStyle(this));
    }

    /**
     * 初始化美狐
     */

    public void initBeautySdk(String beautyKey) {
        if (CommonAppConfig.isYunBaoApp()) {
            beautyKey = DecryptUtil.decrypt(beautyKey);
        }
        CommonAppConfig.getInstance().setBeautyKey(beautyKey);
        if (!TextUtils.isEmpty(beautyKey)) {
            if (!mBeautyInited) {
                mBeautyInited = true;
//                MHSDK.getInstance().init(this, beautyKey);
                CommonAppConfig.getInstance().setTiBeautyEnable(true);

                //根据后台配置设置美颜参数
                MeiyanConfig meiyanConfig = CommonAppConfig.getInstance().getConfig().parseMeiyanConfig();
                int[] dataArray = meiyanConfig.getDataArray();
//                BeautyDataModel.getInstance().setBeautyDataMap(dataArray);

                L.e("美狐初始化------->" + beautyKey);
            }
        } else {
            CommonAppConfig.getInstance().setTiBeautyEnable(false);
        }
    }

}
