package com.vitalii.redditapi.network

import com.vitalii.redditapi.model.Post

interface DataLoader {
    fun loadTopRedditPosts(): List<Post>
}