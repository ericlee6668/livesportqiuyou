package com.yunbao.live.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yunbao.live.R
import com.yunbao.live.bean.MatchLiveBean

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/6 9:59
 * @package: com.yunbao.live.adapter
 * Description：TODO
 */
class RelativeLiveAdapter(data: MutableList<MatchLiveBean>) : BaseQuickAdapter<MatchLiveBean, BaseViewHolder>(R.layout.item_home_recommend, data) {
    var options = RequestOptions()
            .placeholder(R.mipmap.icon_image_loading)
            .error(R.mipmap.icon_load_failed)

    override fun convert(helper: BaseViewHolder, item: MatchLiveBean?) {
        item?.let {
            helper.setText(R.id.tv_game_name, it.title)
                    .setText(R.id.tv_game_des, it.user?.user_nicename)
                    .setText(R.id.tv_main_view_num, it.user?.viewnum)
            val imgMain = helper.getView<ImageView>(R.id.iv_main)
            val imgPlaying = helper.getView<ImageView>(R.id.iv_palying)
            Glide.with(imgMain.context)
                    .load(it.thumb)
                    .apply(options)
                    .into(imgMain)
            if (!TextUtils.isEmpty(it.pull)) {
                imgPlaying.visibility = View.VISIBLE
                Glide.with(mContext).load(R.drawable.gif_main_live_play)
                        .placeholder(R.mipmap.icon_item_living)
                        .error(R.mipmap.icon_item_living)
                        .into(imgPlaying)
            } else {
                imgPlaying.visibility = View.GONE
            }
        }
    }
}