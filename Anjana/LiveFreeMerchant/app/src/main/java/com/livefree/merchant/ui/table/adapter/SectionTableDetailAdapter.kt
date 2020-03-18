package com.livefree.merchant.ui.table.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import com.livefree.merchant.R
import com.livefree.merchant.ui.table.model.TableListData
import com.livefree.merchant.ui.table.onTableItemClickListener
import kotlinx.android.synthetic.main.item_layout_table_detail_list.view.*
import java.util.*

class SectionTableDetailAdapter(
    val tableListData: ArrayList<TableListData>,
    val context: Context,
    val onItemListener: onTableItemClickListener
) : RecyclerView.Adapter<SectionTableDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_layout_table_detail_list, p0, false)
        )

    }

    override fun getItemCount(): Int {
        return tableListData.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.TableName?.text = tableListData.get(p1).name
        p0.chairNo?.text = tableListData.get(p1).chair.toString()
        p0.ServerName?.text = tableListData.get(p1).server.name
        p0.bind(tableListData.get(p1), context, onItemListener)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val chairNo = view.tv_chair_no
        val TableName = view.tv_item_table
        val ServerName = view.tv_server_name
        val bookStatus = view.tv_booked
        val editIcon = view.iv_edit
        val linearLayoutTableDetail = view.table_detail_view

        fun bind(

            tableListData: TableListData,
            context: Context,
            onItemListener: onTableItemClickListener
        ) {
            if (tableListData.assigned) {
                bookStatus.setTextColor(ContextCompat.getColor(context, R.color.colorLightGreen))
                bookStatus.setText("Booked")
            } else {
                bookStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                bookStatus.setText(" Not Booked")
            }

            TableName.setOnClickListener {
                if (linearLayoutTableDetail.visibility === View.VISIBLE) {
                    linearLayoutTableDetail.visibility = View.GONE
                } else {
                    linearLayoutTableDetail.visibility = View.VISIBLE

                }
            }

            editIcon.setOnClickListener {
                val popup = PopupMenu(context, itemView)
                val popupwindow:PopupWindow? = null
                popupwindow?.showAsDropDown(it,-40, 18);
                val inflater = popup.getMenuInflater()
                inflater.inflate(R.menu.pop_up, popup.getMenu())
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit ->
                            onItemListener.onItemEdit(tableListData._id)
                        R.id.delete ->
                            onItemListener.onItemDelete(tableListData._id)
                    }
                    true
                })
                popup.show()
            }

        }
    }


}

