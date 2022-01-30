package com.callmevian.savetheeze.views.utils.loadingDialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.callmevian.savetheeze.R

object LoadingDialog: DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.utils_loadingdialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.utilsLoadingDialog_loadingTextView).text = tag
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }




}