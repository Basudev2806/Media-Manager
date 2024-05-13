package com.techbanda.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basudev.mediamanager.Gallery
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.techbanda.gallery.databinding.ItemImageBinding
import com.techbanda.gallery.databinding.ItemVideoBinding

class MediaAdapter(private val mediaList: List<Gallery>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_IMAGE = 1
        private const val VIEW_TYPE_VIDEO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(binding)
            }
            VIEW_TYPE_VIDEO -> {
                val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VideoViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaItem = mediaList[position]

        when (holder) {
            is ImageViewHolder -> {
                Glide.with(holder.itemView.context)
                    .load(mediaItem.fileUri)
                    .into(holder.binding.picture)
            }
            is VideoViewHolder -> {
                Glide.with(holder.itemView.context)
                    .load(mediaItem.fileUri)
                    .into(holder.binding.picture)
                holder.binding.duration.text = formatDuration(mediaItem.fileVideoDuration)
            }
        }
    }

    override fun getItemCount(): Int = mediaList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            mediaList[position].fileVideoDuration > 0 -> VIEW_TYPE_VIDEO
            else -> VIEW_TYPE_IMAGE
        }
    }

    inner class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    inner class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    fun formatDuration(milliseconds: Long): String {
        val hours = milliseconds / (1000 * 60 * 60)
        val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60)) / 1000

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}