package com.livefree.merchant.ui.section.model

import com.google.gson.annotations.SerializedName

data class UpdateSectionResponse (
    val msg :String,
    val status:Boolean,
    @SerializedName("data")
    val updataData:UpdataData
){

}