package com.kleberson.appmedical.model

import java.time.LocalTime
import java.util.Date

data class Medicines(val id: Int, val name: String, val quantity: String, val time: Int, val dateLimit: Date, val atDate: LocalTime) {
    override fun toString(): String {
        return "Medicines(id=$id, name='$name', category='$quantity', type='$time', date=$dateLimit)"
    }
}