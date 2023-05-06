package com.android.beyikyol2.feature_profile.data.remote

import com.android.beyikyol2.feature_profile.data.remote.dto.*
import com.android.beyikyol2.feature_profile.data.remote.dto.profile.GetProfileDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApi {

    @POST("/mobile-auth/send-number")
    suspend fun sendNumber(
        @Body body: SendNumber
    ) : CheckedNumberDto

    @POST("/mobile-auth/check-phone-number/{phone}")
    suspend fun checkPhoneNumber(@Path("phone") phone: String): CheckPhoneNumberResponse?

    @POST("/mobile-auth/check-code")
    suspend fun checkCode(
        @Body body: CheckCode
    ) : CheckCodeDto?

    @GET("/mobile-auth/get-profile")
    suspend fun getProfile(@Header("Authorization") token: String): GetProfileDto?

    @Multipart
    @PUT("/mobile-auth/change-image")
    suspend fun changeImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): GetProfileDto?

    @PUT("/mobile-auth/edit-profile")
    suspend fun editProfile(
        @Body body: EditProfile,
        @Header("Authorization") token: String
    ) : GetProfileDto?

    @POST("/mobile-auth/save-fcm-token")
    suspend fun saveFcmToken(@Header("Authorization") token: String,@Body body: SaveFcmTokenDto)
}