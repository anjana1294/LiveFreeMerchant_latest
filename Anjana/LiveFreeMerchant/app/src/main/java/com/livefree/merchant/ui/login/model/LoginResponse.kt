package com.livefree.merchant.ui.login.model

data class LoginResponse(
        val status: Boolean,
        val msg: String,
        val data: String,
        val verified: Boolean
)
