package com.android.beyikyol2.feature_other.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.android.beyikyol2.feature_other.data.remote.dto.*
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather
import com.android.beyikyol2.feature_other.data.util.JsonParser
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromAdsJson(json: String): List<Ad> {
        return jsonParser.fromJson<ArrayList<Ad>>(
            json,
            object : TypeToken<ArrayList<Ad>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toAdsJson(ad: List<Ad>): String{
        return jsonParser.toJson(
            ad,
            object : TypeToken<ArrayList<Ad>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromAdsImageJson(json: String): List<AdsImage> {
        return jsonParser.fromJson<ArrayList<AdsImage>>(
            json,
            object : TypeToken<ArrayList<AdsImage>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toAdsImageJson(ad: List<AdsImage>): String{
        return jsonParser.toJson(
            ad,
            object : TypeToken<ArrayList<AdsImage>>(){}.type
        ) ?: "[]"
    }
    @TypeConverter
    fun fromCarsJson(json: String?): List<Car> {
        return jsonParser.fromJson<ArrayList<Car>>(
            json?:"{}",
            object : TypeToken<ArrayList<Car>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toCarsJson(ad: List<Car>?): String{
        return jsonParser.toJson(
            ad?: emptyList(),
            object : TypeToken<ArrayList<Car>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromCostJson(json: String): List<CostChange> {
        return jsonParser.fromJson<ArrayList<CostChange>>(
            json,
            object : TypeToken<ArrayList<CostChange>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toCostJson(ad: List<CostChange>): String{
        return jsonParser.toJson(
            ad,
            object : TypeToken<ArrayList<CostChange>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromCarImageJson(json: String): List<CarImage> {
        return jsonParser.fromJson<ArrayList<CarImage>>(
            json,
            object : TypeToken<ArrayList<CarImage>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toCarImageJson(ad: List<CarImage>): String{
        return jsonParser.toJson(
            ad,
            object : TypeToken<ArrayList<CarImage>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromFuelPriceJson(json: String): List<FuelPricesItem> {
        return jsonParser.fromJson<ArrayList<FuelPricesItem>>(
            json,
            object : TypeToken<ArrayList<FuelPricesItem>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toFuelPriceJson(ad: List<FuelPricesItem>): String{
        return jsonParser.toJson(
            ad,
            object : TypeToken<ArrayList<FuelPricesItem>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromAdJson(json: String): Ad? {
        return jsonParser.fromJson<Ad>(
            json,
            object : TypeToken<Ad>(){}.type
        )
    }

    @TypeConverter
    fun toAdJson(ad: Ad): String{
        return try {
            jsonParser.toJson(
                ad,
                object : TypeToken<Ad>(){}.type
            ) ?: "{}"
        } catch (ex: Exception){
            "{}"
        }
    }


    @TypeConverter
    fun fromTTSJson(json: String): SpeechResponse? {
        return jsonParser.fromJson<SpeechResponse>(
            json,
            object : TypeToken<SpeechResponse>(){}.type
        )
    }

    @TypeConverter
    fun toTTSJson(ad: SpeechResponse): String{
        return try {
            jsonParser.toJson(
                ad,
                object : TypeToken<SpeechResponse>(){}.type
            ) ?: "{}"
        } catch (ex: Exception){
            "{}"
        }
    }

    @TypeConverter
    fun fromWeatherJson(json: String): GetWeather? {
        return jsonParser.fromJson<GetWeather>(
            json,
            object : TypeToken<GetWeather>(){}.type
        )
    }

    @TypeConverter
    fun toWeatherJson(ad: GetWeather): String{
        return try {
            jsonParser.toJson(
                ad,
                object : TypeToken<GetWeather>(){}.type
            ) ?: "{}"
        } catch (ex: Exception){
            "{}"
        }
    }


    @TypeConverter
    fun fromUserJson(json: String): User? {
        return jsonParser.fromJson<User>(
            json,
            object : TypeToken<User>(){}.type
        )
    }

    @TypeConverter
    fun toUserJson(ad: User?): String{
        return try {
            jsonParser.toJson(
                ad,
                object : TypeToken<User>(){}.type
            ) ?: "{}"
        } catch (ex: java.lang.Exception){
            "{}"
        }
    }
}