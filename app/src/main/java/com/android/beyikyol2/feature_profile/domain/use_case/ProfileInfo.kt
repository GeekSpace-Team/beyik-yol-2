package com.android.beyikyol2.feature_profile.domain.use_case

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_profile.data.remote.dto.*
import com.android.beyikyol2.feature_profile.data.remote.dto.profile.GetProfileDto
import com.android.beyikyol2.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class ProfileInfo(
    private val repository: ProfileRepository
) {
    operator fun invoke(phone: String): Flow<Resource<CheckPhoneNumberResponse>>{
        return repository.checkPhoneNumber(phone)
    }
    fun checkCode(phone: String,uuid: String): Flow<Resource<CheckCodeDto>>{
        return repository.checkCode(phone,uuid)
    }
    fun getProfile(token: String): Flow<Resource<GetProfileDto>>{
        return repository.getProfile(token)
    }
    fun changeImage(file: File, token: String): Flow<Resource<GetProfileDto>>{
        return repository.changeImage(file,token)
    }
    fun editProfile(body: EditProfile, token: String): Flow<Resource<GetProfileDto>>{
        return repository.editProfile(body,token)
    }

    fun saveFcmToken(token: String,body: SaveFcmTokenDto): Flow<Resource<String>>{
        return repository.saveFcmToken(token,body)
    }
}