package com.techbanda.gallery

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.basudev.mediamanager.Folder
import com.basudev.mediamanager.Gallery
import com.basudev.mediamanager.MediaManager
import com.basudev.mediamanager.Recent
import com.techbanda.gallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_STORAGE_PERMISSIONS = 10
    private var galleryList = ArrayList<Gallery>()
    private var galleryFolderList = ArrayList<Recent>()
    private var tempgalleryList = ArrayList<Gallery>()
    private var pictureFolders = ArrayList<Folder>()
    private lateinit var adapter: MediaAdapter
    private lateinit var adapter2: MediaFolderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MediaAdapter(mediaList = galleryList)
        adapter2 = MediaFolderAdapter(mediaList = galleryFolderList)
        binding.pictureRecycler.adapter = adapter
        binding.pictureFolderRecycler.adapter = adapter2

        requestStoragePermission()

        if (isStoragePermissionGranted()) {
            loadMedia()
            loadFolderSelector()
            loadFolderMedia()
        } else {
            requestStoragePermission()
        }
    }

    private fun loadFolderMedia() {
        galleryFolderList.addAll(MediaManager(this@MainActivity).getRecentFolders())
        Log.e("RECENT GALLERY FOLDERS",galleryFolderList.toString())
        adapter2.notifyDataSetChanged()
    }

    private fun loadFolderSelector() {
        pictureFolders.add(Folder(folderName = "All", gallery = galleryList))
        pictureFolders.addAll(MediaManager(this@MainActivity).getFolderList())

        val folders = ArrayList<String>()
        for (i in pictureFolders.indices) {
            pictureFolders[i].folderName?.let { folders.add(it) }
        }

        val seletorAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, folders)
        binding.folderSelector.adapter = seletorAdapter

        binding.folderSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (folders[position] == "All") {
                    galleryList.clear()
                    galleryList.addAll(tempgalleryList)
                    Toast.makeText(
                        this@MainActivity,
                        galleryList.size.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    adapter.notifyDataSetChanged()
                } else {
                    galleryList.clear()
                    galleryList.addAll(pictureFolders[position].gallery)
                    Log.e("ALL Photos", galleryList.toString())
                    Toast.makeText(
                        this@MainActivity,
                        galleryList.size.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadMedia() {
        galleryList.clear()
        tempgalleryList.clear()
        galleryList.addAll(MediaManager(this).getAllMedia())
        tempgalleryList.addAll(MediaManager(this).getAllMedia())
        adapter.notifyDataSetChanged()
    }

    private fun requestStoragePermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO) else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_STORAGE_PERMISSIONS)
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) checkSelfPermission(
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED else checkSelfPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Storage permission granted")
                loadMedia()
            } else {
                Toast.makeText(
                    this,
                    "You must grant storage permission to continue",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}