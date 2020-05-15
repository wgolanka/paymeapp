package com.example.paymeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.paymeapp.R
import com.example.paymeapp.dto.Debtor

class DebtorListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DebtorListAdapter.DebtorViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var onItemClick: ((Debtor) -> Unit)? = null

    var onLongItemClick: ((Debtor) -> Unit)? = null

    private var debtors = emptyList<Debtor>()

    inner class DebtorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val debtorItemView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(debtors[adapterPosition])
            }

            itemView.setOnLongClickListener {
                onLongItemClick?.invoke(debtors[adapterPosition])
                return@setOnLongClickListener true
            }
        }
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