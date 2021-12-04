package com.vitalii.redditapi

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vitalii.redditapi.adapter.PostsAdapter
import com.vitalii.redditapi.databinding.ActivityMainBinding
import com.vitalii.redditapi.model.Post
import com.vitalii.redditapi.network.DataLoader
import com.vitalii.redditapi.network.DownloadImage
import com.vitalii.redditapi.network.SimpleDataLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTAG"

    private var listOfPosts: ArrayList<Post> = ArrayList();
    private val dataLoader: DataLoader = SimpleDataLoader()
    private lateinit var adapter: PostsAdapter
    private lateinit var binding: ActivityMainBinding

    private lateinit var downloadManager:DownloadManager
    private val downloadImage:DownloadImage = DownloadImage()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup recycler view
        adapter = PostsAdapter()
        binding.recyclerViewContainer.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewContainer.adapter = adapter

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        if(savedInstanceState == null) {
            GlobalScope.launch(Dispatchers.IO) {
                listOfPosts = dataLoader.loadTopRedditPosts() as ArrayList<Post>
                listOfPosts.forEach {
                    //Log.i(TAG, "\nauthor: ${it.authorName}\nnumber of comments: ${it.numberOfComments}")
                }
                runOnUiThread {
                    adapter.list = listOfPosts
                }
            }
        }

        adapter.onPostItemLongClickListener = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                askPermissions()
            }
            if(checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                downloadImage.loadImg(it.thumbnail!!, downloadManager)
            }
            else {
                askPermissions()
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            //downloadImage(imageUrl)

            //downloadImage.loadImg(it.image!!, downloadManager)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    //downloadImage(imageUrl)
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
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
        adapter.list = listOfPosts
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}