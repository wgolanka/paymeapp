package com.example.paymeapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.R.string.debtWithPln

import com.example.paymeapp.util.round
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var allDebtors: ArrayList<Debtor> = arrayListOf()
    }

    override fun onResume() {
        super.onResume()

        val noDebtorsTextView: TextView = textViewNoDebtors
        noDebtorsTextView.visibility = if (allDebtors.isEmpty()) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        updateDebtSum()
    }

    private var adapter: ArrayAdapter<Debtor>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val debtors: ListView = listViewDebtors
        val addDebtor: Button = buttonAddDebtor

        adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            allDebtors
        )

        debtors.adapter = adapter!!
        addDebtor.setOnClickListener {
            openAddDebtorActivity()
        }
    }

    private fun updateDebtSum() {
        val debtSum: TextView = numDebtSum
        val sum = allDebtors.sumByDouble { it.owed }

        debtSum.text = String.format(getString(debtWithPln), sum.round().toString())
    }

    private fun openAddDebtorActivity() {
        val intent = Intent(this, AddDebtorActivity::class.java)
        startActivity(intent)
    }
}
