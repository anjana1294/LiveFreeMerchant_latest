package com.livefree.merchant.base;

import android.content.Context;
import android.location.Location;

public interface BaseView {

    Context context();

    void showEmptyView();

    void showProgressBar();

    void hideProgressBar();

    void showProgressDialog();

    void hideProgressDialog();

    void showMessage(String msg);

    void setLocationObserverable();

    void requestLocation();

    void showGPSWarningAlert();

    void requestPermissions(String permission);

    void getCurrentLocation(Location location);

}