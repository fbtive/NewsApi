package com.example.technews.newssources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.technews.data.domain.Sources
import com.example.technews.databinding.ListItemSourcesBinding

class SourcesAdapter(
    val clickListener: (url: String) -> Unit
) : ListAdapter<Sources, SourcesAdapter.SourceViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }


    class SourceViewHolder(val binding: ListItemSourcesBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): SourceViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemSourcesBinding.inflate(inflater, parent, false)

                return SourceViewHolder(binding)
            }
        }

        fun bind(sources: Sources, clickListener: (url: String) -> Unit) {
            binding.sources = sources
            binding.sourceCard.setOnClickListener { clickListener(sources.url) }
            binding.executePendingBindings()
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Sources>() {
        override fun areItemsTheSame(oldItem: Sources, newItem: Sources): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Sources, newItem: Sources): Boolean {
            return oldItem == newItem
        }
    }
}