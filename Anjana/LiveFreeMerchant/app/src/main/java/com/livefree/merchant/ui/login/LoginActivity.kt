package com.livefree.merchant.ui.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseActivity
import com.livefree.merchant.custom.SpannyText
import com.livefree.merchant.fcm.MyFirebaseInstanceIdService
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.home.HomeActivity
import com.livefree.merchant.ui.location.LocationActivity
import com.livefree.merchant.ui.login.model.LoginRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.password.forgot.ForgotActivity
import com.livefree.merchant.ui.signUp.SignUpActivity
import com.livefree.merchant.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {


    var sharedPref: SharedPref = SharedPref()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val spannyText = SpannyText(
            resources.getString(R.string.donot_have_any_account), ForegroundColorSpan(
                ContextCompat.getColor(this, R.color.colorGrey)
            )
        ).append(
            " Sign Up",
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorOrange)), StyleSpan(Typeface.BOLD)
        )

        tv_signIn!!.text = spannyText

        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(this@LoginActivity)

        tv_signIn.setOnClickListener(clickListener)
        btn_login.setOnClickListener(clickListener)
        tv_forgot.setOnClickListener(clickListener)

    }


    val clickListener = View.OnClickListener { view ->
        var email = et_username.text.toString().trim()
        var password = et_password.text.toString()
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        when (view.getId()) {
            R.id.tv_signIn -> {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.btn_login -> {
              //  Log.d("FCM TOKEN", sharedPref.getFcmToken())
                if (!email.matches(emailPattern.toRegex())) {
                    et_username?.error = "Invalid email address"
                } else if (TextUtils.isEmpty(email)) {
                    et_username?.error = "Required"
                }
//                else if (!isValidPassword(password)) {
//                    et_password?.error = "Invalid password"
//                }
                else if (TextUtils.isEmpty(password)) {
                    et_password?.error = "Required"
                } else
                    Login(NetworkModule(), sharedPref)
            }

            R.id.tv_forgot -> {
                startActivity(Intent(this, ForgotActivity::class.java))
            }
        }

    }


    internal fun Login(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        val fcmSharedPref = SharedPref().getInstance();
        fcmSharedPref.initFcmSharedPreferences(this)
        var loginRequest = LoginRequest(
            et_username.text.toString().trim(),
            et_password.text.toString().trim(),
            fcmSharedPref.getFcmToken()!!
        )

        service.login(loginRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                if (httpResponse.isSuccessful) {
                    Log.v("Login Response", httpResponse.body().toString())
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        if (response?.verified) {

                            sharedPref.putStringValue("token", httpResponse.body()?.data!!)
                            getCategoryId(networkModule, sharedPref, httpResponse.body()?.data!!)
                            //showHomeScreen()
                        } else {
                            startActivity(Intent(this, LocationActivity::class.java))
                            finish()
                        }
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v("Login Response", error.toString())
                })
    }

    internal fun getCategoryId(networkModule: NetworkModule, sharedPref: SharedPref, token: String) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        service.getCategory(token)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                if (httpResponse.isSuccessful) {
                    Log.v("Category ID Response", httpResponse.body().toString())
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        sharedPref.putStringValue("category_id", httpResponse.body()?.restaurantId!!)
                        sharedPref.putBooleanValue(Constants.IS_LOGGED_IN, true)
                        showHomeScreen()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v("Category ID Response", error.toString())
                })
    }

    override fun showProgressBar() {
        btn_login.visibility = (View.INVISIBLE)
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        btn_login.setVisibility(View.VISIBLE)
    }

    fun showHomeScreen() {
        var intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        var intentFcm = Intent(this, MyFirebaseInstanceIdService::class.java)
        startService(intentFcm)
    }


}

