package com.livefree.merchant.base

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.location.LocationSettingsStatusCodes

import com.livefree.merchant.util.AlertUtil
import com.livefree.merchant.util.Constants
import com.livefree.merchant.util.Constants.LOCATION_SETTINGS_REQUEST
import com.livefree.merchant.util.RxUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import timber.log.Timber


abstract class BaseActivity : AppCompatActivity(), BaseView {

    private var progressDialog: ProgressDialog? = null
    //    var locationUpdatesObservable  : Loca
    lateinit var locationUpdatesObservable: Observable<Location>
    var locationProvider = ReactiveLocationProvider(this)
    private var disposable: Disposable? = null


    //    private NetworkErrorFragment networkErrorFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //        if (networkErrorFragment == null) {
        //            networkErrorFragment = NetworkErrorFragment.newInstance();
        //        }
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onMessageEvent(NetworkEventBusMessage event) {
    //        switch (event.getResultCode()) {
    //            case EVENT_CONNECTIVITY_LOST:
    //                showNetworkErrorFragmentIfNotShowing();
    //                break;
    //            case EVENT_CONNECTIVITY_CONNECTED:
    //                dismissNetworkErrorFragmentIfShowing();
    //                break;
    //        }
    //    }

    //    protected void showNetworkErrorFragmentIfNotShowing() {
    //        if (!isNetworkErrorFragmentShowing()) {
    //            getSupportFragmentManager().beginTransaction()
    //                    .replace(android.R.id.content, networkErrorFragment, NETWORK_ERROR_FRAGMENT)
    //                    .commit();
    //        }
    //    }
    //
    //    protected void dismissNetworkErrorFragmentIfShowing() {
    //        if (isNetworkErrorFragmentShowing()) {
    //            getSupportFragmentManager().beginTransaction().remove(networkErrorFragment).commit();
    //        }
    //    }
    //
    //    private boolean isNetworkErrorFragmentShowing() {
    //        return networkErrorFragment != null && networkErrorFragment.isVisible();
    //    }

    override fun onStart() {
        super.onStart()
        //        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
        RxUtils.dispose(disposable)
        //        EventBus.getDefault().unregister(this);
    }

    override fun context(): Context {
        return this
    }

    override fun showEmptyView() {}

    override fun showProgressBar() {}

    override fun hideProgressBar() {}

    override fun showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog!!.show()
            }
        } else {
            progressDialog = ProgressDialog(this)
            //            progressDialog.setMessage(getString(R.string.processing_msg));
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    override fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    override fun showMessage(msg: String) {}

    protected fun fragmentTransition(fragment: Fragment, tag: String) {
        //        if (!fragment.isVisible()) {
        //            getSupportFragmentManager().beginTransaction()
        //                    .replace(R.id.layout_main, fragment, tag)
        //                    .addToBackStack(null)
        //                    .commit();
        //        }
    }

    @SuppressLint("MissingPermission")
    override fun setLocationObserverable() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(5).setInterval(100)

        val locationSettings = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true).build()

        locationUpdatesObservable = locationProvider
            .checkLocationSettings(locationSettings)
            .doOnNext { locationSettingsResult ->
                val status = locationSettingsResult.getStatus()
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        status.startResolutionForResult(this, Constants.LOCATION_SETTINGS_REQUEST)
                    } catch (e: IntentSender.SendIntentException) {
                        Timber.e(e)
                        showGPSWarningAlert()
                    }
                }
            }
            .flatMap({ locationSettingsResult -> locationProvider.getUpdatedLocation(locationRequest) })
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
    }

    @SuppressLint("MissingPermission")
    override fun requestLocation() {
        showProgressDialog()
        if (disposable != null) {
            RxUtils.dispose(disposable)
        }
        disposable = locationUpdatesObservable.subscribe { newLocation ->
            hideProgressDialog()
            getCurrentLocation(newLocation)
            Log.d("Found location: %s", newLocation.latitude.toString())
            RxUtils.dispose(disposable)
        };
    }


    override fun showGPSWarningAlert() {
        AlertUtil.showActionNotCancelableAlertDialog(this,
            "Location error", "Sorry device location couldn't be accessed, you can't proceed further!",
            "Retry", DialogInterface.OnClickListener { dialog, which ->
            requestPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
            })
    }

    override fun requestPermissions(permission: String) {
        when (permission) {
            android.Manifest.permission.ACCESS_FINE_LOCATION -> {
                RxPermissions(this)
                    .request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe { granted ->
                        if (granted!!) {
                            if (locationUpdatesObservable != null)
                                requestLocation()
                        } else {
                            showGPSWarningAlert()
                        }
                    }
            }
        }
    }

    override fun getCurrentLocation(location: Location) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOCATION_SETTINGS_REQUEST ->
                when (resultCode) {
                    RESULT_OK -> {
                        Timber.d("User enabled location")
                        val states = LocationSettingsStates.fromIntent(data)
                        if (states.isGpsPresent && states.isGpsUsable) {
                            requestLocation()
                        } else {
                            showGPSWarningAlert()
                        }
                    }
                    RESULT_CANCELED -> {
                        Timber.d("User cancelled enabling location.")
                        showGPSWarningAlert()
                    }
                }
        }
    }
}