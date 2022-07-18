package com.example.notesapplication.repository

import android.util.Log
import com.example.notesapplication.api.UserApi
import com.example.notesapplication.models.UserRequest
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {


    suspend fun registerUser(userRequest: UserRequest) {
        val response = userApi.signup(userRequest)

        Log.e("register", response.body().toString())
    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response =userApi.signin(userRequest)
    }


}