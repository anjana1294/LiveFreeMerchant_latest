package com.livefree.merchant.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.util.Constants

/**
 * Created by root on 18/12/19.
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d("FCM TOKEN", token)
        var sharedPref = SharedPref().getFCMInstance()
        sharedPref?.initFcmSharedPreferences(applicationContext)
//        sharedPref?.putStringValue("fcmToken", token!!)
        sharedPref.setFcmToken(token)
    }
}