package com.livefree.merchant.ui.menu.model

data class UpdateMenuRequest(
    val menu_id:String,
    val newname:String,
    val newprice:String,
    val newimage:String

) {
}