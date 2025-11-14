package com.yunbao.live.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.yunbao.common.bean.Incidents
import com.yunbao.live.R
import kotlinx.coroutines.*

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/1 16:03
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class EventFragment : Fragment() {
    private val job by lazy { Job() }
    private val dispatchers by lazy { Dispatchers.Main }
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private lateinit var rootView: View
    private var incidentList: MutableList<Incidents>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (!this::rootView.isInitialized) {
            rootView = inflater.inflate(R.layout.match_list_layout, container, false)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayEvent()
    }

    fun attachEventData(data: MutableList<Incidents>?) {
        if (incidentList == null) {
            incidentList = mutableListOf()
        }
        data?.let { incidentList?.addAll(it) }
        displayEvent()
    }

    fun onInsertIncident(insertData: List<Incidents>?) {
        if (insertData == null) return
        val coroutineScope = CoroutineScope(dispatchers + job)
        coroutineScope.launch {
            val changeList = withContext(Dispatchers.IO) {
                val dataList = mutableListOf<Incidents>()
                if (incidentList?.isNotEmpty() == true) {
                    dataList.addAll(incidentList!!)
                }
                for (item in dataList) {
                    if (!dataList.contains(item)) {
                        dataList.add(item)
                    }
                }
                dataList
            }
            incidentList = changeList
            handler.post { displayEvent() }
        }
    }

    private fun displayEvent() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView != null) {
            var adapter = recyclerView.adapter as EventAdapter?
            if (adapter == null) {
                val dataList = mutableListOf<Incidents>()
                incidentList?.let { list ->
                    dataList.add(Incidents(-1))
                    dataList.addAll(list)
                }
                adapter = EventAdapter(dataList)
                recyclerView.adapter = adapter
            } else {
                incidentList?.let {
                    if (it.isNotEmpty()) {
                        val firstData = it[0]
                        if (firstData.itemType != Incidents.TYPE_START) {
                            it.add(0, Incidents(-1))
                        }
                    }
                    adapter.setNewDiffData(IncidentCallback(it))
                }
            }

            showEmptyView(adapter.itemCount == 0)
        } else {
            showEmptyView(true)
        }
    }

    private fun showEmptyView(show: Boolean) {
        view?.findViewById<View>(R.id.matchEmptyView)?.visibility = if (show) View.VISIBLE else View.GONE
        view?.findViewById<View>(R.id.matchContentView)?.visibility = if (show) View.GONE else View.VISIBLE
    }

    private class IncidentCallback(newList: MutableList<Incidents>) : BaseQuickDiffCallback<Incidents>(newList) {
        override fun areItemsTheSame(oldItem: Incidents, newItem: Incidents): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Incidents, newItem: Incidents): Boolean {
            return oldItem == newItem
        }
    }

    private class EventAdapter(data: MutableList<Incidents>) : BaseMultiItemQuickAdapter<Incidents, BaseViewHolder>(data) {
        init {
            addItemType(Incidents.TYPE_START, R.layout.item_start_event)
            addItemType(Incidents.TYPE_NEUTRAL, R.layout.item_neutral_event)
            addItemType(Incidents.TYPE_HOME, R.layout.item_home_event)
            addItemType(Incidents.TYPE_AWAY, R.layout.item_away_event)
            addItemType(Incidents.TYPE_END, R.layout.item_end_event)
        }

        override fun convert(helper: BaseViewHolder, item: Incidents?) {
            item?.let {
                when (it.itemType) {
                    Incidents.TYPE_NEUTRAL, Incidents.TYPE_END -> bindNaturalEvent(helper, item)
                    Incidents.TYPE_HOME, Incidents.TYPE_AWAY -> bindGameEvent(helper, it)
                }
            }
        }

        private fun bindGameEvent(helper: BaseViewHolder, item: Incidents) {
            when (item.type) {
                1 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_football_goal)
                3 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_yellow_card)
                4 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_red_card)
                8 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_penalty_kick)
                9 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_in_play)
                17 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_own_goals)
                28 -> helper.setImageResource(R.id.imgEvent, R.drawable.icon_var)
            }

            when {
                item.type == 1 -> {
                    helper.setText(R.id.txtEvent, item.player_name)
                            .setText(R.id.txtTime, item.time.toString())
                            .setGone(R.id.subGroup, !TextUtils.isEmpty(item.assistName))
                            .setImageResource(R.id.imgSubEvent, R.drawable.icon_football_assist)
                            .setText(R.id.txtSubEvent, item.assistName)
                }
                item.type != 9 -> {
                    helper.setText(R.id.txtEvent, item.player_name)
                            .setText(R.id.txtTime, item.time.toString())
                            .setGone(R.id.subGroup, false)
                }
                else -> {
                    helper.setText(R.id.txtEvent, item.inPlayerName)
                            .setText(R.id.txtTime, item.time.toString())
                            .setGone(R.id.subGroup, true)
                            .setImageResource(R.id.imgSubEvent, R.drawable.icon_out_play)
                            .setText(R.id.txtSubEvent, item.outPlayerName)
                }
            }
        }

        private fun bindNaturalEvent(helper: BaseViewHolder, item: Incidents) {
            when (item.type) {
                11 -> helper.setText(R.id.txtTime, R.string.midfield)
                12 -> helper.setText(R.id.txtTime, R.string.game_end)
                19 -> helper.setText(R.id.txtTime, R.string.stoppage_time)
            }
        }
    }
}