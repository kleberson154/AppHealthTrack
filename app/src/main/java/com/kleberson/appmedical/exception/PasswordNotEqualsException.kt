package com.kleberson.appmedical.exception

class PasswordNotEqualsException(
    message: String = "As Senhas não são iguais",
) : Exception(message) {
    override val message: String
        get() = "Error: ${super.message}"
}