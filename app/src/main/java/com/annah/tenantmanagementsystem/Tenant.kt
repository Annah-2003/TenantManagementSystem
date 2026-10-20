package com.annah.tenantmanagementsystem

import java.io.Serializable

class Tenant(
    var name: String,
    var apartmentNumber: Int,
    initialRentAmount: Double,
    var phone: String = ""
) : Serializable {

    var isPaid: Boolean = false

    var rentAmount: Double = 0.0
        get() {
            return field
        }
        set(value) {
            if (value >= 0.0) {
                field = value
            } else {
                println("Rent amount cannot be negative.")
            }
        }

    init {
        // Initialize rentAmount using the custom setter to ensure validation
        rentAmount = initialRentAmount
    }

    fun payRent() {
        isPaid = true
        println("Rent paid successfully by $name")
    }

    fun summary(): String {
        val paidText = if (isPaid) "Paid" else "Pending"
        return "Tenant: $name\nApartment: $apartmentNumber\nPhone: $phone\nRent: $rentAmount\nStatus: $paidText"
    }
}