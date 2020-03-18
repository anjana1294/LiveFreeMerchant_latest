package com.livefree.merchant.ui.section.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.livefree.merchant.R
import com.livefree.merchant.ui.server.model.ServerData
import com.livefree.merchant.ui.server.onServerItemClickListener
import com.livefree.merchant.util.CalculateAge
import kotlinx.android.synthetic.main.item_layout_server_list.view.*
import java.util.ArrayList

class ServerListAdapter(
    val serverData: ArrayList<ServerData>,
    val context: Context,
    val onItemListener: onServerItemClickListener
) :
    RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_server_list, p0, false))
    }

    override fun getItemCount(): Int {
        return serverData.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.names?.text = serverData.get(p1).name
        p0.contact?.text = serverData.get(p1).contact
        var dob = CalculateAge.parseStringToDate(serverData.get(p1).dateofbirth)
        p0.age?.text = CalculateAge.calculateAge(dob).toString()

        p0.bind(serverData.get(p1), context,onItemListener)
        //p0.edit?.text = serverName.get(p1)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        /*editIcon.setOnClickListener {
            val popup = PopupMenu(context, itemView)
            val inflater = popup.getMenuInflater()
            inflater.inflate(R.menu.pop_up, popup.getMenu())
            popup.setOnMenuItemClickListener(PopupMvenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit ->
                        onItemListener.onItemEdit(tableListData._id)
                    R.id.delete ->
                        onItemListener.onItemDelete(tableListData._id)
                }
                true
            })
            popup.show()
        }*/

        val names = view.tv_cust_name
        val contact = view.tv_cust_no
        val age = view.tv_cust_age
        val assignStatus = view.tv_assigned
        val editIcon = view.iv_edit

        fun bind(serverData: ServerData, context: Context,onItemListener: onServerItemClickListener) {
            if (serverData.assigned) {
                assignStatus.setTextColor(ContextCompat.getColor(context, R.color.colorLightGreen))
                assignStatus.setText("Assigned")
            } else {
                assignStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                assignStatus.setText(" Not Assigned")
            }
            editIcon.setOnClickListener {
                val popup = PopupMenu(context, itemView)
                val inflater = popup.getMenuInflater()
                inflater.inflate(R.menu.pop_up, popup.getMenu())
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit ->
                            onItemListener.onItemEdit(serverData._id)
                        R.id.delete ->
                            onItemListener.onItemDelete(serverData._id)
                    }
                    true
                })
                popup.show()
            }
        }

    }
}