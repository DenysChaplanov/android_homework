// a way to interact with a remote server
// to receive data about a item catalog
// and send browsing events using Retrofit
package com.vinted.demovinted.data.network.api

import com.vinted.demovinted.data.models.ItemSeenEvent
import com.vinted.demovinted.data.network.responses.CatalogItemListResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface Api {

    @GET("items")
    fun getItemsFeed(
        @QueryMap params: Map<String, String>
    ): Single<CatalogItemListResponse>

    @POST("impressions")
    fun sendEvent(
        @Body events: List<ItemSeenEvent>,
    ): Completable
}
