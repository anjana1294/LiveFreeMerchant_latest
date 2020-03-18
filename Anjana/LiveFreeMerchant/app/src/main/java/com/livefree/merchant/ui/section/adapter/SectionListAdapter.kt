package com.livefree.merchant.ui.section.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.section.model.SectionData
import kotlinx.android.synthetic.main.item_layout_add_section.view.*
import java.util.ArrayList

class SectionListAdapter(
    val sectionData: ArrayList<SectionData>,
    val context: Context,
    val onItemClickListener: RecyclerViewItemListener
) :
    RecyclerView.Adapter<SectionListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.item_layout_add_section, p0, false)
        )

    }

    override fun getItemCount(): Int {
        return sectionData.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemSecList?.text = sectionData.get(p1).name


        p0.bind(sectionData.get(p1).toString(),this.onItemClickListener)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemSecList = view.tv_item_section
        fun bind(items: String, recyclerViewItemListener: RecyclerViewItemListener) {
            itemSecList.setOnClickListener(View.OnClickListener {
                recyclerViewItemListener?.onItemClick(adapterPosition)
            })
        }

    }
}