package com.callmevian.savetheeze.views.frontpageactivity

import androidx.lifecycle.ViewModel
import com.callmevian.savetheeze.data.repositories.UserRepository

object FrontPageViewModel : ViewModel() {


    val userRespo = UserRepository()


    //========== CALLBACKS FUNCTION ==========//
    /**
     * Untuk toasting. Codenya ada di FrontPageActivity
     */
    var toastThis: ((String)->Unit)? = null

    /**
     * Untuk menampilkan loading screen. Codenya ada di FrontPageActivity
     */
    var displayLoadingFragment: ((String)->Unit)? = null

    /**
     * Untuk berhenti menampilkan loading screen. Codenya ada di FrontPageActivity
     */
    var dismissLoadingFragment: (()->Unit)? = null

    /**
     * Untuk menyimpan data akun yang telah login kedalam SharedPreferences.
     * Codenya ada di FrontPageActivity
     */
    var loginSuccess: ((HashMap<String, Any>)->Unit)? = null


    suspend fun signUp(newUserData: HashMap<String, String>){
        displayLoadingFragment!!.invoke("Signing Up New Account")
        var result = userRespo.signUp(newUserData)
        if (result["exception"]!=null){
            dismissLoadingFragment!!.invoke()
            toastThis!!.invoke(result["exception"].toString())
        } else {
            loginSuccess!!.invoke(result)
        }
    }

    suspend fun login(loginData: HashMap<String, String>){
        displayLoadingFragment!!.invoke("Logging In")
        var result = userRespo.login(loginData)
        if (result["exception"]!=null){
            toastThis!!.invoke(result["exception"].toString())
        } else {
            loginSuccess!!.invoke(result)
        }
        dismissLoadingFragment!!.invoke()
    }



}