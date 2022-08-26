package com.example.b_funny.redditlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.b_funny.databinding.FragmentRedditListBinding
import com.example.b_funny.model.RedditPost

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RedditListFragment : Fragment() {
    private val viewModel: RedditListViewModel by lazy {
        ViewModelProvider(this).get(RedditListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRedditListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val onClickListener: (RedditPost) -> Unit = {
            // navigate to detail fragment with this RedditPost
            findNavController().navigate(RedditListFragmentDirections.actionRedditListFragmentToDetailFragment(it))
        }

        binding.postList.adapter = OverviewAdapter(onClickListener)


        return binding.root
    }
}