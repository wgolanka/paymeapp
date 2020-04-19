package com.example.paymeapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.paymeapp.R.string.*
import com.example.paymeapp.util.round
import com.example.paymeapp.viewmodel.DebtorViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val doubleClickTime: Long = 300

    private var adapter: ArrayAdapter<Debtor>? = null

    private lateinit var debtViewModel: DebtorViewModel

    companion object {
        var allDebtors: ArrayList<Debtor> = arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        debtViewModel = ViewModelProvider(this).get(DebtorViewModel::class.java)

        adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            allDebtors
        )

        val debtors: ListView = listViewDebtors
        debtors.adapter = adapter!!

        val addDebtor: Button = buttonAddDebtor
        addDebtor.setOnClickListener { addDebtor() }

        listViewDebtors.onItemLongClickListener = onItemLongClickAction()
        listViewDebtors.onItemClickListener = onDoubleTapAction()

        showNoDebtorsMsgIfNoDebtors()

        allDebtors.add(Debtor("First", 20.0)) //TODO temporary
        allDebtors.add(Debtor("Second", 30.0))
        adapter?.notifyDataSetChanged()
        updateDebtSum()
    }

    private fun updateDebtSum() {
        val debtSum: TextView = numDebtSum
        val sum = allDebtors.sumByDouble { it.owed }

        debtSum.text = String.format(getString(debtWithPln), sum.round().toString())
    }

    private fun onItemLongClickAction(): AdapterView.OnItemLongClickListener {
        return AdapterView.OnItemLongClickListener { parent, view, position, id ->
            cancelDebtFrom(allDebtors[position])
        }
    }

    private fun onDoubleTapAction(): AdapterView.OnItemClickListener {
        var lastClickTime: Long = 0
        return AdapterView.OnItemClickListener { parent, view, position, id ->
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < doubleClickTime) {
                val clickedDebtor = allDebtors[position]
                editDebtor(clickedDebtor.name, clickedDebtor.owed)
            }
            lastClickTime = clickTime
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

    override fun onResume() {
        super.onResume()

        showNoDebtorsMsgIfNoDebtors()
        adapter?.notifyDataSetChanged()
        updateDebtSum()
    }

    private fun addDebtor() {
        val intent = Intent(this, AddEditDebtorActivity::class.java)
        val addDebtorPresenter = AddEditPresenter(
            getString(text_add_new_debtor),
            true, getString(add_new_debtor_button)
        )
        intent.putExtra(AddEditPresenter.className, addDebtorPresenter)
        startActivity(intent)
    }

    private fun editDebtor(debtorName: String, debtorOwed: Double) {
        val intent = Intent(this, AddEditDebtorActivity::class.java)

        val editDebtorPresenter = AddEditPresenter(
            getString(text_edit_debtor),
            false, getString(Save_msg),
            debtorName, debtorOwed
        )
        intent.putExtra(AddEditPresenter.className, editDebtorPresenter)

        startActivity(intent)
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
        allDebtors.remove(debtor)
        adapter?.notifyDataSetChanged()
        updateDebtSum()
        showNoDebtorsMsgIfNoDebtors()
    }
}
