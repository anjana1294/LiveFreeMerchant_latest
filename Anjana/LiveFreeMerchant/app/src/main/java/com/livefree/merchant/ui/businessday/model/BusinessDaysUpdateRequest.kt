package com.livefree.merchant.ui.businessday.model

data class BusinessDaysUpdateRequest(
    val categorytag_id: String, val newdays: ArrayList<BusinessDays>
)