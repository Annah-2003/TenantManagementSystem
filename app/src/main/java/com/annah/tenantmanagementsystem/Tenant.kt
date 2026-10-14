package com.annah.tenantmanagementsystem

class Tenant(
    var name: String,
    var apartmentNumber: Int,
    initialRentAmount: Double,
    var phone: String = ""
) {
    var isPaid: Boolean = false

    var rentAmount: Double = 0.0
        get() {
            println("Rent amount accessed.")
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
        // The constructor receives the tenant's starting details, so every object is complete as soon as it is created.
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
