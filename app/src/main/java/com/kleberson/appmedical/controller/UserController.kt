package com.kleberson.appmedical.controller

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.kleberson.appmedical.database.Db
import com.kleberson.appmedical.exception.PasswordNotEqualsException
import com.kleberson.appmedical.exception.UserExistException
import com.kleberson.appmedical.exception.UserNotExistException
import com.kleberson.appmedical.model.Medicines
import com.kleberson.appmedical.model.User
import java.time.LocalTime
import java.util.Date

class UserController(context: Context) {
    private val db = Db(context)

    fun registerUser(context: Context, name: String, email: String, contact: String, password: String, confirmPassword: String): Boolean {
        val userExists = db.checkUserExists(email)

        try {
            if (userExists){
                throw UserExistException()
            }

            if (password != confirmPassword) {
                throw PasswordNotEqualsException()
            }

            val user = User(id = 0, name = name, email = email, contact = contact, password = password)

            db.insertUser(user)
            return true
        }catch (e: UserExistException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        }catch (e: PasswordNotEqualsException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun loginUser(context: Context, email: String, password: String): Boolean {
        val userExists = db.checkUserExists(email)
        val passwordMatches = db.verifyPassword(email ,password)

        try {
            if (!userExists) {
                throw UserNotExistException("Usuário não encontrado")
            }

            if (!passwordMatches) {
                throw PasswordNotEqualsException("Senha incorreta")
            }
            return true
        } catch (e: UserNotExistException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        } catch (e: PasswordNotEqualsException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return false
        }
    }

    fun getUserByEmail(emailUser: String): User {
        val user = db.getUserByEmail(emailUser) ?: throw UserNotExistException("Usuário não encontrado")

        return user
    }


    fun deleteMedicine(medicines: Medicines, context: Context) {
        try {
            db.deleteMedicine(medicines)
            Toast.makeText(context, "Medicamento removido com sucesso!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao remover medicamento: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMedicine(
        context: Context, name: String, quantity: String, time: Int, dateLimit: Date, atDate: LocalTime,
        emailUser: String) {
        try {
            val user = getUserByEmail(emailUser)
            val medicines = Medicines(id = 0, name = name, quantity = quantity, time = time, dateLimit = dateLimit, atDate = atDate)
            db.insertMedicine(medicines, user.id)
            Toast.makeText(context, "Medicamento adicionado com sucesso!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao adicionar medicamento: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMedicinesUser(emailUser: String): MutableList<Medicines> {
        val user = getUserByEmail(emailUser)
        val medicinesList = db.getMedicinesByUserId(user.id)

        if (medicinesList.isEmpty()) {
            return mutableListOf()
        }

        return medicinesList
    }
}