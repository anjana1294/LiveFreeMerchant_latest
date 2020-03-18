package com.livefree.merchant.ui.location

import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.livefree.merchant.base.BaseActivity
import com.livefree.merchant.ui.data.SharedPref
import com.livefree.merchant.ui.home.HomeActivity
import com.livefree.merchant.ui.location.model.AddressData
import com.livefree.merchant.ui.location.model.Category
import com.livefree.merchant.ui.location.model.LocationRequest
import com.livefree.merchant.ui.network.NetworkModule
import com.livefree.merchant.ui.network.RestService
import com.livefree.merchant.util.AlertUtil
import com.livefree.merchant.util.Constants.PLACE_PICKER_REQUEST
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_location.*
import java.io.IOException
import java.util.*
import com.livefree.merchant.R

class LocationActivity : BaseActivity() {

    var sharedPref: SharedPref = SharedPref()
    var categoriesList = ArrayList<Category>()
    var selectedCategoryId = ""
    lateinit var latLong: LatLng

    internal var fields: List<Place.Field> =
            Arrays.asList<Place.Field>(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        sharedPref = SharedPref().getInstance()
        sharedPref.initSharedPreferences(this)

        getCategories(NetworkModule(), sharedPref)
        et_categories.setInputType(InputType.TYPE_NULL)


        et_categories.setOnClickListener {
            openListDialog(this.categoriesList)
        }

        intent.putExtra("isCategory", selectedCategoryId)
        btn_submit.setOnClickListener(clickListener)
        ed_address.setOnClickListener(clickListener)

        btn_current_location.setOnClickListener {
            setLocationObserverable()
            requestPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!Places.isInitialized()) {
            Places.initialize(
                    context(),

                //Test Live Free
                //    "AIzaSyATsAPxtSIoEIMIXANO0H03HnK0Nk4Uo60"

                    "AIzaSyC7OgAlmnRRibq9la1MOXx-_mKEqNDYZVo"

            ) // Create a new Places client instance. PlacesClient placesClient = Places.createClient(this);
        }
    }

    override fun getCurrentLocation(location: Location) {
        Log.d("Home location: %s", location.latitude.toString())
        this.latLong = LatLng(location.latitude, location.longitude)
        this.getAddressFromLocation(location.latitude, location.longitude)
//        this.location = location
    }


    private fun getCategories(networkModule: NetworkModule, sharedPref: SharedPref) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)

        service.categoryList(sharedPref.getToken())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    if (httpResponse.isSuccessful) {
                        Log.v("category Response", httpResponse.body().toString())
                        hideProgressBar()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            this.categoriesList = response.data
                        } else
                            Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                    }
                },
                        { error ->
                            hideProgressBar()
                            showMessage(error.toString())
                            Log.v("Login Response", error.toString())
                        })
    }


    internal fun saveRestaurantDetails(
            networkModule: NetworkModule, sharedPref: SharedPref
    ) {
        showProgressBar()
        val service: RestService = networkModule.provideRetrofitClient(this)
        val city = ed_city.text.toString()
        val address = ed_address.text.toString()
        val addressData = AddressData(city, this.latLong.longitude, this.latLong.latitude, address)

        service.saveCategory(sharedPref.getToken(),
                LocationRequest(selectedCategoryId, city, addressData))
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ httpResponse ->
                    Log.v("Otp Response", httpResponse.toString())
                    if (httpResponse.isSuccessful) {
                        hideProgressBar()
                        var response = httpResponse.body()
                        if (response?.status!!) {
                            sharedPref.putStringValue("category_id", response.data._id!!)
                            startActivity(Intent(this, HomeActivity::class.java))
                        } else
                            Toast.makeText(context(), response.msg, Toast.LENGTH_LONG).show()
                    }
                },
                        { error ->
                            hideProgressBar()
                            showMessage(error.toString())
                            Log.v("Otp Response", error.toString())
                        })
    }


    private fun openListDialog(categroyList: ArrayList<Category>) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Choose a category")
        var categoriesName = ArrayList<String>();
        var selectedCat = ""
        for (category in categroyList) {
            categoriesName.add(category.name)
        }

        val cs = categoriesName.toTypedArray<CharSequence>()
        val checkedItem = 0 // cow
        builder.setSingleChoiceItems(cs, checkedItem, DialogInterface.OnClickListener { dialog, which ->
            selectedCat = categoriesName[which]
            selectedCategoryId = categroyList.get(which)._id
            Log.e("sda", selectedCat)
        })


        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            et_categories.setText(selectedCat)
        })
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    val clickListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_submit -> {
                val city = ed_city.text.toString()
                val address = ed_address.text.toString()

                if (TextUtils.isEmpty(selectedCategoryId))
                    et_categories?.error = "Please select category"
                else if (TextUtils.isEmpty(address))
                    ed_address?.error = "Please enter address"
                else
                    saveRestaurantDetails(NetworkModule(), sharedPref)
            }

            R.id.ed_address -> {
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
                startActivityForResult(intent, PLACE_PICKER_REQUEST)
            }
        }
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val myAddress = Autocomplete.getPlaceFromIntent(data!!)
                var pickAddress = ""
                var pickupName = ""
                if (!TextUtils.isEmpty(myAddress.address))
                    pickAddress = myAddress.address!!.toString()
                if (!TextUtils.isEmpty(myAddress.name))
                    pickupName = myAddress.name!!.toString()
                ed_address.setText("$pickupName $pickAddress")
                this.latLong = myAddress.latLng!!
                ed_city.setText(pickupName)

            }
        }
    }


    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.ENGLISH)
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) {
                val fetchedAddress = addresses.get(0)
                Log.e("Address Data", fetchedAddress.toString())
                val strAddress = StringBuilder()
                var subLocality = ""
                var featureName = ""
                var locality = ""
                var postalCode = ""
                if (!TextUtils.isEmpty(fetchedAddress.getFeatureName()))
                    featureName = fetchedAddress.getFeatureName()
                if (!TextUtils.isEmpty(fetchedAddress.getSubLocality()))
                    subLocality = fetchedAddress.getSubLocality()
                if (!TextUtils.isEmpty(fetchedAddress.getLocality()))
                    locality = fetchedAddress.getLocality()
                if (!TextUtils.isEmpty(fetchedAddress.getPostalCode()))
                    postalCode = fetchedAddress.getPostalCode()
                strAddress.append(featureName).append(" ").append(subLocality).append(" ").append(locality)
                        .append(" ").append(postalCode)
                ed_address.setText(strAddress)
                ed_city.setText(featureName)
            } else {
                AlertUtil.showActionSnackBar(layout_main, "Location not found.", "Try Again?", View.OnClickListener {
                    setLocationObserverable()
                    requestPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}