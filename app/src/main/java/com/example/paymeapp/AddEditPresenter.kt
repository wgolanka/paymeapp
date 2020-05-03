package com.example.paymeapp

import java.io.Serializable

data class AddEditPresenter(
    val title: String = "",
    val isNewDebtor: Boolean = true,
    val buttonText: String = "Ok",
    val debtorId: String = "",
    val debtorName: String = "",
    val debtorOwed: Double = 0.0
) : Serializable {

    companion object {
        val className: String = AddEditPresenter::class.java.canonicalName
    }
}