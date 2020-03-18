package com.livefree.merchant.ui.businessday

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.businessday.adapter.BusinessDaysAdapter
import com.livefree.merchant.ui.businessday.model.BusinessDays
import com.livefree.merchant.ui.businessday.model.BusinessDaysUpdateRequest
import com.livefree.merchant.ui.businessday.model.BusinessDisplayRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_business_days.*

class BusinessDaysFragment : BaseFragment(), RecyclerViewItemListener {


    var daysItems: ArrayList<BusinessDays> = ArrayList()
    lateinit var adapter: BusinessDaysAdapter
    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_business_days, container, false)
        setHasOptionsMenu(true);
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        addHomeItem()

        rv_working_days.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
//        rv_working_days.addItemDecoration(GridItemDecoration(4, 2))

        getBussinessDays(sharedPref)


    }

    override fun onItemClick(position: Int) {
        adapter.notifyDataSetChanged()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    public interface Callback {
        fun onGridViewItemClick(position: Int)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.save_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_toolbar -> {
                Log.i("item id ", this.daysItems.toString() + "")
       //         Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                this.saveBussinessDays(sharedPref)
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemDelete(menuID: String) {
    }

    override fun onItemEdit(menuID: String) {
    }


    internal fun getBussinessDays(sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var serverRequest = BusinessDisplayRequest(
            sharedPref.getCategoryId()
        )
        service.displayBusinessDay(sharedPref.getToken(), serverRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Server List Response", httpResponse.toString())
                hideProgressDialog()

                if (httpResponse.isSuccessful) {
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        adapter = BusinessDaysAdapter(response.businessDays, context(), this)
                        rv_working_days.adapter = adapter
                        this.daysItems = response.businessDays


                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("business List Response", error.toString())
                })
    }


    internal fun saveBussinessDays(sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var serverRequest = BusinessDaysUpdateRequest(
            sharedPref.getCategoryId(),
            this.daysItems
        )
        service.addBusiness(sharedPref.getToken(), serverRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Server List Response", httpResponse.toString())
                hideProgressDialog()

                if (httpResponse.isSuccessful) {
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
                    Log.v("Server List Response", error.toString())
                })
    }
}