package com.livefree.merchant.ui.table.model

data class UpdateTableListRequest(
    val categorytag_id:String,
    val section_id:String,
    val tableset_id:String,
    val newname:String,
    val newchair:String,
    val newserver_id:String
)
{
}