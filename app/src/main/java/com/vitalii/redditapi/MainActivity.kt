package com.vitalii.redditapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.vitalii.redditapi.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parseJSON()
    }


    private val KEY_DATA = "data"
    private val KEY_CHILDREN = "children"

    private val KEY_AUTHOR_NAME = "author_fullname"
    private val KEY_TIME_OF_CREATED = "created_utc"
    private val KEY_TIME_OF_CREATED2 = "created"
    private val KEY_THUMBNAIL = "thumbnail"
    private val KEY_NUMBER_OF_COMMENTS = "num_comments"
    private val KEY_IMAGE = "url"

    @SuppressLint("LongLogTag")
    private fun parseJSON() {

        GlobalScope.launch(Dispatchers.IO) {
            val url =
                URL("https://www.reddit.com/top.json")
            val httpsURLConnection = url.openConnection() as HttpsURLConnection
            httpsURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            )
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = false
            val responseCode = httpsURLConnection.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {

                    val jsonObjectMain = JSONTokener(response).nextValue() as JSONObject
                    val jsObject = jsonObjectMain.getJSONObject("data")
                    val jsArray = jsObject.getJSONArray("children")

                    for(i in 0 until jsArray.length()) {
                        val postData = jsArray.getJSONObject(i)
                        val obj = postData.getJSONObject(KEY_DATA)
                        val name = obj.getString(KEY_AUTHOR_NAME)
                        val time: Long = obj.getLong(KEY_TIME_OF_CREATED2)
                        val thumbnail: String = obj.getString(KEY_THUMBNAIL)
                        val TIME_OF_CREATED: String = obj.getString(KEY_TIME_OF_CREATED)
                        val numberOfComments: Int = obj.getInt(KEY_NUMBER_OF_COMMENTS)
                        val image: String = obj.getString(KEY_IMAGE)

                        val post = Post(name, time, thumbnail, numberOfComments, image)

                        Log.i(TAG, TIME_OF_CREATED)
                    }
                }
            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
    }
}