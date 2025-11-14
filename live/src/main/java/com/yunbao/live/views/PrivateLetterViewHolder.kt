package com.yunbao.live.views

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.yunbao.common.activity.CustomerServiceDialogActivity
import com.yunbao.common.http.HttpCallback
import com.yunbao.common.utils.AppLog
import com.yunbao.common.views.AbsViewHolder
import com.yunbao.live.R
import com.yunbao.live.bean.LiveInfoContactBean
import com.yunbao.live.http.LiveHttpUtil

class PrivateLetterViewHolder(
    context: Context?,
    parentView: ViewGroup?
) : AbsViewHolder(context, parentView) {
    private var noDataView: View? = null
    private var mLiveUid = ""
    private var mStream = ""
    override fun getLayoutId(): Int {
        return R.layout.view_live_web_view
    }

    override fun init() {
        findViewById<RelativeLayout>(R.id.rl).visibility = View.GONE
        noDataView = findViewById(R.id.view_live_no_data)
        noDataView?.findViewById<TextView>(R.id.message)?.text = "暂无私信"
    }

    fun setLiveInfo(liveUid: String, stream: String) {
        this.mLiveUid = liveUid
        this.mStream = stream
        getData()
    }

    private fun getData() {
        LiveHttpUtil.getLiveInfoContact(mLiveUid, mStream, object : HttpCallback() {
            override fun onSuccess(code: Int, msg: String, info: Array<String>) {
                AppLog.e("code:" + code + "info:" + info.contentToString())
                noDataView?.visibility = View.GONE
                if (code == 0) {
                    val list = JSON.parseArray(
                        info.contentToString(),
                        LiveInfoContactBean::class.java
                    )
                    if (list != null && list.isNotEmpty()) {
                        list[0].live_im?.let {
                            if (it.status == "1") {
                                if (it.json != null) {
                                    val url = it.json.im_url ?: ""
                                    val intent =
                                        Intent(mContext, CustomerServiceDialogActivity::class.java)
                                    intent.putExtra("url", url)
                                    mContext.startActivity(intent)
                                } else {
                                    noDataView?.visibility = View.VISIBLE
                                }

                            }else{
                                noDataView?.visibility = View.VISIBLE
                            }
                        }?:let {
                            noDataView?.visibility = View.VISIBLE
                        }

                    } else {
                        noDataView?.visibility = View.VISIBLE
                    }
                }
            }

            override fun onError(ret: Int, msg: String?) {
                super.onError(ret, msg)
                noDataView?.visibility = View.VISIBLE
            }


        })
    }


}