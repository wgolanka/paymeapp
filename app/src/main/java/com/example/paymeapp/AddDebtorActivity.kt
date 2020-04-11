package com.example.paymeapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.RoundingMode

class AddDebtorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debtor)
    }

    fun addNewDebtor(view: View) {
        val debtorName: EditText = findViewById(R.id.editText_name)
        val debtorOwned: EditText = findViewById(R.id.editTex_owned)

        if (debtorName.text.isNullOrBlank() || debtorOwned.text.isNullOrBlank()) {
            showToastWithText("Please enter debtor name and sum owned")
            return
        }

        val name: String = debtorName.text.toString()
        val owned: Double =
            debtorOwned.text.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

        if (owned <= 0.0) {
            showToastWithText("Owned debt must be bigger then 0!")
            return
        }

        val debtor = Debtor(name, owned)
        MainActivity.allDebtors.add(debtor)

        finish()
    }

    private fun showToastWithText(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}
