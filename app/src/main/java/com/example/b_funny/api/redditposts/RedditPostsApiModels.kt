package com.example.b_funny.api.redditposts


data class RedditPostsResponse(val data: RedditPostsResponseData)

data class RedditPostsResponseData(
    val children: List<Children>,
    val after: String?
)

data class Children(val data: ChildrenData)

data class ChildrenData(
    val author: String,
    val title: String,
    val num_comments: Int,
    val created: Long,
    val thumbnail: String,
    val url: String,
    val subreddit: String,
    val selftext_html: String?,
    val score: Int,
    val domain: String,
    val preview: Preview?,
    val permalink: String
)

data class Preview(val images: List<Image>)

data class Image(val resolutions: List<Resolutions>)

data class Resolutions(val url: String)