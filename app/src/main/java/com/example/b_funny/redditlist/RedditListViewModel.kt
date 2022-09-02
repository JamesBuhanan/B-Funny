

package com.example.b_funny.redditlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_funny.api.redditposts.RedditPostsClient
import com.example.b_funny.api.redditposts.RedditPostsRepository
import com.example.b_funny.model.RedditPost
import com.example.b_funny.utils.SingleLiveEvent
import kotlinx.coroutines.launch


class RedditListViewModel : ViewModel() {

    private val _response = MutableLiveData<List<RedditPost>>()

    val response: LiveData<List<RedditPost>>
        get() = _response

    val showToast = SingleLiveEvent<String>()

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
                    showToast.value = "turn on wifi!"
                    loading = false
                }
            )
        }
    }
}
