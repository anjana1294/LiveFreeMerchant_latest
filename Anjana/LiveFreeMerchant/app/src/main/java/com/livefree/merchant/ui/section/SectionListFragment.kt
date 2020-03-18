package com.livefree.merchant.ui.section

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
import com.livefree.merchant.ui.section.adapter.SectionListAdapter
import com.livefree.merchant.ui.section.model.SectionData
import com.livefree.merchant.ui.section.model.SectionListRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_section_list.*
import kotlinx.android.synthetic.main.layout_empty.*
import java.util.*

class SectionListFragment : BaseFragment(), RecyclerViewItemListener {


    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()
    var sectionData = ArrayList<SectionData>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_section_list, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_addSection.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(
            rv_addSection.getContext(),
            DividerItemDecoration.VERTICAL
        )
        rv_addSection.addItemDecoration(dividerItemDecoration)
        getSectionList(NetworkModule(), sharedPref)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.section_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_section_toolbar -> {
                callback?.openAddSectionScreen()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        callback?.showSectionList(this.sectionData.get(position)._id, this.sectionData.get(position).name)
    }

    interface Callback {
        fun showSectionList(secList: String, toolbarName: String)//id
        fun openAddSectionScreen()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback)
            callback = context
    }

    internal fun getSectionList(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        var sectionRequest = SectionListRequest(
            sharedPref.getCategoryId(),
            "0",
            "150"

        )
        service.sectionList(sharedPref.getToken(), sectionRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("Server List Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        this.sectionData = response.sectionData
                        if (response.sectionData.isNullOrEmpty()) {
                            emptyView.visibility = View.VISIBLE
                        } else {
                            rv_addSection.adapter = SectionListAdapter(
                                response.sectionData, context(), this
                            )
                            emptyView.visibility = View.GONE
                        }
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("Section List Response", error.toString())
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