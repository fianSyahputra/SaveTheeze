package com.callmevian.savetheeze.data.repositories

import com.callmevian.savetheeze.data.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException

class WordRepository {


    suspend fun getAllWords(authToken: String): List<HashMap<String, Any>>{
        val list = mutableListOf<HashMap<String, Any>>()
        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.WORD_API_INSTANCE.getAllWord(authToken)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else {
                    list.add(generateExceptionHashMap("Request to server is failed! Please contact dev! Response code : ${response.code()}"))
                    return@async list
                }
            } catch (err: HttpException) {
                list.add(generateExceptionHashMap(err.message()))
                return@async list
            } catch (err: IOException) {
                list.add(generateExceptionHashMap(err.message!!))
                return@async list
            }
        }.await()
        return resultFromServer!!
    }


    suspend fun addNewWord(authToken: String, newWord: HashMap<String, String>): HashMap<String,Any>{
        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.WORD_API_INSTANCE.addNewWord(authToken, newWord)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else {
                    return@async generateExceptionHashMap("Request to server is failed! Please contact dev! Response code : ${response.code()}")
                }
            } catch (err: HttpException) {
                return@async generateExceptionHashMap(err.message())
            } catch (err: IOException) {
                return@async generateExceptionHashMap(err.message!!)
            }
        }.await()
        return resultFromServer!!
    }



    suspend fun updateWord(authToken: String, wordId: Int, newWord: HashMap<String, String>): HashMap<String,Any>{
        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.WORD_API_INSTANCE.editWord(wordId, authToken, newWord)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else {
                    return@async generateExceptionHashMap("Request to server is failed! Please contact dev! Response code : ${response.code()}")
                }
            } catch (err: HttpException) {
                return@async generateExceptionHashMap(err.message())
            } catch (err: IOException) {
                return@async generateExceptionHashMap(err.message!!)
            }
        }.await()
        return resultFromServer!!
    }


    suspend fun deleteWord(authToken: String, wordId: Int): HashMap<String,Any>?{
        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.WORD_API_INSTANCE.deleteWord(wordId,authToken)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else if(response.code() == 204){
                    return@async null
                }
                else {
                    return@async generateExceptionHashMap("Request to server is failed! Please contact dev! Response code : ${response.code()}")
                }
            } catch (err: HttpException) {
                return@async generateExceptionHashMap(err.message())
            } catch (err: IOException) {
                return@async generateExceptionHashMap(err.message!!)
            }
        }.await()
        return resultFromServer
    }





    private fun generateExceptionHashMap(message: String): HashMap<String, Any>{
        val hashMap = HashMap<String, Any>()
        hashMap["exception"] = message
        return hashMap
    }


}