package com.bfunny.redditlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bfunny.redditposts.RedditPostsClient
import com.bfunny.redditposts.RedditPostsRepository
import com.bfunny.model.RedditPost
import com.bfunny.utils.SingleLiveEvent
import kotlinx.coroutines.launch


class RedditListViewModel : ViewModel() {
    private val _response = MutableLiveData<List<RedditPost>>()
    val response: LiveData<List<RedditPost>>
        get() = _response

    val showToast = SingleLiveEvent<String>()

    private lateinit var repository: RedditPostsRepository

    var loading = false

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

    private var subreddit: String? = null
    fun changeSubreddit(subreddit: String) {
        if (this.subreddit == subreddit) {
            return
        }
        this.subreddit = subreddit
        this.repository = RedditPostsRepository(RedditPostsClient(subreddit))
        getMore()
    }
}
