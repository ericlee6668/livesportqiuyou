package com.yunbao.common.http

import android.app.Application
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.initNet
import com.drake.net.transform.transform
import com.drake.net.utils.fastest
import com.drake.net.utils.scopeNet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import com.yunbao.common.BuildConfig
import com.yunbao.common.CommonAppConfig
import com.yunbao.common.SerializeConfig
import com.yunbao.common.bean.ConfigBean
import com.yunbao.common.interfaces.CommonCallback
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.TimeUnit

/**
 * 动态域名
 */
object NetLegacy {

    @JvmStatic
    fun initNet(app: Application) {
        MMKV.initialize(app)
        app.initNet("") {
            converter(NetConvert())
            connectionTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
        }
    }

    /**
     * 并发检查最快域名
     */
    @JvmStatic
    fun getTest(callback: CommonCallback<ConfigBean>) {
        scopeNet {
            try {
                fastestDomain(this, CommonAppConfig.HOSTS)
            } catch (e: Exception) {
                try {
                    val domains = Get<String>(BuildConfig.GITEE, absolutePath = true).await().toJsonArray<String>()
                    fastestDomain(this, domains)
                } catch (e: Exception) {
                    val domains = Get<String>(BuildConfig.GITLAB, absolutePath = true).await().toJsonArray<String>()
                    fastestDomain(this, domains)
                }
            }
            callback.callback(null)
        }.finally {
            if (it != null) callback.callback(null)
        }
    }

    inline fun <reified T> String.toJsonArray(): MutableList<T> {
        return Gson().fromJson(this, TypeToken.getParameterized(List::class.java, T::class.java).type)
    }

    private suspend fun fastestDomain(scope: CoroutineScope, domains: List<String>) {
        val taskList = domains.map { host ->
            scope.Get<String>("${host}/?service=Test.test", absolutePath = true).transform {
                NetConfig.host = host
                CommonAppConfig.setHost(host)
                it
            }
        }
        scope.fastest(taskList, 0)
    }


    /**
     * 并发获取config
     */
    @JvmStatic
    fun getConfig(callback: CommonCallback<ConfigBean>) {
        scopeNet {
//            val taskList = CommonAppConfig.HOSTS.map { host ->
//                Get<ConfigBean>("${host}/?service=Home.getConfig", absolutePath = true).transform {
//                    NetConfig.host = host
//                   CommonAppConfig.setHost(host)
//                    it
//                }
//            }
//            val result = fastest(taskList, 0)
//            CommonAppConfig.getInstance().config = result
            // CommonAppConfig.getInstance().setLevel(JSONObject.toJSONString(result.level))
            // CommonAppConfig.getInstance().setAnchorLevel(JSONObject.toJSONString(result.levelanchor))
            // SpUtil.getInstance().setStringValue(SpUtil.CONFIG, JSONObject.toJSONString(result))
            // WordFilterUtil.getInstance().initWordMap(result.sensitive_words)
//            callback.callback(result)
        }.finally {
            if (it != null) callback.callback(null)
        }
    }
}