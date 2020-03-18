package com.livefree.merchant.ui.settings

import android.os.Bundle
import android.text.TextUtils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.settings.model.ChangePassRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_change_password.*
import javax.security.auth.callback.Callback

class ChangePasswordFragment : BaseFragment() {
    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_change_password, container, false)
     //   btn_cancel.clickListener(clickListener)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_save.setOnClickListener(clickListener)
        super.onViewCreated(view, savedInstanceState)
    }
    val clickListener = View.OnClickListener { view ->
        var oldPassword:String = et_old_password.text.toString()
        var newPassword:String = et_new_password.text.toString()
        var confirmPassword:String =et_confirm_password.text.toString()

        when (view.getId()) {

            R.id.btn_save -> {

                if (TextUtils.isEmpty(oldPassword)) {
                    et_old_password?.error = "Required"
                } else if (TextUtils.isEmpty(newPassword)) {
                    et_new_password?.error = "Required"
                }  else if (TextUtils.isEmpty(confirmPassword)) {
                    et_confirm_password?.error = "Required"
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(context,"New Password does not match!",Toast.LENGTH_LONG).show()
                } else
                    ChangePass(NetworkModule(),sharedPref)
            }

        }

    }



    internal fun ChangePass(networkModule: NetworkModule,sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var profilechangeRequest = ChangePassRequest(
            et_old_password.text.toString().trim(),
            et_new_password.text.toString().trim()
            //confirm_password.text.toString().trim()

        )

        service.ProfileChangePassword(sharedPref.getToken(),profilechangeRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("ChangePass Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("Change Password Response", error.toString())
                })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

}