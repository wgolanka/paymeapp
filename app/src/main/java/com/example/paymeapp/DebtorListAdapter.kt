package com.example.paymeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DebtorListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DebtorListAdapter.DebtorViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var debtors = emptyList<Debtor>()

    inner class DebtorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val debtorItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtorViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return DebtorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DebtorViewHolder, position: Int) {
        val current = debtors[position]
        holder.debtorItemView.text = current.toString()
    }

    internal fun setDebtors(debtors: List<Debtor>) {
        this.debtors = debtors
        notifyDataSetChanged()
    }

    override fun getItemCount() = debtors.size
}