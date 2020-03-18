package com.livefree.merchant.ui.settings.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.livefree.merchant.R
import com.livefree.merchant.ui.RecyclerViewItemListener
import kotlinx.android.synthetic.main.item_layout_settting.view.*
import java.util.ArrayList

class settingAdapter(
    val items: ArrayList<String>, val context: Context,
   val recyclerViewItem: RecyclerViewItemListener
) : RecyclerView.Adapter<settingAdapter.ViewHolder>() {
    private val listener: AdapterView.OnItemClickListener? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_layout_settting,
                p0,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemList?.text = items.get(p1)
        p0.bind(items.get(p1),recyclerViewItem)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemList = view.tv_setting
        val mContext: Context? = null


        fun bind(items: String, recyclerViewItem: RecyclerViewItemListener) {

            itemList.setOnClickListener(View.OnClickListener {

//                Toast.makeText(mContext, "clicked on " + itemList.text, Toast.LENGTH_SHORT).show()

                recyclerViewItem?.onItemClick(adapterPosition)
            })

        }

    }

}
