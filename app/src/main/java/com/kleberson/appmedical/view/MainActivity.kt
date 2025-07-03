package com.kleberson.appmedical.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kleberson.appmedical.R
import com.kleberson.appmedical.controller.UserController
import com.kleberson.appmedical.util.VerticalSpaceItemDecoration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val emailUser = intent.getStringExtra("email")
        if (emailUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userController = UserController(this)
        val user = userController.getUserByEmail(emailUser)
        val medicines = userController.getMedicinesUser(emailUser)

        val userName = findViewById<TextView>(R.id.textViewNameUser)
        userName.text = user.name

        val btnAddMedicamentos = findViewById<Button>(R.id.buttonAddMedicamentos)
        val btnFinalizar = findViewById<Button>(R.id.buttonFinalizar)

        val recycleActivities = findViewById<RecyclerView>(R.id.recycleMedicines)
        recycleActivities.addItemDecoration(VerticalSpaceItemDecoration(16))
        val adapter = MedicineAdapter(medicines)
        recycleActivities.layoutManager = LinearLayoutManager(this)
        recycleActivities.adapter = adapter

        btnAddMedicamentos.setOnClickListener {
            startActivity(Intent(this, AddMedicinesActivity::class.java).putExtra("email", emailUser))
        }

        btnFinalizar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val emailUser = intent.getStringExtra("email")
        if (emailUser != null) {
            val userController = UserController(this)
            val medicines = userController.getMedicinesUser(emailUser)
            val adapter = MedicineAdapter(medicines)
            val recycleActivities = findViewById<RecyclerView>(R.id.recycleMedicines)
            recycleActivities.adapter = adapter
        }
    }
}