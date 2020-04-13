package com.example.paymeapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.R.string.*
import com.example.paymeapp.util.round
import kotlinx.android.synthetic.main.activity_add_debtor.*

class AddDebtorActivity : AppCompatActivity() {

    private val minDebtValue = 0.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_debtor)
    }

    fun addNewDebtor(view: View) {

        val debtorName: EditText = editText_name
        val debtorOwed: EditText = editTex_owned
        if (debtorName.text.isNullOrBlank() || debtorOwed.text.isNullOrBlank()) {
            showToastWith(getString(enter_debtor_info))
            return
        }

        val name: String = debtorName.text.toString()
        val owed: Double = debtorOwed.round()

        if (owed < minDebtValue) {
            showToastWith(getString(debt_too_small_info))
            return
        }

        val debtor = Debtor(name, owed)
        MainActivity.allDebtors.add(debtor)

        finish()
    }

    private fun showToastWith(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun cancelAddNewDebtor(view: View) {
        val debtorName: EditText = editText_name
        val debtorOwed: EditText = editTex_owned

        if (debtorName.text.isNullOrBlank() && debtorOwed.text.isNullOrBlank()) {
            finish()
        } else {
            showCancelDialog()
        }
    }

    private fun showCancelDialog() {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle(getString(cancel_question))
            setMessage(getString(cancel_info))

            setPositiveButton(yes) { _, _ ->
                Toast.makeText(
                    applicationContext,
                    yes, Toast.LENGTH_SHORT
                ).show()

                finish()
            }

            setNegativeButton(no) { _, _ ->
                Toast.makeText(
                    applicationContext,
                    no, Toast.LENGTH_SHORT
                ).show()
            }
        }

        builder.show()
    }
}
