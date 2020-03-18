package com.livefree.merchant.ui.settings.model

import com.google.gson.annotations.SerializedName

data class
ProfileResponse(
    val status: Boolean,
    val msg: String,
    @SerializedName("data")
    val userData:UserData
) {
}