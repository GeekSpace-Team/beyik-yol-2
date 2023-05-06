package com.android.beyikyol2.feature_profile.domain.use_case

import android.util.Log
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_profile.data.remote.dto.CheckedNumberDto
import com.android.beyikyol2.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class SendNumberUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(phone: String): Flow<Resource<CheckedNumberDto>> {
        Log.e("SendNumberUseCase",phone)
        return repository.sendNumber(phone)
    }
}