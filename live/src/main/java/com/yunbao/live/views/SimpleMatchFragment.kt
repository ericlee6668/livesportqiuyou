package com.yunbao.live.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yunbao.live.R

/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/2 14:42
 * @package: com.yunbao.live.views
 * Description：TODO
 */
abstract class SimpleMatchFragment<T> : Fragment() {

    private lateinit var rootView: View
    private var data: MutableList<T>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!this::rootView.isInitialized) {
            rootView = inflater.inflate(R.layout.simple_list_event_layout, container, false)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        addItemDecoration()?.let { recyclerView.addItemDecoration(it) }
        displayData()
    }

    fun providerData(data: MutableList<T>?) {
        this.data = data
        displayData()
    }

    open fun addItemDecoration(): RecyclerView.ItemDecoration? {
        return null
    }

    abstract fun providerAdapter(): BaseQuickAdapter<T, BaseViewHolder>?

    private fun displayData() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)

        if (recyclerView != null) {
            var adapter = recyclerView.adapter as BaseQuickAdapter<T, BaseViewHolder>?
            if (adapter == null) {
                adapter = providerAdapter()
                data?.let { adapter!!.addData(it) }
                recyclerView.adapter = adapter
            } else {
                data?.let { adapter.addData(it) }
            }
        }
    }
}