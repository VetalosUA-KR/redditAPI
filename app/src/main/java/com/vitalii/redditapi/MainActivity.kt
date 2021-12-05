package com.vitalii.redditapi

import android.Manifest
import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
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
import com.vitalii.redditapi.utils.Utils.Companion.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
import com.vitalii.redditapi.utils.Utils.Companion.RESTORE_STATE_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTAG"

    private var listOfPosts: ArrayList<Post> = ArrayList();
    private val dataLoader: DataLoader = SimpleDataLoader()
    private lateinit var adapter: PostsAdapter
    private lateinit var binding: ActivityMainBinding

    private lateinit var downloadManager: DownloadManager
    private val downloadImage: DownloadImage = DownloadImage()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        if (savedInstanceState == null) {
            GlobalScope.launch(Dispatchers.IO) {
                listOfPosts = dataLoader.loadTopRedditPosts() as ArrayList<Post>
                runOnUiThread {
                    adapter.list = listOfPosts
                }
            }
        }

        clickLickListener()
    }

    private fun initRecyclerView() {
        adapter = PostsAdapter()
        binding.recyclerViewContainer.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewContainer.adapter = adapter
    }

    private fun clickLickListener() {
        adapter.onPostItemLongClickListener = { post, view ->
            val popup = PopupMenu(this, view)
            popup.inflate(R.menu.pop_up_post_item)
            popup.setOnMenuItemClickListener {
                when(it!!.itemId) {
                    R.id.download -> {
                        loadImage(post)
                    }
                }
                true
            }
            popup.show()
        }

        adapter.onPostItemClickListener = {
            if(it.thumbnail.equals("default")) {
                Toast.makeText(this, "No photo to show", Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog = CustomDialog.newInstance(it.thumbnail!!)
                dialog.show(supportFragmentManager, "customDialog")
            }

        }
    }

    private fun loadImage(post: Post) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions()
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadImage.loadImg(post.thumbnail!!, downloadManager)
        } else {
            askPermissions()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
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
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(RESTORE_STATE_KEY, listOfPosts)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        listOfPosts.clear()
        val personList: List<Post> = savedInstanceState.getParcelableArrayList(RESTORE_STATE_KEY)!!
        listOfPosts.addAll(personList)
        adapter.list = listOfPosts
    }

}