package com.vitalii.redditapi.network

import android.util.Log
import com.vitalii.redditapi.model.Post
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SimpleDataLoader : DataLoader {

    private val BASE_URL = "https://www.reddit.com/top.json"
    private val KEY_DATA = "data"
    private val KEY_CHILDREN = "children"

    private val KEY_AUTHOR_NAME = "author"
    private val KEY_TIME_OF_CREATED = "created_utc"
    private val KEY_TIME_OF_CREATED2 = "created"
    private val KEY_THUMBNAIL = "thumbnail"
    private val KEY_NUMBER_OF_COMMENTS = "num_comments"
    private val KEY_IMAGE = "url"

    override fun loadTopRedditPosts(): List<Post> {
        val listOfPost: ArrayList<Post> = ArrayList()
        val httpsURLConnection = prepareURLConnection()

        val responseCode = httpsURLConnection.responseCode

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val response = httpsURLConnection.inputStream.bufferedReader().use { it.readText() }
            val jsonObjectMain = JSONTokener(response).nextValue() as JSONObject
            val jsObject = jsonObjectMain.getJSONObject("data")
            val jsArray = jsObject.getJSONArray("children")

            for (i in 0 until jsArray.length()) {
                val postData = jsArray.getJSONObject(i)
                val obj = postData.getJSONObject(KEY_DATA)
                val name = obj.getString(KEY_AUTHOR_NAME)
                val time: Long = obj.getLong(KEY_TIME_OF_CREATED2)
                val thumbnail: String = obj.getString(KEY_THUMBNAIL)
                val TIME_OF_CREATED: String = obj.getString(KEY_TIME_OF_CREATED)
                val numberOfComments: Int = obj.getInt(KEY_NUMBER_OF_COMMENTS)
                val image: String = obj.getString(KEY_IMAGE)
                Log.i("fsasg", thumbnail)
                val post = Post(name, time, thumbnail, numberOfComments, image)
                listOfPost.add(post)
            }
        } else {
            Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
        }
        return listOfPost;
    }

    private fun prepareURLConnection(): HttpsURLConnection {
        val url = URL(BASE_URL)
        val httpsURLConnection = url.openConnection() as HttpsURLConnection
        httpsURLConnection.setRequestProperty(
            "Accept",
            "application/json"
        )
        httpsURLConnection.requestMethod = "GET"
        httpsURLConnection.doInput = true
        httpsURLConnection.doOutput = false
        return httpsURLConnection
    }
}