package com.livefree.merchant.ui.booking.model

data class FoodMenu (
    val _id: String,
    val categorytag_id: String,
    val name: String,
    val mrp: Float,
    val price: Float,
    var quantity: Int

){

}
