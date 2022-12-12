package com.example.technews.newsheadlines

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.technews.NewsActivity.MainActivity
import com.example.technews.R
import com.example.technews.databinding.FragmentHeadlinesBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HeadlineFragment : Fragment() {

    private lateinit var binding: FragmentHeadlinesBinding
    private val  viewModel : HeadlinesViewModel by viewModels()

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var articlesAdapter: HeadlinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        setupArticlesRecyclerView()
        setupObserver()

        return binding.root
    }

    private fun setupArticlesRecyclerView() {
        articlesAdapter = HeadlinesAdapter()
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.headlineRecyclerView)
        binding.headlineRecyclerView.adapter = articlesAdapter
    }

    private fun setupObserver() {
        viewModel.headlineList.observe(viewLifecycleOwner, Observer {
            articlesAdapter.AddHeaderAndSubmitList(it)
        })
    }

}