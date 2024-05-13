package com.basudev.mediamanager

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

class MediaManager(private val context: Context) {

    @SuppressLint("Range")
    fun getRecentFolders(): ArrayList<Recent> {
        val recentFolders = ArrayList<Recent>()
        val allMedia = getAllMedia()
        val mediaMap = allMedia.groupBy { getParentDirectoryName(it.filePath) }
        var totalAllImageCount = 0
        var duration: Long = 0
        var firstUpdatedImageAll: String? = null
        var firstUpdatedDurationAll: Long = 0

        for ((folderName, mediaList) in mediaMap) {
            val lastUpdatedMedia = mediaList.maxByOrNull { it.date!! }
            val lastUpdatedImage = lastUpdatedMedia?.filePath ?: ""
            val totalImageCount = mediaList.size
            duration = lastUpdatedMedia?.fileVideoDuration ?: 0
            totalAllImageCount += totalImageCount

            if (firstUpdatedImageAll == null) {
                val firstImage = mediaList.firstOrNull { it.filePath != null }
                if (firstImage != null) {
                    firstUpdatedImageAll = firstImage.filePath
                    firstUpdatedDurationAll = firstImage.fileVideoDuration
                }
            }

            recentFolders.add(
                Recent(
                    folderName ?: "",
                    lastUpdatedImage,
                    totalImageCount,
                    duration,
                    ArrayList(allMedia)
                )
            )
        }

        recentFolders.add(
            0,
            Recent(
                "Recents",
                firstUpdatedImageAll ?: "",
                totalAllImageCount,
                firstUpdatedDurationAll,
                getAllMedia()
            )
        )
        recentFolders.add(
            1,
            Recent(
                "Photos",
                getLastUpdatedImage(getAllPictureContent()) ?: "",
                getAllPictureContent().size,
                0,
                getAllPictureContent()
            )
        )
        recentFolders.add(
            2,
            Recent(
                "Videos",
                getLastUpdatedVideo(getAllVideoContent()) ?: "",
                getAllVideoContent().size,
                duration,
                getAllVideoContent()
            )
        )

        return recentFolders
    }

    private fun getLastUpdatedImage(allPictureContent: ArrayList<Gallery>): String? {
        val lastUpdatedMedia = allPictureContent.maxByOrNull { it.date!! }
        return lastUpdatedMedia?.filePath
    }

    private fun getLastUpdatedVideo(allVideoContent: ArrayList<Gallery>): String? {
        val lastUpdatedMedia = allVideoContent.maxByOrNull { it.date!! }
        return lastUpdatedMedia?.filePath
    }

    private fun getLastUpdatedAudio(allAudioContent: ArrayList<Audio>): String? {
        val lastUpdatedMedia = allAudioContent.maxByOrNull { it.date!! }
        return lastUpdatedMedia?.filePath
    }

    fun getFolderList(): ArrayList<Folder> {
        val folders = ArrayList<Folder>()
        val allMedia = getAllMedia()
        val mediaMap = allMedia.groupBy { getParentDirectoryName(it.filePath) }
        for ((folderName, mediaList) in mediaMap) {
            folders.add(Folder(folderName, ArrayList(mediaList)))
        }
        return folders
    }

    private fun getParentDirectoryName(filePath: String?): String? {
        return filePath?.substringBeforeLast('/')?.substringAfterLast('/')
    }

    fun getAllMedia(): ArrayList<Gallery> {
        val allMedia = ArrayList<Gallery>()
        allMedia.addAll(getAllVideoContent())
        allMedia.addAll(getAllPictureContent())
        allMedia.sortByDescending { it.date }

        return allMedia
    }

    @SuppressLint("Range")
    private fun getAllVideoContent(): ArrayList<Gallery> {
        val videos = ArrayList<Gallery>()

        val videoCursor: Cursor? = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            Projections.VIDEO_PROJECTION,
            null,
            null,
            null
        )

        videoCursor?.use {
            while (it.moveToNext()) {
                val video = Gallery(
                    fileName = it.getString(it.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)),
                    filePath = it.getString(it.getColumnIndex(MediaStore.Video.Media.DATA)),
                    fileSize = it.getLong(it.getColumnIndex(MediaStore.Video.Media.SIZE)),
                    fileUri = it.getString(it.getColumnIndex(MediaStore.Video.Media.DATA)),
                    fileVideoDuration = it.getLong(it.getColumnIndex(MediaStore.Video.Media.DURATION)),
                    date = it.getLong(it.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)),
                    fileId = it.getInt(it.getColumnIndex(MediaStore.Video.Media._ID)),
                    type = "video",
                    count = 0
                )
                videos.add(video)
            }
        }

        return videos
    }

    @SuppressLint("Range")
    fun getAllPictureContent(): ArrayList<Gallery> {
        val photos = ArrayList<Gallery>()

        val pictureCursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            Projections.PICTURE_PROJECTION,
            null,
            null,
            null
        )

        pictureCursor?.use {
            while (it.moveToNext()) {
                val photo = Gallery(
                    fileName = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)),
                    filePath = it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA)),
                    fileSize = it.getLong(it.getColumnIndex(MediaStore.Images.Media.SIZE)),
                    fileUri = it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA)),
                    date = it.getLong(it.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)),
                    fileId = it.getInt(it.getColumnIndex(MediaStore.Images.Media._ID)),
                    type = "photo",
                    count = 0
                )
                photos.add(photo)
            }
        }

        return photos
    }

    @SuppressLint("Range")
    fun getAllAudioContent(): ArrayList<Audio> {
        val audioList = ArrayList<Audio>()

        val audioCursor: Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            Projections.AUDIO_PROJECTION,
            null,
            null,
            null
        )

        audioCursor?.use {
            while (it.moveToNext()) {
                val audio = Audio(
                    fileName = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                    filePath = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    fileSize = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.SIZE)),
                    fileUri = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    date = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.DATE_TAKEN)),
                    fileId = it.getInt(it.getColumnIndex(MediaStore.Audio.Media._ID)),
                    type = "audio"
                )
                audioList.add(audio)
            }
        }

        return audioList
    }

    private object Projections {
        val VIDEO_PROJECTION = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media._ID
        )

        val PICTURE_PROJECTION = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )

        val AUDIO_PROJECTION = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.BUCKET_ID,
        )
    }
}
