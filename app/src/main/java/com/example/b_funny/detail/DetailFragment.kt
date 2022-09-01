package com.example.b_funny.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.example.b_funny.R
import com.example.b_funny.databinding.FragmentDetailBinding
import com.example.b_funny.utils.sendNotification

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        val redditPost = DetailFragmentArgs.fromBundle(requireArguments()).selectedRedditPost
        binding.redditPost = redditPost
        binding.btnShare.setOnClickListener {
            share(redditPost.url!!)
        }
        binding.btnDownload.setOnClickListener {
            sendNotification(
                context = requireContext(),
                message = "Nothing actually downloaded",
                title = "Download complete",
            )
        }
        return binding.root
    }

    private fun getShareIntent(url: String): Intent {
        return ShareCompat.IntentBuilder.from(requireActivity())
            .setText(getString(R.string.sharing).format(url))
            .setType("text/plain")
            .intent
    }

    private fun share(url: String) {
        startActivity(getShareIntent(url))
    }
}