package com.kleberson.appmedical.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.kleberson.appmedical.model.Medicines
import com.kleberson.appmedical.model.User
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

class Db(context: Context): SQLiteOpenHelper(context, "healthTrack.db", null, 1) {
    override fun onCreate(db: android.database.sqlite.SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, contact TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS medicines (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, quantity TEXT, time INT, dateLimit TEXT, atDate TEXT, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))")
    }

    override fun onUpgrade(db: android.database.sqlite.SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS medicines")
        onCreate(db)
    }

    fun checkUserExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("contact", user.contact)
        }
        db.insert("users", null, values)
        db.close()
    }

    fun verifyPassword(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun getUserByEmail(emailUser: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(emailUser))

        if (cursor.moveToFirst()) {
            val idUser = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val contact = cursor.getString(cursor.getColumnIndex("contact"))

            cursor.close()
            return User(idUser, name, email, password, contact)
        } else{
            cursor.close()
            return null
        }
    }


    fun deleteMedicine(medicines: Medicines) {
        val db = writableDatabase
        db.delete("medicines", "id = ?", arrayOf(medicines.id.toString()))
        db.close()
    }

    fun insertMedicine(medicines: Medicines, id: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", medicines.name)
            put("quantity", medicines.quantity)
            put("time", medicines.time)
            put("dateLimit", medicines.dateLimit.toString())
            put("atDate", medicines.atDate.toString())
            put("user_id", id)
        }
        db.insert("medicines", null, values)
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun getMedicinesByUserId(id: Int): MutableList<Medicines> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM medicines WHERE user_id = ?", arrayOf(id.toString()))
        val medicinesList = mutableListOf<Medicines>()

        if (cursor.moveToFirst()) {
            do {
                val idMedicine = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val quantity = cursor.getString(cursor.getColumnIndex("quantity"))
                val time = cursor.getInt(cursor.getColumnIndex("time"))
                val dateLimit = Date(cursor.getString(cursor.getColumnIndex("dateLimit")))
                val atDate = LocalTime.parse(cursor.getString(cursor.getColumnIndex("atDate")))

                medicinesList.add(Medicines(idMedicine, name, quantity, time, dateLimit, atDate))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return medicinesList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeExpiredMedicines(id: Int) {
        val db = writableDatabase
        val currentDate = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).toString()
        db.delete("medicines", "dateLimit < ?", arrayOf(currentDate))
        db.close()
    }

    fun updateMedicineTime(medicine: Medicines) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("atDate", medicine.atDate.toString())
        }
        db.update("medicines", values, "id = ?", arrayOf(medicine.id.toString()))
        db.close()
    }

}

//INSERT INTO medicines (name, quantity, time, dateLimit, atDate, user_id)
//VALUES ('NomeDoRemedio', 'Quantidade', 8, '03/07/2025', '08:00', 1);