package com.annah.tenantmanagementsystem

object TenantRepository {
    val tenants = mutableListOf<Tenant>()

    fun addTenant(tenant: Tenant) {
        tenants.add(tenant)
    }

    fun totalRent(): Double = tenants.sumOf { it.rentAmount }
}
