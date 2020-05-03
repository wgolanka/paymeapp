package com.example.paymeapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paymeapp.R.string.*
import com.example.paymeapp.util.round
import com.example.paymeapp.viewmodel.DebtorViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var debtViewModel: DebtorViewModel

    private var allDebtors: ArrayList<Debtor> = arrayListOf()

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
            }
        })

        val addDebtor: Button = buttonAddDebtor
        addDebtor.setOnClickListener { addDebtor() }

        adapter.onItemClick = { debtor ->
            editDebtor(debtor)
        }
//        listViewDebtors.onItemLongClickListener = onItemLongClickAction()
//        listViewDebtors.onItemClickListener = onDoubleTapAction()
//
//        showNoDebtorsMsgIfNoDebtors()
    }

    private fun updateDebtSum() {
        val debtSum: TextView = numDebtSum
        val sum = debtViewModel.getAll().sumByDouble { it.owed }

        debtSum.text = String.format(getString(debtWithPln), sum.round().toString())
    }

    private fun onItemLongClickAction(): AdapterView.OnItemLongClickListener {
        return AdapterView.OnItemLongClickListener { parent, view, position, id ->
            cancelDebtFrom(allDebtors[position])
        }
    }

    private fun showNoDebtorsMsgIfNoDebtors() {
        val noDebtorsTextView: TextView = textViewNoDebtors
        noDebtorsTextView.visibility = if (allDebtors.isEmpty()) {
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
            debtor.id, debtor.name, debtor.owed)

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
                toastWith(getString(debt_removed))
                removeDebtFrom(debtor)
            }

            setNegativeButton(getString(Cancel_msg)) { _, _ ->
                toastWith(getString(Cancel_msg))
            }
        }

        builder.show()
        return true
    }

    private fun toastWith(text: String) {
        Toast.makeText(
            applicationContext,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun removeDebtFrom(debtor: Debtor) {
//        debtViewModel.delete(debtor) //TODO
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //TODO extract codes
        if (resultCode == 1) { // add new debtor
            val newDebtor = data?.getSerializableExtra("Debtor") as Debtor
            debtViewModel.insert(newDebtor)
        } else if (resultCode == 2) { // edit debtor
            val updatedDebtor = data?.getSerializableExtra("Debtor") as Debtor
            debtViewModel.update(updatedDebtor) //TODO
        }
    }
}
