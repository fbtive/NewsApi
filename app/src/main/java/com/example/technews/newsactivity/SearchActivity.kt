package com.example.technews.newsactivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.example.technews.R
import com.example.technews.databinding.ActivitySearchBinding
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.material.transition.platform.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "shared_search_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 600L
        }
        window.sharedElementReturnTransition = MaterialElevationScale(false).apply {
            addTarget(android.R.id.content)
            duration = 400L
        }

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)

        setupSearchView()
        setupNavigationBack()
        setupSearchRecycler()
        setupObserver()
    }

    private fun setupSearchView() {
        binding.searchField.requestFocusFromTouch()

        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { if(query.isNotEmpty()) viewModel.searchArticle(it) }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let { if(query.isNotEmpty()) viewModel.searchArticle(it) }
                return true
            }
        })
    }

    private fun setupSearchRecycler() {
        adapter = SearchAdapter { navigateToWebview(it) }
        binding.articlesRecycler.adapter = adapter

        binding.swipeView.setOnRefreshListener {
            binding.searchField.query.let {
                viewModel.searchArticle(it.toString())
            }

        }
    }

    private fun setupObserver() {
        viewModel.articles.observe(this) {
            adapter.submitList(it)
        }

        viewModel.isRefreshing.observe(this) {
            binding.swipeView.isRefreshing = it
        }
    }

    private fun navigateToWebview(url: String) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(Intent.EXTRA_TEXT, url)

        startActivity(intent)
    }

    private fun callBrowser(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error_no_browser), Toast.LENGTH_SHORT)
        }
    }

    private fun setupNavigationBack() {
        binding.mainToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}