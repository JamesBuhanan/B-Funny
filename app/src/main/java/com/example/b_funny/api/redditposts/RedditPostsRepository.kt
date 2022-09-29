package com.example.b_funny.api.redditposts

import com.example.b_funny.model.Comment
import com.example.b_funny.model.RedditPost
import com.example.b_funny.model.toRedditPosts
import com.example.b_funny.utils.ContentType

class RedditPostsRepository(
    private val redditPostsClient: RedditPostsClient,
) {
    private var after = "0"
    private val mutableList = mutableListOf<RedditPost>()

    suspend fun getMore(): Result<List<RedditPost>> {
        return try {
            val redditPostsResponse = redditPostsClient.getTop(after, "25")
            after = redditPostsResponse.data.after

            val redditPosts = redditPostsResponse.toRedditPosts().filter { redditPost ->
                redditPost.url != null &&
                        ContentType.getContentType(redditPost.url) == ContentType.Type.IMAGE
            }
            mutableList.addAll(redditPosts)

            Result.success(mutableList)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getComments(permalink: String): Result<List<Comment>> {
        return try {
            val result = redditPostsClient.getComments(permalink)
            Result.success(result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}