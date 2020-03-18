package com.crazy.instalogin_graphapi.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.crazy.instalogin_graphapi.R
import kotlinx.android.synthetic.main.dialog_webview.*


class InstagramLoginDialog(
    context: Context,
    private val mUrl: String,
    private val mListener: OAuthDialogListener
) : Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_webview)

        try {
            if (window != null) window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            window!!.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setCancelable(false)

        setUpWebView()

        CookieSyncManager.createInstance(context)
        val cookieManager = CookieManager.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        } else {
            cookieManager.removeAllCookie()
        }

        imgClose.setOnClickListener {
            onClose()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {

        webview.isVerticalScrollBarEnabled = false
        webview.isHorizontalScrollBarEnabled = false
        webview.webViewClient = OAuthWebViewClient()
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(mUrl)

    }

    private fun onClose() {
        dismiss()
    }

    companion object {
        private const val TAG = "Instagram-WebView"
        fun newInstance(
            context: Context,
            url: String,
            listener: OAuthDialogListener
        ): InstagramLoginDialog {

            return InstagramLoginDialog(
                context,
                url,
                listener
            )
        }
    }


    private inner class OAuthWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            progressBar.visibility = View.GONE
            super.onPageFinished(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progressBar.visibility = View.VISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            Log.d(TAG, "Redirecting URL $url")
            if (url.startsWith(context.getString(R.string.instagram_redirect_uri))) {
                val urls = url.split("=").toTypedArray()
                Log.e("TAG", "Token: " + urls[1])
                mListener.onComplete(urls[1])
                dismiss()
                return true
            }
            return false
        }

        override fun onReceivedError(
            view: WebView, errorCode: Int,
            description: String, failingUrl: String
        ) {
            Log.d(TAG, "Page error: $description")
            super.onReceivedError(view, errorCode, description, failingUrl)
            mListener.onError(description)
            dismiss()
        }
    }

    interface OAuthDialogListener {
        fun onComplete(accessToken: String?)
        fun onError(error: String?)
    }

}