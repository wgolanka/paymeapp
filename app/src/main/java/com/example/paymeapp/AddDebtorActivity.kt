package com.example.paymeapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.util.round
import kotlinx.android.synthetic.main.activity_add_debtor.*

class AddDebtorActivity : AppCompatActivity() {

    private val yes = "Yes"
    private val no = "No"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debtor)
    }

    fun addNewDebtor(view: View) {

        val debtorName: EditText = editText_name
        val debtorOwed: EditText = editTex_owned
        if (debtorName.text.isNullOrBlank() || debtorOwed.text.isNullOrBlank()) {
            showToastWithText("Please enter debtor name and sum owed")
            return
        }

        val name: String = debtorName.text.toString()
        val owed: Double = debtorOwed.round()

        if (owed <= 0.0) {
            showToastWithText("Owed debt must be bigger then 0!")
            return
        }

        val debtor = Debtor(name, owed)
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

    fun cancelAddNewDebtor(view: View) {
        val debtorName: EditText = findViewById(R.id.editText_name)
        val debtorOwed: EditText = findViewById(R.id.editTex_owned)

        if (debtorName.text.isNullOrBlank() && debtorOwed.text.isNullOrBlank()) {
            finish()
        } else {
            showCancelDialog()
        }
    }

    private fun showCancelDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Are you sure to cancel?")
        builder.setMessage("This debtor will NOT be saved")

        builder.setPositiveButton(yes) { _, _ ->
            Toast.makeText(
                applicationContext,
                yes, Toast.LENGTH_SHORT
            ).show()

            finish()
        }

        builder.setNegativeButton(no) { _, _ ->
            Toast.makeText(
                applicationContext,
                no, Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }
}
