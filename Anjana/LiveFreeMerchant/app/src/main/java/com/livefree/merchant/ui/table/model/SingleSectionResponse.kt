package com.livefree.merchant.ui.table.model

import com.google.gson.annotations.SerializedName
import com.livefree.merchant.ui.section.model.SectionData

data class SingleSectionResponse(
    val status:Boolean,
    val msg:String,
    @SerializedName("data")
    val singleData: SectionData
)
{
}