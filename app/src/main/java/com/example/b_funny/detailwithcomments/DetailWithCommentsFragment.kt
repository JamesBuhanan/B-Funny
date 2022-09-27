package com.example.b_funny.detailwithcomments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.b_funny.databinding.FragmentDetailBinding
import com.example.b_funny.databinding.FragmentDetailWithCommentsBinding


class DetailWithCommentsFragment : Fragment() {
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
        return binding.root
    }

}