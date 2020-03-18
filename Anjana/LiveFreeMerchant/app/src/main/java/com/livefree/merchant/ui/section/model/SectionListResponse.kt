package com.livefree.merchant.ui.section.model

import com.google.gson.annotations.SerializedName
data class SectionListResponse(val status:Boolean,
                          val msg:String,
                          @SerializedName("data")
                          val sectionData:ArrayList<SectionData>){

}