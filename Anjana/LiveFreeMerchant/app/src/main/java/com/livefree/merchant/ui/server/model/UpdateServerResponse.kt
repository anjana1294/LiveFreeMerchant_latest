package com.livefree.merchant.ui.server.model

import com.google.gson.annotations.SerializedName

data class UpdateServerResponse (
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val updateServerData:UpdateServerData
){
}