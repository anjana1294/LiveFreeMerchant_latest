package com.livefree.merchant.ui.location.model

data class CategoryListResponse(
    val status: Boolean,
    val msg: String,
    val data: ArrayList<Category>) {
}