package com.example.androidfinal.ui.about

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.androidfinal.R

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        val webView = root.findViewById<WebView>(R.id.fragment_about_web_view)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = Callback()
        webView.loadUrl("https://www.themoviedb.org/about/our-history")
        return root
    }

    private inner class Callback : WebViewClient() {
        override fun shouldOverrideKeyEvent(view: WebView, event: KeyEvent): Boolean {
            return false
        }
    }

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}