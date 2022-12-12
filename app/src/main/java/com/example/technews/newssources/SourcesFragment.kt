package com.example.technews.newssources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.technews.R
import com.example.technews.databinding.FragmentSourcesBinding
import com.example.technews.newsheadlines.HeadlinesViewModel
import com.google.android.material.transition.MaterialFadeThrough


class SourcesFragment : Fragment() {

    private lateinit var binding: FragmentSourcesBinding

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

        return binding.root
    }

}