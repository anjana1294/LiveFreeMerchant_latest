package com.livefree.merchant.ui.settings.model

import com.google.gson.annotations.SerializedName

class EditResponse(
    val msg: String,
    val status: Boolean,
    @SerializedName("data")
    val editData:EditData
) {
}