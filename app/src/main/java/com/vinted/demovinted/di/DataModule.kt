// data conversion with Moshi
// simplifies working with data in JSON format
package com.vinted.demovinted.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vinted.demovinted.data.network.interceptor.BigDecimalAdapter


class DataModule {

    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(BigDecimalAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}