package com.livefree.merchant.ui.menu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.livefree.merchant.R
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.menu.model.MenuData
import kotlinx.android.synthetic.main.item_layout_menu.view.*
import java.util.ArrayList

class MenuListAdapter(
    val menuData: ArrayList<MenuData>,
    val context: Context,
    val onItemClickListener: RecyclerViewItemListener
) :
    RecyclerView.Adapter<MenuListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_menu, p0, false))

    }

    override fun getItemCount(): Int {
        return menuData.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.menuName?.text = menuData.get(p1).name
        p0.menuPrice?.text = menuData.get(p1).price.toString()

        p0.bind(
            menuData.get(p1), context,onItemClickListener
        )
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuName = view.tv_item_menu
        val menuPrice = view.tv_item_price
        val editMenu = view.iv_edit_menu

        fun bind(
            menuData: MenuData,
                 context: Context,
            onItemClickListener: RecyclerViewItemListener) {
//            menuName.setOnClickListener(View.OnClickListener {
//                onItemClickListener?.onItemClick(adapterPosition)
//                   }
//            )
            editMenu.setOnClickListener {
                val popup = PopupMenu(context, itemView)
                val inflater = popup.getMenuInflater()
                inflater.inflate(R.menu.pop_up, popup.getMenu())
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit ->
                            onItemClickListener.onItemEdit(menuData._id)
                        R.id.delete ->
                            onItemClickListener.onItemDelete(menuData._id)
                    }
                    true
                })
                popup.show()
            }

        }

    }
}