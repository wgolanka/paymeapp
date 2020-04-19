package com.example.paymeapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debtor_table")
data class Debtor(@PrimaryKey val name: String, var owed: Double) {

    override fun toString(): String {
        return "$name owe you $owed PLN"
    }
}
