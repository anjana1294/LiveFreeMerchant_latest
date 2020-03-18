package com.livefree.merchant.ui.section

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.section.adapter.SectionTableDetailAdapter
import kotlinx.android.synthetic.main.fragement_section_table_details.*

class SectionTableDetailFragment : BaseFragment()
{
    val chairNo: ArrayList<String> = ArrayList()
    val serverName: ArrayList<String> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView=inflater.inflate(R.layout.fragement_section_table_details,container,false)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChair()
        addName()

        rv_table.layoutManager = LinearLayoutManager(activity)
        rv_table.adapter = SectionTableDetailAdapter(chairNo,serverName, context())

    }

    fun addChair() {
        chairNo.add("4")
        chairNo.add("1")
        chairNo.add("7")
    }
    fun addName() {
        serverName.add("Jim")
        serverName.add("John")
        serverName.add("Sam")
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.table_toolbar,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}