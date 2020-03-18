package com.livefree.merchant.ui.location.model

data class LocationResponse(val status: Boolean,
                            val msg: String,
                            val data:RestaurantDetailData) {
}