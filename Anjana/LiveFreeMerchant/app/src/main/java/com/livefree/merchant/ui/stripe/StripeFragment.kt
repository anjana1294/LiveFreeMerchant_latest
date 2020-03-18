package com.livefree.merchant.ui.stripe

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import com.livefree.merchant.R
import com.livefree.merchant.base.BaseFragment


class StripeFragment:BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootview = inflater.inflate(R.layout.fragment_stripe_payment, container, false)
        val webView: WebView = rootview.findViewById(R.id.webView)


        val webSettings = webView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true)
        webView.setWebViewClient(activity?.let { MyWebView(it) })
        webView?.loadUrl("https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_GOYMdDB1XtfwDGVvkfv7CbuQwXWI8t60&scope=read_write")
        return rootview

    }


    class MyWebView internal constructor(private val activity: Activity) : WebViewClient() {
        private var progressBar: ProgressBar? = null

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
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

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)


        }
    }
}



  /*  interface Callback{
        fun showStripePayment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is StripeFragment.Callback )
            callback=context
    }*/
