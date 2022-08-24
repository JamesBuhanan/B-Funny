package com.example.b_funny.api.redditposts

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditPostsAPI {
    @GET("/top.json")
    suspend fun getTop(@Query("after") after: String, @Query("limit") limit: String): RedditPostsResponse
}