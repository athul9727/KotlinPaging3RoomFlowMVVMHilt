package com.example.picsapp.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.picsapp.repository.db.ImagesDao
import com.example.picsapp.repository.model.ImageListResponseItem
import com.example.picsapp.util.AppResult
import com.example.picsapp.util.NetworkManager.isOnline
import com.example.picsapp.util.Utils.handleApiError
import com.example.picsapp.util.Utils.handleSuccess
import com.example.picsapp.util.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface  Repository {

    suspend fun getalldatafromapi() : AppResult<List<ImageListResponseItem>>

    fun getAll(): Flow<PagingData<ImageListResponseItem>>

    suspend fun deleteitem(model: ImageListResponseItem)
}

class RepositoryImpl(
    private val api: Api,
    private val context: Context,
    private val imgdao: ImagesDao
) :

    Repository {

    companion object {
        const val PAGE_SIZE = 30
    }

    override suspend fun getalldatafromapi(): AppResult<List<ImageListResponseItem>> {
        if (isOnline(context)) {
            return try {
                val response = api.getAlldata()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.IO) { imgdao.addall(it) }
                    }
                    return handleSuccess(response)

                } else {
                    handleApiError(response)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {
            return context.noNetworkConnectivityError()

        }
    }





    override fun getAll(): Flow<PagingData<ImageListResponseItem>> {
        return Pager(PagingConfig(PAGE_SIZE)) {
            imgdao.getAll()
        }.flow
    }

    override suspend fun deleteitem(model: ImageListResponseItem) {
        withContext(Dispatchers.IO) {
            imgdao.delete(model)
        }
    }

}



//Network
interface Api {
    @GET("/v2/list?page=1&limit=400")
    suspend fun getAlldata(): Response<List<ImageListResponseItem>>
}