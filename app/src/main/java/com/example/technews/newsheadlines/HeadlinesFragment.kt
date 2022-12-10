package com.example.technews.newsheadlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.technews.MainActivity
import com.example.technews.R
import com.example.technews.databinding.FragmentHeadlinesBinding


class HeadlineFragment : Fragment() {
    private lateinit var binding: FragmentHeadlinesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeadlinesBinding.inflate(layoutInflater, container, false)

        binding.articleBtn.setOnClickListener {
            findNavController().navigate(HeadlineFragmentDirections.actionHeadlineFragmentDstToArticleFragment())
        }

        val activity = (requireActivity() as MainActivity)
        activity.recalculateToolbarAndRootView(binding.root, binding.appBarLayout)
        activity.setupAppBarScrollBehavior(binding.appBarLayout, binding.mainToolbar, binding.searchText, binding.nestedScrollView)

        return binding.root
    }
}