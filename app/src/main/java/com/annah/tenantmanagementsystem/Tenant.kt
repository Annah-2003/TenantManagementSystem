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
        get() = field
        set(value) {
            if (value >= 0.0) field = value
        }

    val rent: String
        get() = rentAmount.toString()

    init {
        rentAmount = initialRentAmount
    }

    constructor(name: String, phone: String, rent: String) : this(
        name = name,
        apartmentNumber = 0,
        initialRentAmount = rent.toDoubleOrNull() ?: 0.0,
        phone = phone
    )

    fun payRent() {
        isPaid = true
    }

    fun summary(): String = "Tenant: $name\nPhone: $phone\nRent: KSh $rent"
}
