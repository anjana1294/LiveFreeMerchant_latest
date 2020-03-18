package com.livefree.merchant.ui.about.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livefree.merchant.R
import kotlinx.android.synthetic.main.item_layout_about_images.view.*
import java.util.ArrayList

 class AboutAdapter(val itemImages: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_about_images, p0,false))
    }

    override fun getItemCount(): Int {
        return itemImages.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.images?.setImageResource(R.drawable.add_photo)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val images = view.iv_add_photo


    }
}
