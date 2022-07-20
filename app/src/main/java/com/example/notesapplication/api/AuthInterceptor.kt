package com.example.notesapplication.api

import com.example.notesapplication.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

//Inject will tell hilt that if we want AuthInterceptor obj called its constructor
// whenever hilts req its obj, it will call its constructor
class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        // request() method is called to get the request

        //basically request that we are getting,
        // modify that request into new request by adding header into it.
        //here in this val request we will get new request object.

        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token") //Bearer is the api requirement
        return chain.proceed(request.build())
        // proceed() method is called it means proceed further with new request
    }
}

// suppose we have 50 post/get request in which header is used,

//adding header in every request is tedious way,

//alternatively we can use "OkHttp interceptor"

//Headers that needs to be added to every request can be specified using an OkHttp interceptor.

//In simple words OkHttp interceptor is interceptor will add header in a request before post/get request is send

//==============================
//github readme

//
//dependencies : implementation 'com.squareup.okhttp3:okhttp:4.9.3'
//
//
//Add a header dynamically:
//For example Shared Preference Token
//
//Method 1:
//-> add header in every request
//A request Header can be updated dynamically using the @Header annotation. A corresponding parameter must be provided to the @Header.
//If the value is null, the header will be omitted. Otherwise, toString will be called on the value, and the result used.
//
//@GET("user")
//Call<User> getUser(@Header("Authorization") String authorization)
//
//
//Method 2:
//-> suppose we have 50 post/get request in which header is used,
//adding header in every request is tedious way,
//alternatively we can use "OkHttp interceptor"
//
//Headers that needs to be added to every request can be specified using an OkHttp interceptor.
//In simple words OkHttp interceptor is interceptor will add header in a request before post/get request is send
//
