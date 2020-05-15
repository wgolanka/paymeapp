package com.example.paymeapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.R
import com.example.paymeapp.R.string.*
import com.example.paymeapp.addDebtorCode
import com.example.paymeapp.debtorClassId
import com.example.paymeapp.dto.Debtor
import com.example.paymeapp.editDebtorCode
import com.example.paymeapp.presenter.AddEditPresenter
import com.example.paymeapp.util.round
import kotlinx.android.synthetic.main.activity_add_edit_debtor.*
import java.util.*

class AddEditDebtorActivity : AppCompatActivity() {

    private val minDebtValue = 0.1
    private var addEditPresenter: AddEditPresenter = AddEditPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_debtor)

        addEditPresenter = intent.getSerializableExtra(AddEditPresenter.className) as AddEditPresenter

        if (!addEditPresenter.isNewDebtor) {
            editTextName.setText(addEditPresenter.debtorName)
            editTextOwed.setText(addEditPresenter.debtorOwed.toString())
            editTextPhoneNumber.setText(addEditPresenter.debtorPhoneNumber)

            buttonNotify.visibility = View.VISIBLE
            buttonSimulatePayment.visibility = View.VISIBLE
        } else {
            buttonNotify.visibility = View.INVISIBLE
            buttonSimulatePayment.visibility = View.INVISIBLE
        }

        textViewAddNewDebtor.text = addEditPresenter.title
        buttonAdd.text = addEditPresenter.buttonText

    }

    override fun onBackPressed() {
        cancelAddOrEditDebtor()
    }

    private fun cancelAddOrEditDebtor() {
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

    fun onButtonSaveOrAdd(view: View) {

        val debtorName: EditText = editTextName
        val debtorOwed: EditText = editTextOwed
        val debtorPhoneNumber: EditText = editTextPhoneNumber
        if (notFilled(debtorName, debtorOwed, debtorPhoneNumber)) {
            toastWith(getString(enter_debtor_info))
            return
        }

        val name: String = debtorName.text.toString()
        val owed: Double = debtorOwed.round()
        val phoneNumber: String = debtorPhoneNumber.text.toString()

        if (owed < minDebtValue) {
            toastWith(getString(debt_too_small_info))
            return
        }

        val replyIntent = Intent()
        if (addEditPresenter.isNewDebtor) {
            replyIntent.putExtra(
                debtorClassId,
                Debtor(
                    UUID.randomUUID().toString(),
                    name,
                    owed,
                    phoneNumber
                )
            )
            setResult(addDebtorCode, replyIntent)
        } else {
            replyIntent.putExtra(
                debtorClassId,
                Debtor(addEditPresenter.debtorId, name, owed, phoneNumber)
            )
            setResult(editDebtorCode, replyIntent)
        }

        finish()
    }

    private fun notFilled(
        debtorName: EditText,
        debtorOwed: EditText,
        debtorPhoneNumber: EditText
    ) = debtorName.text.isNullOrBlank() || debtorOwed.text.isNullOrBlank() || debtorPhoneNumber.text.isNullOrBlank()

    private fun toastWith(text: String) {
        makeText(
            applicationContext,
            text,
            LENGTH_SHORT
        ).show()
    }

    fun onButtonCancel(view: View) {
        cancelAddOrEditDebtor()
    }

    fun onButtonNotifyDebtor(view: View) {
        val debtorPhoneNumber = if (!editTextPhoneNumber.text.isNullOrEmpty()) editTextPhoneNumber.text else
            addEditPresenter.debtorPhoneNumber
        val debtorOwed = if (!editTextOwed.text.isNullOrEmpty()) editTextOwed.text else
            addEditPresenter.debtorOwed.toString()

        val intentSms = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                String.format(
                    getString(smsNumberToParse),
                    debtorPhoneNumber.toString()
                )
            )
        )
        intentSms.putExtra(
            getString(smsBody), String.format(getString(oweMoneyMsg), debtorOwed.toString())
        )

        if (intentSms.resolveActivity(packageManager) != null) {
            startActivity(intentSms)
        }
    }

    fun onButtonSimulatePayment(view: View) {
        val intent = Intent(this@AddEditDebtorActivity, DebtPaymentSimulation::class.java)
        intent.putExtra(AddEditPresenter.className, addEditPresenter)
        startActivity(intent)
    }
}
