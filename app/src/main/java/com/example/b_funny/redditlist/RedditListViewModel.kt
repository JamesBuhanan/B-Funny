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
import com.example.b_funny.api.redditposts.RedditPostsClient
import com.example.b_funny.api.redditposts.RedditPostsRepository
import com.example.b_funny.model.RedditPost
import kotlinx.coroutines.launch


class RedditListViewModel : ViewModel() {

    private val _response = MutableLiveData<List<RedditPost>>()

    val response: LiveData<List<RedditPost>>
        get() = _response

    private val repository = RedditPostsRepository(RedditPostsClient())

    var loading = false

    init {
        getMore()
    }

    fun getMore() {
        loading = true
        viewModelScope.launch {
            val result: Result<List<RedditPost>> = repository.getMore()

            result.fold(
                onSuccess = { allPosts ->
                    _response.value = allPosts.toList()
                    loading = false
                },
                onFailure = {
                    // Show toast?
                    loading = false
                }
            )
        }
    }
}
