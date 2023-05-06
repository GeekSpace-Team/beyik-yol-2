package com.android.beyikyol2.feature_other.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.beyikyol2.R
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDtoItem
import com.android.beyikyol2.feature_other.data.remote.dto.speech.AudioConfig
import com.android.beyikyol2.feature_other.data.remote.dto.speech.Input
import com.android.beyikyol2.feature_other.data.remote.dto.speech.SpeechRequest
import com.android.beyikyol2.feature_other.data.remote.dto.speech.Voice
import com.android.beyikyol2.feature_other.domain.use_case.GetHomeInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetHomeViewModel @Inject constructor(
    private val getHomeInfo: GetHomeInfo
): ViewModel() {
    private val _state = mutableStateOf(GetHomeState())
    val state: State<GetHomeState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private val _refreshing = mutableStateOf(false)
    val refreshing: State<Boolean> = _refreshing

    private val _isLoad = mutableStateOf(false)
    val isLoad: State<Boolean> = _isLoad

    private var refreshJob: Job? = null

    fun onRefresh(token: String,isSend: Boolean){
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo(token,isSend)
                .onEach { result->
                    when(result){
                        is Resource.Success->{
                            _state.value = state.value.copy(
                                homeInfo = result.data,
                                isLoading = false
                            )
                            _isLoad.value=true
                        }
                        is Resource.Error->{
                            _state.value = state.value.copy(
                                homeInfo = result.data,
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            ))
                        }
                        is Resource.Loading->{
//                            _state.value = state.value.copy(
//                                homeInfo = result.data,
//                                isLoading = isLd
//                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getUserInbox(token: String){
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.getUserInbox(token)
                .onEach { result->
                    when(result){
                        is Resource.Success->{
                            _state.value = state.value.copy(
                                userInboxState = result.data,
                                isLoading = false
                            )
//                            _isLoad.value=true
                        }
                        is Resource.Error->{
                            _state.value = state.value.copy(
                                userInboxState = result.data,
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            ))
                        }
                        is Resource.Loading->{
                            _state.value = state.value.copy(
                                userInboxState = result.data,
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun deleteInbox(token: String,id: String,onSuccess: (GetUserInboxDtoItem?)-> Unit, onError:(Int)-> Unit){
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.deleteInbox(token,id)
                .onEach { result->
                    when(result){
                        is Resource.Success->{
                            _state.value = state.value.copy(
                                isLoading = false
                            )
                            onSuccess(result.data)
//                            _isLoad.value=true
                        }
                        is Resource.Error->{
                            _state.value = state.value.copy(
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            ))
                            onError(R.string.error)
                        }
                        is Resource.Loading->{
                            _state.value = state.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getEvacuator(region: String){
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.getEvacuators(region)
                .onEach { result->
                    when(result){
                        is Resource.Success->{
                            _state.value = state.value.copy(
                                evacuatorDto = result.data,
                                isLoading = false
                            )
//                            _isLoad.value=true
                        }
                        is Resource.Error->{
                            _state.value = state.value.copy(
                                evacuatorDto = result.data,
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            ))
                        }
                        is Resource.Loading->{
                            _state.value = state.value.copy(
                                evacuatorDto = result.data,
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getConstantPage(type: String){
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.getConstantPage(type)
                .onEach { result->
                    when(result){
                        is Resource.Success->{
                            _state.value = state.value.copy(
                                constantData = result.data,
                                isLoading = false
                            )
//                            _isLoad.value=true
                        }
                        is Resource.Error->{
                            _state.value = state.value.copy(
                                constantData = result.data,
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            ))
                        }
                        is Resource.Loading->{
                            _state.value = state.value.copy(
                                constantData = result.data,
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }


    }

    fun getAutoComplete(input: String) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.getAutoComplete(input)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                autoComplete = result.data
                            )
//                            _isLoad.value = true
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                autoComplete = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                autoComplete = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun searchFromMap(query: String) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.searchFromMap(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                searchFromMap = result.data,
                                isLoading = false
                            )
//                            _isLoad.value = true
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                searchFromMap = result.data,
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                searchFromMap = result.data,
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun textToSpeech(text: String,languageCode: String="ru-RU",languageName: String="ru-RU-Wavenet-B") {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            val body = SpeechRequest(
                audioConfig= AudioConfig("LINEAR16", listOf("handset-class-device"),0,1),
                        input= Input(text),
                        voice= Voice(languageCode,languageName)
            )
            getHomeInfo.textToSpeech(body)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                ttsResult = result.data
                            )
//                            _isLoad.value = true
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                ttsResult = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                ttsResult = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getWeather() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.getWeather()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                weather = result.data
                            )
//                            _isLoad.value = true
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                weather = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                weather = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    fun getDirection(params: HashMap<String, Any>) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            getHomeInfo.getDirection(params)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                directionState = result.data
                            )
//                            _isLoad.value = true
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                directionState = result.data
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                directionState = result.data
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    sealed class UIEvent{
        data class ShowSnackbar(val message: String): UIEvent()
    }
}