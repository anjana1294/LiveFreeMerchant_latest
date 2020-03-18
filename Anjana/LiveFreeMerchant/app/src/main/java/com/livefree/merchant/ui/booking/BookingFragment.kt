package com.livefree.merchant.ui.booking

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.booking.adapter.BookingAdapter
import com.livefree.merchant.ui.booking.model.BookingRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_booking.*
import java.util.ArrayList
import javax.security.auth.callback.Callback
import android.widget.ArrayAdapter
import android.widget.ListView
import com.livefree.merchant.util.Constants.FOOD_BOOKINGID
import kotlinx.android.synthetic.main.layout_empty.*


class BookingFragment : BaseFragment(), onItemClick {

    val id: ArrayList<String> = ArrayList()
    val serverName: ArrayList<String> = ArrayList()
    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_booking, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_booking.layoutManager = LinearLayoutManager(activity)
        //rv_booking.adapter = BookingAdapter( serverName,serverContact,id , context())

        val dividerItemDecoration = DividerItemDecoration(
            rv_booking.getContext(),
            DividerItemDecoration.VERTICAL
        )
        getBookingList(NetworkModule(),sharedPref )
        //  dividerItemDecoration.setDrawable(context().getDrawable(R.drawable.divider_line))
        rv_booking.addItemDecoration(dividerItemDecoration)
    }

    internal fun getBookingList(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var bookingRequest = BookingRequest(
            sharedPref.getCategoryId()

        )
        service.bookingList(sharedPref.getToken(), bookingRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Booking List Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {

                        if(response.bookingData.isNullOrEmpty())
                        {
                            emptyView.visibility = View.VISIBLE
                        }
                        else {
                            //  this.sectionData = response.sectionData

                            rv_booking.adapter = BookingAdapter(response.bookingData, context(),this)
                            emptyView.visibility = View.GONE
                            Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                        }

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("Booking List Response", error.toString())
                })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

    fun openDialog(v: View) {
        var adapter: ArrayAdapter<String>? = null
        var listView: ListView? = null
        var names = arrayOf("Pizza", "Pasta", "Coffee", "Humburger")
        val alertDialog = AlertDialog.Builder(context)
        val rowList = layoutInflater.inflate(R.layout.layout_listview, null)
        listView = rowList.findViewById(R.id.listView)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, names)
        listView.setAdapter(adapter)
        adapter?.notifyDataSetChanged()
        alertDialog.setView(rowList)
        val dialog = alertDialog.create()
        dialog.show()
    }

    override fun onClick(bookingID: String) {
        FoodDialog.newInstance(bookingID).show(childFragmentManager,FOOD_BOOKINGID)

    }

}