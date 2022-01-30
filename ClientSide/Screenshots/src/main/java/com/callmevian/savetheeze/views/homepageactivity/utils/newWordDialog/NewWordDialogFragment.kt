package com.callmevian.savetheeze.views.homepageactivity.utils.newWordDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.callmevian.savetheeze.R
import com.callmevian.savetheeze.views.homepageactivity.HomePageViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewWordDialogFragment: DialogFragment() {

    lateinit var wordText: TextInputEditText
    lateinit var translateText: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.utils_addnewworddialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordText = view.findViewById(R.id.utilsAddNewWordDialog_textInputEditText_word)
        translateText = view.findViewById(R.id.utilsAddNewWordDialog_textInputEditText_translate)

        wordText.setText("")
        translateText.setText("")


        //  Cancel Button OnClickListener
        view.findViewById<Button>(R.id.utilsAddNewWordDialog_btnCancel).setOnClickListener {
            dismiss()
        }

        //  Add Button OnClickListener
        view.findViewById<Button>(R.id.utilsAddNewWordDialog_btnAdd).setOnClickListener {
            if (wordText.text.isNullOrBlank()){
                HomePageViewModel.toastThis!!.invoke("Word tidak boleh kosong!")
            }
            else if (translateText.text.isNullOrBlank()){
                HomePageViewModel.toastThis!!.invoke("Translate tidak boleh kosong!")
            }
            else {
                CoroutineScope(Dispatchers.Default).launch {
                    val newWord = HashMap<String, String>()
                    newWord["word"] = wordText.text.toString()
                    newWord["translate"] = translateText.text.toString()
                    HomePageViewModel.addNewWord(newWord)
                    dismiss()
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        wordText.setText("")
        translateText.setText("")
        dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

}