package com.example.paymeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.paymeapp.dto.Debtor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Debtor::class], version = 1, exportSchema = false)
abstract class DebtorRoomDatabase : RoomDatabase() {

    abstract fun debtorDao(): DebtorDao

    private class DebtorDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
//                    val debtDao = database.debtorDao()
//                    debtDao.deleteAll()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DebtorRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DebtorRoomDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DebtorRoomDatabase::class.java,
                    "debtor_database"
                )
                    .addCallback(DebtorDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(context: Context): DebtorRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DebtorRoomDatabase::class.java,
                    "debtor_database"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}