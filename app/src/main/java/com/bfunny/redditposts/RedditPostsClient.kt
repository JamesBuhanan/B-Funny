package com.bfunny.redditposts

import com.bfunny.model.Comment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RedditPostsClient(private val subreddit: String) {

    private val redditApi: RedditPostsService

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        redditApi = retrofit.create(RedditPostsService::class.java)
    }

    suspend fun getTop(after: String, limit: String): RedditPostsResponse {
        return redditApi.getTop(subreddit, after, limit)
    }

    suspend fun getComments(permalink: String): List<Comment> {
        val newPermalink = permalink.removePrefix("/")
        val redditCommentsResponses: List<Map<String, Any>> =
            redditApi.getComments(newPermalink)
        // remove the "/" at the very beg
        // Convert to List of Comments here
        val comments = mutableListOf<Comment>()
        redditCommentsResponses[1].getComments(0, comments)
        return comments
    }
}

private fun Map<String, Any>.getComments(level: Int, comments: MutableList<Comment>) {
    val data = this["data"] as Map<String, Any>
    val children = data["children"] as List<Map<String, Any>>
    children.forEach {
        val childData = it["data"] as Map<String, Any>
        processRecursively(level, comments, childData)
    }
}

private fun processRecursively(level: Int, comments: MutableList<Comment>, data: Map<String, Any>) {
    val scoreDouble = data["score"] as Double?
    val scoreInt = scoreDouble?.toInt() ?: 0
    comments.add(
        Comment(
            level = level,
            commentBody = data["body"] as String?,
            commentScore = scoreInt,
            commentAuthor = data["author"] as String?
        )
    )
    when (val replies = data["replies"]) {
        is String, null -> {}
        is Map<*, *> -> {
            (replies as Map<String, Any>).getComments(level + 1, comments)
        }

        else -> {
            throw Exception("Should never happen")
        }
    }
}
