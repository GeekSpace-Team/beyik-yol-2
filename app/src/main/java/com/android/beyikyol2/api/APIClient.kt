package com.android.beyikyol2.api

import com.android.beyikyol2.feature_other.data.remote.OtherApi.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    fun create(): Retrofit {
        // on below line we are creating a retrofit
        // builder and passing our base url
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create())
            // at last we are building our retrofit builder.
            .build()
    }
}