package com.callmevian.savetheeze.data.retrofit.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("user/signup/")
    suspend fun signUp(@Body newUserData: HashMap<String, String>): Response<HashMap<String, Any>>

    @POST("user/login/")
    suspend fun login(@Body loginData: HashMap<String, String>): Response<HashMap<String, Any>>

    @POST("user/logout/")
    suspend fun logout(@Header("Authorization") authToken: String): Response<HashMap<String, String>>
}