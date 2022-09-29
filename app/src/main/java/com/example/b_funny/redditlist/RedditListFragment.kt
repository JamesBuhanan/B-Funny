package com.example.b_funny.redditlist

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.b_funny.R
import com.example.b_funny.databinding.FragmentRedditListBinding
import com.example.b_funny.model.RedditPost

const val SUBREDDIT_SHARED_PREFERENCES = "SUBREDDIT_SHARED_PREFERENCES"
const val SUBREDDIT = "SUBREDDIT"
const val ALL = "all"
const val FUNNY = "funny"
const val WHOLESOME_MEMES = "wholesomememes"

class RedditListFragment : Fragment() {
    private val viewModel: RedditListViewModel by lazy {
        ViewModelProvider(this).get(RedditListViewModel::class.java)
    }
    private val sharedPreferences by lazy {
        requireContext()
            .getSharedPreferences(SUBREDDIT_SHARED_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val persistedSubreddit = requireNotNull(sharedPreferences.getString(SUBREDDIT, ALL))
        changeSubreddit(persistedSubreddit)

        val binding = FragmentRedditListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val onClickListener: (RedditPost) -> Unit = {
            // navigate to detail fragment with this RedditPost
            findNavController().navigate(
                RedditListFragmentDirections.actionRedditListFragmentToDetailFragment(it)
            )
        }
        val onClickListener2: (RedditPost) -> Unit = {
            // navigate to detail fragment with this RedditPost
            findNavController().navigate(
                RedditListFragmentDirections.actionRedditListFragmentToDetailWithCommentsFragment(it)
            )
        }
        binding.postList.adapter = OverviewAdapter(onClickListener, onClickListener2)
        binding.postList.setOnScrollChangeListener { _, _, _, _, _ ->
            val lastGridItemPosition =
                (binding.postList.layoutManager as GridLayoutManager).findLastVisibleItemPosition()

            val lastViewModelPosition = (viewModel.response.value?.size ?: 0) - 1
            if (lastGridItemPosition >= lastViewModelPosition) {
                if (!viewModel.loading) {
                    viewModel.getMore()
                }
            }
        }
        viewModel.showToast.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    private fun changeSubreddit(subreddit: String) {
        sharedPreferences.edit().putString(SUBREDDIT, subreddit).apply()
        viewModel.changeSubreddit(subreddit)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val subreddit = when (item.itemId) {
            R.id.id_all -> ALL
            R.id.id_wholesomememes -> WHOLESOME_MEMES
            else -> FUNNY
        }
        changeSubreddit(subreddit)
        return super.onOptionsItemSelected(item)
    }
}