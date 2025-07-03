package com.kleberson.appmedical.exception

class UserExistException(
    message: String = "O usuario já existe",
) : Exception(message) {
    override val message: String
        get() = "Erro: ${super.message}"
}