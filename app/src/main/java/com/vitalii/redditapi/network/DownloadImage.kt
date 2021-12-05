package com.vitalii.redditapi.network

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.vitalii.redditapi.Contract
import java.io.File

class DownloadImage(): Contract.Model {


    override fun downloadImage(url: String, downloadManager: DownloadManager) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }
        downloadManager.enqueue(request)
    }
}