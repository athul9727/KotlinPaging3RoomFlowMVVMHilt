package com.example.picsapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.picsapp.repository.Repository
import com.example.picsapp.repository.db.ImagesDao
import com.example.picsapp.repository.model.ImageListResponseItem
import com.example.picsapp.util.AppResult
import com.example.picsapp.util.ConnectivityLiveData
import com.example.picsapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository,
    private val application: Application) : ViewModel() {

    val showLoading = ObservableBoolean()
    val imageList = MutableLiveData<List<ImageListResponseItem>>()
    val showError = SingleLiveEvent<String>()
    val connectivityLiveData = ConnectivityLiveData(application)

    private val statusmessage = SingleLiveEvent<String>()
    val message: SingleLiveEvent<String>
        get() {  return statusmessage }


    fun getalldatafromapi() {
        showLoading.set(true)
        viewModelScope.launch {

            val result =  repository.getalldatafromapi()
            showLoading.set(false)

            when (result) {
                is AppResult.Success -> {
                     imageList.value = result.successData
                     showError.value = null

                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }

}