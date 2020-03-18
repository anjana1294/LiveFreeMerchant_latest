package com.livefree.merchant.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.livefree.merchant.R;
import com.livefree.merchant.ui.about.AboutFragment;
import com.livefree.merchant.ui.booking.BookingFragment;
import com.livefree.merchant.ui.businessday.BusinessDaysFragment;
import com.livefree.merchant.ui.home.HomeFragment;
import com.livefree.merchant.ui.menu.AddMenuDetailFragment;
import com.livefree.merchant.ui.profile.ProfileFragment;
import com.livefree.merchant.ui.section.AddSectionFragment;
import com.livefree.merchant.ui.section.MenuListFragment;
import com.livefree.merchant.ui.section.SectionListFragment;
import com.livefree.merchant.ui.server.AddServerDetailFragment;
import com.livefree.merchant.ui.server.ServerListFragment;
import com.livefree.merchant.ui.settings.SettingFragment;
import com.livefree.merchant.ui.settings.UserProfileFragment;
import com.livefree.merchant.ui.table.AddTableDetailFragment;
import com.livefree.merchant.ui.table.SectionTableDetailListFragment;
import com.livefree.merchant.util.TextviewReg;

public class BaseFragment extends Fragment implements BaseView {
    private ImageView imageView;
    private TextviewReg textviewReg;
    private ProgressDialog progressDialog;

    public Context context() {
        return getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            Toolbar toolbar = getActivity().findViewById(R.id.app_toolbar);
            // View toolbarView = getActivity().findViewById(R.id.top_relative);
            if (this instanceof HomeFragment) {
                toolbar.setTitle("Live Free Merchant");

                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof BookingFragment) {
                toolbar.setTitle("My Booking");
                //toolbarView.setVisibility(View.VISIBLE);
            } else if (this instanceof ProfileFragment) {
                toolbar.setTitle("Profile");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof SettingFragment) {
                toolbar.setTitle("Settings");

            } else if (this instanceof AboutFragment) {
                toolbar.setTitle("About");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof BusinessDaysFragment) {
                toolbar.setTitle("Business Days");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof ServerListFragment) {
                toolbar.setTitle("Server");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof SectionListFragment) {
                toolbar.setTitle("Section");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof AddServerDetailFragment) {
                toolbar.setTitle("Add Server");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof AddSectionFragment) {
                toolbar.setTitle("Add Section");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof SectionTableDetailListFragment) {
                String title = getArguments().getString("ARG_PARAM2");
                toolbar.setTitle(title);
                // toolbarView.setVisibility(View.GONE);
            }
            else if (this instanceof SectionTableDetailListFragment) {
                toolbar.setTitle("Add Table");
                // toolbarView.setVisibility(View.GONE);
            }else if (this instanceof AddMenuDetailFragment) {
                toolbar.setTitle("Menu");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof MenuListFragment) {
                toolbar.setTitle("Menu");
                // toolbarView.setVisibility(View.GONE);
            } else if (this instanceof UserProfileFragment) {
                toolbar.setTitle("My Profile");
                // toolbarView.setVisibility(View.GONE);
            }
            else if (this instanceof UserProfileFragment) {
                toolbar.setTitle("Edit");
                // toolbarView.setVisibility(View.GONE);
            }
            else if(this instanceof AddTableDetailFragment){
                toolbar.setTitle("Table");

            }




        }

    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.processing_msg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
    }

    @Override
    public void setLocationObserverable() {

    }

    @Override
    public void requestLocation() {

    }

    @Override
    public void showGPSWarningAlert() {

    }

    @Override
    public void requestPermissions(String permission) {

    }

    @Override
    public void getCurrentLocation(Location location) {

    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }*/
}
