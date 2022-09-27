package com.example.b_funny.api.redditposts

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditPostsService {
    @GET("/r/{subreddit}/top.json")
    suspend fun getTop(
        @Path("subreddit") subreddit: String,
        @Query("after") after: String,
        @Query("limit") limit: String
    ): RedditPostsResponse

    // CommentsManager.BASE_COMMENT_URL + mPermalink + ".json?" + "raw_json=1"
    @GET("{permalink}.json?raw_json=1")
    suspend fun getComments(
        @Path("permalink") permalink: String
    ): List<Map<String, Any>>
}