package com.livefree.merchant.ui.table.model

data class AddTableResponse(
    val status:Boolean,
    val msg:String,
    val tableData:ArrayList<TableData>
) {
}