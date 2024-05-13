package com.basudev.mediamanager

data class Gallery(
    var fileName: String? = null,
    var filePath: String? = null,
    var fileSize: Long? = null,
    var fileUri: String? = null,
    var fileVideoDuration: Long = 0, // for video only
    var date: Long? = null,
    var fileId: Int = 0,
    var isSelected: Boolean = false,
    val type: String,
    var count: Int?,
    var isCheckOn: Boolean = false
)