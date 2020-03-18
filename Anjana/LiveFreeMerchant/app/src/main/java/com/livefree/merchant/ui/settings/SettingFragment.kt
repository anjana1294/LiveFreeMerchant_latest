package com.livefree.merchant.ui.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.settings.adapter.settingAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : BaseFragment(), RecyclerViewItemListener {



    val setting: ArrayList<String> = ArrayList()

    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addItem()
//        showProgressDialog()

        rv_settings.layoutManager = LinearLayoutManager(activity)
        rv_settings.adapter = settingAdapter(setting, context(), this)

    }


    fun addItem() {
        setting.clear()
        setting.add(resources.getString(R.string.profile))
        setting.add(resources.getString(R.string.change_password))
    }

    override fun onItemClick(position: Int) {
        when (position) {
            0 -> {
                callback?.let { getProfile(NetworkModule(), sharedPref, it) }
            }
            1 -> {

                callback?.showPasswordScreen()
            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    interface Callback {
        fun showPasswordScreen()
        fun showUserProfileScreen()
    }

    internal fun getProfile(networkModule: NetworkModule, sharedPref: SharedPref, callback: Callback) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        service.getProfile(sharedPref.getToken())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Profile Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        sharedPref.putStringValue("userData", Gson().toJson(response.userData))
                        callback.showUserProfileScreen()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("Profile Response", error.toString())
                })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }
    override fun onItemDelete(menuID: String) {

    }

    override fun onItemEdit(menuID: String) {
    }
}


