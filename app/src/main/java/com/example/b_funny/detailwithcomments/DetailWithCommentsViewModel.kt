package com.example.b_funny.detailwithcomments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b_funny.api.redditposts.RedditPostsClient
import com.example.b_funny.api.redditposts.RedditPostsRepository
import com.example.b_funny.model.Comment
import com.example.b_funny.utils.SingleLiveEvent
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
