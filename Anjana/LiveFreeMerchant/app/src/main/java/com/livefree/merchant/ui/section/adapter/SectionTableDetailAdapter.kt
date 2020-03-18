package com.livefree.merchant.ui.section.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import kotlinx.android.synthetic.main.item_layout_table_detail.view.*
import java.util.ArrayList

class SectionTableDetailAdapter(val chairNo: ArrayList<String>,val serverName: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<SectionTableDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_table_detail, p0, false))

    }

    override fun getItemCount(): Int {
        return chairNo.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.chairNo?.text = chairNo.get(p1)
        p0.serverName?.text = serverName.get(p1)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chairNo  = view.tv_chair_no
        val serverName  = view.tv_item_table

    }
}