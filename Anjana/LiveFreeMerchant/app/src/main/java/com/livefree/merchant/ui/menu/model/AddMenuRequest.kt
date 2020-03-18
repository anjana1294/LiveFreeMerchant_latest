package com.livefree.merchant.ui.menu.model

data class AddMenuRequest(
    val categorytag_id:String,
    val name:String,
    val price: String,
    val image:String
){
}