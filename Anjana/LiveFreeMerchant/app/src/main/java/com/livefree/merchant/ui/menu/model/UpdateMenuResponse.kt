package com.livefree.merchant.ui.menu.model

data class UpdateMenuResponse(
    val msg:String,
    val status:Boolean,
    val updateData:ArrayList<UpdateData>

) {
}