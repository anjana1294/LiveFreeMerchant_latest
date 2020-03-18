package com.livefree.merchant.ui.businessday.adapter

import android.content.Context
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.businessday.model.BusinessDays
import com.livefree.merchant.util.DatePickerUtil
import kotlinx.android.synthetic.main.item_layout_business_days.view.*
import java.util.*


class BusinessDaysAdapter(
    val bussinessDays: ArrayList<BusinessDays>,
    val context: Context,
    val onItemClickListener: RecyclerViewItemListener
) : RecyclerView.Adapter<BusinessDaysAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BusinessDaysAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_business_days, p0, false))
    }

    override fun getItemCount(): Int {
        return bussinessDays.size
    }

    override fun onBindViewHolder(p0: BusinessDaysAdapter.ViewHolder, p1: Int) {
        p0.itemList?.text = bussinessDays.get(p1).short
        p0.openTime?.text = bussinessDays.get(p1).openingtime
        p0.closeTime?.text = bussinessDays.get(p1).closingtime
        if (bussinessDays.get(p1).isopening) {
            p0.itemList?.setBackground(getDrawable(context, R.drawable.stroke_border_radius_corner_white_solid_orange))
//            p0.itemList?.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        } else {
            p0.itemList?.setBackground(getDrawable(context, R.drawable.stroke_border_radius_corner_white))
//            p0.itemList?.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))

        }
        p0.bindView(bussinessDays.get(p1), onItemClickListener)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemList = view.tv_item_days_working_day
        val openTime = view.tv_item_days_opening_time
        val closeTime = view.tv_item_days_closing_time

        val context = itemView.context

        fun bindView(businessDay: BusinessDays, onItemClickListener: RecyclerViewItemListener) {


            itemList.setOnClickListener(View.OnClickListener {
                var isOpen = businessDay.isopening
                businessDay.isopening = !isOpen
                if (businessDay.isopening) {
                    itemList?.setBackground(getDrawable(context, R.drawable.stroke_border_radius_corner_white_solid_orange))
                } else {
                    itemList?.setBackground(
                        getDrawable(
                            context,
                            R.drawable.stroke_border_radius_corner_white
                        )
                    )
                }
                onItemClickListener?.onItemClick(adapterPosition)
            })

            openTime.setOnClickListener {
                DatePickerUtil.openTimePicker(context) {
                    openTime.setText(it)
                    businessDay.openingtime = it
                    onItemClickListener?.onItemClick(adapterPosition)

                }
            }

            closeTime.setOnClickListener {
                DatePickerUtil.openTimePicker(context) {
                    closeTime.setText(it)
                    businessDay.closingtime = it
                    onItemClickListener?.onItemClick(adapterPosition)
                }
            }
        }

    }
}


