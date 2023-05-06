package com.android.beyikyol2.feature_profile.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_profile.data.remote.dto.CheckCodeDto
import com.android.beyikyol2.feature_profile.data.remote.dto.CheckedNumberDto
import com.android.beyikyol2.feature_profile.data.remote.dto.EditProfile
import com.android.beyikyol2.feature_profile.data.remote.dto.SaveFcmTokenDto
import com.android.beyikyol2.feature_profile.data.remote.dto.profile.GetProfileDto
import com.android.beyikyol2.feature_profile.domain.use_case.ProfileInfo
import com.android.beyikyol2.feature_profile.domain.use_case.SendNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInfo: ProfileInfo,
    private val sendNumberUseCase: SendNumberUseCase
) : ViewModel() {
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state
    private var job: Job? = null

    fun checkPhoneNumber(phone: String) {
        job?.cancel()
        job = viewModelScope.launch {
            profileInfo(phone)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                checkPhoneState = result.data,
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                checkPhoneState = result.data,
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                checkPhoneState = result.data,
                                isLoading = true
                            )
                        }
                        else -> {}
                    }
                }
        }
    }

    fun sendPost(
        number: String,
        onSuccess: (CheckedNumberDto?) -> Unit,
        onError: (Int) -> Unit
    ) {
        Log.e("Phone", number)
        job?.cancel()
        job = viewModelScope.launch {
            sendNumberUseCase(phone = number)
                .onEach { result ->
                    Log.e("result", result.message.toString())
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                sendNumberResult = result.data,
                                isLoading = false
                            )
                            onSuccess(result.data)
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                sendNumberResult = result.data,
                                isLoading = false
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                sendNumberResult = result.data,
                                isLoading = true
                            )
                        }
                        else -> {}
                    }
                }.launchIn(this)
        }


    }

    fun checkCode(
        number: String,
        uuid: String,
        onSuccess: (CheckCodeDto?) -> Unit,
        onError: (Int) -> Unit
    ) {
        Log.e("Phone", number)
        job?.cancel()
        job = viewModelScope.launch {
            profileInfo.checkCode(phone = number, uuid)
                .onEach { result ->
                    Log.e("result", result.message.toString())
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                checkCodeState = result.data,
                                isLoading = false
                            )
                            onSuccess(result.data)
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                checkCodeState = result.data,
                                isLoading = false
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                checkCodeState = result.data,
                                isLoading = true
                            )
                        }
                        else -> {}
                    }
                }.launchIn(this)
        }
    }

    fun getProfile(
        token: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            profileInfo.getProfile(token)
                .onEach { result ->
                    when (result) {

                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                profileState = result.data,
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                profileState = result.data,
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                profileState = result.data,
                                isLoading = true
                            )
                        }
                        else -> {}
                    }
                }.launchIn(this)
        }
    }

    fun changeImage(file: File, token: String,onSuccess: (GetProfileDto?) -> Unit,onError: (Int) -> Unit) {
        job?.cancel()
        job = viewModelScope.launch {
            profileInfo.changeImage(file,token)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                changeImageState = result.data,
                                isLoading = false
                            )
                            onSuccess(result.data)
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                changeImageState = result.data,
                                isLoading = false
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                changeImageState = result.data,
                                isLoading = true
                            )
                        }
                        else -> {}
                    }
                }.launchIn(this)
        }
    }

    fun editProfile(body: EditProfile, token: String,onSuccess: (GetProfileDto?) -> Unit,onError: (Int) -> Unit) {
        job?.cancel()
        job = viewModelScope.launch {
            profileInfo.editProfile(body,token)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                editProfileState = result.data,
                                isLoading = false
                            )
                            onSuccess(result.data)
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                editProfileState = result.data,
                                isLoading = false
                            )
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                editProfileState = result.data,
                                isLoading = true
                            )
                        }
                        else -> {}
                    }
                }.launchIn(this)
        }
    }

    fun saveFcmToken(body: SaveFcmTokenDto, token: String,onSuccess: (String) -> Unit,onError: (Int) -> Unit) {
        job?.cancel()
        job = viewModelScope.launch {
            profileInfo.saveFcmToken(token,body)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            onSuccess("Success")
                        }
                        is Resource.Error -> {
                            onError(R.string.error)
                        }
                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(this)
        }
    }
}