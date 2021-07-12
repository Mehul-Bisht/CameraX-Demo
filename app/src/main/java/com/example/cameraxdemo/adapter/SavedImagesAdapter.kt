package com.example.cameraxdemo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cameraxdemo.databinding.SavedImagesListItemBinding

class SavedImagesAdapter : RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>() {

    private var onItemClickListener: ((Uri) -> Unit)? = null
    private var onItemLongClickListener: ((Uri) -> Unit)? = null

    fun setOnItemClickListener(listener: (Uri) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (Uri) -> Unit) {
        onItemLongClickListener = listener
    }

    inner class SavedImagesViewHolder(val binding: SavedImagesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItemUri: Uri) {
            Glide.with(binding.root)
                .load(currentItemUri)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivSavedImage)
        }
    }

    val diffCallBack = object : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<Uri>) {

        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {
        val binding = SavedImagesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) {
        val currentItemUri = differ.currentList[position]
        holder.bind(currentItemUri)

        holder.binding.root.setOnClickListener {
            onItemClickListener?.let {
                it(currentItemUri)
            }
        }

        holder.binding.root.setOnLongClickListener {
            onItemLongClickListener?.let {
                it(currentItemUri)
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}