package com.livefree.merchant.ui.server.model

data class ServerData(
    val assigned:Boolean,
    val _id:String,
    val name: String,
    val contact:String,
    val dateofbirth:String,
    val categorytag_id:String,
    val status:Boolean
)