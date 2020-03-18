package com.livefree.merchant.ui.login.model

/**
 * Created by root on 16/12/19.
 */
data class LoginRequest(val email: String,
                        val password: String,
                        val fcmToken:String) {
}
