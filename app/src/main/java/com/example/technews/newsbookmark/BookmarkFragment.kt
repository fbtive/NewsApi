package com.example.technews.newsbookmark

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.technews.R
import com.example.technews.data.domain.Article
import com.example.technews.databinding.FragmentBookmarkBinding
import com.example.technews.newsactivity.ArticleActivity
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: BookmarkViewModel by viewModels()

    private lateinit var adapter: BookmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupObserver()
        return binding.root
    }

    private fun setupRecyclerView() {
        val clickListener = { url: String ->
            navigateToWebView(url)
        }

        val deleteListener = { article: Article ->
            viewModel.deleteArticle(article)
        }

        val browserListener = { url: String ->
            callBrowser(url)
        }

        adapter = BookmarkAdapter(clickListener, deleteListener, browserListener)
        binding.localRecyclerview.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.articles.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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