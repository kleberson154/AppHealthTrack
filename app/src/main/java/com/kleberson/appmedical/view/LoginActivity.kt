package com.kleberson.appmedical.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kleberson.appmedical.R
import com.kleberson.appmedical.controller.UserController

class LoginActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val inputEmail = findViewById<EditText>(R.id.editTextEmailLogin)
        val inputPassword = findViewById<EditText>(R.id.editTextPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        val btnSendSignUp = findViewById<Button>(R.id.buttonSignUp)

        val sharedPref = getSharedPreferences("healthTrack_prefs", MODE_PRIVATE)
        inputEmail.setText(sharedPref.getString("email", ""))
        inputPassword.setText(sharedPref.getString("password", ""))

        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                inputEmail.error = "Email é obrigatório"
                inputPassword.error = "Senha é obrigatória"
                return@setOnClickListener
            }

            sharedPref.edit().clear().putString("email", email).putString("password", password).apply()

            val userController = UserController(this)
            val isLoginSuccessful = userController.loginUser(this, email, password)

            if (isLoginSuccessful){
                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtra("email", email)
                })
            }
        }

        btnSendSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}