package com.vitalii.redditapi.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import java.net.URL


class ImageLoader(private val imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val imageURL = urls[0]
        var bimage: Bitmap? = null
        try {
            val stream: InputStream = URL(imageURL).openStream()
            bimage = BitmapFactory.decodeStream(stream)
        } catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return bimage
    }

    override fun onPostExecute(result: Bitmap?) {
        imageView.setImageBitmap(result)
    }
}