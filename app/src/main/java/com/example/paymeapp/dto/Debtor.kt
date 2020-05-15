package com.example.paymeapp.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "debtor_table")
data class Debtor(@PrimaryKey val id: String, var name: String, var owed: Double, var phoneNumber: String) :
    Serializable {

    override fun toString(): String {
        return "$name owe you $owed PLN"
    }
}
