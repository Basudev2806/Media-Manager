package com.basudev.mediamanager

data class Recent(
    val folderName: String,
    val lastUpdatedImage: String,
    val totalImageCount: Int,
    val duration : Long = 0,
    var gallery: ArrayList<Gallery>
)