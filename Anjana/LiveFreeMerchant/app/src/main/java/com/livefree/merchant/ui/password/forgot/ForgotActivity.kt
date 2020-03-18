package com.livefree.merchant.ui.password.forgot

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseActivity
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.password.forgot.model.ForgotRequest
import com.livefree.merchant.ui.password.otp.OTPActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.progressBar
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ForgotActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        ButterKnife.bind(this);
        setSupportActionBar(app_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.title = "Forgot Password"
        }

        app_toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        btn_send.setOnClickListener(clickListener)
    }

    val clickListener = View.OnClickListener { view ->

        when (view.getId()) {

            R.id.btn_send -> {
                if (TextUtils.isEmpty(et_your_number.toString())) {
                    et_first_name?.error = "Required"
                } else
                    forgot(NetworkModule())

            }

        }


    }

    internal fun forgot(networkModule: NetworkModule) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        var forgot = ForgotRequest(
            et_your_number.text.toString().trim()

        )

        service.forgot(forgot)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Forgot Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {

                        showOTPScreen()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v("Forgot Response", error.toString())
                })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgressBar() {
        btn_send.visibility = (View.INVISIBLE)
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        btn_send.setVisibility(View.VISIBLE)
    }

    fun showOTPScreen() {
        val value: String = et_your_number.text.toString()
        var intent = Intent(this, OTPActivity::class.java)
        intent.putExtra("isForgot", true)
        intent.putExtra("email", value)

        startActivity(intent)

    }


}