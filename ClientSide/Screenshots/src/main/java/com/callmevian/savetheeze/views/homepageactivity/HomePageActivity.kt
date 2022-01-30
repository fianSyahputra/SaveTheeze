package com.callmevian.savetheeze.views.homepageactivity

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callmevian.savetheeze.R
import com.callmevian.savetheeze.views.frontpageactivity.FrontPageActivity
import com.callmevian.savetheeze.views.frontpageactivity.FrontPageViewModel
import com.callmevian.savetheeze.views.homepageactivity.utils.adapters.WordRecyclerAdapter
import com.callmevian.savetheeze.views.homepageactivity.utils.newWordDialog.NewWordDialogFragment
import com.callmevian.savetheeze.views.utils.loadingDialog.LoadingDialog
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageActivity: AppCompatActivity() {


    //========== Activity View Variables ==========//
    lateinit var welcomeName: TextView
    lateinit var wordRecyclerView: RecyclerView
    lateinit var noWordText: TextView
    lateinit var fabAddWord: FloatingActionButton
    lateinit var appBar: MaterialToolbar

    //========== Utility Variables ==========//
    /**
     * Variabel untuk menampilkan dialog yang berisikan
     * new word form
     */
    val newWordDialog = NewWordDialogFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        //  Preparing account data first!
        val sp = getSharedPreferences("LOGIN_ACCOUNT_DATA", MODE_PRIVATE)
        val name = sp.getString("name",null)
        val authToken = sp.getString("token",null)

        //  Check apakah nama dan authToken berniali null atau tidak.
        //  Bila null,  maka kembali ke halaman login
        // Bila tidak null, persiapkan data akun

        if (name == null || authToken == null) {
            logout(sp)
        } else {


            HomePageViewModel.setLogedInAccountData(name, authToken)
        }


        //  View Variables Init
        welcomeName = findViewById(R.id.activityHomePage_welcomeUser)
        wordRecyclerView = findViewById(R.id.activityHomePage_recyclerView)
        noWordText = findViewById(R.id.activityHOmePage_noWordText)
        fabAddWord = findViewById(R.id.activityHomePage_addNewWord)
        appBar = findViewById(R.id.activityHOmePage_appBar)


        //  Get Words Data
        CoroutineScope(Dispatchers.Default).launch {
            HomePageViewModel.getAllWord()
        }

        //  OBSERVER SECTION

        HomePageViewModel.USER_NAME.observe(this, Observer {
            var nameToShow = ""
            var wordHolder = ""
            var maxLenght = 13
            var index = 0
            it.forEach { char ->
                if(index < maxLenght){
                    if (char != ' '){
                        wordHolder += char
                    }
                    else {
                        nameToShow += wordHolder
                        wordHolder = ""
                    }
                }
                index++
                println(wordHolder)
            }
            nameToShow = "Hello ${nameToShow} !"
            welcomeName.text = nameToShow
        })


        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        HomePageViewModel.USER_WORDS.observe(this){
            if (it.isEmpty()) {
                noWordText.visibility = View.VISIBLE
                wordRecyclerView.visibility = View.GONE
            }
            else {
                noWordText.visibility = View.GONE
                wordRecyclerView.visibility = View.VISIBLE
                val adapter = WordRecyclerAdapter(this, it)
                wordRecyclerView.layoutManager = linearLayoutManager
                wordRecyclerView.adapter = adapter
            }


        }


        appBar.setOnMenuItemClickListener { 
            when(it.itemId){
                R.id.activityHomePage_appbarMenu_logout -> {
                    CoroutineScope(Dispatchers.Default).launch {
                        HomePageViewModel.logout()
                    }
                    true
                }
                else -> false
            }
        }
        



        fabAddWord.setOnClickListener {
            newWordDialog.show(supportFragmentManager, null)
        }

        //  CALLBACKS IMPLEMENTATION SECTIONS

        FrontPageViewModel.toastThis = {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
            }
        }

        HomePageViewModel.displayLoadingFragment = {
            CoroutineScope(Dispatchers.Main).launch {
                LoadingDialog.show(supportFragmentManager, it)
            }
        }

        HomePageViewModel.dismissLoadingFragment = {
            CoroutineScope(Dispatchers.Main).launch {
                if (LoadingDialog.isVisible){
                    LoadingDialog.dismiss()
                }
            }
        }

        HomePageViewModel.logoutSuccess = {
            logout(sp)
        }


    }

    override fun onBackPressed() {
        //super.onBackPressed()

        MaterialAlertDialogBuilder(this)
            .setMessage("Yakin ingin Logout dari SaveTheeze ?")
            .setNegativeButton("Tidak"){dialog, which -> dialog.dismiss()}
            .setPositiveButton("Ya"){dialog, which->
                CoroutineScope(Dispatchers.Default).launch {
                    HomePageViewModel.logout()
                }
            }
            .show()

    }




    private fun logout(sp: SharedPreferences){
        sp.edit().clear().apply()
        val movingIntent = Intent(this, FrontPageActivity::class.java)
        movingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(movingIntent)
        finish()
    }



}