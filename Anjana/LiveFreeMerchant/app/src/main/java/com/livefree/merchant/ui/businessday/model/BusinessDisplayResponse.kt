package com.livefree.merchant.ui.businessday.model

import com.google.gson.annotations.SerializedName

data class BusinessDisplayResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val businessDays:ArrayList<BusinessDays>
) {
}