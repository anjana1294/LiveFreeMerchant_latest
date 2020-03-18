package com.livefree.merchant.ui.signUp

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

import com.livefree.merchant.ui.login.LoginActivity
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.password.otp.OTPActivity
import com.livefree.merchant.ui.signUp.model.SignupRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.progressBar

class SignUpActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        val spannyText = SpannyText(
            resources.getString(R.string.already_have_an_account), ForegroundColorSpan(
                ContextCompat.getColor(this, R.color.colorGrey)
            )
        ).append(
            " Log In",
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorOrange))
        )
        tv_login!!.text = spannyText

        val termsAndCond = SpannyText(
            resources.getString(R.string.cb_tnc_msg), ForegroundColorSpan(
                ContextCompat.getColor(this, R.color.colorGrey)
            )
        ).append(
            " Terms and Conditions",
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorOrange)), StyleSpan(Typeface.BOLD)
        )
        cb_tnc.setText(termsAndCond)

        tv_login.setOnClickListener(clickListener)
        btn_SignUp.setOnClickListener(clickListener)

    }

    val clickListener = View.OnClickListener { view ->
        var username = et_first_name.text.toString()
        var email = et_email.text.toString().trim()
        var phone = et_phone.text.toString().trim()
        var password = input_password.text.toString()
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        when (view.getId()) {
            R.id.tv_login -> {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.btn_SignUp -> {

                if (TextUtils.isEmpty(username)) {
                    et_first_name?.error = "Required"
                } else if (TextUtils.isEmpty(phone)) {
                    et_phone?.error = "Required"
                } else if (!email.matches(emailPattern.toRegex())) {
                    et_email?.error = "Invalid email address"
                } else if (TextUtils.isEmpty(email)) {
                    et_email?.error = "Required"
                } else if (TextUtils.isEmpty(password)) {
                    input_password?.error = "Required"
                } else
                    SignUp(NetworkModule())
            }

        }

    }

       internal fun SignUp(networkModule: NetworkModule) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        var signupRequest = SignupRequest(
            et_first_name.text.toString().trim(),
            et_email.text.toString().trim(),
            et_phone.text.toString().trim(),
            input_password.text.toString().trim()
        )


        service.signup(signupRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("SignUp Response", httpResponse.toString())


                if (httpResponse.isSuccessful) {
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        showOTPScreen()
                        Log.v("request", signupRequest.toString())

                        //  showHomeScreen()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()

                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v("SignUp Response", error.toString())
                })
    }

    override fun showProgressBar() {
        btn_SignUp.visibility = (View.INVISIBLE)
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        btn_SignUp.setVisibility(View.VISIBLE)
    }

    fun showOTPScreen() {
        val email: String = et_email.text.toString()
        var intent = Intent(this, OTPActivity::class.java)
        intent.putExtra("isForgot", false)
        intent.putExtra("email", email)
        startActivity(intent)

    }


}
