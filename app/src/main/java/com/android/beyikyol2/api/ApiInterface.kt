package com.android.beyikyol2.api

import com.android.beyikyol2.feature_profile.data.remote.dto.CheckPhoneNumberResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @POST("/mobile-auth/check-phone-number/{phone}")
    suspend fun checkPhoneNumber(@Path("phone") phone: String): CheckPhoneNumberResponse
}