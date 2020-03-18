package com.livefree.merchant.ui.about

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.RecyclerViewItemListener
import com.livefree.merchant.ui.about.model.AboutRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.util.FileUtils
import com.livefree.merchant.util.ImagePicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_about.*
import com.livefree.merchant.R
import com.livefree.merchant.ui.about.adapter.AboutAdapter
import com.livefree.merchant.ui.about.model.DisplayAboutRequest
import com.livefree.merchant.ui.about.model.DisplayData
import com.livefree.merchant.ui.about.model.UpdateAboutRequest
import com.livefree.merchant.ui.home.HomeActivity
import com.squareup.picasso.Picasso


class AboutFragment : BaseFragment(), RecyclerViewItemListener {


    var sharedPref: SharedPref = SharedPref()
    private var callback: Callback? = null
    private var profilePath: String? = null
    private var AboutID: String? = ""
    private var contentResolver: ContentResolver? = null
    var encodedImage: String = ""
    lateinit var adapter: AboutAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(
            R.layout.fragment_about, container, false
        )
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_about_save.setOnClickListener(clickListener)
        iv_about_pic.setOnClickListener {
            ImagePicker.setImage(childFragmentManager, context()) {
                showProgressDialog()
                FileUtils.encodeToBase64(it)
                {
                    encodedImage = it
                    hideProgressDialog()
                }
                iv_about_pic.setImageBitmap(it)
//                if (!uri.toString().contains("content://")) {
//                    uri = Uri.fromFile(File(uri.toString()))
//                }
//                Picasso.with(context).load(uri)
//                    .placeholder(R.drawable.ic_icon_loader).error(R.drawable.ic_empty_cart)
//                    .into(iv_about_pic)
            }


        }
        /*  if (!TextUtils.isEmpty(menuID)) {
              getAbout(menuID)
          }*/

        getDisplayAbout(sharedPref)
    }

    val clickListener = View.OnClickListener { view ->
        var business_name = et_business_name.text.toString()
        var desc = et_desc.text.toString().trim()

        when (view.getId()) {
            R.id.btn_about_save -> {
                if (TextUtils.isEmpty(business_name)) {
                    et_business_name?.error = "Please enter business name"
                } else if (TextUtils.isEmpty(desc)) {
                    et_desc?.error = "Please enter few line for description"
                } else if (TextUtils.isEmpty(encodedImage)) {
                    Toast.makeText(context, "Please Select An Image", Toast.LENGTH_LONG).show()
                } else
                    if (TextUtils.isEmpty(AboutID))
                        AboutSave(NetworkModule(), sharedPref)
                    else
                        onUpdateAbout(sharedPref)


            }
        }
    }


    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addImages()
        rv_about.layoutManager = GridLayoutManager(activity, 2)
        rv_about.addItemDecoration(GridItemDecoration(4, 2))
        rv_about.adapter = AboutAdapter(itemImages, context())
    }*/

    override fun onAttach(context: Context?) {
        super.onAttach(context)
//        if (context is Callback)
//            callback = context
    }

    override fun onItemClick(position: Int) {
        callback?.showAbout("")
    }

    interface Callback {
        fun showAbout(about: String)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.edit_toolbar -> {
                Log.i("item id ", item.getItemId().toString() + "")
             //   Toast.makeText(context(),"Edit",Toast.LENGTH_SHORT).show()

                et_business_name.setFocusableInTouchMode(true)
                et_business_name.requestFocus()
                btn_about_save.visibility = View.VISIBLE
                et_business_name.isEnabled = true
                et_desc.isEnabled = true
                iv_about_pic.isClickable=true
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    internal fun AboutSave(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,", "")
        var aboutRequest = AboutRequest(
            sharedPref.getCategoryId(),
            et_business_name.text.toString().trim(),
            et_desc.text.toString().trim(),
            encodedImage
        )

        service.saveAddAbout(sharedPref.getToken(), aboutRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v("About Response", httpResponse.toString())

                if (httpResponse.isSuccessful) {
                    hideProgressDialog()
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
                    Log.v("About Response", error.toString())
                })
    }


    // Updated About Data
    internal fun onUpdateAbout(sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,", "")
        var aboutRequest = UpdateAboutRequest(
            sharedPref.getCategoryId(),
            et_business_name.text.toString(),
            et_desc.text.toString(),
            //   iv_about_pic.isClickable=true,
            encodedImage


        )
        service.UpdateAbout(sharedPref.getToken(), aboutRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v(" About Update Response", httpResponse.toString())
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
                    Log.v("Update About Response", error.toString())
                })
    }

    //Display About Data
    internal fun getDisplayAbout(sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = NetworkModule().provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,", "")
        var displayaboutRequest = DisplayAboutRequest(
            sharedPref.getCategoryId()


        )
        service.DisplayAbout(sharedPref.getToken(), displayaboutRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe({ httpResponse ->
                Log.v(" About Display Response", httpResponse.toString())
                hideProgressDialog()

                if (httpResponse.isSuccessful) {
                    var response = httpResponse.body()
                    if (response?.status!!) {
                        AboutID = response.displayData._id
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                        var displayData = response.displayData
                        et_business_name.setText(displayData.name)
                        et_desc.setText(displayData.about)
                        this.encodedImage = displayData.image
                        Picasso.with(context).load(displayData.image).error(R.drawable.add_photo).into(iv_about_pic)


                        et_business_name.isEnabled = false
                        et_desc.isEnabled = false
                          iv_about_pic.isClickable = false
                        btn_about_save.visibility = View.INVISIBLE

                    } else
                        Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                }
            },
                { error ->
                    hideProgressDialog()
                    showMessage(error.toString())
                    Log.v(" About Display Response", error.toString())
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
