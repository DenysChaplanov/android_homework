package com.vinted.demovinted.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.data.repository.FeedRepository
import com.vinted.demovinted.di.DataModule
import com.vinted.demovinted.di.NetworkingModule

class FeedViewModel constructor(): ViewModel() {
    private val feedRepository: FeedRepository
    private val feedLiveData = MutableLiveData<List<CatalogItem>>()
    val feedData: LiveData<List<CatalogItem>>get() = feedLiveData
    init {
        val moshi = DataModule().providesMoshi()
        feedRepository = FeedRepository(NetworkingModule().providesApi(moshi))
        feedRepository.getAllItems()
            .subscribe(
                {
                    Log.d("Test", it.toString())
                    feedLiveData.postValue(it)
                },
                {
                    Log.e("Test", it.toString())
                }
            )

    }
    fun Test() {

    }
}