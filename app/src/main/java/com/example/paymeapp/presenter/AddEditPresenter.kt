package com.example.paymeapp.presenter

import java.io.Serializable

data class AddEditPresenter(
    val title: String = "",
    val isNewDebtor: Boolean = true,
    val buttonText: String = "Ok",
    val debtorId: String = "",
    val debtorName: String = "",
    val debtorOwed: Double = 0.0,
    val debtorPhoneNumber: String = ""
) : Serializable {

    companion object {
        val className: String = AddEditPresenter::class.java.canonicalName
    }
}