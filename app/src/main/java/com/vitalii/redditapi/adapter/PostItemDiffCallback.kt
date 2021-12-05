package com.vitalii.redditapi.adapter

import androidx.recyclerview.widget.DiffUtil
import com.vitalii.redditapi.model.Post

class PostItemDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}