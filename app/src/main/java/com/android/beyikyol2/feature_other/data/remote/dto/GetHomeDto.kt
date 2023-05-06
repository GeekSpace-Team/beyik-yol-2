package com.android.beyikyol2.feature_other.data.remote.dto

import com.android.beyikyol2.feature_other.data.local.entity.GetHomeEntity
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather

data class GetHomeDto(
    val ads: List<Ad>?,
    val banner: List<Ad>?,
    val cars: List<Car>?,
    val fuel_price: List<FuelPricesItem>?,
    val inboxCount: Int?,
    val popup: Ad?,
    val user: User?,
    val tts: SpeechResponse?,
    val weatherInfo: GetWeather?
) {
    fun toGetHomeEntity(): GetHomeEntity {
        return GetHomeEntity(
            ads, banner, cars, fuel_price, inboxCount, popup, user,tts,weatherInfo
        )
    }
}