package com.callmevian.savetheeze.views.frontpageactivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.callmevian.savetheeze.R
import com.callmevian.savetheeze.views.frontpageactivity.FrontPageViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment: Fragment() {

    //========== SOME CALLBACK`S FUNCTION ==========//
    var callSignUpFragment: (()->Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailText = view.findViewById<TextInputEditText>(R.id.fragmentLogin_editText_email)
        val password = view.findViewById<TextInputEditText>(R.id.fragmentLogin_editText_password)

        emailText.setText("")
        password.setText("")

        // Button login OnClickListener()
        view.findViewById<Button>(R.id.fragmentLogin_btnLogin).setOnClickListener {

            if (emailText.text.isNullOrBlank()) {
                FrontPageViewModel.toastThis!!.invoke("Email tidak boleh kosong!")
            }
            else if (password.text.isNullOrBlank()) {
                FrontPageViewModel.toastThis!!.invoke("Password tidak boleh kosong!")
            }
            else {
                CoroutineScope(Dispatchers.Default).launch {
                    val loginData = HashMap<String, String>()
                    loginData["email"] = emailText.text.toString()
                    loginData["password"] = password.text.toString()

                    FrontPageViewModel.login(loginData)
                }
            }

        }

        // Button SignUp OnClickListener()
        view.findViewById<Button>(R.id.fragmentLogin_btnSignUp).setOnClickListener {
            callSignUpFragment!!.invoke()
        }


    }

}