package com.yunbao.live.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.yunbao.live.R
import kotlinx.coroutines.*

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/5 17:02
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class BasketLiveFragment : Fragment() {

    private val job by lazy { Job() }
    private val dispatchers by lazy { Dispatchers.Main }

    companion object {
        fun create(homeName: String?, awayName: String?): BasketLiveFragment {
            return BasketLiveFragment().apply {
                arguments = Bundle().apply {
                    putString("home", homeName)
                    putString("away", awayName)
                }
            }
        }
    }

    private var homeName: String? = null
    private var awayName: String? = null
    var data: List<List<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeName = arguments?.getString("home")
        awayName = arguments?.getString("away")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_list_event_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, true)
        showData()
    }

    fun showLiveData(data: List<List<String>>?) {
        this.data = data
        showData()
    }

    private fun showData() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView) ?: return
        val coroutineScope = CoroutineScope(dispatchers + job)
        coroutineScope.launch {
            val dataList = withContext(Dispatchers.IO) {
                val dataList = mutableListOf<Any>()
                data?.let {
                    if (it.isNotEmpty()) {
                        dataList.add(LiveTitle("第一节"))
                        dataList.addAll(it[0])
                    }
                    if (it.size >= 2) {
                        dataList.add(LiveTitle("第二节"))
                        dataList.addAll(it[1])
                    }
                    if (it.size >= 3) {
                        dataList.add(LiveTitle("第三节"))
                        dataList.addAll(it[2])
                    }
                    if (it.size >= 4) {
                        dataList.add(LiveTitle("第四节"))
                        dataList.addAll(it[2])
                    }
                    if (it.size >= 5) {
                        dataList.add(LiveTitle("加时"))
                        dataList.addAll(it[2])
                    }
                }

                dataList
            }
            if (recyclerView.adapter == null) {
                recyclerView.adapter = BasketLiveAdapter(homeName, awayName, dataList)
            } else {
                (recyclerView.adapter as BasketLiveAdapter).setNewData(dataList)
            }
        }
    }
}

private class BasketLiveAdapter(private val homeName: String?, private val awayName: String?, private var data: MutableList<Any>?) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val resId = if (viewType == 0) R.layout.item_baketball_live_header else R.layout.item_basketball_live
        val itemView = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemData = data?.get(position)
        if (itemData is LiveTitle) {
            holder.setText(R.id.txtSection, itemData.title)
        } else {
            itemData as String?
            val array = itemData?.split("^")
            holder.setText(R.id.txtTime, array?.get(1) ?: "")
                    .setText(R.id.txtScore, array?.get(4) ?: "")
                    .setText(R.id.txtEvent, array?.get(5) ?: "")
                    .setText(R.id.txtTeam, getTeamName(homeName, awayName, array?.get(2) ?: ""))
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val item = data?.get(position)
        return if (item is LiveTitle) 0 else 1
    }

    private fun getTeamName(homeName: String?, awayName: String?, value: String): String? {
        return when (value) {
            "0" -> ""
            "1" -> homeName
            else -> awayName
        }
    }

    fun setNewData(data: MutableList<Any>?) {
        this.data = data
        notifyDataSetChanged()
    }
}

private data class LiveTitle(val title: String)
