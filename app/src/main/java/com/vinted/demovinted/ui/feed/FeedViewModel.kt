//business logic and data for the FeedFragment
package com.vinted.demovinted.ui.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.data.models.ItemSeenEvent
import com.vinted.demovinted.data.repository.FeedRepository
import com.vinted.demovinted.di.DataModule
import com.vinted.demovinted.di.NetworkingModule
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class FeedViewModel : ViewModel() {
    private val feedRepository: FeedRepository //instance (for loading items and sending events)
    private val feedLiveData = MutableLiveData<List<CatalogItem>>() //list of items
    val feedData: LiveData<List<CatalogItem>> = feedLiveData //access feedLiveData from outside the class

    private val loadingState = MutableLiveData<Boolean>() //current LiveData loading status
    private var currentPage = 0
    private val disposable = CompositeDisposable() //to manage asynchronous operations and prevent memory leaks
    private val querySubject = BehaviorSubject.createDefault("") //search query tracking (empty by default)
    init {
        val moshi = DataModule().providesMoshi()
        feedRepository = FeedRepository(NetworkingModule().providesApi(moshi))
        loadInitialItems()
        onSearchItem()
    }

    //to load start page
    fun loadInitialItems() {
        loadingState.value = true
        currentPage = 0
        disposable.add(feedRepository.getAllItems(currentPage, "")
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

    //to load additional product pages when scrolling
    fun loadMoreItems() {
        if (loadingState.value == true) return //to avoid concurrent requests
        loadingState.value = true
        currentPage++
        disposable.add(feedRepository.getAllItems(currentPage, querySubject.value.orEmpty())
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

    fun onSearch(search: String){
        querySubject.onNext(search) //notifications about a new search request
    }

    //processes search text changes and sends appropriate data load requests
    private fun onSearchItem() {
        querySubject
            //remove extra spaces from beginning and end
            .map { it.trim() }
            //remove duplicate values
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .debounce(500, TimeUnit.MILLISECONDS)
            //request to server with new search text
            .switchMapSingle {
                currentPage = 0
                Log.d("NewTextText", it)
                feedRepository.getAllItems(currentPage, it)
            }
            .subscribe(
                {
                    feedLiveData.postValue(it)
                    loadingState.postValue(false)
                },
                {
                    Log.e("Test", it.toString())
                    loadingState.postValue(false)
                }
            )
            //destroying subscriptions to a data thread
            .also { disposable.add(it) }
    }

    //when the ViewModel is destroyed, resources are released
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    //to send an event about viewing a product
    fun sendItemViewEvent(item: CatalogItem) {
        val itemViewEvent = ItemSeenEvent(System.currentTimeMillis(), item.id)
        disposable.add(feedRepository.sendEvent(listOf(itemViewEvent))
            .subscribe(
                {
                    Log.d("sendItem", "Item ${item.id} view event sent successfully")
                },
                {
                    Log.e("FeedViewModel", "Failed to send item view event", it)
                }
            ))
    }
}
