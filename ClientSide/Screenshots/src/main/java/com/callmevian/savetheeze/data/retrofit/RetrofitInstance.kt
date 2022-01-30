package com.callmevian.savetheeze.data.retrofit

import com.callmevian.savetheeze.data.retrofit.api.UserApi
import com.callmevian.savetheeze.data.retrofit.api.WordApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    private const val REST_API_URL =  "https://savetheeze.herokuapp.com/api/"

    private val INSTANCE by lazy{
        Retrofit.Builder()
            .baseUrl(REST_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val USER_API_INSTANCE: UserApi by lazy{
        INSTANCE.create(UserApi::class.java)
    }

    val WORD_API_INSTANCE by lazy{
        INSTANCE.create(WordApi::class.java)
    }



}