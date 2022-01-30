package com.callmevian.savetheeze.views.frontpageactivity

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.callmevian.savetheeze.R
import com.callmevian.savetheeze.views.frontpageactivity.fragments.LoginFragment
import com.callmevian.savetheeze.views.frontpageactivity.fragments.SignUpFragment
import com.callmevian.savetheeze.views.homepageactivity.HomePageActivity
import com.callmevian.savetheeze.views.utils.loadingDialog.LoadingDialog
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.*

class FrontPageActivity: AppCompatActivity() {

    //========== FRAGMENTS VARIABLES ==========//
    val loginFragment = LoginFragment()
    val signUpFragment = SignUpFragment()

    //========== ACTIVITY VIEWS VARIABLES ==========//
    lateinit var title: TextView
    lateinit var fragmentSpace: FrameLayout

    //========== UTILITY VARIABLES ==========//
    var fragmentDisplayed: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frontpage)

        //  ACTIVITY VIEW VARIABLES INIT
        title = findViewById(R.id.activityFrontPage_fragmentTitle)
        fragmentSpace = findViewById(R.id.activityFrontPage_mainFragmentSpace)

        //  Tampilkan login fragment secara default
        displayFragment(loginFragment)




        //========== CALLBACKS IMPLEMENTATION ==========//
        //  Callbacks from LoginFragment
        loginFragment.callSignUpFragment = {
            displayFragment(signUpFragment)
        }



        //  CALLBACK IMPLEMENTATION SECTION
        FrontPageViewModel.toastThis = {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            }
        }

        FrontPageViewModel.displayLoadingFragment = {
            CoroutineScope(Dispatchers.Main).launch {
                LoadingDialog.show(supportFragmentManager, it)
            }
        }

        FrontPageViewModel.dismissLoadingFragment = {
            CoroutineScope(Dispatchers.Main).launch {
                if (LoadingDialog.isVisible){
                    LoadingDialog.dismiss()
                }
            }
        }

        FrontPageViewModel.loginSuccess = {
            val accountData = it["account"]  as LinkedTreeMap<String, Any>
            val authToken = it["token"]
            val accountId = accountData["id"] as Double
            val accountEmail = accountData["email"]
            val accountName = accountData["name"]



            val sp = getSharedPreferences("LOGIN_ACCOUNT_DATA", MODE_PRIVATE)
            val spEditor = sp.edit()
            spEditor.putInt("id", accountId.toInt())
            spEditor.putString("email", accountEmail.toString())
            spEditor.putString("name", accountName.toString())
            spEditor.putString("token", authToken.toString())
            spEditor.apply()

            val movingIntent = Intent(applicationContext, HomePageActivity::class.java)
            movingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(movingIntent)
            finish()

        }

    }

    override fun onBackPressed() {
        if (fragmentDisplayed!! == signUpFragment){
            displayFragment(loginFragment)
        }
        else {
            super.onBackPressed()
        }
    }


    //========== COMPONENT FUNCTION ==========//
    /**
     * Digunakan untuk mengganti fragment yang sedang ditampilkan pada
     * FronPageActivity
     */
    private fun displayFragment(fragmentClass: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentSpace.id, fragmentClass)
        fragmentTransaction.commit()
        when(fragmentClass){
            loginFragment -> {title.text = "LOGIN PAGE"}
            signUpFragment -> {title.text = "SIGNUP PAGE"}
        }
        fragmentDisplayed = fragmentClass
    }


}