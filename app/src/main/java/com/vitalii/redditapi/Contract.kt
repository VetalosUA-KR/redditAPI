package com.vitalii.redditapi

import android.app.DownloadManager
import com.vitalii.redditapi.model.Post

interface Contract {

    interface View {
        fun showTopPost(listOfPosts: ArrayList<Post>)
        fun showToast(msg: String)
    }

    interface Presenter {
        fun loadTopRedditPosts()
        fun downloadImage(url: String, downloadManager: DownloadManager)
        fun attachView(view: View)
    }

    interface Model {

        fun downloadImage(url: String, downloadManager: DownloadManager)
    }
}