package com.livefree.merchant.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.livefree.merchant.ui.about.AboutFragment
import com.livefree.merchant.ui.booking.BookingFragment
import com.livefree.merchant.ui.businessday.BusinessDaysFragment
import com.livefree.merchant.ui.login.LoginActivity
import com.livefree.merchant.ui.logout.LogOutDialog
import com.livefree.merchant.ui.menu.AddMenuDetailFragment
import com.livefree.merchant.ui.profile.ProfileFragment
import com.livefree.merchant.ui.section.*
import com.livefree.merchant.ui.server.AddServerDetailFragment
import com.livefree.merchant.ui.server.ServerListFragment
import com.livefree.merchant.ui.settings.ChangePasswordFragment
import com.livefree.merchant.ui.settings.SettingFragment
import com.livefree.merchant.ui.settings.UserProfileFragment
import com.livefree.merchant.ui.table.AddTableDetailFragment
import com.livefree.merchant.ui.table.SectionTableDetailListFragment
import com.livefree.merchant.util.Constants
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.livefree.merchant.R
import com.livefree.merchant.ui.stripe.StripeFragment


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SettingFragment.Callback,
    HomeFragment.Callback, AboutFragment.Callback, ProfileFragment.Callback, SectionListFragment.Callback,
    ServerListFragment.Callback, MenuListFragment.Callback, FragmentManager.OnBackStackChangedListener,
    AddMenuDetailFragment.Callback, SectionTableDetailListFragment.Callback, LogOutDialog.Callback {


    override fun onBackStackChanged() {


        drawerToggle?.setDrawerIndicatorEnabled(false);
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is HomeFragment
            || currentFragment is BookingFragment
            || currentFragment is ProfileFragment
            || currentFragment is SettingFragment
        ) {
            drawerToggle?.setHomeAsUpIndicator(R.drawable.ic_menu)
            app_toolbar.setNavigationOnClickListener {
                if (drawer_layout.isDrawerOpen(GravityCompat.START))
                    drawer_layout.closeDrawers()
                else
                    drawer_layout.openDrawer(GravityCompat.START)
            }
        } else {
            drawerToggle?.setHomeAsUpIndicator(R.drawable.ic_back)
            app_toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        drawer_layout.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
        drawerToggle?.syncState()
    }


    internal lateinit var onItemClickListener: BottomNavigationView.OnNavigationItemSelectedListener
    private var drawerToggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(app_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false);

        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager.addOnBackStackChangedListener(this)

        app_toolbar.setNavigationOnClickListener(View.OnClickListener {
            Toast.makeText(
                applicationContext,
                "Back clicked!",
                Toast.LENGTH_SHORT
            ).show()
        })

        nav_view.setNavigationItemSelectedListener(this)

        showHomeFragment()

        onItemClickListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bot_home -> {
                    showHomeFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bot_booking -> {
                    showBookingFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bot_profile -> {
                    showProfileFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.bot_setting -> {
                    showSettingFragment()
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        }

        drawerToggle = object : ActionBarDrawerToggle(
            this, drawer_layout, app_toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }

        drawer_layout.addDrawerListener(drawerToggle as ActionBarDrawerToggle)

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setItemIconTintList(null)
        bottom_navigation_view.setOnNavigationItemSelectedListener(onItemClickListener)
        drawerToggle?.syncState()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
            if (currentFragment is HomeFragment
                || currentFragment is BookingFragment
                || currentFragment is ProfileFragment
                || currentFragment is SettingFragment
            ) {
                finish()
            } else {
                super.onBackPressed()

            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                showHomeFragment()
            }
            R.id.nav_booking -> {
                showBookingFragment()
            }
            R.id.nav_profile -> {
                showProfileFragment()
            }
            R.id.nav_setting -> {
                showSettingFragment()
            }
            R.id.nav_logout -> {
                LogOutDialog().show(supportFragmentManager, Constants.LOGOUT_DIALOG_FRAGMENT)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun showHomeFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, HomeFragment(), Constants.HOME_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    fun showProfileFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, ProfileFragment(), Constants.PROFILE_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showChangePasswordFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, ChangePasswordFragment(), Constants.CHANGE_PASSWORD_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showBookingFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, BookingFragment(), Constants.BOOKING_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showServerFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, ServerListFragment(), Constants.SERVER_LIST_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showSectionFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, SectionListFragment(), Constants.SECTION_LIST_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showMenuFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, MenuListFragment(), Constants.MENU_LIST_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showSettingFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, SettingFragment(), Constants.SETTING_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showAboutFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, AboutFragment(), Constants.ABOUT_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showBusinessDaysFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, BusinessDaysFragment(), Constants.BUSINESS_DAYS_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showSectionTableDetailFragment(sectionID: String, toolbarName: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = SectionTableDetailListFragment.newInstance(sectionID, toolbarName);
        fragmentTransaction.replace(
            R.id.container, fragment, Constants.SECTION_TABLE_FRAGMENT
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showAddSectionFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, AddSectionFragment(), Constants.ADD_SECTION_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showAddServerDetailFragment(_id: String, serverID: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.container,
            AddServerDetailFragment.newInstance(serverID),
            Constants.ADD_SERVER_DETAIL_FRAGMENT
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showAddMenuFragment(menuID: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.container,
            AddMenuDetailFragment.newInstance(menuID),
            Constants.ADD_MENU_DETAIL_FRAGMENT
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showUserProfileFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, UserProfileFragment(), Constants.USER_PROFILE_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun showCreateTableFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, AddMenuDetailFragment(), Constants.ADD_MENU_TABLE_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun ShowAddTableDetailFragment(sectionID: String, tableID: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, AddTableDetailFragment.newInstance(sectionID, tableID), Constants.ADD_TABLE_DETAIL_FRAGMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    fun showStripePaymentFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, StripeFragment(), Constants.STRIPE_PAYMENT)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun showPasswordScreen() {
        showChangePasswordFragment()
    }

    override fun showUserProfileScreen() {
        showUserProfileFragment()
    }

    override fun showAbout(about: String) {
        showAboutFragment()
    }

    override fun showSectionList(sectionID: String, toolbarName: String) {
        showSectionTableDetailFragment(sectionID, toolbarName)
    }

    override fun openAddSectionScreen() {
        showAddSectionFragment()
    }

    override fun openAddServerScreen(_id: String, serverID: String) {
        showAddServerDetailFragment(_id, serverID)
    }


    override fun showCreateTable() {
        showCreateTableFragment()
    }

    override fun openAddTableDetail(_id: String, tableID: String) {
        ShowAddTableDetailFragment(_id, tableID)
    }

    override fun openAddMenuScreen(menuID: String) {
        showAddMenuFragment(menuID)
    }

    override fun showLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    fun OAuthLink() {

        var webView= WebView(this)
        webView?.webViewClient = MyWebViewClient(this)
        setContentView(webView)
        webView?.loadUrl("https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_GOYMdDB1XtfwDGVvkfv7CbuQwXWI8t60&scope=read_write")


      /*  var url: String =
            "https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_GOYMdDB1XtfwDGVvkfv7CbuQwXWI8t60&scope=read_write"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
*/
        //https@ //connect.stripe.com/oauth/authorize?
        // response_type=code&client_id=ca_GOYMdDB1XtfwDGVvkfv7CbuQwXWI8t60&scope=read_write
    }

    override fun onGridViewItemClick(position: Int) {
        when (position) {
            0 -> {
                showBookingFragment()
            }
            1 -> {
                showServerFragment()
            }
            2 -> {
                showSectionFragment()

            }
            3 -> {
                showMenuFragment()
            }

        }
    }

    override fun openProfileScreens(position: Int) {
        when (position) {
            0 -> {
                showAboutFragment();
            }
            1 -> {
                showBusinessDaysFragment()
            }
            2 -> {
                showServerFragment()
            }
            3 -> {
                showSectionFragment()
            }
            4 -> {
                showMenuFragment()
            }
            5 -> {
                showStripePaymentFragment()
            }

        }
    }


    companion object {
        fun newInstance(param1: String): SectionTableDetailListFragment {
            val fragment = SectionTableDetailListFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", param1)
            fragment.arguments = args
            return fragment
        }
    }

    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }

}



