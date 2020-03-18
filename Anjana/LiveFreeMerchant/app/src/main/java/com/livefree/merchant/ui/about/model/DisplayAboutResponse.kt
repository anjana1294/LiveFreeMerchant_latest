package com.livefree.merchant.ui.about.model

import com.google.gson.annotations.SerializedName

data class DisplayAboutResponse(
    val status: Boolean,
    val msg: String,
    @SerializedName("data")
    val displayData:DisplayData
) {
}