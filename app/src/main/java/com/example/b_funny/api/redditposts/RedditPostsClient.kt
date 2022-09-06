package com.example.b_funny.api.redditposts

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RedditPostsClient(private val subreddit: String) {

    private val redditApi: RedditPostsService

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        redditApi = retrofit.create(RedditPostsService::class.java)
    }

    suspend fun getTop(after: String, limit: String): RedditPostsResponse {
        return redditApi.getTop(subreddit, after, limit)
    }
}