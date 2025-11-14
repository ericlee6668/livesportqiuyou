package com.yunbao.main.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.yunbao.common.utils.AndroidBug5497Workaround
import com.yunbao.common.utils.SpUtil
import com.yunbao.main.R
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import com.just.agentweb.WebViewClient


class MyConfigCustomerService(var context: Context, var parentView: ViewGroup) :
    AbsMainViewHolder(context, parentView) {


    var mFrameLayout: FrameLayout? = null;
    var url: String? = ""
    var agentWeb: AgentWeb? = null
    override fun getLayoutId() = R.layout.view_main_my_config_customer_service


    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        mFrameLayout = findViewById(R.id.container_framelayout)
        url = SpUtil.getInstance().getStringValue("cs")

        val mActivity = mContext as Activity
        AndroidBug5497Workaround.assistActivity(mActivity);
        agentWeb = AgentWeb.with(mActivity)
            .setAgentWebParent(mFrameLayout!!, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebViewClient(mWebViewClient)
            //   .setWebChromeClient(mWebChromeClient)
            .createAgentWeb()
            .ready()
            // .go("http://www.baidu.com")
            .get() //初始化

        agentWeb?.let {
//            it.webCreator.webView.loadDataWithBaseURL("file:///android_asset/", htmlString, null, "UTF-8", null);
//            it.webCreator.webView.settings.useWideViewPort = true
            it.webCreator.webView.settings.loadWithOverviewMode = true
            it.webCreator.webView.settings.javaScriptEnabled = true
            it.webCreator.webView.settings.allowContentAccess = true;
            it.webCreator.webView.settings.allowFileAccessFromFileURLs = true
//            it.webCreator.webView.settings.defaultTextEncodingName = "UTF-8"
            it.webCreator.webView.settings.javaScriptCanOpenWindowsAutomatically = true;
            it.webCreator.webView.settings.allowUniversalAccessFromFileURLs =
                true
            it.webCreator.webView.settings.setAppCacheEnabled(false)//缓存？
            it.webCreator.webView.settings.databaseEnabled = false
            //辅助WebView处理图片上传操作
            // it.webCreator.webView.webChromeClient = mBaseWebChromeClient;
            it.webCreator.webView.settings.domStorageEnabled = true;
            //支持内容重新布局
            it.webCreator.webView.settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            //支持自动加载图片
            it.webCreator.webView.settings.loadsImagesAutomatically = true;
            it.webCreator.webView.settings.useWideViewPort = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.webCreator.webView.settings.mixedContentMode =
//                                        WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            };
            it.webCreator.webView.settings.cacheMode =
                WebSettings.LOAD_NO_CACHE;
            it.webCreator.webView.isDrawingCacheEnabled = true
            it.webCreator.webView.isAlwaysDrawnWithCacheEnabled = true
            it.webCreator.webView.settings.setSupportZoom(false)//支持缩放
            it.webCreator.webView.isEnabled = true
            it.webCreator.webView.settings.javaScriptCanOpenWindowsAutomatically =
                true
            it.webCreator.webView.settings.allowFileAccess = true
            it.webCreator.webView.settings.allowContentAccess = true
            it.webCreator.webView.settings.savePassword = true
            it.webCreator.webView.settings.builtInZoomControls = true
            it.webCreator.webView.clearCache(true)
            it.webCreator.webView.clearHistory()
            it.webCreator.webView.requestFocus()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.webCreator.webView.settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }

            it.webCreator.webView.settings.setSupportMultipleWindows(true)

            it.webCreator.webView.loadUrl(url)

        }
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
//            super.onReceivedSslError(view, handler, error)
            handler?.proceed();// 接受所有网站的证书
        }

    }
}