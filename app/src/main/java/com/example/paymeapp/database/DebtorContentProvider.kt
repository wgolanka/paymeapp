package com.example.paymeapp.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.paymeapp.dto.Debtor

class DebtorContentProvider(private val contentProvider: ContentProvider) {

    private val authority = "com.example.android.DebtorContentProvider.provider"

    private val debtorTableName = "debtor_table"

    private val codeDebtorDirectory = 1

    private val codeDebtorItem = 2

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI(authority, debtorTableName, codeDebtorDirectory)
        uriMatcher.addURI(authority, "$debtorTableName/*", codeDebtorItem)
    }

    private fun debtorFromContentValues(values: ContentValues?): Debtor? {
        val id = "id"
        val name = "name"
        val owed = "owed"
        val phoneNumber = "phoneNumber"
        if (values != null
            && values.containsKey(id)
            && values.containsKey(name)
            && values.containsKey(owed)
            && values.containsKey(phoneNumber)
        ) {
            return Debtor(
                values.getAsString(id),
                values.getAsString(name),
                values.getAsDouble(owed),
                values.getAsString(phoneNumber)
            )
        }

        return null
    }

    @Override
    fun query(
        uri: Uri, projection: Array<String?>?, selection: String?,
        selectionArgs: Array<String?>?, sortOrder: String?
    ): Cursor? {
        val code: Int = uriMatcher.match(uri)
        return if (code == codeDebtorDirectory || code == codeDebtorItem) {
            val context: Context = contentProvider.context ?: return null
            val debtorDao: DebtorDao = DebtorRoomDatabase.getInstance(context).debtorDao()

            val cursor = if (code == codeDebtorDirectory) {
                debtorDao.selectAll()
            } else {
                debtorDao.selectById(uri.lastPathSegment ?: "")
            }

            cursor?.setNotificationUri(context.contentResolver, uri)
            cursor
        } else {
            throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    @Override
    suspend fun insert(uri: Uri, values: ContentValues?): Boolean {
        when (uriMatcher.match(uri)) {
            codeDebtorDirectory -> {
                val context: Context = contentProvider.context ?: return false

                val debtor = debtorFromContentValues(values) ?: return false
                DebtorRoomDatabase.getInstance(context).debtorDao()
                    .insert(debtor)

                context.contentResolver.notifyChange(uri, null)
                return true
            }
            codeDebtorItem -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    @Override
    suspend fun delete(
        uri: Uri, selection: String?,
        selectionArgs: Array<String?>?
    ): Boolean {
        when (uriMatcher.match(uri)) {
            codeDebtorDirectory -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            codeDebtorItem -> {
                val context: Context = contentProvider.context ?: return false
                val id = uri.lastPathSegment ?: return false
                DebtorRoomDatabase.getInstance(context).debtorDao().deleteOne(id)
                context.contentResolver.notifyChange(uri, null)
                return true
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    @Override
    suspend fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String?>?
    ): Boolean {
        when (uriMatcher.match(uri)) {
            codeDebtorDirectory -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            codeDebtorItem -> {
                val context: Context = contentProvider.context ?: return false
                val debtor = debtorFromContentValues(values) ?: return false
                DebtorRoomDatabase.getInstance(context).debtorDao().update(
                    debtor.id,
                    debtor.name,
                    debtor.owed,
                    debtor.phoneNumber
                )
                context.contentResolver.notifyChange(uri, null)
                return true
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}