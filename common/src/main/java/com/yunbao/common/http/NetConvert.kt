package com.yunbao.common.http

import com.alibaba.fastjson.JSONObject
import com.drake.net.convert.DefaultConvert
import com.drake.net.error.RequestParamsException
import com.drake.net.error.ResponseException
import com.drake.net.error.ServerResponseException
import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.exception.ParseError
import java.lang.reflect.Type

class NetConvert : DefaultConvert(success = "200", code = "ret") {
    override fun <S> convert(
        succeed: Type,
        request: Request,
        response: Response,
        cache: Boolean
    ): S? {
        val body = response.body().string()
        response.log = body  // 日志记录响应信息
        val code = response.code()

        when {
            code in 200..299 -> { // 请求成功
                val url = request.url().toString()
                if (succeed === String::class.java && !url.contains("service=Test.test")) return body as S
                val jsonObject = org.json.JSONObject(body) // 获取JSON中后端定义的错误码和错误信息
                if (jsonObject.getString(this.code) == success) { // 对比后端自定义错误码
                    if (succeed === String::class.java) return body as S
                    val data = jsonObject.getJSONObject("data")
                    val info = data.getJSONArray("info")
                    val dataCode = data.getInt("code")
                    return if (dataCode == 0 && info != null && info.length() > 0) {
                        val toString = info[0].toString()
                        toString.parseBody(succeed)
                    } else null
                } else { // 错误码匹配失败, 开始写入错误异常
                    throw ResponseException(code, jsonObject.getString(message), request, body)
                }
            }
            code in 400..499 -> throw RequestParamsException(code, request) // 请求参数错误
            code >= 500 -> throw ServerResponseException(code, request) // 服务器异常错误
            else -> throw ParseError(request)
        }
    }

    override fun <S> String.parseBody(succeed: Type): S? {
        return JSONObject.parseObject(this, succeed)
    }
}