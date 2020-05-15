package com.example.paymeapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paymeapp.*
import com.example.paymeapp.R.string.*
import com.example.paymeapp.adapter.DebtorListAdapter
import com.example.paymeapp.dto.Debtor
import com.example.paymeapp.presenter.AddEditPresenter
import com.example.paymeapp.util.round
import com.example.paymeapp.viewmodel.DebtorViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var debtViewModel: DebtorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = DebtorListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        debtViewModel = ViewModelProvider(this).get(DebtorViewModel::class.java)

        debtViewModel.allDebtors.observe(this, Observer { debtors ->
            debtors?.let {
                adapter.setDebtors(it)
                updateDebtSum()
                showNoDebtorsMsgIfNoDebtors()
            }
        })

        val addDebtor: Button = buttonAddDebtor
        addDebtor.setOnClickListener {
            addDebtor()
        }

        adapter.onItemClick = { debtor ->
            editDebtor(debtor)
        }

        adapter.onLongItemClick = { debtor ->
            cancelDebtFrom(debtor)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == addDebtorCode) {
            val newDebtor = data?.getSerializableExtra(debtorClassId) as Debtor
            if (debtViewModel.existsByName(newDebtor.name)) {
                editDebtor(newDebtor)
                Toast.makeText(
                    applicationContext,
                    "Please change name, debtor like this already exist",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                debtViewModel.insert(newDebtor)
            }
        } else {
            if (resultCode == editDebtorCode) { // edit debtor
                val updatedDebtor = data?.getSerializableExtra(debtorClassId) as Debtor
                debtViewModel.update(updatedDebtor)
            }
        }
    }

    private fun updateDebtSum() {
        val debtSum: TextView = numDebtSum
        val sum = debtViewModel.getAll().sumByDouble { it.owed }

        debtSum.text = String.format(getString(debtWithPln), sum.round().toString())
    }

    private fun showNoDebtorsMsgIfNoDebtors() {
        val noDebtorsTextView: TextView = textViewNoDebtors
        noDebtorsTextView.visibility = if (!debtViewModel.hasDebtors()) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun addDebtor() {
        val addDebtorPresenter = AddEditPresenter(
            getString(text_add_new_debtor),
            true, getString(add_new_debtor_button)
        )

        val intent = Intent(this@MainActivity, AddEditDebtorActivity::class.java)
        intent.putExtra(AddEditPresenter.className, addDebtorPresenter)
        startActivityForResult(intent, 1)
    }

    private fun editDebtor(debtor: Debtor) {
        val editDebtorPresenter = AddEditPresenter(
            getString(text_edit_debtor),
            false, getString(Save_msg),
            debtor.id, debtor.name, debtor.owed, debtor.phoneNumber
        )

        val intent = Intent(this@MainActivity, AddEditDebtorActivity::class.java)
        intent.putExtra(AddEditPresenter.className, editDebtorPresenter)
        startActivityForResult(intent, 2)
    }

    private fun cancelDebtFrom(debtor: Debtor): Boolean {
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
                shortToastWith(getString(debt_removed))
                debtViewModel.deleteOne(debtor.id)
            }

            setNegativeButton(getString(Cancel_msg)) { _, _ ->
                shortToastWith(getString(Cancel_msg))
            }
        }

        builder.show()
        return true
    }

    private fun shortToastWith(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}
