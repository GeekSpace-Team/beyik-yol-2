package com.android.beyikyol2.feature_profile.domain.repository

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_profile.data.remote.dto.*
import com.android.beyikyol2.feature_profile.data.remote.dto.profile.GetProfileDto
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProfileRepository {
    fun checkPhoneNumber(phone: String): Flow<Resource<CheckPhoneNumberResponse>>
    fun sendNumber(phone: String): Flow<Resource<CheckedNumberDto>>
    fun checkCode(phone: String,uuid: String): Flow<Resource<CheckCodeDto>>
    fun getProfile(token: String): Flow<Resource<GetProfileDto>>
    fun changeImage(file: File, token: String): Flow<Resource<GetProfileDto>>
    fun saveFcmToken(token: String,body: SaveFcmTokenDto): Flow<Resource<String>>
    fun editProfile(body: EditProfile, token: String): Flow<Resource<GetProfileDto>>
}