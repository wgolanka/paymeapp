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
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allDebtors: LiveData<List<Debtor>>

    init {
        val wordsDao = DebtorRoomDatabase.getDatabase(application).debtorDao()
        repository = DebtorRepository(wordsDao)
        allDebtors = repository.allWords
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(debtor: Debtor) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(debtor)
    }
}