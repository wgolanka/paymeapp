package com.example.paymeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    companion object {
        var allDebtors: ArrayList<Debtor> = arrayListOf()
    }

    private var adapter: ArrayAdapter<Debtor>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val debtors: ListView = findViewById(R.id.listView_debtors)
        val addDebtor: Button = findViewById(R.id.button_add_debtor)
        val noDebtorsTextView: TextView = findViewById(R.id.textView_no_debtors)

        adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            allDebtors
        )

        debtors.adapter = adapter
        addDebtor.setOnClickListener {
            openAddDebtorActivity() //TODO I don't notify adapter about changed debtors and it still updated them
        }

        if (allDebtors.isEmpty()) { // TODO where to put it so it changes after debtor is added?
            noDebtorsTextView.visibility = View.VISIBLE
        } else {
            noDebtorsTextView.visibility = View.INVISIBLE
        }
    }

    private fun updateDebtSum() { //TODO where to put this method so it runs always after adding new debtor?
        val debtSum: TextView = findViewById(R.id.num_debt_sum)
        var sum = 0.0
        for (debtor in allDebtors) {
            sum += debtor.owned
        }
        sum = sum.toBigDecimal().setScale(2, RoundingMode.UP).toDouble() //TODO add tests for this
        debtSum.text = "$sum PLN"
    }

    private fun openAddDebtorActivity() {
        val intent = Intent(this, AddDebtorActivity::class.java)
        startActivity(intent)
    }
}
