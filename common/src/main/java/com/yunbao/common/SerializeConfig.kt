package com.yunbao.common

import com.drake.serialize.serialize.serial

object SerializeConfig {
    @JvmStatic
    var domains: List<String> by serial(CommonAppConfig.HOSTS)
}