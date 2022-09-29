package com.example.b_funny.redditlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.b_funny.databinding.RedditListItemBinding
import com.example.b_funny.model.RedditPost

class OverviewAdapter(
    val onClickListener: (RedditPost) -> Unit,
    val onClickListener2: (RedditPost) -> Unit
) :
    ListAdapter<RedditPost, RedditListItemViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditListItemViewHolder {
        val binding = RedditListItemBinding.inflate(LayoutInflater.from(parent.context))
        return RedditListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RedditListItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.thumbnail.setOnClickListener {
            onClickListener(item)
        }
        holder.binding.title.setOnClickListener {
            onClickListener2(item)
        }

        holder.bind(item)
    }
}

class RedditListItemViewHolder(val binding: RedditListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(redditPost: RedditPost) {
        binding.redditPost = redditPost
        // This is important, because it forces the data binding to execute immediately,
        // which allows the RecyclerView to make the correct view size measurements
        binding.executePendingBindings()
    }
}

object DiffCallback : DiffUtil.ItemCallback<RedditPost>() {
    override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem.title == newItem.title
    }
}