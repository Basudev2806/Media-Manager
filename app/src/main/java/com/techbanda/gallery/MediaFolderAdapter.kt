package com.techbanda.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.basudev.mediamanager.Recent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.techbanda.gallery.databinding.ItemFolderImageBinding
import com.techbanda.gallery.databinding.ItemFolderViewBinding
import com.techbanda.gallery.databinding.ItemImageBinding
import com.techbanda.gallery.databinding.ItemVideoBinding

class MediaFolderAdapter(private val mediaList: List<Recent>) :
    RecyclerView.Adapter<MediaFolderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaItem = mediaList[position]
        Glide.with(holder.itemView.context)
            .load(mediaItem.lastUpdatedImage)
            .into(holder.binding.picture)
        holder.binding.duration.isVisible = mediaItem.duration> 0
        holder.binding.duration.text = formatDuration(mediaItem.duration)
        holder.binding.folderName.text = mediaItem.folderName
        holder.binding.fileCount.text = mediaItem.totalImageCount.toString()
    }

    override fun getItemCount(): Int = mediaList.size

    inner class ViewHolder(val binding: ItemFolderViewBinding) : RecyclerView.ViewHolder(binding.root)

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