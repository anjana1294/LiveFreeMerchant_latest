package com.livefree.merchant.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.livefree.merchant.R
import com.livefree.merchant.fcm.MyFirebaseInstanceIdService
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.home.HomeActivity
import com.livefree.merchant.ui.login.LoginActivity


class SplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000
    var sharedPref: SharedPref = SharedPref()

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if (sharedPref.isLogged()) {
                var intentHome = Intent(this, HomeActivity::class.java)
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intentHome)
                finish()
            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(this)
    }

    fun fetchFcmToken() {
        var intentFcm = Intent(this, MyFirebaseInstanceIdService::class.java)
        startService(intentFcm)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        fetchFcmToken()
    }
}