package com.kleberson.appmedical.model

data class Medicines(val id: Int, val name: String, val quantity: String, val time: String, val dateLimit: String) {
    override fun toString(): String {
        return "Medicines(id=$id, name='$name', category='$quantity', type='$time', date=$dateLimit)"
    }
}