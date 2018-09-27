package com.eightsquare.videochatdemo

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife


/**
 * Created by maxx on 2/27/18.
 */

class WebViewFragment : Fragment() {

    @BindView(R.id.webView)
    lateinit var webView: WebView
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar
    var mUrl: String? = ""
    var mTitle: String? = ""

    companion object {
        fun newInstance(title: String, url: String): WebViewFragment {
            val webViewFragment = WebViewFragment()
            webViewFragment.mTitle = title
            webViewFragment.mUrl = url
            return webViewFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
        setupWebViewClient()
    }

    private fun setupWebViewClient() {
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                    checkForERemit(view, url)
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                checkForERemit(view, request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

        }
    }

    private fun checkForERemit(view: WebView?, url: String?) {
        view?.loadUrl(url.toString())
    }

    private fun setupWebView() {
        initWebView(webView)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                }
            }

            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                return false
            }
        }
        if (!TextUtils.isEmpty(mUrl))
            webView.loadUrl(mUrl)
    }


    private fun initWebView(webView: WebView) {
        val settings = webView.settings
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            allowContentAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            loadsImagesAutomatically = true
            setSupportMultipleWindows(true)
            setSupportZoom(true)

            //remove zoom buttons
            builtInZoomControls = true
            displayZoomControls = false
        }


        // Allow third party cookies for Android Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        } else
            CookieManager.getInstance().setAcceptCookie(true)

        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }
}