// a customized Retrofit that can be used to make HTTP requests
// to the server and process JSON data using the Moshi library
// RxJava2 for working with reactive threads
package com.vinted.demovinted.di

import com.squareup.moshi.Moshi
import com.vinted.demovinted.data.network.api.Api
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class NetworkingModule {
    fun providesApi(
        moshi: Moshi
    ): Api {
        //OkHttp built in retrofit
        return Retrofit.Builder()
            .baseUrl("http://mobile-homework-api.vinted.net/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(Api::class.java)
    }
}