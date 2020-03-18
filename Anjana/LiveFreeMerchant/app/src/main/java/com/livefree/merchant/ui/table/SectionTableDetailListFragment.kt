package com.livefree.merchant.ui.table

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.table.adapter.SectionTableDetailAdapter
import com.livefree.merchant.ui.table.model.DeletetTableListdataRequest
import com.livefree.merchant.ui.table.model.TableListRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragement_section_table_list.*
import com.livefree.merchant.R
import kotlinx.android.synthetic.main.layout_empty.*


class SectionTableDetailListFragment : BaseFragment(), onTableItemClickListener {


    var sharedPref: SharedPref = SharedPref()
    private var callback: Callback? = null
    var param1: String = ""
    private var section_id: String = ""

    companion object {
        fun newInstance(section_id: String, tableName: String): SectionTableDetailListFragment {
            val fragment = SectionTableDetailListFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", section_id)
            args.putString("ARG_PARAM2", tableName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("ARG_PARAM1")
            section_id = it.getString("ARG_PARAM2")

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragement_section_table_list, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_table.layoutManager = LinearLayoutManager(activity)
        getTableList(NetworkModule(), sharedPref)


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.table_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.addTable_toolbar -> {
                Log.i("item id ", item.getItemId().toString() + "")
                callback?.openAddTableDetail(param1, "")
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    interface Callback {
        fun openAddTableDetail(_id: String, section_id: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    internal fun getTableList(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var tableRequest = TableListRequest(
                sharedPref.getCategoryId(),
                this.param1


        )
        service.tableList(sharedPref.getToken(), tableRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Table List Response", httpResponse.toString())

                    if (httpResponse.isSuccessful) {
                        hideProgressDialog()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            if (response.tableList.size > 0) {
                                rv_table.adapter =
                                        SectionTableDetailAdapter(response.tableList, context(), this)

                                Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                            } else {
                                emptyView.visibility = View.VISIBLE
                            }
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

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }


    override fun onItemDelete(_id: String) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var tableRequest = DeletetTableListdataRequest(
                sharedPref.getCategoryId(),
                this.param1,
                _id
        )
        service.deleteTable(sharedPref.getToken(), tableRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Table List Response", httpResponse.toString())
                    if (httpResponse.isSuccessful) {
                        hideProgressDialog()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            this.getTableList(NetworkModule(), sharedPref)
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

    override fun onItemEdit(_id: String) {
        callback?.openAddTableDetail(param1, _id)

    }
}


