package com.example.paymeapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paymeapp.Debtor
import java.util.*

@Dao
interface DebtorDao {

    @Query("SELECT * from debtor_table ORDER BY name ASC")
    fun getAllDebtorsAlphabetised(): LiveData<List<Debtor>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(debtor: Debtor)

    @Query("DELETE FROM debtor_table WHERE name == :debtorName")
    suspend fun deleteOne(debtorName: String)

    @Query("DELETE FROM debtor_table")
    suspend fun deleteAll()

    @Query("UPDATE debtor_table SET name = :updatedName, owed = :updatedDebt WHERE id == :id")
    suspend fun update(id: String, updatedName: String, updatedDebt : Double)
}