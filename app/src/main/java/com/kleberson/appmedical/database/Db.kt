package com.kleberson.appmedical.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.kleberson.appmedical.model.Medicines
import com.kleberson.appmedical.model.User
import java.text.SimpleDateFormat
import java.util.Date

class Db(context: Context): SQLiteOpenHelper(context, "healthTrack.db", null, 1) {
    override fun onCreate(db: android.database.sqlite.SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, contact TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS medicines (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, quantity TEXT, time TEXT, dateLimit TEXT, user_id INTEGER, FOREIGN KEY(user_id) REFERENCES users(id))")
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

    @SuppressLint("SimpleDateFormat")
    fun insertMedicine(user: User, medicineName: String, medicineQuantity: String, medicineTime: String, medicineDateLimit: Date) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", medicineName)
            put("quantity", medicineQuantity)
            put("time", medicineTime)
            put("dateLimit", SimpleDateFormat("yyyy-MM-dd").format(medicineDateLimit))
            put("user_id", user.id)
        }
        db.insert("activities", null, values)
        db.close()
    }

    @SuppressLint("Range", "SimpleDateFormat")
    fun getMedicinesByUser(id: Int): MutableList<Medicines> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM activities WHERE user_id = ?", arrayOf(id.toString()))
        val activities = mutableListOf<Medicines>()

        if (cursor.moveToFirst()) {
            do {
                val idMedicines = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val quantity = cursor.getString(cursor.getColumnIndex("category"))
                val time = cursor.getString(cursor.getColumnIndex("type"))
                val dateLimit = SimpleDateFormat("yyyy-MM-dd").format(cursor.getString(cursor.getColumnIndex("date")))

                activities.add(Medicines(idMedicines, name, quantity, time, dateLimit))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return activities
    }

    fun deleteMedicine(medicines: Medicines) {
        val db = writableDatabase
        db.delete("activities", "id = ?", arrayOf(medicines.id.toString()))
        db.close()
    }

}