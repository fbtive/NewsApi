package com.example.technews.newsactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.technews.data.domain.Article
import com.example.technews.databinding.ListItemArticleSearchBinding

class SearchAdapter(val clickListener: (url:String) -> Unit): ListAdapter<Article, SearchAdapter.ArticleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ArticleViewHolder(val binding: ListItemArticleSearchBinding): RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent: ViewGroup): ArticleViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemArticleSearchBinding.inflate(inflater, parent, false)

                return ArticleViewHolder(binding)
            }
        }

        fun bind(article: Article, clickListener: (url: String) -> Unit) {
            binding.article = article
            binding.cardView.setOnClickListener{clickListener(article.url)}
            binding.executePendingBindings()
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Article>() {
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
    }
}