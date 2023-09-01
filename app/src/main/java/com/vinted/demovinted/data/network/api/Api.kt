package com.vinted.demovinted.data.network.api

import com.vinted.demovinted.data.network.responses.CatalogItemListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    @GET("items")
    fun getItemsFeed(
        @QueryMap params: Map<String, String>
    ): Single<CatalogItemListResponse>

    @GET("items")
    fun getItemsByBrand(
        @Query("brand") brand: Map<String, String>
    ): Single<CatalogItemListResponse>
}

