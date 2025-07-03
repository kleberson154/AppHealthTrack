package com.kleberson.appmedical.view

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.appmedical.R
import com.kleberson.appmedical.controller.UserController

class AddMedicinesActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_medicines)
        val userEmail = intent.getStringExtra("email")
        if (userEmail == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userController = UserController(this)

        val nameMedicine = findViewById<EditText>(R.id.editTextNameMedicine)
        val quantityMedicine = findViewById<EditText>(R.id.editTextQuantityMedicine)
        val timeMedicine = findViewById<EditText>(R.id.editTextTimeMedicine)
        val dateMedicine = findViewById<CalendarView>(R.id.calendarView)
    }
}