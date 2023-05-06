package com.android.beyikyol2.feature_other.data.remote

import com.android.beyikyol2.feature_other.data.remote.dto.GetConstantDto
import com.android.beyikyol2.feature_other.data.remote.dto.GetHomeDto
import com.android.beyikyol2.feature_other.data.remote.dto.evacuator.GetEvacuatorDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDtoItem
import com.android.beyikyol2.feature_other.data.remote.dto.map.SearchFromMap
import com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete.GetAutoComplete
import com.android.beyikyol2.feature_other.data.remote.dto.map.direction.GetDirection
import com.android.beyikyol2.feature_other.data.remote.dto.speech.SpeechRequest
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.Objects

interface OtherApi {
    @GET("/other/get-home?")
    suspend fun getHome(@Header("Authorization") token: String,@Query("isSend") isSend: Boolean): GetHomeDto

    @GET("/inbox/get-user-inbox")
    suspend fun getUserInbox(@Header("Authorization") token: String): GetUserInboxDto?

    @DELETE("/inbox/delete-inbox/{id}")
    suspend fun deleteInbox(@Header("Authorization") token: String,@Path("id") id: String): GetUserInboxDtoItem?

    @GET("/evacuator/get-all-evacuators-mobile?")
    suspend fun getEvacuators(@Query("region") region: String): GetEvacuatorDto?

    @GET("constant/get-constant-by-type?")
    suspend fun getConstantPage(@Query("type") type: String): GetConstantDto?

    @GET("https://maps.googleapis.com/maps/api/place/queryautocomplete/json?key=AIzaSyDJy-_ydiaAH6z2A0exJETzhKDlUhX7vyE")
    suspend fun autoCompleteMap(@Query("input") input: String): GetAutoComplete?

    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json?key=AIzaSyDJy-_ydiaAH6z2A0exJETzhKDlUhX7vyE")
    suspend fun searchFromMap(@Query("query") query: String): SearchFromMap

    @POST("https://texttospeech.googleapis.com/v1beta1/text:synthesize?key=AIzaSyDJy-_ydiaAH6z2A0exJETzhKDlUhX7vyE")
    suspend fun textToSpeech(@Body body: SpeechRequest): SpeechResponse?

    @GET("https://api.openweathermap.org/data/2.5/weather?q=Ashgabat&appid=a2fe4fb63c29aa32f8e3c254e9cbde16&units=metric")
    suspend fun getWeather(): GetWeather?

    @GET("https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyDJy-_ydiaAH6z2A0exJETzhKDlUhX7vyE")
    suspend fun getDirection(@QueryMap params: HashMap<String, Any>): GetDirection?

    companion object {
        const val BASE_URL = "https://beyikyol.com/"
    }
}