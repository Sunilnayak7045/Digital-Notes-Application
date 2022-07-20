package com.example.notesapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapplication.api.UserApi
import com.example.notesapplication.models.UserRequest
import com.example.notesapplication.models.UserResponse
import com.example.notesapplication.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())

        val response = userApi.signup(userRequest)
        handleResponse(response)

        Log.e("register", response.body().toString())

    }

    suspend fun loginUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())

        val response =userApi.signin(userRequest)
        handleResponse(response)

    }



    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {


            //Success response (response.body().toString()) = UserResponse(token=eyJhbGciOiJ.., user=User(createdAt=2022-07-18T10:00:38.676Z, email=abc109@gmail.com, id=null, updatedAt=2022-07-18T10:00:38.676Z, username=abc))
            // In case of Successful, json inside the response.body() is passed,
            // in response to it we get java obj
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){


            //error response (response.body().toString()) = null
            // In case of error, we have to make java obj , JSONObject will parse json to java obj
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            //message is the response parameter
        }
        else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


}