package com.example.paymeapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.R.string.*
import com.example.paymeapp.util.round
import kotlinx.android.synthetic.main.activity_add_edit_debtor.*
import java.util.*

class AddEditDebtorActivity : AppCompatActivity() {

    private val minDebtValue = 0.1
    private var addEditPresenter: AddEditPresenter = AddEditPresenter()

    var allDebtors: ArrayList<Debtor> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_debtor)

        addEditPresenter = intent.getSerializableExtra(AddEditPresenter.className) as AddEditPresenter

        if (!addEditPresenter.isNewDebtor) {
            editTextName.setText(addEditPresenter.debtorName)
            editTextOwed.setText(addEditPresenter.debtorOwed.toString())
        }

        textViewAddNewDebtor.text = addEditPresenter.title
        buttonAdd.text = addEditPresenter.buttonText
    }

    fun addNewDebtor(view: View) {
        //TODO do not add if already exist

        val debtorName: EditText = editTextName
        val debtorOwed: EditText = editTextOwed
        if (debtorName.text.isNullOrBlank() || debtorOwed.text.isNullOrBlank()) {
            toastWith(getString(enter_debtor_info))
            return
        }

        val name: String = debtorName.text.toString()
        val owed: Double = debtorOwed.round()

        if (owed < minDebtValue) {
            toastWith(getString(debt_too_small_info))
            return
        }

        val replyIntent = Intent()
        if (addEditPresenter.isNewDebtor) {
            replyIntent.putExtra("Debtor", Debtor(UUID.randomUUID().toString(), name, owed))
            setResult(1, replyIntent)
        } else {
            replyIntent.putExtra("Debtor", Debtor(addEditPresenter.debtorId, name, owed))
            setResult(2, replyIntent)
        }

        finish()
    }

    private fun editExistingDebtor(newDebtor: Debtor) {
        if (newDebtor.name != addEditPresenter.debtorName || newDebtor.owed != addEditPresenter.debtorOwed) {
            val debtor =
                allDebtors.find { debtor -> debtor.name == addEditPresenter.debtorName }
            allDebtors.remove(debtor)
            allDebtors.add(newDebtor)
        }
    }

    private fun toastWith(text: String) {
        makeText(
            applicationContext,
            text,
            LENGTH_SHORT
        ).show()
    }

    fun cancelAddNewDebtor(view: View) { //TODO why app craches when not used view is removed
        val debtorName: EditText = editTextName
        val debtorOwed: EditText = editTextOwed

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
                toastWith(getString(yes))
                finish()
            }

            setNegativeButton(no) { _, _ ->
                toastWith(getString(no))
            }
        }

        builder.show()
    }
}
