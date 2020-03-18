package com.livefree.merchant.ui.menu.model

import com.google.gson.annotations.SerializedName

data class SingleMenuResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val singleMenuData:MenuData
){
}