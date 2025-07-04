package com.kleberson.appmedical.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.appmedical.R
import com.kleberson.appmedical.controller.UserController
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

class AddMedicinesActivity: AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
        val btnAddMedicinesActivity = findViewById<Button>(R.id.buttonAddMedicine)
        var selectedDate = Date(dateMedicine.date)
        dateMedicine.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            selectedDate = calendar.time
        }

        btnAddMedicinesActivity.setOnClickListener{
            val name = nameMedicine.text.toString()
            val quantity = quantityMedicine.text.toString()
            val time = timeMedicine.text.toString()
            val dateLimit = selectedDate
            val atDate = LocalTime.now(ZoneId.of("America/Sao_Paulo"))

            if (name.isNotEmpty() && quantity.isNotEmpty() && time.isNotEmpty()) {
                userController.addMedicine(this, name = name, quantity = quantity, time = time.toInt(), dateLimit = dateLimit, atDate = atDate, emailUser = userEmail)
                finish()
            } else {

                nameMedicine.error = "Preencha todos os campos"
            }
        }
    }
}