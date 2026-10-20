package com.annah.tenantmanagementsystem

object TenantRepository {
    val allTenants = mutableListOf<Tenant>()

    fun addTenant(tenant: Tenant) {
        allTenants.add(tenant)
    }
}