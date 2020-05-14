package com.example.paymeapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.util.round
import kotlinx.android.synthetic.main.activity_debt_payment_simulation.*

class DebtPaymentSimulation : AppCompatActivity() {

    private var addEditPresenter: AddEditPresenter = AddEditPresenter()

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_payment_simulation)
        addEditPresenter = intent.getSerializableExtra(AddEditPresenter.className) as AddEditPresenter
        editTextDebtorBasicInfo.setText("${addEditPresenter.debtorName}, ${addEditPresenter.debtorPhoneNumber}")
        editTextDebtorDebt.setText("Owed: ${addEditPresenter.debtorOwed}")
    }

    fun startSimulation(view: View) {
        val payRateText = editTextPaySpeed.text
        val commissionText = editTextInterestRate.text
        if (payRateText.isNullOrBlank()) {
            toastWith("Pay rate is required")
            return
        }
        val payRate = payRateText.toString().toDouble()

        if (commissionText.isNullOrBlank() || commissionText.toString().toDouble() <= 0) {
            toastWith("Commission is required and must be bigger than 0")
            return
        }

        val commission = commissionText.toString().toDouble() // todo should be in %
        var commissionSum = 0.0
        var debt = addEditPresenter.debtorOwed
        var firstIteration = true

        val runnable = object : Runnable {
            override fun run() {

                val previousDebt = debt
                if (!firstIteration) {
                    debt -= debt * commission
                    commissionSum += previousDebt - debt
                }

                debt -= payRate

                if (debt <= 0) {
                    editTextDebtorDebt.setText("0")
                    toastWith("commission together was ${commissionSum.round()}")
                    return
                }
                editTextDebtorDebt.setText(debt.round().toString())
                firstIteration = false
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    fun stopSimulation(view: View) {
        toastWith("I'm not implemented :(")
    }

    private fun toastWith(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}
