package com.vitalii.redditapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.vitalii.redditapi.model.Post
import com.vitalii.redditapi.network.DataLoader
import com.vitalii.redditapi.network.SimpleDataLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTAG"

    private var listOfPosts: ArrayList<Post> = ArrayList();

    private val dataLoader: DataLoader = SimpleDataLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            GlobalScope.launch(Dispatchers.IO) {
                listOfPosts = dataLoader.loadTopRedditPosts() as ArrayList<Post>
                Log.i(TAG, "size: ${listOfPosts.size}")
                listOfPosts.forEach {
                    //Log.i(TAG, "\nauthor: ${it.authorName}\nnumber of comments: ${it.numberOfComments}")
                }
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("key", listOfPosts)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        listOfPosts.clear()
        val personList: List<Post> = savedInstanceState.getParcelableArrayList("key")!!
        listOfPosts.addAll(personList)
    }
}