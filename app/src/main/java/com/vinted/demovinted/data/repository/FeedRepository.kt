package com.vinted.demovinted.data.repository

import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.data.models.ItemSeenEvent
import com.vinted.demovinted.data.network.api.Api
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FeedRepository constructor(private val feedApi: Api) {
    fun getAllItems(page: Int, search: String): Single<List<CatalogItem>> {
        return feedApi.getItemsFeed(mapOf("page" to page.toString(), "search_text" to search))
            .subscribeOn(Schedulers.io())
            .map { it.items }
    }
    fun sendEvent(events: List<ItemSeenEvent>): Completable {
        return feedApi.sendEvent(events)
            .subscribeOn(Schedulers.io())
    }

}