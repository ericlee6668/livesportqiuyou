package com.yunbao.common.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.FileProvider
import com.yunbao.common.R
import com.yunbao.common.event.CloseEvent
import com.yunbao.common.utils.AppWebWorkaround
import com.yunbao.common.utils.FileUtils
import com.yunbao.common.utils.ProcessResultUtil
import com.yunbao.common.utils.SoftKeyBoardUtils
import kotlinx.android.synthetic.main.activity_dialog_customer_service.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*


/**
 * 客服弹窗
 */
class CustomerServiceDialogActivity : AbsActivity() {

    private var mProcessResultUtil: ProcessResultUtil?=null
    private var mCameraFilePath=""
    private val REQUEST_CODE_FILE_CHOOSER=1
    private var mUploadCallBackAboveL: ValueCallback<Array<Uri>>?=null
    private var mUploadCallBack: ValueCallback<Uri>?=null
    private var url = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_dialog_customer_service
    }

    override fun main() {
        super.main()
        initView()
        initClick()
        mProcessResultUtil = ProcessResultUtil(this)
        AppWebWorkaround.assistActivity(this)
    }

    override fun onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
        }

        super.onBackPressed()
    }

    override fun finish() {
        SoftKeyBoardUtils.hideKeyBoard(this, vCustomerEmpty)
        EventBus.getDefault().post(CloseEvent())
        super.finish()
    }

    private fun chooseImage() {
        if (mProcessResultUtil == null) {
            mProcessResultUtil = ProcessResultUtil(this)
        }
        mProcessResultUtil!!.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            showFileChooser()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {

        url = intent.getStringExtra("url")

        val tvTitle = findViewById<TextView>(R.id.titleView)
        tvTitle.text = "客服"
        mWebView?.settings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = true
            setAppCacheEnabled(true)
            if (Build.VERSION.SDK_INT >= 21) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        mWebView?.loadUrl(url)
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    pb.visibility = View.INVISIBLE
                } else {
                    pb.visibility = View.VISIBLE
                    pb.progress = newProgress
                }
            }

            // For Android < 3.0
            fun openFileChooser(valueCallback: ValueCallback<Uri>) {
                mUploadCallBack = valueCallback
                chooseImage()
            }

            // For Android  >= 3.0
            fun openFileChooser(valueCallback: ValueCallback<Uri>, acceptType: String?) {
                mUploadCallBack = valueCallback
                chooseImage()
            }

            //For Android  >= 4.1
            fun openFileChooser(
                    valueCallback: ValueCallback<Uri>,
                    acceptType: String?,
                    capture: String?
            ) {
                mUploadCallBack = valueCallback
                chooseImage()
            }

            // For Android >= 5.0
            override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams?
            ): Boolean {
                mUploadCallBackAboveL = filePathCallback
                chooseImage()
                return true
            }
        }
        initBtn()
    }

    private fun initBtn() {
        btnRight.setOnClickListener {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initClick() {
        vw_close_customer_service.setOnTouchListener { _: View?, _: MotionEvent ->
            finish()
            false
        }

        vw_close_customer_service.setOnClickListener { finish() }
        vCustomerEmpty.setOnClickListener { finish() }
    }

    /**
     * 打开选择文件/相机
     */
    private fun showFileChooser() {

//        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//        intent1.setDataAndType(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        val intent1 = Intent(Intent.ACTION_GET_CONTENT)
        intent1.addCategory(Intent.CATEGORY_OPENABLE)
        intent1.type = "*/*"
        val intent2 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mCameraFilePath = Environment.getExternalStorageDirectory().absolutePath + File.separator +
                System.currentTimeMillis().toString() + ".jpg"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // android7.0注意uri的获取方式改变
            val photoOutputUri = FileProvider.getUriForFile(
                    this@CustomerServiceDialogActivity, "$packageName.fileProvider",
                    File(mCameraFilePath)
            )
            intent2.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri)
        } else {
            intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(mCameraFilePath)))
        }
        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser")
        chooser.putExtra(Intent.EXTRA_INTENT, intent1)
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent2))
        startActivityForResult(chooser, REQUEST_CODE_FILE_CHOOSER)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FILE_CHOOSER) {
            var result = if (data == null || resultCode != RESULT_OK) null else data.data
            if (result == null && !TextUtils.isEmpty(mCameraFilePath)) {
                // 看是否从相机返回
                val cameraFile: File = File(mCameraFilePath)
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile)
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result))
                }
            }
            if (result != null) {
                val path: String = FileUtils.getPath(this, result)
                if (!TextUtils.isEmpty(path)) {
                    val f = File(path)
                    if (f.exists() && f.isFile) {
                        val newUri = Uri.fromFile(f)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (mUploadCallBackAboveL != null) {
                                if (newUri != null) {
                                    mUploadCallBackAboveL!!.onReceiveValue(arrayOf(newUri))
                                    mUploadCallBackAboveL = null
                                    return
                                }
                            }
                        } else if (mUploadCallBack != null) {
                            if (newUri != null) {
                                mUploadCallBack!!.onReceiveValue(newUri)
                                mUploadCallBack = null
                                return
                            }
                        }
                    }
                }
            }
            clearUploadMessage()
            return
        }
    }

    /**
     * webview没有选择文件也要传null，防止下次无法执行
     */
    private fun clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL!!.onReceiveValue(null)
            mUploadCallBackAboveL = null
        }
        if (mUploadCallBack != null) {
            mUploadCallBack!!.onReceiveValue(null)
            mUploadCallBack = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView.destroy()
    }
}