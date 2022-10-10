package com.example.b_funny

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.b_funny.detailwithcomments.CommentsAdapter
import com.example.b_funny.model.Comment
import com.example.b_funny.model.RedditPost
import com.example.b_funny.redditlist.OverviewAdapter


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<RedditPost>?) {
    val adapter = recyclerView.adapter as OverviewAdapter
    adapter.submitList(data)
}

@BindingAdapter("listData2")
fun bindRecyclerView2(recyclerView: RecyclerView, data: List<Comment>?) {
    val adapter = recyclerView.adapter as CommentsAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}
