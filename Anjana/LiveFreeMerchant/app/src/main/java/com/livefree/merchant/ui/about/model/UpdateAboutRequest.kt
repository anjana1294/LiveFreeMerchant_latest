package com.livefree.merchant.ui.about.model

data class UpdateAboutRequest(
    val categorytagabout_id:String,
    val newname:String,
    val newabout:String,
    val newimage:String

) {
}