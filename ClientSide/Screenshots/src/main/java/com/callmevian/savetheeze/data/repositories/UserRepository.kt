package com.callmevian.savetheeze.data.repositories

import com.callmevian.savetheeze.data.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.HttpException
import java.io.IOException

class UserRepository {


    suspend fun signUp(newUserData: HashMap<String, String>): HashMap<String, Any>{

        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.USER_API_INSTANCE.signUp(newUserData)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else {
                    return@async generateExceptionHashMap("Request to server is failed! Please contact dev!")
                }
            } catch (err: HttpException) {
                return@async generateExceptionHashMap(err.message())
            } catch (err: IOException) {
                return@async generateExceptionHashMap(err.message!!)
            }
        }.await()

        return resultFromServer!!
    }

    suspend fun login(loginData: HashMap<String, String>): HashMap<String, Any>{

        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.USER_API_INSTANCE.login(loginData)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else {
                    if (response.code() == 400) {
                        return@async generateExceptionHashMap("Akun dengan kombinasi Email dan password tersebut tidak ditemukan")
                    }
                    else {
                        return@async  generateExceptionHashMap("Request to server is failed! Please contact dev! Response code : ${response.code()}")
                    }
                }
            } catch (err: HttpException) {
                return@async generateExceptionHashMap(err.message())
            } catch (err: IOException) {
                return@async generateExceptionHashMap(err.message!!)
            }
        }.await()
        return resultFromServer!!
    }

    suspend fun logout(authToken: String): HashMap<String, Any>{

        val resultFromServer = CoroutineScope(Dispatchers.IO).async {
            try {
                val response = RetrofitInstance.USER_API_INSTANCE.logout(authToken)
                if (response.isSuccessful){
                    return@async response.body()
                }
                else {
                    return@async generateExceptionHashMap("Request to server is failed! Please contact dev!")
                }
            } catch (err: HttpException) {
                return@async generateExceptionHashMap(err.message())
            } catch (err: IOException) {
                return@async generateExceptionHashMap(err.message!!)
            }
        }.await()

        return resultFromServer as HashMap<String, Any>
    }





    private fun generateExceptionHashMap(message: String): HashMap<String, Any>{
        val hashMap = HashMap<String, Any>()
        hashMap["exception"] = message
        return hashMap
    }

}