package com.example.b_funny.model


import java.io.Serializable

data class RedditPost(
    val title: String? = null,
    val url: String? = null,
    val domain: String? = null,
    val author: String? = null,
    val subreddit: String? = null,
    val selftext_html: String? = null,
    val permalink: String? = null,
    val thumbnail: String? = null,
    val score: Int = 0,
    val comments: Int = 0,
    val time: Long = 0,
) : Serializable