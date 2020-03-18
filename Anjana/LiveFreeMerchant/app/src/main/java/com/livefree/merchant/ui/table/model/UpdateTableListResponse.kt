package com.livefree.merchant.ui.table.model

import com.google.gson.annotations.SerializedName

data  class UpdateTableListResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val updateTableListData:ArrayList<UpdateTableListData>
)
{}