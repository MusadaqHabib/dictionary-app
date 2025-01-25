package com.example.mydictionary
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.*

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "more.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "other"
        const val COLUMN_ID = "id"
        const val COLUMN_word = "word"
        const val COLUMN_meaning = "meaning"
        const val COLUMN_category = "category"
    }

    init {
        if (!checkDatabase(context)) {
            copyDatabase(context)
        }
    }

    fun checkDatabase(context: Context): Boolean {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        return dbFile.exists()
    }

    private fun copyDatabase(context: Context) {
        try {
            val inputStream: InputStream = context.assets.open(DATABASE_NAME)
            val outputStream: OutputStream = FileOutputStream(context.getDatabasePath(DATABASE_NAME))

            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
       /* val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_word TEXT,
                $COLUMN_meaning TEXT,
                $COLUMN_category TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)*/
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed later
    }
}
