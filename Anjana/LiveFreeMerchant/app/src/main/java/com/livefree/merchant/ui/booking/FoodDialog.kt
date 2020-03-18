package com.livefree.merchant.ui.booking

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseDialog
import com.livefree.merchant.ui.booking.adapter.FoodMenuAdapter
import com.livefree.merchant.ui.booking.model.FoodOrderRequest
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_order_food.*
import kotlinx.android.synthetic.main.layout_empty.*

class FoodDialog : BaseDialog() {
    private var bookingID = ""
    var sharedPref: SharedPref = SharedPref()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            bookingID = arguments!!.getString(ARG_BOOKING_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.dialog_order_food, container, false)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_food_order!!.layoutManager = LinearLayoutManager(context())

        getMenuList(NetworkModule(), sharedPref)
    }

    companion object {
        private val ARG_BOOKING_ID = "booking_id"

        fun newInstance(bookingID: String): FoodDialog {
            val fragment = FoodDialog()
            val args = Bundle()
            args.putString(ARG_BOOKING_ID, bookingID)
            fragment.arguments = args
            return fragment
        }
    }


    internal fun getMenuList(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var foodOrderRequest = FoodOrderRequest(
            bookingID
        )
        service.bookingMenu(sharedPref.getToken(), foodOrderRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Food Menu List Response", httpResponse.toString())

                hideProgressBar()
                if (httpResponse.isSuccessful) {
                    var response = httpResponse.body()
                    if (response?.status!!) {

                        if (response.foodMenu.isNullOrEmpty()) {
                            emptyView.visibility = View.VISIBLE
                        } else {
                            rv_food_order!!.adapter = FoodMenuAdapter(response.foodMenu, context())
                            emptyView.visibility = View.GONE
//                            Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                        }

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                 } else{
                    emptyView.visibility = View.VISIBLE
                }
            },
                { error ->
                    hideProgressBar()
                    Toast.makeText(context(), error.message, Toast.LENGTH_LONG).show()
                    Log.v("Food Menu List Response", error.toString())
                })
    }


    override fun showProgressBar() {
        super.showProgressBar()
        progressBar.visibility = View.VISIBLE

    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        progressBar.visibility = View.INVISIBLE
    }
}