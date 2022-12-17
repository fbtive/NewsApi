package com.example.technews.newsbookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.technews.data.domain.Article
import com.example.technews.databinding.ListItemArticlesLocalBinding

class BookmarkAdapter(
    val clickListener: (url: String) -> Unit,
    val deleteListener: (article: Article) -> Unit,
    val browserListener: (url: String) -> Unit
): ListAdapter<Article, BookmarkAdapter.BookmarkViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, deleteListener, browserListener)
    }

    class BookmarkViewHolder(val binding: ListItemArticlesLocalBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): BookmarkViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemArticlesLocalBinding.inflate(inflater, parent, false)
                return BookmarkViewHolder(binding)
            }
        }

        fun bind(
            article: Article,
            clickListener: (url: String) -> Unit,
            deleteListener: (article: Article) -> Unit,
            browserListener: (url: String) -> Unit
        ) {
            binding.article = article

            binding.articleThumbnail.setOnClickListener { clickListener(article.url) }
            binding.articleTitle.setOnClickListener { clickListener(article.url) }
            binding.articleDescription.setOnClickListener { clickListener(article.url) }
            binding.btnDelete.setOnClickListener { deleteListener(article) }
            binding.btnBrowser.setOnClickListener { browserListener(article.url) }

            binding.executePendingBindings()
        }
    }

    class BookmarkDiffCallback: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}