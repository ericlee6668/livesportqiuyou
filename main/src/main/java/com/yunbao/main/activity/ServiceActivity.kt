package com.yunbao.main.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.yunbao.common.CommonAppConfig
import com.yunbao.common.activity.AbsActivity
import com.yunbao.main.R

/**
 * //TODO 如果是 从 个人设置进入 要通过 type 来判断  是 隐私 还是 服务 ，默认是 服务协议
 */

class ServiceActivity : AbsActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_service
    }

    var me:String ="1"
    override fun main() {
        super.main()
        val btn_back=findViewById<ImageView>(R.id.btn_back)
        btn_back.setOnClickListener{
            finish()
        }

        val  tv_content=findViewById<TextView>(R.id.tv_content)
        var tv_title_head =findViewById<TextView>(R.id.tv_title_head)
        me=intent.getStringExtra("me")
        if (me == "2"){
            tv_title_head.text="球友服务政策"
            //getSite_privacy_policy
            val getSite_privacy_policy = CommonAppConfig.getInstance().config.site_user_agreement //服务
            var msConten: Spanned? = null
            if (!TextUtils.isEmpty(getSite_privacy_policy)) {
                msConten = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(
                        getSite_privacy_policy.trim { it <= ' ' },
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    Html.fromHtml(getSite_privacy_policy.trim { it <= ' ' })
                }
                tv_content.text = msConten
            }
            return
        }
        tv_title_head.text="球友隐私政策"
        val getSite_privacy_policy = CommonAppConfig.getInstance().config.site_privacy_policy //隐私政策
        var msConten: Spanned? = null
        if (!TextUtils.isEmpty(getSite_privacy_policy)) {
            msConten = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(
                    getSite_privacy_policy.trim { it <= ' ' },
                    Html.FROM_HTML_MODE_LEGACY
                )
            } else {
                Html.fromHtml(getSite_privacy_policy.trim { it <= ' ' })
            }
            tv_content.text = msConten
        }


    }

}