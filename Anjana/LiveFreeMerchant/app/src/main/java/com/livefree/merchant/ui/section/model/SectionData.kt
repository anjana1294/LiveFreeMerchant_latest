package com.livefree.merchant.ui.section.model

data class SectionData(
    val assigned:Boolean,
    val _id:String,
    val name: String,
    val image:String,
    val categorytag_id:String,
    val server_id:String,
    val section_id:String,
    val createdById:String,
    val status:Boolean,
    val chair:Short
) {
}