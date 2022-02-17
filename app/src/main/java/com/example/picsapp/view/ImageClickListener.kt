package com.starapps.kotlinmvvm.view

import com.example.picsapp.repository.model.ImageListResponseItem


interface ImageClickListener {
    fun onItemClick(imagedata : ImageListResponseItem)
}