package com.livefree.merchant.ui.booking.model

import com.google.gson.annotations.SerializedName

data class BookingResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val bookingData:ArrayList<BookingData>) {
}