package com.example.b_funny.detailwithcomments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.b_funny.databinding.FragmentDetailWithCommentsBinding


class DetailWithCommentsFragment : Fragment() {
    private val viewModel: DetailWithCommentsViewModel by lazy {
        ViewModelProvider(this)[DetailWithCommentsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailWithCommentsBinding.inflate(inflater, container, false)
        binding.commentsView.adapter = CommentsAdapter()
        val redditPost =
            DetailWithCommentsFragmentArgs.fromBundle(requireArguments()).selectedRedditPost
        binding.redditPost = redditPost
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getComments(redditPost.permalink.orEmpty())
        return binding.root
    }
}