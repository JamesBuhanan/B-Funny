package com.bfunny.detailwithcomments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bfunny.redditposts.RedditPostsClient
import com.bfunny.redditposts.RedditPostsRepository
import com.bfunny.model.Comment
import com.bfunny.utils.SingleLiveEvent
import kotlinx.coroutines.launch


class DetailWithCommentsViewModel : ViewModel() {
    private val _response = MutableLiveData<List<Comment>>()
    val response: LiveData<List<Comment>>
        get() = _response

    val showToast = SingleLiveEvent<String>()

    private val repository = RedditPostsRepository(RedditPostsClient(""))

    var loading = false

    fun getComments(permalink: String) {
        loading = true
        viewModelScope.launch {
            val result: Result<List<Comment>> = repository.getComments(permalink)

            result.fold(
                onSuccess = { comments ->
                    _response.value = comments.toList()
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
