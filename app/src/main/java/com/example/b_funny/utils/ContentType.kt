package com.example.b_funny.utils

import java.net.URI
import java.net.URISyntaxException

object ContentType {
    /**
     * Checks if `host` is contains by any of the provided `bases`
     *
     *
     * For example "www.youtube.com" contains "youtube.com" but not "notyoutube.com" or
     * "youtube.co.uk"
     *
     * @param host  A hostname from e.g. [URI.getHost]
     * @param bases Any number of hostnames to compare against `host`
     * @return If `host` contains any of `bases`
     */
    fun hostContains(host: String?, vararg bases: String?): Boolean {
        if (host == null || host.isEmpty()) return false
        for (base in bases) {
            if (base == null || base.isEmpty()) continue
            val index = host.lastIndexOf(base)
            if (index < 0 || index + base.length != host.length) continue
            if (base.length == host.length || host[index - 1] == '.') return true
        }
        return false
    }

    fun isGif(uri: URI): Boolean {
        return try {
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            (hostContains(host, "gfycat.com")
                    || hostContains(host, "v.redd.it")
                    || path.endsWith(".gif")
                    || path.endsWith(".gifv")
                    || path.endsWith(".webm")
                    || path.endsWith(".mp4"))
        } catch (e: NullPointerException) {
            false
        }
    }

    fun isGifLoadInstantly(uri: URI): Boolean {
        return try {
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            hostContains(host, "gfycat.com") || hostContains(host, "v.redd.it") || (hostContains(
                host,
                "imgur.com"
            )
                    && (path.endsWith(".gif") || path.endsWith(".gifv") || path.endsWith(
                ".webm"
            ))) || path.endsWith(".mp4")
        } catch (e: NullPointerException) {
            false
        }
    }

    fun isImage(uri: URI): Boolean {
        return try {
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            host == "i.reddituploads.com" || path.endsWith(".png") || path.endsWith(
                ".jpg"
            ) || path.endsWith(".jpeg")
        } catch (e: NullPointerException) {
            false
        }
    }

    fun isAlbum(uri: URI): Boolean {
        return try {
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            hostContains(host, "imgur.com", "bildgur.de") && (path.startsWith("/a/")
                    || path.startsWith("/gallery/")
                    || path.startsWith("/g/")
                    || path.contains(","))
        } catch (e: NullPointerException) {
            false
        }
    }

    fun isVideo(uri: URI): Boolean {
        return try {
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            hostContains(
                host, "youtu.be", "youtube.com",
                "youtube.co.uk"
            ) && !path.contains("/user/") && !path.contains("/channel/")
        } catch (e: NullPointerException) {
            false
        }
    }

    fun isImgurLink(url: String?): Boolean {
        return try {
            val uri = URI(url)
            val host = uri.host.lowercase()
            (hostContains(host, "imgur.com", "bildgur.de")
                    && !isAlbum(uri)
                    && !isGif(uri)
                    && !isImage(uri))
        } catch (e: URISyntaxException) {
            false
        } catch (e: NullPointerException) {
            false
        }
    }

    /**
     * Attempt to determine the content type of a link from the URL
     *
     * @param url URL to get ContentType from
     * @return ContentType of the URL
     */
    fun getContentType(url: String): Type {
        var url = url
        if (!url.startsWith("//") &&
            (
                    url.startsWith("/") &&
                            url.length < 4 ||
                            url.startsWith("#spoiler") ||
                            url.startsWith("/spoiler") ||
                            url.startsWith("#s-") ||
                            url == "#s" ||
                            url == "#ln" ||
                            url == "#b" ||
                            url == "#sp"
                    )
        ) {
            return Type.SPOILER
        }
        if (url.startsWith("mailto:")) {
            return Type.EXTERNAL
        }
        if (url.startsWith("//")) url = "https:$url"
        if (url.startsWith("/")) url = "reddit.com$url"
        if (!url.contains("://")) url = "http://$url"
        return try {
            val uri = URI(url)
            val host = uri.host.lowercase()
            val scheme = uri.scheme.lowercase()
            if (hostContains(
                    host,
                    "v.redd.it"
                ) || host == "reddit.com" && url.contains("reddit.com/video/")
            ) {
                return if (url.contains("DASH_")) {
                    Type.VREDDIT_DIRECT
                } else {
                    Type.VREDDIT_REDIRECT
                }
            }
            if (scheme != "http" && scheme != "https") {
                return Type.EXTERNAL
            }
            if (isVideo(uri)) {
                return Type.VIDEO
            }
            if (isGif(uri)) {
                return Type.GIF
            }
            if (isImage(uri)) {
                return Type.IMAGE
            }
            if (isAlbum(uri)) {
                return Type.ALBUM
            }
            if (hostContains(host, "imgur.com", "bildgur.de")) {
                return Type.IMGUR
            }
            if (hostContains(host, "xkcd.com") && !hostContains("imgs.xkcd.com") && !hostContains(
                    "what-if.xkcd.com"
                )
            ) {
                return Type.XKCD
            }
            if (hostContains(host, "tumblr.com") && uri.path.contains("post")) {
                return Type.TUMBLR
            }
            if (hostContains(host, "reddit.com", "redd.it")) {
                return Type.REDDIT
            }
            if (hostContains(host, "vid.me")) {
                return Type.VID_ME
            }
            if (hostContains(host, "deviantart.com")) {
                return Type.DEVIANTART
            }
            if (hostContains(host, "streamable.com")) {
                Type.STREAMABLE
            } else Type.LINK
        } catch (e: URISyntaxException) {
            if (e.message != null && (e.message!!.contains("Illegal character in fragment")
                        || e.message!!.contains("Illegal character in query")
                        || e.message!!
                    .contains(
                        "Illegal character in path"
                    ))
            ) //a valid link but something un-encoded in the URL
            {
                return Type.LINK
            }
            e.printStackTrace()
            Type.NONE
        } catch (e: NullPointerException) {
            if (e.message != null && (e.message!!.contains("Illegal character in fragment")
                        || e.message!!.contains("Illegal character in query")
                        || e.message!!
                    .contains(
                        "Illegal character in path"
                    ))
            ) {
                return Type.LINK
            }
            e.printStackTrace()
            Type.NONE
        }
    }

    fun displayImage(t: Type?): Boolean {
        return when (t) {
            Type.ALBUM, Type.DEVIANTART, Type.IMAGE, Type.XKCD, Type.TUMBLR, Type.IMGUR, Type.SELF -> true
            else -> false
        }
    }

    fun fullImage(t: Type?): Boolean {
        return when (t) {
            Type.ALBUM, Type.DEVIANTART, Type.GIF, Type.IMAGE, Type.IMGUR, Type.STREAMABLE, Type.TUMBLR, Type.XKCD, Type.VIDEO, Type.SELF, Type.VREDDIT_DIRECT, Type.VREDDIT_REDIRECT, Type.VID_ME -> true
            Type.EMBEDDED, Type.EXTERNAL, Type.LINK, Type.NONE, Type.REDDIT, Type.SPOILER -> false
            else -> false
        }
    }

    fun mediaType(t: Type?): Boolean {
        return when (t) {
            Type.ALBUM, Type.DEVIANTART, Type.GIF, Type.IMAGE, Type.TUMBLR, Type.XKCD, Type.IMGUR, Type.VREDDIT_DIRECT, Type.VREDDIT_REDIRECT, Type.STREAMABLE, Type.VID_ME -> true
            else -> false
        }
    }

    fun isImgurImage(lqUrl: String?): Boolean {
        return try {
            val uri = URI(lqUrl)
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            (host.contains("imgur.com") || host.contains("bildgur.de")) && (path.endsWith(
                ".png"
            ) || path.endsWith(".jpg") || path.endsWith(".jpeg"))
        } catch (e: Exception) {
            false
        }
    }

    fun isImgurHash(lqUrl: String?): Boolean {
        return try {
            val uri = URI(lqUrl)
            val host = uri.host.lowercase()
            val path = uri.path.lowercase()
            host.contains("imgur.com") && !(path.endsWith(".png") && !path.endsWith(
                ".jpg"
            ) && !path.endsWith(".jpeg"))
        } catch (e: Exception) {
            false
        }
    }

    fun formatGifUrl(s: String): String {
        var s = s
        if (s.endsWith("v") && !s.contains("streamable.com")) {
            s = s.substring(0, s.length - 1)
        } else if (s.contains("gfycat") && !s.contains("mp4") && !s.contains("webm")) {
            if (s.contains("-size_restricted")) {
                s = s.replace("-size_restricted", "")
            }

            // https://gfycat.com/cajax/get/ShamelessOrderlyIndianspinyloach
            // https://thumbs.gfycat.com/ShamelessOrderlyIndianspinyloach-mobile.mp4
            s = s.replace("gfycat", "thumbs.gfycat")
            s = "$s-mobile.mp4"
        }
        if ((s.contains(".webm") || s.contains(".gif")) && !s.contains(".gifv") && s.contains(
                "imgur.com"
            )
        ) {
            s = s.replace(".gif", ".mp4")
            s = s.replace(".webm", ".mp4")
        }
        if (s.endsWith("/")) s = s.substring(0, s.length - 1)
        if (s.endsWith("?r")) s = s.substring(0, s.length - 2)
        if (s.contains("v.redd.it") && !s.contains("DASH")) {
            if (s.endsWith("/")) {
                s = s.substring(s.length - 2)
            }
            s = "$s/DASH_9_6_M"
        }
        return s
    }

    fun getVideoType(url: String): VideoType {
        if (url.contains("v.redd.it")) {
            return VideoType.VREDDIT
        }
        if (url.contains(".mp4") || url.contains("webm") || url.contains("redditmedia.com")) {
            return VideoType.DIRECT
        }
        if (url.contains("gfycat") && !url.contains("mp4")) return VideoType.GFYCAT
        if (url.contains("imgur.com")) return VideoType.IMGUR
        if (url.contains("vid.me")) return VideoType.VID_ME
        return if (url.contains("streamable.com")) VideoType.STREAMABLE else VideoType.OTHER
    }

    enum class VideoType {
        IMGUR, VID_ME, STREAMABLE, GFYCAT, DIRECT, OTHER, VREDDIT;

        fun shouldLoadPreview(): Boolean {
            return this == OTHER
        }
    }

    enum class Type {
        ALBUM, DEVIANTART, EMBEDDED, EXTERNAL, GIF, VREDDIT_DIRECT, VREDDIT_REDIRECT, IMAGE, IMGUR, LINK, NONE, REDDIT, SELF, SPOILER, STREAMABLE, VIDEO, XKCD, TUMBLR, VID_ME
    }
}