package com.livefree.merchant.ui.booking.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import com.livefree.merchant.ui.booking.model.FoodMenu
import kotlinx.android.synthetic.main.item_dialog_order_food.view.*

class FoodMenuAdapter(

    val FoodMenu: ArrayList<FoodMenu>,
    val context: Context
) : RecyclerView.Adapter<FoodMenuAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog_order_food, p0, false))
    }

    override fun getItemCount(): Int {
        return  FoodMenu.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.names?.text = FoodMenu.get(p1).name
        p0.quantity?.text = FoodMenu.get(p1).quantity.toString()
        p0.price?.text = FoodMenu.get(p1).price.toString()    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val names = view.tv_item_food
        val quantity = view.tv_item_quatity
        val price  = view.tv_item_price

    }
}