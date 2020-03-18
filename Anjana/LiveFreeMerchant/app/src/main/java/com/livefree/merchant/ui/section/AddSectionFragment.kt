package com.livefree.merchant.ui.section

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.section.model.AddSectionRequest
import com.livefree.merchant.util.FileUtils
import com.livefree.merchant.util.ImagePicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_section_detail.*
import com.livefree.merchant.R


class AddSectionFragment : BaseFragment() {
    var sharedPref: SharedPref = SharedPref()
    private var profilePath: String? = null
    private var contentResolver: ContentResolver? = null
    var encodedImage: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_section_detail, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref?.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_save_btn.setOnClickListener(clickListener)
        iv_section_pic.setOnClickListener(clickListener)

    }

    val clickListener = View.OnClickListener { view ->
        var section_name = tv_section_name.text.toString()


        when (view.getId()) {
            R.id.tv_save_btn -> {
                if (TextUtils.isEmpty(section_name)) {
                    tv_section_name?.error = "Please enter section name"
                } else if (TextUtils.isEmpty(encodedImage)) {
                    Toast.makeText(context, "Please Select Image", Toast.LENGTH_LONG).show()
                } else
                    SectionSave(NetworkModule(), sharedPref)
            }
            R.id.iv_section_pic -> {
                ImagePicker.setImage(childFragmentManager, context()) {
                    showProgressDialog()
                    FileUtils.encodeToBase64(it, {
                        encodedImage = it;
                        hideProgressDialog()
                    })
//                    if (!uri.toString().contains("content://")) {
//                        uri = Uri.fromFile(File(uri.toString()))
//                    }
                    iv_section_pic.setImageBitmap(it)
//                    Picasso.with(context).load(uri).fit()
//                        .placeholder(R.drawable.ic_icon_loader)
//                        .error(R.drawable.ic_empty_cart)
//                        .into(iv_section_pic)
                }

            }
        }


    }

    /* override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
         inflater?.inflate(R.menu.section_toolbar, menu)
         super.onCreateOptionsMenu(menu, inflater)
     }*/
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    internal fun SectionSave(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,","")

        var sectionRequest = AddSectionRequest(
                sharedPref.getCategoryId(),
                tv_section_name.text.toString().trim(),
                encodedImage
        )

        service.addSection(sharedPref.getToken(), sectionRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Section Response", httpResponse.toString())

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
                            Log.v("Section Response", error.toString())
                        })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }

}