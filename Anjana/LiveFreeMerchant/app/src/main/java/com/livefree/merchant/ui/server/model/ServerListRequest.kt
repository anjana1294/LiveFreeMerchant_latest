package com.livefree.merchant.ui.server.model

data class ServerListRequest(
    val categorytag_id: String,
    val startingValue: String,
    val lastValue: String
) {}