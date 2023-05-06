package com.android.beyikyol2.feature_other.domain.repository

import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_other.data.remote.dto.GetConstantDto
import com.android.beyikyol2.feature_other.data.remote.dto.GetHomeDto
import com.android.beyikyol2.feature_other.data.remote.dto.evacuator.GetEvacuatorDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDto
import com.android.beyikyol2.feature_other.data.remote.dto.inbox.GetUserInboxDtoItem
import com.android.beyikyol2.feature_other.data.remote.dto.map.SearchFromMap
import com.android.beyikyol2.feature_other.data.remote.dto.map.autocomplete.GetAutoComplete
import com.android.beyikyol2.feature_other.data.remote.dto.map.direction.GetDirection
import com.android.beyikyol2.feature_other.data.remote.dto.speech.SpeechRequest
import com.android.beyikyol2.feature_other.data.remote.dto.speech.response.SpeechResponse
import com.android.beyikyol2.feature_other.data.remote.dto.weather.GetWeather
import kotlinx.coroutines.flow.Flow

interface GetHomeRepository {

    fun getHome(token: String,isSend: Boolean): Flow<Resource<GetHomeDto>>
    fun getUserInbox(token: String): Flow<Resource<GetUserInboxDto>>
    fun getEvacuator(region: String): Flow<Resource<GetEvacuatorDto>>
    fun getConstantPage(type: String): Flow<Resource<GetConstantDto>>
    fun getAutoComplete(input: String): Flow<Resource<GetAutoComplete>>
    fun searchFromMap(query: String): Flow<Resource<SearchFromMap>>
    fun textToSpeech(body: SpeechRequest): Flow<Resource<SpeechResponse>>
    fun getWeather(): Flow<Resource<GetWeather>>
    fun getDirection(params: HashMap<String, Any>): Flow<Resource<GetDirection>>
    fun deleteInbox(token: String,id: String): Flow<Resource<GetUserInboxDtoItem>>

}