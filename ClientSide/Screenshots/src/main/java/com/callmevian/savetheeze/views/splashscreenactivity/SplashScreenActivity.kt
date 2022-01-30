package com.callmevian.savetheeze.views.splashscreenactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.callmevian.savetheeze.R
import com.callmevian.savetheeze.views.frontpageactivity.FrontPageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            val movingIntent = Intent(applicationContext, FrontPageActivity::class.java)
            movingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(movingIntent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

}