package com.example.b_funny.api.redditposts

import com.example.b_funny.model.Comment
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

    suspend fun getComments(): List<Comment> {
        val redditCommentsResponses: List<Map<String, Any>> =
            redditApi.getComments("r/Awww/comments/xlbv1g/so_adorable/")
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
    comments.add(
        Comment(
            level = level,
            commentBody = data["body"] as String?,
        )
    )
    when (val replies = data["replies"]) {
        is String -> {}
        is Map<*, *> -> {
            (replies as Map<String, Any>).getComments(level + 1, comments)
        }

        else -> {
            throw Exception("Should never happen")
        }
    }
}
