package com.livefree.merchant.ui.password.changePasword

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
import com.livefree.merchant.ui.login.LoginActivity
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.password.changePasword.model.ChangeRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ChangePasswordActivity : BaseActivity() {


    var isEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        ButterKnife.bind(this);
        setSupportActionBar(app_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayShowTitleEnabled(true)
            actionBar.title = "Change Password"
        }
        isEmail = intent.getStringExtra("email")
        btn_done.setOnClickListener(clickListener)
    }

    val clickListener = View.OnClickListener { view ->
        var newPassword:String=new_password.text.toString()
        var conPassword:String=confirm_password.text.toString()

        when (view.getId()) {
            R.id.btn_done -> {
                if (TextUtils.isEmpty(newPassword)) {
                    new_password?.error = "Required"
                } else if (TextUtils.isEmpty(conPassword)) {
                    confirm_password?.error = "Required"
                }
                else if (!newPassword.equals(conPassword)){
                    Toast.makeText(this,"Does not match !",Toast.LENGTH_LONG).show()

                }else
                ChangePassword(NetworkModule())

            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    internal fun ChangePassword(networkModule: NetworkModule) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        var changeRequest = ChangeRequest(
            isEmail,
            new_password.text.toString().trim()
            //confirm_password.text.toString().trim()

        )


        service.ChangePassword(changeRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("ChangePass Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {

                        showLoginScreen()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v("Change Password Response", error.toString())
                })
    }

    override fun showProgressBar() {
        btn_done.visibility = (View.INVISIBLE)
        change_progressBar.visibility = View.VISIBLE
    }



    override fun hideProgressBar() {
        change_progressBar.visibility = View.INVISIBLE
        btn_done.setVisibility(View.VISIBLE)
    }

    fun showLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()

    }

}