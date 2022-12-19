package com.example.technews.newssources

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.technews.R
import com.example.technews.databinding.FragmentSourcesBinding
import com.example.technews.newsactivity.MainViewModel
import com.example.technews.utils.EventObserver
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SourcesFragment : Fragment() {

    private lateinit var binding: FragmentSourcesBinding
    private val viewModel: SourcesViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: SourcesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSourcesBinding.inflate(inflater, container, false)


        setupSourceRecycler()
        setupObserver()
        return binding.root
    }

    private fun setupSourceRecycler() {
        adapter = SourcesAdapter { callBrowser(it) }
        binding.sourcesRecycler.adapter = adapter

        binding.swipeView.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupObserver() {
        viewModel.shimmer.observe(viewLifecycleOwner) {
            binding.shimmerLayout.visibility = if(it) View.VISIBLE else View.GONE
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeView.isRefreshing = it
        }

        viewModel.sources.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyView.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
        }

        mainViewModel.eventLocaleChanged.observe(viewLifecycleOwner, EventObserver {
            viewModel.refresh()
        })

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