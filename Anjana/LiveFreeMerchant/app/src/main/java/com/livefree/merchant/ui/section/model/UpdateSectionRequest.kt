package com.livefree.merchant.ui.section.model

data class UpdateSectionRequest(
    val section_id:String,
    val categorytag_id:String,
    val tableset_id:String,
    val newserver_id:String,
    val newname:String,
    val newimage:String,
    val newchair:String
) {
}