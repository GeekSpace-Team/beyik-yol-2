package com.android.beyikyol2.feature_profile.presentation

import com.android.beyikyol2.feature_profile.data.remote.dto.CheckCodeDto
import com.android.beyikyol2.feature_profile.data.remote.dto.CheckPhoneNumberResponse
import com.android.beyikyol2.feature_profile.data.remote.dto.CheckedNumberDto
import com.android.beyikyol2.feature_profile.data.remote.dto.profile.GetProfileDto

data class ProfileState(
    val checkPhoneState: CheckPhoneNumberResponse? = null,
    var sendNumberResult: CheckedNumberDto? = null,
    var checkCodeState: CheckCodeDto? = null,
    val profileState: GetProfileDto? = null,
    val changeImageState: GetProfileDto? = null,
    val editProfileState: GetProfileDto? = null,
    val isLoading: Boolean = false
)