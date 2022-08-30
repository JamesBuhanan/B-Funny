/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.b_funny.redditlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_funny.api.redditposts.RedditPostsAPIClient
import com.example.b_funny.api.redditposts.RedditPostsResponse
import com.example.b_funny.model.RedditPost
import com.example.b_funny.model.toRedditPosts
import com.example.b_funny.utils.ContentType
import kotlinx.coroutines.launch

//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//
///**
// * The [ViewModel] that is attached to the [OverviewFragment].
// */
class RedditListViewModel : ViewModel() {
    //
//    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<List<RedditPost>>()

    // The external immutable LiveData for the request status String
    val response: LiveData<List<RedditPost>>
        get() = _response

    var after = "0"
    var loading = false

    init {
        getMore()
    }

    fun getMore() {
        loading = true
        viewModelScope.launch {
            val redditPostsResponse: RedditPostsResponse =
                RedditPostsAPIClient().getNews(after, "25")
            after = redditPostsResponse.data.after
            val redditPosts = redditPostsResponse.toRedditPosts().filter { redditPost ->
                redditPost.url != null &&
                        ContentType.getContentType(redditPost.url) == ContentType.Type.IMAGE
            }
            val newPosts = when (val existingPosts = _response.value) {
                null -> redditPosts
                else -> existingPosts + redditPosts
            }
            _response.value = newPosts
            loading = false
        }
    }
}
