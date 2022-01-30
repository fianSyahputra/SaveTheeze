package com.callmevian.savetheeze.views.homepageactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.callmevian.savetheeze.data.models.word.Word
import com.callmevian.savetheeze.data.repositories.UserRepository
import com.callmevian.savetheeze.data.repositories.WordRepository
import com.callmevian.savetheeze.views.frontpageactivity.FrontPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HomePageViewModel: ViewModel() {


    var USER_NAME = MutableLiveData<String>()
    private lateinit var authToken: String

    var USER_WORDS = MutableLiveData<List<Word>>()

    private val userRepo = UserRepository()
    private val wordRepo = WordRepository()


    //========== CALLBACKS VARIABLE ==========//

    /**
     * Untuk toasting. Codenya ada di HomePageActivity
     */
    var toastThis: ((String)->Unit)? = null

    /**
     * Untuk menampilkan loading screen. Codenya ada di HomePageActivity
     */
    var displayLoadingFragment: ((String)->Unit)? = null

    /**
     * Untuk berhenti menampilkan loading screen. Codenya ada di HomePageActivity
     */
    var dismissLoadingFragment: (()->Unit)? = null

    /**
     * Untuk logout. Codenya ada di HOmePageActivity
     */
    var logoutSuccess: (()->Unit)? = null


    fun setLogedInAccountData(name: String, token: String){
        USER_NAME.value = name
        authToken = "token $token"
    }

    suspend fun getAllWord(){
        val data = wordRepo.getAllWords(authToken)
        println("This is from getAllWord()")
        println(data)
        if (data.isNotEmpty() && data[0]["exception"] != null) {
            toastThis!!.invoke(data[0]["exception"].toString())
        } else {
            val list = mutableListOf<Word>()
            data.forEach{
                val id = (it["id"] as Double).toInt()
                val word = it["word"].toString()
                val translate = it["translate"].toString()
                list.add(Word(id, word, translate))
            }
            withContext(Dispatchers.Main){
                USER_WORDS.value = list
            }
        }
    }

    suspend fun addNewWord(newWord: HashMap<String, String>){
        displayLoadingFragment!!.invoke("Saving New Word!")
        var result = wordRepo.addNewWord(authToken, newWord)
        if (result["exception"]!=null){
            toastThis!!.invoke(result["exception"].toString())
        } else {
            getAllWord()
        }
        dismissLoadingFragment!!.invoke()
    }

    suspend fun editWord(wordId: Int, newWord: HashMap<String, String>){
        displayLoadingFragment!!.invoke("Updating The Word!")
        var result = wordRepo.updateWord(authToken, wordId, newWord)
        if (result["exception"]!=null){
            toastThis!!.invoke(result["exception"].toString())
        } else {
            getAllWord()
        }
        dismissLoadingFragment!!.invoke()
    }



    suspend fun deleteWord(wordId: Int){
        displayLoadingFragment!!.invoke("Deleting Word!")
        var result = wordRepo.deleteWord(authToken, wordId)
        if(result == null){
            getAllWord()
        }
        else if (result["exception"]!=null){
            toastThis!!.invoke(result["exception"].toString())
        } else {
            toastThis!!.invoke(result["message"].toString())
        }
        dismissLoadingFragment!!.invoke()
    }


    suspend fun logout(){
        displayLoadingFragment!!.invoke("Logging Out!")
        var result = userRepo.logout(authToken)
        if (result["exception"]!=null){
            toastThis!!.invoke(result["exception"].toString())
        } else {
            logoutSuccess!!.invoke()
        }
        dismissLoadingFragment!!.invoke()
    }


}