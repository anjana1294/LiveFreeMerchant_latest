package com.livefree.merchant.ui.password.otp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseActivity
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.location.LocationActivity
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.password.changePasword.ChangePasswordActivity
import com.livefree.merchant.ui.password.otp.model.OtpRequest
import com.livefree.merchant.ui.password.otp.model.ResentRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class OTPActivity : BaseActivity() {

    /*  @OnTextChanged(R.id.ed_one)
      internal fun firstEditTextChanged(s: Editable) {
          if (s.length == 1) {
              ed_two.requestFocus()
          }
      }

      @OnTextChanged(R.id.ed_two)
      internal fun secondEditTextChanged(s: Editable) {
          if (s.length == 1) {
              ed_three.requestFocus()
          }
      }

      @OnTextChanged(R.id.ed_three)
      internal fun thirdEditTextChanged(s: Editable) {
          if (s.length == 1) {
              ed_four.requestFocus()
          }
      }*/

    var isForgot: Boolean = true

    var email: String = ""

    var sharedPref: SharedPref = SharedPref()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        ButterKnife.bind(this)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(this)

        setSupportActionBar(app_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.title = "Verification Code"
        }
        app_toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        isForgot = intent.getBooleanExtra("isForgot", false)
        email = intent.getStringExtra("email")


        btn_verify.setOnClickListener(clickListener)
        tv_resent_otp_code.setOnClickListener(clickListener)
        ed_one.requestFocus()
        ed_one.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (ed_one.getText().toString().length === 1)
                //size as per your requirement
                {
                    ed_two.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {


            }

            override fun afterTextChanged(s: Editable) {

            }

        })

        ed_two.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (ed_one.getText().toString().length === 1)
                //size as per your requirement
                {
                    ed_three.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {


            }

            override fun afterTextChanged(s: Editable) {

            }

        })
        ed_three.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (ed_one.getText().toString().length === 1)
                //size as per your requirement
                {
                    ed_four.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {


            }

            override fun afterTextChanged(s: Editable) {

            }

        })

    }

    val clickListener = View.OnClickListener { view ->

        when (view.getId()) {
            R.id.tv_resent_otp_code -> {
                resendOtp(NetworkModule())
            }
            R.id.btn_verify -> {
                otp(NetworkModule(), sharedPref)
            }

        }


    }

    internal fun otp(
        networkModule: NetworkModule,
        sharedPref: SharedPref
    ) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        var otp_text: String
        otp_text =
            ed_one.text.toString() + "" + ed_two.text.toString() + "" + ed_three.text.toString() + "" + ed_four.text.toString()

        var otp = OtpRequest(otp_text, email)
        service.verifyOTP(otp)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Otp Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        sharedPref.putStringValue("token", response.token)
                        if (isForgot) {
                            showChangePasswordScreen()

                        } else
                            startActivity(Intent(this, LocationActivity::class.java))
                        finish()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())

                    Log.v("Otp Response", error.toString())
                })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgressBar() {
        btn_verify.visibility = (View.INVISIBLE)
        otp_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        otp_progressBar.visibility = View.INVISIBLE
        btn_verify.setVisibility(View.VISIBLE)
    }


    internal fun resendOtp(networkModule: NetworkModule) {
        otp_progressBar
        val service: RestService = networkModule.provideRetrofitClient(this)

        service.ResendOtp(ResentRequest(email))
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v(" Resent Otp Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        ed_one.setText("")
                        ed_two.setText("")
                        ed_three.setText("")
                        ed_four.setText("")
                        ed_one.requestFocus()
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v(" Resent Otp Response", error.toString())
                })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun showChangePasswordScreen() {
        var intent = Intent(this, ChangePasswordActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

}