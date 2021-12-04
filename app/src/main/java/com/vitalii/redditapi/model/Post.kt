package com.vitalii.redditapi.model

data class Post(
    val authorName: String,
    val timeOfCrate: Long,
    val thumbnail: String,
    val numberOfComments: Int,
    val image: String
)
