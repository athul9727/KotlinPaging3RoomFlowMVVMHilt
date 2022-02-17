package com.example.picsapp.repository.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.picsapp.repository.model.ImageListResponseItem

@Dao
interface ImagesDao {



//    @Query("SELECT * FROM ImagesTable")
//    suspend fun getlist(): List<ImageListResponseItem>

    @Query("SELECT * FROM ImagesTable")
    fun getAll(): PagingSource<Int, ImageListResponseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addall(users: List<ImageListResponseItem>)

    @Delete
    suspend fun delete(model: ImageListResponseItem)

}