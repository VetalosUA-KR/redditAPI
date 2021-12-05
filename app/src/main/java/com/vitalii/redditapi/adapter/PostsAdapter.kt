package com.vitalii.redditapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vitalii.redditapi.databinding.AdapterSingePostItemBinding
import com.vitalii.redditapi.model.Post
import com.vitalii.redditapi.network.ImageLoader

class PostsAdapter: RecyclerView.Adapter<PostsAdapter.PostItemViewHolder>() {

    var list = listOf<Post>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onPostItemLongClickListener: ((Post) -> Unit)? = null
    var onPostItemClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AdapterSingePostItemBinding.inflate(layoutInflater)
        return PostItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        val currentPost = list[position]
        with(holder.binding) {
            postItemAuthor.text = currentPost.authorName
            postItemCountComments.text = currentPost.numberOfComments.toString()
            postItemTimeAddPost.text = currentPost.timeOfCrate.toString()
            ImageLoader(ivPostItemAuthorThumbnail).execute(currentPost.thumbnail)
        }

        initListener(holder, currentPost)
    }

    private fun initListener(holder: PostItemViewHolder, currentPost: Post) {
        holder.itemView.setOnLongClickListener {
            onPostItemLongClickListener?.invoke(currentPost)
            true
        }
        holder.itemView.setOnClickListener {
            onPostItemClickListener?.invoke(currentPost)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class PostItemViewHolder(val binding: AdapterSingePostItemBinding): RecyclerView.ViewHolder(binding.root)
}