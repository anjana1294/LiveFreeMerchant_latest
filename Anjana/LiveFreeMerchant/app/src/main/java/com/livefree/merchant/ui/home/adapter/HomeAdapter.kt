package com.livefree.merchant.ui.home.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import com.livefree.merchant.ui.RecyclerViewItemListener
import kotlinx.android.synthetic.main.item_layout_home.view.*
import java.util.*


class HomeAdapter(
    val items: ArrayList<String>,
    val context: Context,
    val onItemClickListener: RecyclerViewItemListener
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HomeAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_home, p0, false))

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: HomeAdapter.ViewHolder, p1: Int) {
        p0.itemList?.text = items.get(p1)
        p0.bindView(items.get(p1), onItemClickListener)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemList = view.tv_home
        val context = itemView.context


        fun bindView(items: String, onItemClickListener: RecyclerViewItemListener) {
            itemList.setOnClickListener(View.OnClickListener {

                onItemClickListener?.onItemClick(adapterPosition)
//                when (adapterPosition) {
//
//                    0 -> {
//
//                        val intent = Intent(context, BookingFragment::class.java)
//                        context.startActivity(intent)
//                    }
//
//                    1->{
//                        val intent = Intent(context, ServerListFragment::class.java)
//                        context.startActivity(intent)
//
//                    }
//                    2->{
//                        val intent = Intent(context, SectionListFragment::class.java)
//                        context.startActivity(intent)
//
//                    }
//                    3->{
//                        val intent = Intent(context, AddMenuDetailFragment::class.java)
//                        context.startActivity(intent)
//
//                    }
//                }
            })
        }

    }
}


