package com.livefree.merchant.ui.data

import android.content.Context
import android.content.SharedPreferences
import com.google.android.libraries.places.internal.it
import com.livefree.merchant.util.Constants

class SharedPref {
    private val MyPREFERENCES = "Live Free"
    private val MyFCM_PREFERENCES = "FCM Live Free"

    private var sharedPreferences: SharedPreferences? = null
    private var sharedPrefFcm: SharedPreferences? = null
    private val sharedPreferencesManager: SharedPref? = null
    private val sharedPreferencesFCMManager: SharedPref? = null
        // lateinit var fcmPreferences: SharedPreferences

    fun getInstance(): SharedPref {
        return if (sharedPreferencesManager == null) {
            SharedPref()
        } else sharedPreferencesManager
    }

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
    }
    fun initFcmSharedPreferences(context: Context) {
        sharedPrefFcm = context.getSharedPreferences(MyFCM_PREFERENCES , Context.MODE_PRIVATE)
    }
    fun setFcmToken( value: String?) {
        val editor = sharedPrefFcm!!.edit()
        editor.putString("FCMToken", value)
        editor.commit()
    }

    fun getFcmToken(): String? {
        return sharedPrefFcm?.getString("FCMToken", "")
    }

    fun getFCMInstance(): SharedPref {
        return if (sharedPreferencesFCMManager == null) {
            SharedPref()
        } else sharedPreferencesFCMManager
    }

    fun putStringValue(key: String, value: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun putIntValue(key: String, value: Int) {
        val editor = sharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun isLogged(): Boolean {
        return sharedPreferences!!.getBoolean(Constants.IS_LOGGED_IN, false)


    }

    fun putFloatValue(key: String, value: Float) {
        val editor = sharedPreferences!!.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    fun clearSharedPreferences() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.commit()
    }

    fun getUserData(): String? {
        return sharedPreferences!!.getString("userData", "")
    }


    fun getToken(): String? {
        return sharedPreferences!!.getString("token", "")
    }

    fun getCategoryId(): String {
        return sharedPreferences!!.getString("category_id", "")
    }

    fun getSectionId(): String {
        return sharedPreferences!!.getString("section_id", "")
    }

    fun getLogged(): String {
        return sharedPreferences!!.getString("logged", "")
    }

   /* fun getFcmToken(): String? {
        return sharedPreferences!!.getString("fcmToken","")
    }*/
}