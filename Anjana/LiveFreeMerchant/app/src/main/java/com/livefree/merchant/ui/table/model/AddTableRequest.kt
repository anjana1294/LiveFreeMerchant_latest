package com.livefree.merchant.ui.table.model

data class AddTableRequest(
    val categorytag_id:String,
    val section_id:String,
    val name:String,
    val chair:String,
    val server_id:String
){
}