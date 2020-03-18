package com.livefree.merchant.ui.server

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.section.adapter.ServerListAdapter
import com.livefree.merchant.ui.server.model.DeleteServerRequest
import com.livefree.merchant.ui.server.model.ServerData
import com.livefree.merchant.ui.server.model.ServerListRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_server_list.*
import kotlinx.android.synthetic.main.layout_empty.*

class ServerListFragment : BaseFragment(), RecyclerViewItemListener, onServerItemClickListener {


    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()
    var server_id: String = ""
    var serverList: ArrayList<ServerData>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_server_list, container, false)
        setHasOptionsMenu(true);
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_server_list.layoutManager = LinearLayoutManager(activity)

        val dividerItemDecoration = DividerItemDecoration(
            rv_server_list.getContext(),
            DividerItemDecoration.VERTICAL
        )

        rv_server_list.addItemDecoration(dividerItemDecoration)
        getServerList(NetworkModule(), sharedPref)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.server_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_server_toolbar -> {
                callback?.openAddServerScreen(server_id, "")//serverID

            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        //   callback?.openAddServerScreen()
    }

    //This id is used for category i.e Restaurant.
    interface Callback {
        fun openAddServerScreen(_id: String, serverID: String)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    internal fun getServerList(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var serverRequest = ServerListRequest(
            sharedPref.getCategoryId(),
            "0",
            "150"

        )
        service.serverList(sharedPref.getToken(), serverRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Server List Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        if (response.serverData.isNullOrEmpty()) {
                            emptyView.visibility = View.VISIBLE

                        } else {
                            rv_server_list.adapter = ServerListAdapter(response.serverData, context(), this)
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
                    Log.v("Server List Response", error.toString())
                })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

    override fun onItemDelete(serverID: String) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var deleteRequest = DeleteServerRequest(
            serverID
        )
        service.deleteServer(sharedPref.getToken(), deleteRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Delete List Response", httpResponse.toString())
                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        this.getServerList(NetworkModule(), sharedPref)
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

    override fun onItemEdit(serverID: String) {
        callback?.openAddServerScreen(server_id, serverID)
    }

}