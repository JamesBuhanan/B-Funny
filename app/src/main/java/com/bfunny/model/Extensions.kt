package com.bfunny.model

import com.bfunny.redditposts.Children
import com.bfunny.redditposts.ChildrenData
import com.bfunny.redditposts.RedditPostsResponse
import com.bfunny.redditposts.RedditPostsResponseData

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
        permalink = permalink,
    )
}
