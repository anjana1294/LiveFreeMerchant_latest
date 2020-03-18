package com.livefree.merchant.ui.table

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.section.model.UpdateSectionRequest
import com.livefree.merchant.ui.server.model.ServerData
import com.livefree.merchant.ui.server.model.ServerListRequest
import com.livefree.merchant.ui.table.model.AddTableRequest
import com.livefree.merchant.ui.table.model.SingleSectionRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_table_details.*
import javax.security.auth.callback.Callback

import com.livefree.merchant.R
import com.livefree.merchant.ui.table.model.UpdateTableListRequest

class AddTableDetailFragment : BaseFragment() {
    var sharedPref: SharedPref = SharedPref()
    private var callback: Callback? = null
    private var count: Int = 0
    var serverList = ArrayList<ServerData>()
    var selectedServerId = ""
    var sectionID = "";
    var tableID = "";


    companion object {
        fun newInstance(SectioID: String, tableID: String): AddTableDetailFragment {
            val fragment = AddTableDetailFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", SectioID)
            args.putString("ARG_PARAM6", tableID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionID = it.getString("ARG_PARAM1")
            tableID = it.getString("ARG_PARAM6")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_table_details, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        getServerList(NetworkModule(), sharedPref)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)

        tv_save_table_btn.setOnClickListener(clickListener)
        ic_remove.setOnClickListener(clickListener)
        ic_add.setOnClickListener(clickListener)

        et_server.setInputType(InputType.TYPE_NULL)

        et_server.setOnClickListener {
            openListDialog(this.serverList)
        }

        Log.e("Table ID", tableID)
        Log.e("Table ID", sectionID)

        if (!TextUtils.isEmpty(tableID)) {
            getTableSection(tableID)
        }
    }

    val clickListener = View.OnClickListener { view ->
        var tableName = et_table.text.toString().trim()
        var chairNumber = tv_quantity_chair.text.toString().trim()



        when (view.getId()) {

            R.id.tv_save_table_btn -> {
                if (TextUtils.isEmpty(tableName)) {
                    et_table?.error = "Please enter the table number or name"
                } else if (chairNumber.equals("0")) {
                    et_chair?.error = "Please enter number of chair above 0"
                } else if (TextUtils.isEmpty(selectedServerId)) {
                    et_server?.error = "Please select at least one server"
                } else
                    if (TextUtils.isEmpty(tableID))
                        AddTable(NetworkModule(), sharedPref)
                    else
                        getTableEdit(NetworkModule(), sharedPref)

            }

            R.id.ic_remove -> {

                if (count != 0) {

                    count = count - 1
                    tv_quantity_chair.setText(count.toString())
                    et_chair.setText(count.toString())
                }
            }
            R.id.ic_add -> {
                count = count + 1
                // var stringVal: String? = count?.let { Integer.toString(it) }
                tv_quantity_chair.setText(count.toString())
                et_chair.setText(count.toString())
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.edit_toolbar -> {
                Log.i("item id ", item.getItemId().toString() + "")
                //  Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()

                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    /*   override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
           inflater?.inflate(R.menu.section_toolbar, menu)
           super.onCreateOptionsMenu(menu, inflater)
       }*/
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    internal fun AddTable(
        networkModule: NetworkModule, sharedPref: SharedPref
    ) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var tableRequest = AddTableRequest(
            sharedPref.getCategoryId(),
            sectionID,
            et_table.text.toString(),
            et_chair.text.toString(),
            selectedServerId
        )

        service.addTable(sharedPref.getToken(), tableRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Add Table Response", httpResponse.toString())
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
                    Log.v("Add Table Response", error.toString())
                })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

    private fun openListDialog(serverNameList: ArrayList<ServerData>) {
        val builder = AlertDialog.Builder(context(), R.style.AlertDialogStyle)

        builder.setTitle("Choose Server")
        var categoriesName = ArrayList<String>()
        var selectedServer = ""
        for (category in serverNameList) {
            categoriesName.add(category.name)
        }
        val cs = categoriesName.toTypedArray<CharSequence>()

        val checkedItem = 1// cow
        builder.setSingleChoiceItems(cs, checkedItem, DialogInterface.OnClickListener { dialog, which ->
            selectedServer = categoriesName[which]
            selectedServerId = serverNameList.get(which)._id
            Log.e("sda", selectedServer)
        })

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            et_server.setText(selectedServer)
        })
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
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

                        this.serverList = response.serverData
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

    fun getTableEdit(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var updateRequest = UpdateTableListRequest(
            sharedPref.getCategoryId(),
            sectionID,
            tableID,
            et_table.text.toString(),
            et_chair.text.toString(),
            selectedServerId

        )
        service.updateTable(sharedPref.getToken(), updateRequest)
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

    fun getTableSection(tableID: String) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var sectionRequest = SingleSectionRequest(
            sharedPref.getCategoryId(),
            sectionID,
            tableID
        )
        service.singleTable(sharedPref.getToken(), sectionRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v(" single table Response", httpResponse.toString())
                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        var sectionData = response.singleData

                        if (serverList.size > 0) {
                            try {
                                var serverName = serverList.filter {
                                    it._id.equals(sectionData.server_id)
                                }
                                et_server.setText(serverName.get(0).name)
                            } catch (e: Exception) {
                                et_server.setText(sectionData.server_id)
                            }
                        }
                        et_table.setText(sectionData.name)
                        et_chair.setText(sectionData.chair.toString())

                        selectedServerId = sectionData.server_id
                        tv_quantity_chair.setText(sectionData.chair.toString())
                       count = sectionData.chair.toInt()
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