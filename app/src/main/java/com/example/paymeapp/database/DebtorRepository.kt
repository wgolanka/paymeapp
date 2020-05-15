package com.example.paymeapp.database

import androidx.lifecycle.LiveData
import com.example.paymeapp.dto.Debtor

class DebtorRepository(private val debtorDao: DebtorDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allDebtors: LiveData<List<Debtor>> = debtorDao.getAllDebtorsAlphabetised()

    suspend fun insert(debtor: Debtor) {
        debtorDao.insert(debtor)
    }

    fun getAll(): LiveData<List<Debtor>> {
        return allDebtors
    }

    suspend fun update(updatedDebtor: Debtor) {
        return debtorDao.update(
            updatedDebtor.id,
            updatedDebtor.name,
            updatedDebtor.owed,
            updatedDebtor.phoneNumber
        )
    }

    suspend fun deleteOne(debtorId: String) {
        return debtorDao.deleteOne(debtorId)
    }
}