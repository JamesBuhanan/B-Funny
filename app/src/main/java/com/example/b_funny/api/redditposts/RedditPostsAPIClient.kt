package com.example.b_funny.api.redditposts

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RedditPostsAPIClient() {

    private val redditApi: RedditPostsAPI

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        redditApi = retrofit.create(RedditPostsAPI::class.java)
    }

    suspend fun getNews(after: String, limit: String): RedditPostsResponse {
        return redditApi.getTop(after, limit)
    }
}