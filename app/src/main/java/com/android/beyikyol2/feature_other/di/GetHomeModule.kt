package com.android.beyikyol2.feature_other.di

import android.app.Application
import androidx.room.Room
import com.android.beyikyol2.feature_other.data.local.Converters
import com.android.beyikyol2.feature_other.data.local.GetHomeDao
import com.android.beyikyol2.feature_other.data.local.GetHomeDatabase
import com.android.beyikyol2.feature_other.data.remote.OtherApi
import com.android.beyikyol2.feature_other.data.remote.dto.GetHomeDto
import com.android.beyikyol2.feature_other.data.repository.GetHomeRepositoryImpl
import com.android.beyikyol2.feature_other.data.util.GsonParser
import com.android.beyikyol2.feature_other.domain.repository.GetHomeRepository
import com.android.beyikyol2.feature_other.domain.use_case.GetHomeInfo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetHomeModule {

    @Provides
    @Singleton
    fun provideGetHomeUseCase(repository: GetHomeRepository): GetHomeInfo {
        return GetHomeInfo(repository)
    }

    @Provides
    @Singleton
    fun provideGetHomeRepository(
        db: GetHomeDatabase,
        api: OtherApi
    ): GetHomeRepository {
        return GetHomeRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideGetHomeDatabase(app: Application): GetHomeDatabase {
        return Room.databaseBuilder(
            app, GetHomeDatabase::class.java, "home_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideOtherApi(): OtherApi {
        return Retrofit.Builder()
            .baseUrl(OtherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OtherApi::class.java)
    }

}