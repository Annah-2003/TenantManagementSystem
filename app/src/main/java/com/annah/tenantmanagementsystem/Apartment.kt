package com.annah.tenantmanagementsystem

class Apartment(val apartmentNumber: Int) {
    val tenants: MutableList<Tenant> = mutableListOf()

    fun addTenant(tenant: Tenant) {
        tenants.add(tenant)
    }

    fun showTenants(): String {
        val builder = StringBuilder("Apartment: $apartmentNumber")

        for (tenant in tenants) {
            builder.appendLine()
            builder.appendLine("Tenant: ${tenant.name}")
            builder.appendLine("Rent: ${tenant.rentAmount}")
            builder.append("Rent paid: ${tenant.isPaid}")
        }

        return builder.toString()
    }
}
