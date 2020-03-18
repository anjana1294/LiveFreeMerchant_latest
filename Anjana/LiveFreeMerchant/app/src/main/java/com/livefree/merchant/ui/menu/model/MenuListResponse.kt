package com.livefree.merchant.ui.menu.model

import com.google.gson.annotations.SerializedName

data class MenuListResponse(val status:Boolean,
                            val msg:String,
                            @SerializedName("data")
                            val menuData:ArrayList<MenuData>) {
}