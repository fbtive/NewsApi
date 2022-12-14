package com.example.technews.newsheadlines

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.technews.R
import com.example.technews.data.ArticleFilter
import com.example.technews.data.domain.Article
import com.example.technews.databinding.FragmentHeadlinesBinding
import com.example.technews.newsactivity.ArticleActivity
import com.example.technews.newsactivity.MainViewModel
import com.example.technews.utils.EventObserver
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HeadlineFragment : Fragment() {

    private lateinit var binding: FragmentHeadlinesBinding
    private val  viewModel : HeadlinesViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var articlesAdapter: HeadlinesAdapter
    private lateinit var categoryChips: List<Chip>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setupFilterChip()
        setupArticlesRecyclerView()
        setupObserver()

        return binding.root
    }

    private fun setupFilterChip() {
        val inflater = LayoutInflater.from(binding.categoryFilter.context)

        categoryChips = ArticleFilter.values().asList().map {
            val chip = inflater.inflate(R.layout.chip_category_filter, binding.categoryFilter, false) as Chip
            chip.text = when(it) {
                ArticleFilter.GENERAL -> getString(R.string.filter_general)
                ArticleFilter.TECHNOLOGY -> getString(R.string.filter_technology)
                ArticleFilter.BUSINESS -> getString(R.string.filter_business)
                ArticleFilter.HEALTH -> getString(R.string.filter_health)
                ArticleFilter.ENTERTAINMENT -> getString(R.string.filter_entertainment)
                ArticleFilter.SCIENCE -> getString(R.string.filter_science)
                ArticleFilter.SPORTS -> getString(R.string.filter_sports)
            }
            chip.tag = it.filter

            chip.setOnClickListener {
                viewModel.changeFilter(it.tag.toString())
            }

            binding.categoryFilter.addView(chip)

            chip
        }

    }

    private fun setupArticlesRecyclerView() {
        val clickListener = { url: String ->
            navigateToWebView(url)
        }

        val saveLocalListener = { article: Article ->
            viewModel.saveArticleToDB(article)
        }

        val callBrowserListener = { url: String ->
            callBrowser(url)
        }

        articlesAdapter = HeadlinesAdapter(clickListener, saveLocalListener, callBrowserListener)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.headlineRecyclerView)
        binding.headlineRecyclerView.adapter = articlesAdapter
    }

    private fun setupObserver() {
        mainViewModel.eventLocaleChanged.observe(viewLifecycleOwner, EventObserver {
            viewModel.refresh()
        })

        viewModel.headlineList.observe(viewLifecycleOwner) {
            articlesAdapter.AddHeaderAndSubmitList(it)
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { binding.swipeView.isRefreshing = it }

        viewModel.shimmer.observe(viewLifecycleOwner) {
           binding.shimmerLayout.root.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.categoryFilter.observe(viewLifecycleOwner) { filter ->
            categoryChips.forEach {
                it.isChecked = (it.tag.toString() == filter)
            }
        }
    }

    private fun navigateToWebView(url: String) {
        val intent = Intent(requireContext(), ArticleActivity::class.java)
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(intent)
    }

    private fun callBrowser(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, getString(R.string.error_no_browser), Toast.LENGTH_SHORT)
        }
    }
}