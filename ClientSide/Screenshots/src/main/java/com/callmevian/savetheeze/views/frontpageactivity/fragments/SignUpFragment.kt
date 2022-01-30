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

class SignUpFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_signup,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailTextView = view.findViewById<TextInputEditText>(R.id.fragmentSignUp_editText_email)
        val passwordTextView = view.findViewById<TextInputEditText>(R.id.fragmentSignUp_editText_password)
        val nameTextView = view.findViewById<TextInputEditText>(R.id.fragmentSignUp_editText_nama)

        emailTextView.setText("")
        passwordTextView.setText("")
        nameTextView.setText("")


        //  SignUp Button OnClickListener()
        view.findViewById<Button>(R.id.fragmentSignUp_btnLogin).setOnClickListener {

            if (emailTextView.text.isNullOrBlank()){
                FrontPageViewModel.toastThis!!.invoke("Email tidak boleh kosong!")
            }
            else if (passwordTextView.text.isNullOrBlank()){
                FrontPageViewModel.toastThis!!.invoke("Password tidak boleh kosong!")
            }
            else if (nameTextView.text.isNullOrBlank()){
                FrontPageViewModel.toastThis!!.invoke("Nama tidak boleh kosong!")
            }
            else {
                CoroutineScope(Dispatchers.Default).launch {
                    val hashMap = HashMap<String,String>()
                    hashMap["email"] = emailTextView.text.toString()
                    hashMap["password"] = passwordTextView.text.toString()
                    hashMap["name"] = nameTextView.text.toString()

                    FrontPageViewModel.signUp(hashMap)
                }
            }
        }

    }

}