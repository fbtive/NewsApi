package com.example.technews.newsactivity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.technews.R
import com.example.technews.databinding.ActivityArticleBinding

class ArticleActivity : BaseActivity() {

    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article)

        var url = ""
        if(intent.hasExtra(Intent.EXTRA_TEXT) != null) {
            url = intent.getStringExtra(Intent.EXTRA_TEXT)!!
            binding.mainToolbar.title = url
        }

        binding.webviewLayout.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.loadingIndicator.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.loadingIndicator.visibility = View.GONE
            }
        }

        binding.webviewLayout.loadUrl(url)
        binding.webviewLayout.settings.javaScriptEnabled = true
        binding.webviewLayout.settings.setSupportZoom(true)

        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mainToolbar.setOnMenuItemClickListener{ menu ->
            when(menu.itemId) {
                R.id.menu_copy -> {
                    val clipboardManager = ContextCompat.getSystemService(applicationContext, ClipboardManager::class.java)
                    val clipData = ClipData.newPlainText("url", url)
                    clipboardManager?.setPrimaryClip(clipData)
                    Toast.makeText(applicationContext, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.webviewLayout.canGoBack()){
                    binding.webviewLayout.goBack()
                }else {
                    finish()
                }
            }
        })
    }

}