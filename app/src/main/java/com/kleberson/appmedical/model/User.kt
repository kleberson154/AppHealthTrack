package com.kleberson.appmedical.model

class User(val id: Int, val name: String, val email: String, val contact: String, val password: String, val medicines: List<Medicines> = emptyList()) {
    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email', contact='$contact', password='$password', activities=$medicines)"
    }
}