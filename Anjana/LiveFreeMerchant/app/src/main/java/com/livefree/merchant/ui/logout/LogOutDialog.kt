package com.livefree.merchant.ui.logout

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseDialog
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_logout.*

class LogOutDialog: BaseDialog() {
    private var callback:Callback?=null
    var sharedPref: SharedPref = SharedPref()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_logout, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_no.setOnClickListener(clickListener)
        btn_yes.setOnClickListener(clickListener)
    }
    val clickListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_no -> {
                dismiss()
            }
            R.id.btn_yes -> {
                dismiss()
                getLogOut(NetworkModule(),sharedPref)
                /* startActivity((Intent(context, HomeActivity::class.java)))
                activity?.finish()*/

            }
        }

    }



    interface Callback {
        fun showLoginScreen()
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
        }
    }

    internal fun getLogOut(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())

        service.logOut(sharedPref.getToken())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("LogOut Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        sharedPref.clearSharedPreferences()
                        callback?.showLoginScreen()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("LogOut Response", error.toString())
                })
    }

}
