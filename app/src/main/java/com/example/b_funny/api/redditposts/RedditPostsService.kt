package com.example.b_funny.api.redditposts

import retrofit2.http.GET
import retrofit2.http.Query

interface RedditPostsService {
    @GET("/top.json")
    suspend fun getTop(
        @Query("after") after: String,
        @Query("limit") limit: String
    ): RedditPostsResponse
}