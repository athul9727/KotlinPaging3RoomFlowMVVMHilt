package com.starapps.kotlinmvvm.view

import android.R.attr.data
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.picsapp.R
import com.example.picsapp.databinding.ListItemBinding
import com.example.picsapp.repository.model.ImageListResponseItem


class RecAdapter(
    val context: Context?,
    val clickListener: ImageClickListener
    ) :

    PagingDataAdapter<ImageListResponseItem,RecAdapter.ImageViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val mDataBinding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item, parent, false
        )
        return ImageViewHolder(mDataBinding)
    }




    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ImageViewHolder(private val mDataBinding: ListItemBinding) :
        RecyclerView.ViewHolder(mDataBinding.root) {

        fun onBind(position: Int) {
            val row = getItem(position)
            mDataBinding.imagedata = row
            mDataBinding.imageClickInterface = clickListener
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageListResponseItem>() {
        override fun areItemsTheSame(oldItem: ImageListResponseItem, newItem: ImageListResponseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageListResponseItem, newItem: ImageListResponseItem): Boolean {
            return oldItem == newItem
        }
    }

    fun getData(pos:Int): ImageListResponseItem? {
        return getItem(pos)
    }

}
