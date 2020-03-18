package com.livefree.merchant.ui.settings.model

 data class UserData(val verified: String,
                     val otpForgetPassword: String,
                     val name: String,
                     val email: String,
                     val phone: String,
                     val logo: String
 ) {}