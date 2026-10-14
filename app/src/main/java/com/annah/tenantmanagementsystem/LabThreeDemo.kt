package com.annah.tenantmanagementsystem

fun main() {
    // Two objects from the same class can store different values because each object has its own copy of the properties.
    val firstTenant = Tenant("John", 101, 15000.0, "0712345678")
    val secondTenant = Tenant("Mary", 101, 18000.0, "0798765432")

    firstTenant.payRent()
    println("${firstTenant.name} paid: ${firstTenant.isPaid}")
    println("${secondTenant.name} paid: ${secondTenant.isPaid}")

    // A constructor is useful because it lets us give the tenant all important details at the moment we create the object.
    val testTenant = Tenant("Amina", 202, 12000.0)
    testTenant.rentAmount = -500.0

    // Getting a property reads its value; setting a property tries to change its value.
    // The getter runs whenever rentAmount is accessed, like in the println below.
    println(testTenant.rentAmount)

    val apartment = Apartment(101)
    apartment.addTenant(firstTenant)
    apartment.addTenant(secondTenant)
    // Composition means an Apartment object has Tenant objects inside its tenants list.
    println(apartment.showTenants())
}
