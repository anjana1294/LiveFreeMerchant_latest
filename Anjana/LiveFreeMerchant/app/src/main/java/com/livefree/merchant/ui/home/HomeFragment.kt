package com.livefree.merchant.ui.home

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R

import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.about.GridItemDecoration
import com.livefree.merchant.ui.home.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*




class HomeFragment : BaseFragment(), RecyclerViewItemListener {


    val homeItems: ArrayList<String> = ArrayList()

    private var callback: Callback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addHomeItem()

        rv_home.layoutManager = GridLayoutManager(activity, 2) as RecyclerView.LayoutManager?
        rv_home.adapter = HomeAdapter(homeItems, context(), this)
        rv_home.addItemDecoration(GridItemDecoration(16, 2))

    }

    override fun onItemClick(position: Int) {
        callback?.onGridViewItemClick(position)
    }


    fun addHomeItem() {
        homeItems.clear()
        homeItems.add(resources.getString(com.livefree.merchant.R.string.today_booking))
        homeItems.add(resources.getString(R.string.servers))
        homeItems.add(resources.getString(R.string.sections))
        homeItems.add(resources.getString(R.string.menu))

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HomeFragment.Callback)
            callback = context;

    }

    public interface Callback {
        fun onGridViewItemClick(position: Int)
    }
    override fun onItemDelete(menuID: String) {
    }

    override fun onItemEdit(menuID: String) {
    }


}
