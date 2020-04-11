package com.example.paymeapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var allDebtors: ArrayList<Debtor> = arrayListOf()
    private var adapter: ArrayAdapter<Debtor>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val debtors: ListView = findViewById(R.id.listView_debtors)
        val addDebtor: Button = findViewById(R.id.button_add_debtor)

        adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            allDebtors
        )

        debtors.adapter = adapter

        val debtor1 = Debtor("Adam", 2456.12)
        val debtor2 = Debtor("Julie", 12.12)

        addDebtor.setOnClickListener {

            allDebtors.add(debtor1)
            allDebtors.add(debtor2)
            adapter!!.notifyDataSetChanged()

            updateDebtSum() //TODO where to put this method so it runs always after adding new debtor?
        }
        updateDebtSum()
    }

    private fun updateDebtSum() {
        val debtSum: TextView = findViewById(R.id.num_debt_sum)
        var sum = 0.0
        for (debtor in allDebtors) {
            sum += debtor.owned
        }
        //TODO round the sum number, it gets ugly with big numbers
        debtSum.text = "$sum PLN"
    }
}
