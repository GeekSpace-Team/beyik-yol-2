package com.android.beyikyol2.feature_car.di

import com.android.beyikyol2.feature_car.data.remote.CarApi
import com.android.beyikyol2.feature_car.data.repository.CarRepositoryImpl
import com.android.beyikyol2.feature_car.domain.repository.CarRepository
import com.android.beyikyol2.feature_car.domain.use_case.CarInfo
import com.android.beyikyol2.feature_other.data.local.GetHomeDatabase
import com.android.beyikyol2.feature_other.data.remote.OtherApi
import com.android.beyikyol2.feature_other.data.repository.GetHomeRepositoryImpl
import com.android.beyikyol2.feature_other.domain.repository.GetHomeRepository
import com.android.beyikyol2.feature_other.domain.use_case.GetHomeInfo
import com.android.beyikyol2.feature_profile.data.remote.ProfileApi
import com.android.beyikyol2.feature_profile.data.repository.ProfileRepositoryImpl
import com.android.beyikyol2.feature_profile.domain.repository.ProfileRepository
import com.android.beyikyol2.feature_profile.domain.use_case.ProfileInfo
import com.android.beyikyol2.feature_profile.domain.use_case.SendNumberUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CarModule {

    @Provides
    @Singleton
    fun provideProfileUseCase(repository: CarRepository): CarInfo {
        return CarInfo(repository)
    }

    @Provides
    @Singleton
    fun provideCarRepository(
        api: CarApi
    ): CarRepository {
        return CarRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCarApi(): CarApi {
        return Retrofit.Builder()
            .baseUrl(OtherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CarApi::class.java)
    }
}