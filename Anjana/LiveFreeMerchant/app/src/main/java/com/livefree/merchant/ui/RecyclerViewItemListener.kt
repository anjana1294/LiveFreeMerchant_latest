package com.livefree.merchant.ui

public interface RecyclerViewItemListener {

    fun onItemClick(position: Int)
    fun onItemDelete(menuID:String)
    fun onItemEdit(menuID:String)

}
