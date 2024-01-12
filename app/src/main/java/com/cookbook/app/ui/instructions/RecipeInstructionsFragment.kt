package com.cookbook.app.ui.instructions

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cookbook.app.R
import com.cookbook.app.models.Result

class RecipeInstructionsFragment : Fragment() {
    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_recipe_instructions, container, false)
        // Retrieve the Result object from the Intent
        val myBundle: Result? = activity?.intent?.parcelable("result")

        view.findViewById<WebView>(R.id.webview_instructions).webViewClient =
            object : WebViewClient() {}
        view.findViewById<WebView>(R.id.webview_instructions).settings.javaScriptEnabled = true
        val url : String = myBundle!!.sourceUrl
        view.findViewById<WebView>(R.id.webview_instructions).loadUrl(url)
        return view

    }
}