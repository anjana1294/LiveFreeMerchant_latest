package com.livefree.merchant.ui.server.model

import com.google.gson.annotations.SerializedName

data class SingleServerResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val singleServerData:ServerData
) {
}