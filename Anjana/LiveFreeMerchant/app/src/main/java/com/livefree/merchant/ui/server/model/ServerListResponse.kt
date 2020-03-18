package com.livefree.merchant.ui.server.model

import com.google.gson.annotations.SerializedName

class ServerListResponse(val status:Boolean,
                         val msg:String,
                         @SerializedName("data")
                         val serverData:ArrayList<ServerData>) {
}