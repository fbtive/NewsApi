package com.example.technews.newsheadlines

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.technews.R
import com.example.technews.data.domain.Article
import com.example.technews.databinding.ListItemArticlesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeadlinesAdapter(
    val clickListener: (url: String) -> Unit,
    val saveLocalListener: (article: Article) -> Unit,
    val callBrowserListener: (url: String) -> Unit
): ListAdapter<HeadlineItem, RecyclerView.ViewHolder>(HeadlinesDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun AddHeaderAndSubmitList(list: List<Article>?){
        adapterScope.launch {
            val items = when(list) {
                null -> null
                else -> listOf(HeadlineItem.HeadlineHeader) + list.map { HeadlineItem.HeadlineArticle(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is HeadlineItem.HeadlineHeader -> HeadlineItem.ARTICLE_VIEW_TYPE_HEADER
            is HeadlineItem.HeadlineArticle -> HeadlineItem.ARTICLE_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HeadlineItem.ARTICLE_VIEW_TYPE_HEADER-> BannerViewHolder.from(parent)
            HeadlineItem.ARTICLE_VIEW_TYPE_ITEM -> ArticlesViewHolder.from(parent)
            else -> throw ClassCastException("Unknow Headline Type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ArticlesViewHolder -> {

                val item = getItem(position) as HeadlineItem.HeadlineArticle
                holder.bind(item.article, clickListener, saveLocalListener, callBrowserListener)
            }
        }
    }

    class ArticlesViewHolder private constructor(
        val binding: ListItemArticlesBinding
    ): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ArticlesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemArticlesBinding.inflate(layoutInflater, parent, false)

                return ArticlesViewHolder(binding)
            }
        }

        fun bind(
            item: Article,
            clickListener: (url: String) -> Unit,
            saveLocalListener: (article: Article) -> Unit,
            callBrowserListener: (url: String) -> Unit
        ) {
            binding.article = item
            binding.articleThumbnail.setOnClickListener { clickListener(item.url) }
            binding.articleTitle.setOnClickListener { clickListener(item.url) }
            binding.btnBookmark.setOnClickListener {
                saveLocalListener(item)
            }
            binding.btnBrowser.setOnClickListener { callBrowserListener(item.url) }

            val color = if (item.saved) R.color.primaryColor else R.color.primaryTextColor
            binding.btnBookmark.apply {
                setColorFilter(ContextCompat.getColor(context, color))
            }

            binding.executePendingBindings()
        }

    }

    class BannerViewHolder private constructor(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup) : BannerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return BannerViewHolder(layoutInflater.inflate(R.layout.list_item_article_banner, parent, false))
            }
        }
    }

    class HeadlinesDiffCallback: DiffUtil.ItemCallback<HeadlineItem>() {
        override fun areItemsTheSame(oldItem: HeadlineItem, newItem: HeadlineItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HeadlineItem, newItem: HeadlineItem): Boolean {
            return (oldItem.url == newItem.url)
        }
    }

}

sealed class HeadlineItem {
    abstract val url: String

    companion object {
        const val ARTICLE_VIEW_TYPE_HEADER = 0
        const val ARTICLE_VIEW_TYPE_ITEM = 1
    }

    data class HeadlineArticle(val article: Article): HeadlineItem() {
        override val url: String = article.url
    }

    object HeadlineHeader: HeadlineItem() {
        override val url: String = ""
    }
}