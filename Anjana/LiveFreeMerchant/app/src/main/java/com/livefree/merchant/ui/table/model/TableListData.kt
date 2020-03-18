package com.livefree.merchant.ui.table.model

import com.google.gson.annotations.SerializedName
import com.livefree.merchant.ui.server.model.ServerData

data class TableListData(
    val categorytag_id:String,
    val assigned:Boolean,
    val chair:Short,
    val createdById:String,
    val name:String,
    val section_id:String,
    @SerializedName("server_id")
    val server: ServerData,
    val _id:String) {
}