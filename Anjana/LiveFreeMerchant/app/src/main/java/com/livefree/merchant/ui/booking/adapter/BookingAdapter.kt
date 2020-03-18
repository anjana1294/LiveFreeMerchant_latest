package com.livefree.merchant.ui.booking.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import com.livefree.merchant.custom.SpannyText
import com.livefree.merchant.ui.booking.model.BookingData
import com.livefree.merchant.ui.booking.onItemClick
import kotlinx.android.synthetic.main.item_layout_booking.view.*
import java.util.*

class BookingAdapter(

    val bookingData: ArrayList<BookingData>,
    val context: Context, val onItemClick: onItemClick
) :
    RecyclerView.Adapter<BookingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_booking, p0, false))

    }

    override fun getItemCount(): Int {
        return bookingData.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val orderID = SpannyText(
            "Order ID: ", ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.colorToolbar)
            ), StyleSpan(Typeface.BOLD)
        )
            .append(bookingData.get(p1).booking_code)

        p0.names?.text = bookingData.get(p1).customerBooking.name
        p0.contact?.text = bookingData.get(p1).customerBooking.phone
        p0.date?.text = bookingData.get(p1).guestDate
        p0.table?.text = bookingData.get(p1).tableset_id.name
        p0.chair?.text = bookingData.get(p1).tableset_id.chair.toString()
        p0.server?.text = bookingData.get(p1).server_id.name
        p0.section?.text = bookingData.get(p1).section_id.name
        p0.time?.text = bookingData.get(p1).guestTime
        p0.orderID?.text = orderID
        p0.tv_emailID?.text = bookingData.get(p1).customerBooking.email
        p0.onBind(onItemClick, bookingData.get(p1))
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val names = view.tv_customer_name
        val contact = view.tv_number
        val date = view.tv_cust_date
        val table = view.tv_cust_table
        val server = view.tv_server_name
        val foodMenu = view.tv_food_order
        val time = view.tv_cust_time
        val chair = view.tv_cust_chair
        val section = view.tv_cus_section
        val orderID = view.tv_order_id
        val tv_emailID = view.tv_email
        fun onBind(onItemClick: onItemClick, booking: BookingData) {
            foodMenu.setOnClickListener {
                onItemClick.onClick(booking._id)
            }


        }
    }
}

