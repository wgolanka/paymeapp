package com.example.paymeapp

data class Debtor(val name: String, var owned: Double) {

    override fun toString(): String {
        return "$name owns you $owned PLN"
    }
}
