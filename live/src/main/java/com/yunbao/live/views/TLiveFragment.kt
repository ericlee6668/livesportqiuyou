package com.yunbao.live.views

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.yunbao.common.bean.TLive
import com.yunbao.live.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/1 16:03
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class TLiveFragment : Fragment() {
    private lateinit var rootView: View
    private var liveList: MutableList<TLive>? = null
    private var home: String? = null
    private var away: String? = null

    companion object {
        fun create(home: String?, away: String?): TLiveFragment {
            return TLiveFragment().apply {
                arguments = Bundle().apply {
                    putString("home", home)
                    putString("away", away)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            home = it.getString("home")
            away = it.getString("away")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!this::rootView.isInitialized) {
            rootView = inflater.inflate(R.layout.layout_football_match_live, container, false)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.footballLiveList)
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, true)
        displayLive()
    }

    fun attachLiveData(data: MutableList<TLive>?) {
        this.liveList = data
        displayLive()
    }

    fun insertLive(data: MutableList<TLive>?) {
        if (data == null) return
        this.liveList = data
        displayLive()
    }

    private fun displayLive() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.footballLiveList)
        view?.findViewById<TextView>(R.id.txtHome)?.text = home
        view?.findViewById<TextView>(R.id.txtAway)?.text = away
        recyclerView?.let {
            val itemDecorationCount = it.itemDecorationCount
            if (itemDecorationCount == 0) {
                it.addItemDecoration(TimeDecoration(it.context))
            }

            var adapter: LiveAdapter? = it.adapter as LiveAdapter?
            if (adapter == null) {
                adapter = LiveAdapter(liveList)
                recyclerView.adapter = adapter
            } else {
                liveList?.let { list -> adapter.setNewDiffData(LiveCallback(list)) }
            }
            showEmptyView(adapter.itemCount == 0)
        } ?: kotlin.run { showEmptyView(true) }
    }

    private fun showEmptyView(show: Boolean) {
        view?.findViewById<View>(R.id.footballLiveEmpty)?.visibility = if (show) View.VISIBLE else View.GONE
        view?.findViewById<View>(R.id.footballLiveContainer)?.visibility = if (show) View.GONE else View.VISIBLE
    }

    private class LiveAdapter(data: MutableList<TLive>?) : BaseQuickAdapter<TLive, BaseViewHolder>(R.layout.item_match_live, data) {

        override fun convert(helper: BaseViewHolder, item: TLive?) {
            helper.setText(R.id.txtTime, item?.time.toString())
                    .setText(R.id.txtContent, item?.data)
            val imgEvent = helper.getView<ImageView>(R.id.imgEvent)
            imgEvent.setImageResource(getEventRes(item?.type))
        }

        @DrawableRes
        private fun getEventRes(type: Int?): Int {
            return when (type) {
                1 -> R.drawable.icon_football_goal
                2 -> R.drawable.icon_corner_kick
                3 -> R.drawable.icon_yellow_card
                4 -> R.drawable.icon_red_card
                6 -> R.drawable.icon_free_kick
                9 -> R.drawable.icon_football_substitution
                15 -> R.drawable.icon_yellow_red
                17 -> R.drawable.icon_own_goals
                else -> 0
            }
        }
    }

    private class TimeDecoration(context: Context) : RecyclerView.ItemDecoration() {

        private var itemLeftPadding = 0
        private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.game_time_tag)
        private val path = Path()
        private val dashPaint: Paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = ContextCompat.getColor(context, R.color.live_gary)
            pathEffect = DashPathEffect(floatArrayOf(4F, 4F), 0F)
        }
        private val paint = Paint().apply {
            isAntiAlias = true
            itemLeftPadding = context.resources.getDimensionPixelOffset(R.dimen.dp10) + bitmap.width
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(itemLeftPadding, 0, 0, 0)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)

            val childCount = parent.childCount

            if (childCount > 1) {
                val firstChild = parent.getChildAt(0)
                val lastChild = parent.getChildAt(childCount - 1)
                val lineTop = firstChild.top + bitmap.height / 2
                path.reset()
                path.moveTo((firstChild.left - itemLeftPadding / 2 + bitmap.width / 4).toFloat(), lineTop.toFloat())
                path.lineTo((lastChild.left - itemLeftPadding / 2 + bitmap.width / 4).toFloat(), (lastChild.top + bitmap.height / 2).toFloat())
                c.drawPath(path, dashPaint)
            }

            for (i in 0 until childCount) {
                val child: View = parent.getChildAt(i)
                val centerX: Int = child.left - itemLeftPadding / 2
                val rect = Rect(centerX - bitmap.width / 2, child.top, centerX + bitmap.width / 2, child.top + bitmap.height)
                c.drawBitmap(bitmap, null, rect, paint)
            }
        }
    }

    private class LiveCallback(data: MutableList<TLive>) : BaseQuickDiffCallback<TLive>(data) {
        override fun areItemsTheSame(oldItem: TLive, newItem: TLive): Boolean {
            return oldItem.data == newItem.data
        }

        override fun areContentsTheSame(oldItem: TLive, newItem: TLive): Boolean {
            return oldItem.data == newItem.data
        }
    }
}