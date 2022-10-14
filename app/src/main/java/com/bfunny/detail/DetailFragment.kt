package com.bfunny.detail

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.bfunny.R
import com.bfunny.databinding.FragmentDetailBinding


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
            doThing(redditPost.title!!, redditPost.url!!)
        }
        return binding.root
    }

    private fun doThing(postTitle: String, url: String) {
        val downloadManager: DownloadManager = requireActivity()
            .baseContext
            .getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url)
        val imgName = URLUtil.guessFileName(url, null, fileExtension)

        val request = DownloadManager.Request(uri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setAllowedOverMetered(true)
                .setTitle(imgName)
                .setDescription("")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imgName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .allowScanningByMediaScanner()
        }
        downloadManager.enqueue(request)
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