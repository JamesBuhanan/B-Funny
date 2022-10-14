package com.bfunny.detailwithcomments

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bfunny.R
import com.bfunny.databinding.CommentRowBinding
import com.bfunny.databinding.HeaderRowBinding
import com.bfunny.model.Comment

private const val TYPE_HEADER = 0
private const val TYPE_COMMENT = 1

class CommentsAdapter(private val url: String?) :
    ListAdapter<Comment, RecyclerView.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_COMMENT) {
            val binding = CommentRowBinding.inflate(LayoutInflater.from(parent.context))
            CommentViewHolder(binding)
        } else {
            val binding = HeaderRowBinding.inflate(LayoutInflater.from(parent.context))
            HeaderViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_COMMENT
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentViewHolder -> {
                val item = getItem(position)
                holder.bind(item)
            }
            is HeaderViewHolder -> {
                holder.bind(url)
            }
        }
    }
}

class HeaderViewHolder(private val binding: HeaderRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(url: String?) {
        binding.url = url
    }
}

class CommentViewHolder(private val binding: CommentRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        binding.comment = comment
        binding.commentIndicatorSpace.setPadding(8 * comment.level, 0, 0, 0)
        binding.commentIndicatorColor.background
        when (comment.level) {
            0 -> binding.commentIndicatorColor.visibility = GONE
            1 -> binding.commentIndicatorColor.setColor(R.color.purple_200)
            2 -> binding.commentIndicatorColor.setColor(R.color.purple_500)
            3 -> binding.commentIndicatorColor.setColor(R.color.purple_700)
            4 -> binding.commentIndicatorColor.setColor(R.color.blue)
            5 -> binding.commentIndicatorColor.setColor(R.color.teal_200)
            6 -> binding.commentIndicatorColor.setColor(R.color.teal_700)
            7 -> binding.commentIndicatorColor.setColor(R.color.green)
            else -> binding.commentIndicatorColor.setColor(R.color.black)

        }
        // This is important, because it forces the data binding to execute immediately,
        // which allows the RecyclerView to make the correct view size measurements
        binding.executePendingBindings()
    }
}

private fun LinearLayout.setColor(color: Int) {
    setBackgroundColor(ContextCompat.getColor(context, color))
}

object DiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.commentBody == newItem.commentBody
    }
}