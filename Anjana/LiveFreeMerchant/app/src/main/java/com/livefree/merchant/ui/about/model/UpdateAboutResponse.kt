package com.livefree.merchant.ui.about.model

import com.google.gson.annotations.SerializedName

data class UpdateAboutResponse (
   val msg :String,
   val status:Boolean,
   @SerializedName("data")
   val displayData:DisplayData
){
}