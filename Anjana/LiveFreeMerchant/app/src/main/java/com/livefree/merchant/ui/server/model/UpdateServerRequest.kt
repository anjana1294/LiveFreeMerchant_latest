package com.livefree.merchant.ui.server.model

data class UpdateServerRequest(
    val server_id:String,
    val newname:String,
    val newcontact:String,
    val newdob:String
) {
}