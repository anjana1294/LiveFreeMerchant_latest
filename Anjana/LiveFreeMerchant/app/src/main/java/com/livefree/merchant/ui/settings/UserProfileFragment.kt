package com.livefree.merchant.ui.settings

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.gson.Gson
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.ui.settings.model.EditRequest
import com.livefree.merchant.ui.settings.model.UserData
import com.livefree.merchant.util.FileUtils
import com.livefree.merchant.util.ImagePicker
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_userprofile.*
import javax.security.auth.callback.Callback

class UserProfileFragment : BaseFragment() {
    private var callback: Callback? = null
    var sharedPref: SharedPref = SharedPref()
    var encodedImage: String = """"""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_userprofile, container, false)
        setHasOptionsMenu(true)
        sharedPref = SharedPref().getInstance()
        sharedPref.initSharedPreferences(context())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userData = Gson().fromJson(sharedPref.getUserData(), UserData::class.java)!!

        try {
            Picasso.with(context()).load(userData.logo).placeholder(R.mipmap.app_icon).into(civ_profile)
        } catch (e: Exception) {
            Picasso.with(context()).load(R.mipmap.app_icon).into(civ_profile)

        }
        username.setText(userData.name)
        email.setText(userData.email)
        phone.setText(userData.phone)
        Log.d("user data", userData.name)
        btn_save_changes.setOnClickListener(clickListener)
        ed_camera.setOnClickListener(clickListener)


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.edit_toolbar -> {
                ed_camera.visibility = View.VISIBLE
                username.isEnabled = true
                phone.isEnabled = true
                username.isFocusableInTouchMode=true
                username.requestFocus()
                btn_save_changes.visibility = View.VISIBLE
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    val clickListener = View.OnClickListener { view ->

        var edit_username = username.text.toString()
        var edit_phone = phone.text.toString()


        when (view.id) {
            R.id.btn_save_changes -> {
                if (TextUtils.isEmpty(edit_username)) {
                    username?.error = "Required"
                } else if (TextUtils.isEmpty(edit_phone)) {
                    phone?.error = "Required"
                }/* else if (civ_profile.getDrawable()==null) {
                    Toast.makeText(context,"Please Select An Image",Toast.LENGTH_LONG).show()
                } */ else
                    getEdit(NetworkModule(), sharedPref)
            }

            R.id.ed_camera -> {
                ImagePicker.setImage(childFragmentManager, context()) {
                    showProgressDialog()
                    FileUtils.encodeToBase64(it) {
                        encodedImage = it
                        hideProgressDialog()
                    }
                    civ_profile.setImageBitmap(it)
                }
            }
        }
    }


    internal fun getEdit(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressDialog()
        val service: RestService = networkModule.provideRetrofitClient(context())
        encodedImage = encodedImage.replace("data:image/png;base64,", "")
        var editRequest = EditRequest(username.text.toString().trim(), phone.text.toString().trim(), encodedImage)

        service.editProfile(sharedPref.getToken(), editRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Edit Server Response", httpResponse.toString())

                    if (httpResponse.isSuccessful) {
                        hideProgressDialog()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                            ed_camera.visibility = View.GONE
                            username.isEnabled = false
                            phone.isEnabled = false

                            btn_save_changes.visibility = View.GONE

                        } else
                            Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                    }
                },
                        { error ->
                            hideProgressDialog()
                            showMessage(error.toString())
                            Log.v("Edit Response", error.toString())
                        })
    }

    override fun showProgressDialog() {
        super.showProgressDialog()
    }

    override fun hideProgressDialog() {
        super.hideProgressDialog()
    }
}
