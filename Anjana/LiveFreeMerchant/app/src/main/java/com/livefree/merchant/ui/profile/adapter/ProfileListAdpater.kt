package com.livefree.merchant.ui.profile.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import com.livefree.merchant.ui.RecyclerViewItemListener
import kotlinx.android.synthetic.main.item_layout_profile.view.*
import java.util.*

class ProfileListAdpater(
    val items: ArrayList<String>,
    val context: Context,
    val onItemClickListener: RecyclerViewItemListener
) :
    RecyclerView.Adapter<ProfileListAdpater.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_layout_profile, p0, false)
        var viewHolder = ViewHolder(view)
        return viewHolder

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemList?.text = items.get(p1)
        p0.bind(items.get(p1), this.onItemClickListener)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemList = view.tv_item_profile

        fun bind(items: String, recyclerViewItem: RecyclerViewItemListener) {
            itemList.setOnClickListener(View.OnClickListener {

                //                Toast.makeText(mContext, "clicked on " + itemList.text, Toast.LENGTH_SHORT).show()

                recyclerViewItem?.onItemClick(adapterPosition)
            })

        }
    }
}
