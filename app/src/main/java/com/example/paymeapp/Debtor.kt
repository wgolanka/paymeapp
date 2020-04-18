package com.example.paymeapp

data class Debtor(val name: String, var owed: Double) {

    override fun toString(): String {
        return "$name owe you $owed PLN"
    }
}
