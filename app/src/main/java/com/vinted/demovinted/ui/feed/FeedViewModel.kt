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
    private val feedRepository: FeedRepository
    private val feedLiveData = MutableLiveData<List<CatalogItem>>()
    val feedData: LiveData<List<CatalogItem>> = feedLiveData

    private val loadingState = MutableLiveData<Boolean>()
    private var currentPage = 0
    private val disposable = CompositeDisposable()
    private val querySubject = BehaviorSubject.createDefault("")
    init {
        val moshi = DataModule().providesMoshi()
        feedRepository = FeedRepository(NetworkingModule().providesApi(moshi))
        loadInitialItems()
        onSearchItem()
    }

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

    fun loadMoreItems() {
        if (loadingState.value == true) return
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
        querySubject.onNext(search)
    }

    private fun onSearchItem() {
        querySubject
            .map { it.trim() }
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .debounce(500, TimeUnit.MILLISECONDS)
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
            .also { disposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
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
