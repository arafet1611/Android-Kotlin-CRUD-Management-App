package com.example.tp5

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.lang.Exception

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "annuaire.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_NAME = "person"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TELEPHONE = "telephone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_RUE = "rue"
        const val COLUMN_VILLE = "ville"
        const val COLUMN_CODE_POSTALE = "code_postale"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_TELEPHONE TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_RUE TEXT,
                $COLUMN_VILLE TEXT,
                $COLUMN_CODE_POSTALE TEXT
            )
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }
fun insertIntoDb( myDataModel: MyDataModel):Long {
    val db = this.writableDatabase
    val contentValues = ContentValues()
    contentValues.put(COLUMN_ID , myDataModel.id)
    contentValues.put(COLUMN_NAME , myDataModel.name)
    contentValues.put(COLUMN_TELEPHONE , myDataModel.telephone)
    contentValues.put(COLUMN_EMAIL , myDataModel.email)
    contentValues.put(COLUMN_RUE , myDataModel.rue)
    contentValues.put(COLUMN_CODE_POSTALE , myDataModel.codePostale)
val success = db.insert(TABLE_NAME, null , contentValues)
    db.close()
    return success

}
    fun getAllDataFromDb(): ArrayList<MyDataModel> {
        val dataList: ArrayList<MyDataModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }



        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
                val telephoneIndex = cursor.getColumnIndex(COLUMN_TELEPHONE)
                val emailIndex = cursor.getColumnIndex(COLUMN_EMAIL)
                val rueIndex = cursor.getColumnIndex(COLUMN_RUE)
                val villeIndex = cursor.getColumnIndex(COLUMN_VILLE)
                val codePostaleIndex = cursor.getColumnIndex(COLUMN_CODE_POSTALE)

                if (idIndex != -1 && nameIndex != -1 && telephoneIndex != -1 &&
                    emailIndex != -1 && rueIndex != -1 && villeIndex != -1 && codePostaleIndex != -1) {
                    val id = cursor.getLong(idIndex)
                    val name = cursor.getString(nameIndex) ?: ""
                    val telephone = cursor.getString(telephoneIndex) ?: ""
                    val email = cursor.getString(emailIndex) ?: ""
                    val rue = cursor.getString(rueIndex) ?: ""
                    val ville = cursor.getString(villeIndex) ?: ""
                    val codePostale = cursor.getString(codePostaleIndex) ?: ""

                    val myDataModel = MyDataModel(id, name, telephone, email, rue, ville, codePostale)
                    dataList.add(myDataModel)
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return dataList
    }
    fun updatePerson(myDataModel: MyDataModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_NAME, myDataModel.name)
        contentValues.put(COLUMN_TELEPHONE, myDataModel.telephone)
        contentValues.put(COLUMN_EMAIL, myDataModel.email)
        contentValues.put(COLUMN_RUE, myDataModel.rue)
        contentValues.put(COLUMN_VILLE, myDataModel.ville)
        contentValues.put(COLUMN_CODE_POSTALE, myDataModel.codePostale)

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(myDataModel.id.toString())
        val rowsAffected = db.update(TABLE_NAME, contentValues, whereClause, whereArgs)

        db.close()

        if (rowsAffected > 0) {
            Log.d("DatabaseHelper", "updatePerson: Successfully updated data for name ${myDataModel.name}")
        } else {
            Log.d("DatabaseHelper", "updatePerson: Failed to update data for ID ${myDataModel.id}")
        }

        return rowsAffected
    }
    fun deletePerson(personId: Long): Int {
        val db = this.writableDatabase

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(personId.toString())

        val rowsAffected = db.delete(TABLE_NAME, whereClause, whereArgs)

        db.close()

        return rowsAffected
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}