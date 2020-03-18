package com.livefree.merchant.ui.booking.model

import com.google.gson.annotations.SerializedName

data  class FoodOrderResponse(val status:Boolean,
                              val msg:String,

                              @SerializedName("menuItem")
                              val foodMenu:ArrayList<FoodMenu>) {
}