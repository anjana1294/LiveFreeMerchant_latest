package com.livefree.merchant.ui.server

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.server.model.AddServerRequest
import com.livefree.merchant.ui.server.model.SingleServerRequest
import com.livefree.merchant.ui.server.model.UpdateServerRequest

import com.livefree.merchant.util.DatePickerUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.fragment_server_detail.*
import java.text.SimpleDateFormat
import java.util.*


class AddServerDetailFragment : BaseFragment(), onServerItemClickListener {
    private var serverID: String = ""
    private var param2: String? = null
    var sharedPref: SharedPref = SharedPref()

    /*   private const val ARG_ID = "serverID"*/


    companion object {
        fun newInstance(serverID: String): AddServerDetailFragment {
            val fragment = AddServerDetailFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", serverID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serverID = it.getString("ARG_PARAM1")

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_server_detail, container, false)
        //  setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  tv_cancel_server_btn.setOnClickListener(clickListener)
        tv_save_server_btn.setOnClickListener(clickListener)
        et_serverage.setOnClickListener(clickListener)

        if (!TextUtils.isEmpty(serverID)) {
            getServer(serverID)
        }
    }

    val clickListener = View.OnClickListener { view ->
        var serverName = et_servername.text.toString().trim()
        var serverContact = et_servercontact_detail.text.toString()
        var serverAge = et_serverage.text.toString()

        when (view.getId()) {

            R.id.tv_save_server_btn -> {
                if (TextUtils.isEmpty(serverName)) {
                    et_servername?.error = "Please enter the name"
                } else if (TextUtils.isEmpty(serverContact)) {
                    et_servercontact_detail?.error = "Please enter the contact number"
                } else if (TextUtils.isEmpty(serverAge)) {
                    et_serverage?.error = "Please enter the Date of Birth"
                } else {
                    if (TextUtils.isEmpty(serverID))
                        AddServer(NetworkModule(), sharedPref)
                    else
                        getEdit(NetworkModule(), sharedPref)

                }
            }
            R.id.et_serverage -> {
                DatePickerUtil.openDatePicker(context()) {
                    val myFormat = "dd-MM-yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    et_serverage!!.setText(sdf.format(it).toString())
                }
            }


        }

    }

    internal fun AddServer(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var addRequest = AddServerRequest(
            sharedPref.getCategoryId(),
            et_servername.text.toString().trim(),
            et_servercontact_detail.text.toString().trim(),
            et_serverage.text.toString().trim()

        )


        service.addServer(sharedPref.getToken(), addRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Add Server Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressBar()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressBar()
                    showMessage(error.toString())
                    Log.v("Add Server Response", error.toString())
                })
    }

    override fun showProgressBar() {
        tv_save_server_btn.visibility = (View.INVISIBLE)
        server_progressBar.visibility = View.VISIBLE
    }


    override fun hideProgressBar() {
        server_progressBar.visibility = View.INVISIBLE
        tv_save_server_btn.setVisibility(View.VISIBLE)
    }
    /*override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.section_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }*/


    override fun onItemDelete(serverID: String) {
    }

    override fun onItemEdit(serverID: String) {
    }

    fun getEdit(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var updateRequest = UpdateServerRequest(
            serverID,
            et_servername.text.toString().trim(),
            et_servercontact_detail.text.toString().trim(),
            et_serverage.text.toString().trim()
        )
        service.updateServer(sharedPref.getToken(), updateRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("update Response", httpResponse.toString())
                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        //     this.getServerList(NetworkModule(), sharedPref)
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("Table List Response", error.toString())
                })

    }

    fun getServer(serverID: String) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var serverRequest = SingleServerRequest(
            serverID
        )
        service.singleServer(sharedPref.getToken(), serverRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("server single List Response", httpResponse.toString())
                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        var serverData = response.singleServerData
                        et_servername.setText(serverData.name)
                        et_servercontact_detail.setText(serverData.contact)
                        et_serverage.setText(serverData.dateofbirth)
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("server single Response", error.toString())
                })

    }

}