package com.livefree.merchant.ui.password.otp.model

data class OtpResponse(
    val status: Boolean,
    val msg: String,
    var token: String

) {

}