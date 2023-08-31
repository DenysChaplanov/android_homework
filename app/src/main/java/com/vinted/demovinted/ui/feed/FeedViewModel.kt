package com.vinted.demovinted.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.data.repository.FeedRepository
import com.vinted.demovinted.di.DataModule
import com.vinted.demovinted.di.NetworkingModule
import io.reactivex.disposables.CompositeDisposable

class FeedViewModel : ViewModel() {
    private val feedRepository: FeedRepository
    private val feedLiveData = MutableLiveData<List<CatalogItem>>()
    val feedData: LiveData<List<CatalogItem>> = feedLiveData

    private val loadingState = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = loadingState

    private var currentPage = 0

    private val disposable = CompositeDisposable()

    init {
        val moshi = DataModule().providesMoshi()
        feedRepository = FeedRepository(NetworkingModule().providesApi(moshi))
    }

    fun loadInitialItems() {
        loadingState.value = true
        currentPage = 0
        disposable.add(feedRepository.getAllItems(currentPage)
            .subscribe(
                {
                    feedLiveData.postValue(it)
                    loadingState.postValue(false)
                },
                {
                    Log.e("Test", it.toString())
                    loadingState.postValue(false)
                }
            ))
    }

    fun loadMoreItems() {
        if (loadingState.value == true) return

        loadingState.value = true
        currentPage++
        disposable.add(feedRepository.getAllItems(currentPage)
            .subscribe(
                { items ->
                    val currentItems = feedLiveData.value.orEmpty().toMutableList()
                    currentItems.addAll(items)
                    feedLiveData.postValue(currentItems)
                    loadingState.postValue(false)
                },
                {
                    Log.e("Test", it.toString())
                    loadingState.postValue(false)
                }
            ))
    }
}
