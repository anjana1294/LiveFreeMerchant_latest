package com.livefree.merchant.ui.section

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.menu.adapter.MenuListAdapter
import com.livefree.merchant.ui.menu.model.DeleteMenuRequest
import com.livefree.merchant.ui.menu.model.MenuListRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.util.AlertUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_menu_list.*
import com.livefree.merchant.R
import kotlinx.android.synthetic.main.layout_empty.*


class MenuListFragment : BaseFragment(), RecyclerViewItemListener {

    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()
    var param1: String = ""
    private var menuId: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_menu_list, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_menu_list.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(
                rv_menu_list.getContext(),
                DividerItemDecoration.VERTICAL
        )
        rv_menu_list.addItemDecoration(dividerItemDecoration)
        getMenuList(NetworkModule(), sharedPref)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_menu_toolbar -> {
                callback?.openAddMenuScreen(menuId)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        //  callback?.showSectionList("")
    }

    interface Callback {
        //  fun showSectionList(secList: String)//id
        fun openAddMenuScreen(menuID: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    internal fun getMenuList(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var menuListRequest = MenuListRequest(
                sharedPref.getCategoryId(),
                "0",
                "150"

        )
        service.menuList(sharedPref.getToken(), menuListRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Menu List Response", httpResponse.toString())

                    if (httpResponse.isSuccessful) {
                        hideProgressDialog()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            if (response.menuData.isNullOrEmpty()) {
                                emptyView.visibility = View.VISIBLE

                                rv_menu_list.visibility = View.GONE
                            } else {

                                rv_menu_list.adapter = MenuListAdapter(response.menuData, context(), this)
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
                            Log.v("Menu List Response", error.toString())
                        })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

    override fun onItemDelete(menuID: String) {

        AlertUtil.showActionAlertDialog(context(), "Confirmation", "Are you sure you want to delete this menu?"
                , "Yes", "No", DialogInterface.OnClickListener { dialog, which ->

            showProgressDialog()
            val service: RestService = NetworkModule().provideRetrofitClient(context())
            var menuRequest = DeleteMenuRequest(
                    menuID
            )
            service.deleteMenu(sharedPref.getToken(), menuRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe({ httpResponse ->
                        Log.v("Menu List Response", httpResponse.toString())
                        if (httpResponse.isSuccessful) {
                            hideProgressDialog()
                            var response = httpResponse.body()
                            if (response?.status!!) {
                                this.getMenuList(NetworkModule(), sharedPref)
                                Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                            } else
                                Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                        }
                    },
                            { error ->
                                hideProgressDialog()
                                showMessage(error.toString())
                                Log.v("Menu List Response", error.toString())
                            })
        })

    }

    override fun onItemEdit(menuID: String) {
        callback?.openAddMenuScreen(menuID)
    }
}