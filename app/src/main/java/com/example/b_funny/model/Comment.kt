package com.example.b_funny.model

data class Comment(
    var commentAuthor: String? = null,
    var commentBody: String? = null,
    var commentScore: Int? = 0,
    var commentTime: Long = 0,
    var level: Int = 0,
)