package com.vitalii.redditapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vitalii.redditapi.databinding.AdapterSingePostItemBinding
import com.vitalii.redditapi.model.Post
import com.vitalii.redditapi.network.ImageLoader
import java.util.*

class PostsAdapter : ListAdapter<Post, PostsAdapter.PostItemViewHolder>(PostItemDiffCallback()) {

    var onPostItemLongClickListener: ((Post, View) -> Unit)? = null
    var onPostItemClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AdapterSingePostItemBinding.inflate(layoutInflater)
        return PostItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val currentPost = getItem(position)
        with(holder.binding) {
            postItemAuthor.text = currentPost.authorName
            postItemCountComments.text = currentPost.numberOfComments.toString()
            postItemTimeAddPost.text = getHour(currentPost.timeOfCrate)
            ImageLoader(ivPostItemAuthorThumbnail).execute(currentPost.thumbnail)
        }
        initListener(holder, currentPost)
    }

    private fun getHour(ms: Long): String {
        val date = Date()
        val days: Long = (date.time / 1000 - ms) / (60 * 60)
        return "$days hours ago"
    }

    private fun initListener(holder: PostItemViewHolder, currentPost: Post) {
        holder.itemView.setOnLongClickListener {
            onPostItemLongClickListener?.invoke(currentPost, holder.binding.viewTop)
            true
        }
        holder.itemView.setOnClickListener {
            onPostItemClickListener?.invoke(currentPost)
        }
    }

    class PostItemViewHolder(val binding: AdapterSingePostItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}