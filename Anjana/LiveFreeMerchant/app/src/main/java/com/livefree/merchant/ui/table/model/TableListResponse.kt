package com.livefree.merchant.ui.table.model

import com.google.gson.annotations.SerializedName

data class TableListResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val tableList:ArrayList<TableListData>
){}