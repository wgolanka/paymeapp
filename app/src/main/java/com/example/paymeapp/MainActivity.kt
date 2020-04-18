package com.example.paymeapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.paymeapp.R.string.*
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

        allDebtors.add(Debtor("First", 20.0)) //TODO temporary
        allDebtors.add(Debtor("Second", 30.0))
        adapter?.notifyDataSetChanged()

        debtors.adapter = adapter!!
        addDebtor.setOnClickListener {
            openAddDebtorActivity()
        }

        listViewDebtors.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                debtRemission(allDebtors[position])
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

    private fun debtRemission(debtor: Debtor): Boolean {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setTitle(String.format(getString(remove_debt_from), debtor.name))
            setMessage(
                String.format(
                    getString(debt_cancel_inside_msg),
                    debtor.name,
                    debtor.owed.toString()
                )
            )

            setPositiveButton(getString(remove_debt_btn_msg)) { _, _ ->
                Toast.makeText(
                    applicationContext,
                    getString(debt_removed),
                    Toast.LENGTH_SHORT
                ).show()

                removeDebtFrom(debtor)
            }

            setNegativeButton(getString(Cancel_msg)) { _, _ ->
                Toast.makeText(
                    applicationContext,
                    getString(Cancel_msg), Toast.LENGTH_SHORT
                ).show()
            }
        }

        builder.show()

        return true
    }

    private fun removeDebtFrom(debtor: Debtor) {
        allDebtors.remove(debtor)
        adapter?.notifyDataSetChanged()
        updateDebtSum()
        //TODo when debtors size = 0, show no debtors msg
    }
}
