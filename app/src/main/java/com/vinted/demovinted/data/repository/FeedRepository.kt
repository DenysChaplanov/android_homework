package com.vinted.demovinted.data.repository

import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.data.network.api.Api
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FeedRepository constructor(private val feedApi: Api) {
    fun getAllItems(): Single<List<CatalogItem>>{
        return feedApi.getItemsFeed(mapOf("page" to "0"))
            .subscribeOn(Schedulers.io())
            .map { it.items }
    }
}