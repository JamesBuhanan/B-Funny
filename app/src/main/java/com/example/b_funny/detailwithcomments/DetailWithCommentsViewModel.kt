package com.example.b_funny.detailwithcomments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_funny.api.redditposts.RedditPostsClient
import com.example.b_funny.api.redditposts.RedditPostsRepository
import com.example.b_funny.model.RedditPost
import com.example.b_funny.utils.SingleLiveEvent
import kotlinx.coroutines.launch


class DetailWithCommentsViewModel : ViewModel() {
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
