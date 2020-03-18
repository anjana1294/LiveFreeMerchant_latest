package com.livefree.merchant.ui.menu

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.menu.model.AddMenuRequest
import com.livefree.merchant.ui.menu.model.SingleMenuRequest
import com.livefree.merchant.ui.menu.model.UpdateMenuRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.util.FileUtils
import com.livefree.merchant.util.ImagePicker
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_menu.*
import com.livefree.merchant.R

class AddMenuDetailFragment : BaseFragment() {
    var sharedPref: SharedPref = SharedPref()
    var encodedImage: String = ""
    private var callback: Callback? = null
    private var menuID: String = ""

    companion object {
        fun newInstance(menuID: String): AddMenuDetailFragment {
            val fragment = AddMenuDetailFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", menuID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            menuID = it.getString("ARG_PARAM1")

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_detail_menu, container, false)
        //  setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_save_menu_btn.setOnClickListener(clickListener)
        iv_food_pic.setOnClickListener(clickListener)

        if (!TextUtils.isEmpty(menuID)) {
            getServer(menuID)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }

    val clickListener = View.OnClickListener { view ->
        var foodName = tv_food_name.text.toString().trim()
        var foodPrice = tv_price.text.toString()
        //  var foodImage = iv_food_pic.text.toString()


        when (view.getId()) {

            R.id.tv_save_menu_btn -> {
                if (TextUtils.isEmpty(foodName)) {
                    tv_food_name?.error = "Please enter the food name"
                } else if (TextUtils.isEmpty(foodPrice)) {
                    tv_price?.error = "Please enter the price"
                } else if (TextUtils.isEmpty(encodedImage)) {
                    Toast.makeText(context, "Please select an image", Toast.LENGTH_LONG).show()
                } else

                    if (TextUtils.isEmpty(menuID))
                        AddMenu(NetworkModule(), sharedPref)
                    else
                        getEdit(NetworkModule(), sharedPref)
            }
            R.id.iv_food_pic -> {

                ImagePicker.setImage(childFragmentManager, context()) {
                    showProgressDialog()
                    FileUtils.encodeToBase64(it) {
                        encodedImage = it;
                        hideProgressDialog()
                    }
                    iv_food_pic.setImageBitmap(it)
                }

            }


        }
    }

    /* override fun onOptionsItemSelected(item: MenuItem?): Boolean {
         when (item?.itemId) {
             R.id.edit_toolbar -> {
                 Log.i("item id ", item.getItemId().toString() + "")
                 Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()

                 return super.onOptionsItemSelected(item)
             }
             else -> return super.onOptionsItemSelected(item)
         }
         return super.onOptionsItemSelected(item)
     }*/


    /*   override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
           inflater?.inflate(R.menu.section_toolbar, menu)
           super.onCreateOptionsMenu(menu, inflater)
       }*/


    internal fun AddMenu(
            networkModule: NetworkModule, sharedPref: SharedPref
    ) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,","")

        var addRequest = AddMenuRequest(
                sharedPref.getCategoryId(),
                tv_food_name.text.toString(),
                tv_price.text.toString(),
                encodedImage
        )

        service.addMenu(sharedPref.getToken(), addRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Add Menu Response", httpResponse.toString())
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
                            //                    showProgressDialog()
                            showMessage(error.toString())
                            Log.v("Add Menu Response", error.toString())
                        })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

    interface Callback {
        fun showCreateTable()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AddMenuDetailFragment.Callback)
            callback = context
    }

    fun getEdit(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,","")
        var updateRequest = UpdateMenuRequest(
                menuID,
                tv_food_name.text.toString().trim(),
                tv_price.text.toString().trim(),
                encodedImage
        )
        service.updateMenu(sharedPref.getToken(), updateRequest)
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
                }, { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v("Table List Response", error.toString())
                })

    }

    fun getServer(serverID: String) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        var menuRequest = SingleMenuRequest(
                serverID
        )
        service.singleMenu(sharedPref.getToken(), menuRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("menu single Response", httpResponse.toString())
                    if (httpResponse.isSuccessful) {
                        hideProgressDialog()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            var menuData = response.singleMenuData
                            tv_food_name.setText(menuData.name)
                            tv_price.setText(menuData.price.toString())
                            this.encodedImage = menuData.image
                            Picasso.with(context).load(menuData.image).error(R.drawable.add_photo).into(iv_food_pic)
//                        encodedImage
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


