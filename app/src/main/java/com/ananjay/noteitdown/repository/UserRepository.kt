package com.ananjay.noteitdown.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ananjay.noteitdown.api.UserApi
import com.ananjay.noteitdown.models.UserRequest
import com.ananjay.noteitdown.models.UserResponse
import com.ananjay.noteitdown.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


private const val TAG = "UserRepository"
class UserRepository @Inject constructor(private val userAPI: UserApi){

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
    get() =  _userResponseLiveData
    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        Log.d(TAG, "registerUser: ${response.body().toString()}")
        handleResponse(response)


    }

    suspend fun signInUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signin(userRequest)
        Log.d(TAG, "loginUser: ${response.body().toString()}")
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>){
        if(response.isSuccessful && response.body() != null){
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody() != null){
            //in case of error retrofit doesn't parse the error response to give error message, but it does in case of success. hence,
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong!!!"))
        }
    }

}