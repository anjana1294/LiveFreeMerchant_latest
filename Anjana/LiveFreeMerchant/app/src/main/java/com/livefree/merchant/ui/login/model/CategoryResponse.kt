package com.livefree.merchant.ui.login.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(val status: Boolean,
                            val msg: String,
                            val categoryId: String,
                            @SerializedName("data1")
                            val restaurantId: String) {
}