package com.example.b_funny.detailwithcomments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.b_funny.databinding.CommentRowBinding
import com.example.b_funny.databinding.RedditListItemBinding
import com.example.b_funny.model.Comment
import com.example.b_funny.model.RedditPost

class CommentsAdapter : ListAdapter<Comment, CommentViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentRowBinding.inflate(LayoutInflater.from(parent.context))
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class CommentViewHolder(private val binding: CommentRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        binding.comment = comment
        // This is important, because it forces the data binding to execute immediately,
        // which allows the RecyclerView to make the correct view size measurements
        binding.executePendingBindings()
    }
}

object DiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.commentBody == newItem.commentBody
    }
}