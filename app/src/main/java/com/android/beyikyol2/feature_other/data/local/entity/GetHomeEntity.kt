package com.android.beyikyol2.feature_other.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.beyikyol2.feature_other.data.remote.dto.*
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather

@Entity
data class GetHomeEntity(
    val ads: List<Ad>?,
    val banner: List<Ad>?,
    val cars: List<Car>?,
    val fuel_price: List<FuelPricesItem>?,
    val inboxCount: Int?,
    val popup: Ad? = null,
    val user: User? = null,
    val tts: SpeechResponse?,
    val weatherInfo: GetWeather?,
    @PrimaryKey val id: Int? = null
) {
    fun toHome(): GetHomeDto {
        return GetHomeDto(
            ads, banner, cars, fuel_price, inboxCount, popup, user,tts,weatherInfo
        )
    }
}
