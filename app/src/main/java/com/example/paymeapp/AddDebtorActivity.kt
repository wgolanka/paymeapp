package com.example.paymeapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddDebtorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debtor)
    }

    fun addNewDebtor(view: View) {
        val debtorName: EditText = findViewById(R.id.editText_name)
        val debtorOwned: EditText = findViewById(R.id.editTex_owned)

        //TODO handle null or empty fields ( 0 in case of owned sum)

        val name: String = debtorName.text.toString()
        val owned: Double = debtorOwned.text.toString().toDouble() //TODO handle possible exception


        val debtor = Debtor(name, owned)
        MainActivity.allDebtors.add(debtor)

        finish()
    }
}
