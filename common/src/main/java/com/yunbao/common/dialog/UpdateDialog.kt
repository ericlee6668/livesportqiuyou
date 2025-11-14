package com.yunbao.common.dialog

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.inf.IEntity
import com.arialyy.aria.core.task.DownloadTask
import com.yunbao.common.R
import com.yunbao.common.bean.ConfigBean
import com.yunbao.common.utils.*
import java.io.File

class UpdateDialog(
    var mVersion: ConfigBean,
    val mProcessResultUtil: ProcessResultUtil
) : AbsDialogFragment(),
    View.OnClickListener {
    var updateContent: TextView? = null
    private var progressBar: ProgressBar? = null
    private var mTaskId: Long = -1
    private var tvConfirm: TextView? = null
    private var icClose: ImageView? = null
    private var tvCancel: TextView? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_version_update
    }

    override fun getDialogStyle(): Int {
        return R.style.update_dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateContent = findViewById<View>(R.id.updateContent) as TextView
        progressBar = findViewById<View>(R.id.pb) as ProgressBar
        tvConfirm = findViewById<View>(R.id.tv_confirm) as TextView
        icClose = findViewById<View>(R.id.ic_delete) as ImageView
        tvCancel = findViewById<View>(R.id.tv_cancel) as TextView
        icClose!!.setOnClickListener(this)
        tvConfirm?.setOnClickListener(this)
        tvCancel!!.setOnClickListener(this)
        initDownload()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tv_confirm) {
            if (mTaskId == -1L) {
                mProcessResultUtil.requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    mTaskId = Aria.download(this@UpdateDialog)
                        .load(mVersion.downloadApkUrl)
                        .setFilePath(
                            Environment.getExternalStorageDirectory().absolutePath + "/" + context?.getString(
                                R.string.app_name
                            )
                                    + mVersion.version
                                    + ".apk"
                        )
                        .ignoreFilePathOccupy()
                        .ignoreCheckPermissions()
                        .create()
                    tvConfirm?.text = getString(R.string.update_downloading, "0%")
                    tvConfirm?.isEnabled = false
                }
            } else {
                if (Aria.download(this).load(mTaskId).taskExists()) {
                    if (Aria.download(this).load(mTaskId).entity.isComplete) {
                        SystemUtil.installApk(
                            activity,
                            File(Aria.download(this).load(mTaskId).entity.filePath)
                        )
                    } else {
                        Aria.download(this).load(mTaskId).resume()
                        tvConfirm?.text = getString(
                            R.string.update_downloading,
                            Aria.download(this).load(mTaskId).entity.percent.toString() + "%"
                        )
                        tvConfirm?.isEnabled = false
                    }
                }
            }
        } else if (id == R.id.ic_delete) {
            if (Aria.download(this).load(mTaskId).taskState == IEntity.STATE_RUNNING) {
                ToastUtil.show(mContext.getString(R.string.background_update))
            }
            dismiss()
        } else if (id == R.id.tv_cancel) {
            if (Aria.download(this).load(mTaskId).taskState == IEntity.STATE_RUNNING) {
                Aria.download(this).load(mTaskId).stop()
            }
            dismiss()
        }
    }

    private fun initDownload() {
        updateContent?.text = mVersion.updateDes
        val entity = Aria.download(this).getFirstDownloadEntity(mVersion.downloadApkUrl)
        var file: File? = null
        if (entity != null && !TextUtils.isEmpty(entity.filePath)) {
            file = File(entity.filePath)
        }
        if (file != null && file.exists() && entity.fileSize == file.length()) {
            val state = entity.state
            mTaskId = entity.id
            if (state != IEntity.STATE_RUNNING) {
                progressBar!!.progress = entity.percent
                tvConfirm?.text = "继续更新"
            } else {
                tvConfirm?.isEnabled = false
                tvConfirm?.text =
                    getString(R.string.update_downloading, entity.percent.toString() + "%")
            }
            if (state == IEntity.STATE_COMPLETE) {
                progressBar!!.progress = entity.percent
                tvConfirm?.text = "立即安装"
            }
        } else {
            tvConfirm?.text = "立即更新"
            if (Aria.download(this).load(mTaskId).taskExists()) {
                Aria.download(this).load(mTaskId).cancel(true)
                L.e("安装文件已损坏，重新下载")
            }
        }
        if (mVersion.forceUpdate == 1) {
            icClose!!.visibility = View.GONE
            tvCancel!!.visibility = View.GONE
            isCancelable = false
        } else {
            icClose!!.visibility = View.VISIBLE
            tvCancel!!.visibility = View.VISIBLE
        }
        Aria.download(this).register()
    }

    @Download.onTaskPre
    fun onTaskPre(task: DownloadTask?) {
    }

    @Download.onTaskStop
    fun onTaskStop(task: DownloadTask?) {
        tvConfirm?.text = "继续更新"
        tvConfirm?.isEnabled = true
    }

    @Download.onTaskFail
    fun onTaskFail(task: DownloadTask?) {
        ToastUtil.show("下载失败")
        tvConfirm?.text = "继续更新"
        tvConfirm?.isEnabled = true
    }

    @Download.onTaskCancel
    fun onTaskCancel(task: DownloadTask?) {
        progressBar!!.progress = 0
    }

    @Download.onTaskRunning
    fun onTaskRunning(task: DownloadTask) {
        val len = task.fileSize
        if (len == 0L) {
            progressBar!!.progress = 0
        } else {
            progressBar!!.progress = task.percent
            val progress = task.percent.toString() + "%"
            tvConfirm?.text = mContext.getString(R.string.update_downloading, progress)
        }
        L.e("更新进度:", task.percent.toString() + "")
    }

    @Download.onTaskComplete
    fun onTaskComplete(task: DownloadTask) {
        ToastUtil.show("下载完成")
        tvConfirm?.text = "立即安装"
        progressBar!!.progress = 100
        tvConfirm?.isEnabled = true
        SystemUtil.installApk(activity, File(task.downloadEntity.filePath))
    }

    override fun canCancel(): Boolean {
        return false
    }

    override fun setWindowAttributes(window: Window) {
        try {
            val windowManager =
                requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displaymetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displaymetrics)
            val layoutParams = window.attributes
            layoutParams.width = displaymetrics.widthPixels
            layoutParams.height = displaymetrics.heightPixels
            layoutParams.gravity = layoutParams.gravity or Gravity.CENTER
            window.attributes = layoutParams
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Aria.download(this).unRegister()
    }


}