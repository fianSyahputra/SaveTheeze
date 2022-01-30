package com.callmevian.savetheeze.views.homepageactivity.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.callmevian.savetheeze.R
import com.callmevian.savetheeze.data.models.word.Word
import com.callmevian.savetheeze.views.homepageactivity.HomePageViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordRecyclerAdapter(
    val context: Context,
    val wordsData: List<Word>
    ): RecyclerView.Adapter<WordRecyclerAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var wordLayout = itemView.findViewById<TextInputLayout>(R.id.componentRecyclerView_words_textInputLayout_word)
        var wordEditText = itemView.findViewById<TextInputEditText>(R.id.componentRecyclerView_words_editText_word)

        var translateLayout = itemView.findViewById<TextInputLayout>(R.id.componentRecyclerView_words_textInputLayout_translate)
        var translateEditText = itemView.findViewById<TextInputEditText>(R.id.componentRecyclerView_words_editText_translate)

        var btnEdit = itemView.findViewById<Button>(R.id.componentRecyclerView_words_btnEdit)
        var btnDone = itemView.findViewById<Button>(R.id.componentRecyclerView_words_btnDone)
        var btnDelete = itemView.findViewById<Button>(R.id.componentRecyclerView_words_btnDelete)

    }

    //========== Adapter Utility Variable ==========//
    private var isEditing = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.component_recyclerview_words, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.wordLayout.isFocusable = false
        holder.wordEditText.setText(wordsData[position].word)
        holder.wordEditText.isEnabled = false

        holder.translateLayout.isFocusable = false
        holder.translateEditText.setText(wordsData[position].translate)
        holder.translateEditText.isEnabled = false

        holder.btnDone.visibility = View.GONE
        holder.btnDone.setOnClickListener {
            if (
                wordsData[position].word != holder.wordEditText.text.toString() ||
                wordsData[position].translate != holder.translateEditText.text.toString()
            ){//    Bila ada yang berubah, update ke server
                CoroutineScope(Dispatchers.Default).launch {
                    val newWord = HashMap<String, String>()
                    newWord["word"] = holder.wordEditText.text.toString()
                    newWord["translate"] = holder.translateEditText.text.toString()
                    HomePageViewModel.editWord(wordsData[position].id, newWord)
                }
            }

        }


        holder.btnEdit.setOnClickListener {

            if (!isEditing){//  Masuk Mode Editing

                isEditing = true

                holder.wordLayout.isFocusable = true
                holder.wordEditText.isEnabled = true

                holder.translateLayout.isFocusable = true
                holder.translateEditText.isEnabled = true

                holder.btnEdit.text = "CANCEL"
                holder.btnDone.visibility = View.VISIBLE



            }
            else {// Kembalikan nilai Word dan Translate ke nilai semula

                isEditing = false

                holder.wordLayout.isFocusable = false
                holder.wordEditText.isEnabled = false
                holder.wordEditText.setText(wordsData[position].word)


                holder.translateLayout.isFocusable = false
                holder.translateEditText.isEnabled = false
                holder.translateEditText.setText(wordsData[position].translate)


                holder.btnEdit.text = "EDIT WORD"
                holder.btnDone.visibility = View.GONE
            }
        }

        holder.btnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                HomePageViewModel.deleteWord(wordsData[position].id)
            }
        }





    }

    override fun getItemCount(): Int {
        return wordsData.size
    }


}