package com.android.beyikyol2.feature_other.data.repository

import android.util.Log
import com.android.beyikyol2.core.util.Resource
import com.android.beyikyol2.feature_other.data.local.GetHomeDao
import com.android.beyikyol2.feature_other.data.local.entity.GetHomeEntity
import com.android.beyikyol2.feature_other.data.remote.OtherApi
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
import com.android.beyikyol2.feature_other.domain.repository.GetHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetHomeRepositoryImpl(
    private val api: OtherApi,
    private val dao: GetHomeDao
): GetHomeRepository {


    override fun getHome(token: String,isSend: Boolean): Flow<Resource<GetHomeDto>> = flow {
        emit(Resource.Loading())

        val homeInfos =  try {
            dao.getHome().toHome()
        } catch (ex: java.lang.Exception){
            null
        }

        emit(Resource.Loading(data = homeInfos))

        try {
            val remoteHome = api.getHome(token,isSend)
            emit(Resource.Success(remoteHome))
//            dao.deleteAll()
//            dao.insertHome(remoteHome.toGetHomeEntity())
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = homeInfos))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = homeInfos))
        }

//        try {
//            val newGetHomeInfos = dao.getHome().toHome()
//            emit(Resource.Success(newGetHomeInfos))
//        } catch (_: java.lang.Exception){}
    }

    override fun getUserInbox(token: String): Flow<Resource<GetUserInboxDto>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getUserInbox(token)
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }

    }

    override fun getEvacuator(region: String): Flow<Resource<GetEvacuatorDto>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getEvacuators(region)
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getConstantPage(type: String): Flow<Resource<GetConstantDto>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getConstantPage(type)
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }


    override fun getAutoComplete(input: String): Flow<Resource<GetAutoComplete>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.autoCompleteMap(input)
            emit(Resource.Success(remote))
            Log.e("Query",input)
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun searchFromMap(query: String): Flow<Resource<SearchFromMap>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.searchFromMap(query)
            emit(Resource.Success(remote))
            Log.e("Result",query)
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun textToSpeech(body: SpeechRequest): Flow<Resource<SpeechResponse>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.textToSpeech(body)
            Log.e("TTS-r",body.input.text)
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getWeather(): Flow<Resource<GetWeather>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getWeather()
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun getDirection(params: HashMap<String, Any>): Flow<Resource<GetDirection>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.getDirection(params)
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }

    override fun deleteInbox(token: String, id: String): Flow<Resource<GetUserInboxDtoItem>> = flow {
        emit(Resource.Loading())

        val data =  null

        emit(Resource.Loading(data = data))

        try {
            val remote = api.deleteInbox(token,id)
            emit(Resource.Success(remote))
        } catch (e: HttpException){
            Log.e("Service error",e.message())
            emit(Resource.Error(message = "Oops, something went wrong!", data = data))
        } catch (e: IOException) {
            Log.e("Service error",e.message.toString())
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection", data = data))
        }
    }
}