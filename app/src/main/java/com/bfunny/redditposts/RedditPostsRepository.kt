package com.bfunny.redditposts

import com.bfunny.model.Comment
import com.bfunny.model.RedditPost
import com.bfunny.model.toRedditPosts
import com.bfunny.utils.ContentType

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