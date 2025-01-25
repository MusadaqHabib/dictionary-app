package com.example.mydictionary

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.mydictionary.models.DictionaryItem

class DatabaseOperations(context: Context) {

    private val dbHelper = DataBaseHelper(context)

    init {
        val isDatabaseCopiedSuccessfully = dbHelper.checkDatabase(context)

        if (!isDatabaseCopiedSuccessfully) {
            Log.e("DatabaseOperations", "Error: Unable to copy database file.")
            Log.e("DatabaseOperations", "Error: Database file does not exist.")
        }
    }

    fun insertData(word: String, meaning: String, category: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DataBaseHelper.COLUMN_word, word)
            put(DataBaseHelper.COLUMN_meaning, meaning)
            put(DataBaseHelper.COLUMN_category, category)
        }
        return db.insert(DataBaseHelper.TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun fetchDictionaryData(): List<DictionaryItem> {
        val dictionaryData = mutableListOf<DictionaryItem>()

        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DataBaseHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(DataBaseHelper.COLUMN_ID))
                val word = it.getString(it.getColumnIndex(DataBaseHelper.COLUMN_word))
                val meaning = it.getString(it.getColumnIndex(DataBaseHelper.COLUMN_meaning))
                val category = it.getString(it.getColumnIndex(DataBaseHelper.COLUMN_category))

                val dictionaryItem = DictionaryItem(id, word, meaning, category)
                dictionaryData.add(dictionaryItem)
            }
        }
        Log.e("DatabaseOperations", "Dictionary data size: ${dictionaryData.size}")
        return dictionaryData
    }
}
