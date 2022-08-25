package com.example.b_funny.model

import com.example.b_funny.api.redditposts.Children
import com.example.b_funny.api.redditposts.ChildrenData
import com.example.b_funny.api.redditposts.RedditPostsResponse
import com.example.b_funny.api.redditposts.RedditPostsResponseData

fun RedditPostsResponse.toRedditPosts(): List<RedditPost> {
    return data.toRedditPosts()
}

private fun RedditPostsResponseData.toRedditPosts(): List<RedditPost> {
    return children.toRedditPosts()
}

private fun List<Children>.toRedditPosts(): List<RedditPost> {
    val redditPosts = mutableListOf<RedditPost>()

    forEach { children ->
        val redditPost = children.toRedditPost()
        redditPosts.add(redditPost)
    }

    return redditPosts
}

private fun Children.toRedditPost(): RedditPost {
    return data.toRedditPost()
}

private fun ChildrenData.toRedditPost(): RedditPost {
    return RedditPost(
        author = author,
        title = title,
        url = url,
        thumbnail = thumbnail,
        score = score,
    )
}
