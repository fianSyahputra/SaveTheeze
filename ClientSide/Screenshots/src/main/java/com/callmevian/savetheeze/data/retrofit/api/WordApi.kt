package com.callmevian.savetheeze.data.retrofit.api

import com.callmevian.savetheeze.data.models.word.Word
import retrofit2.Response
import retrofit2.http.*

interface WordApi {

    @GET("words/")
    suspend fun getAllWord(@Header("Authorization") authToken: String): Response<List<HashMap<String, Any>>>

    @POST("words/")
    suspend fun addNewWord(
        @Header("Authorization") authToken: String,
        @Body newWord: HashMap<String, String>
    ) : Response<HashMap<String, Any>>

    @PUT("words/{wordId}/")
    suspend fun editWord(
        @Path("wordId") wordId: Int,
        @Header("Authorization") authToken: String,
        @Body newWord: HashMap<String, String>
    ): Response<HashMap<String, Any>>

    @DELETE("words/{wordId}/")
    suspend fun deleteWord(
        @Path("wordId") wordId: Int,
        @Header("Authorization") authToken: String
    ): Response<HashMap<String, Any>>

}