package com.livefree.merchant.ui.location.model

import com.google.gson.annotations.SerializedName

data class LocationRequest(val category: String,
                           val city: String,
                           @SerializedName("address")
                           val address: AddressData) {
}