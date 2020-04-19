package com.example.paymeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.paymeapp.Debtor

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Debtor::class], version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun debtorDao(): DebtorDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "debtor_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}