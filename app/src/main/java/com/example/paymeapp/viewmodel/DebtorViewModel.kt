package com.example.paymeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.paymeapp.Debtor
import com.example.paymeapp.database.DebtorRepository
import com.example.paymeapp.database.DebtorRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DebtorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DebtorRepository

    val allDebtors: LiveData<List<Debtor>>

    init {
        val debtorsDao = DebtorRoomDatabase.getDatabase(application, viewModelScope).debtorDao()
        repository = DebtorRepository(debtorsDao)
        allDebtors = repository.allDebtors
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(debtor: Debtor) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(debtor)
    }

    fun getAll(): List<Debtor> {
        val all = repository.getAll()
        if(all.value == null) return emptyList()
        return repository.getAll().value!!
    }

    fun update(updatedDebtor: Debtor) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(updatedDebtor)
    }
}