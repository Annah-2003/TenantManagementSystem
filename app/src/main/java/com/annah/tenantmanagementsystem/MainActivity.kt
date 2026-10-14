package com.annah.tenantmanagementsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.annah.tenantmanagementsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentApartment: Apartment? = null
    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("EMAIL")?.let { email ->
            Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            saveTenant()
        }

        binding.payButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            tenant.payRent()
            refreshTenantList()
            Toast.makeText(this, "Rent marked as paid for ${tenant.name}", Toast.LENGTH_SHORT).show()
        }

        binding.callButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tenant.phone}"))
            startActivity(intent)
        }

        binding.shareButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, tenant.summary())
            startActivity(Intent.createChooser(intent, "Share tenant"))
        }
    }

    private fun saveTenant() {
        val apartmentNumber = binding.apartmentEditText.text.toString().trim().toIntOrNull()
        val name = binding.tenantNameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val rent = binding.rentEditText.text.toString().trim().toDoubleOrNull()

        var hasError = false
        hasError = requireValue(binding.apartmentEditText, apartmentNumber == null) || hasError
        hasError = requireValue(binding.tenantNameEditText, name.isEmpty()) || hasError
        hasError = requireValue(binding.phoneEditText, phone.isEmpty()) || hasError
        hasError = requireValue(binding.rentEditText, rent == null) || hasError

        if (hasError || apartmentNumber == null || rent == null) {
            Toast.makeText(this, "Please complete the tenant details", Toast.LENGTH_SHORT).show()
            return
        }

        val apartment = if (currentApartment?.apartmentNumber == apartmentNumber) {
            currentApartment!!
        } else {
            Apartment(apartmentNumber)
        }

        val tenant = Tenant(name, apartmentNumber, rent, phone)
        apartment.addTenant(tenant)
        currentApartment = apartment
        lastTenant = tenant

        refreshTenantList()
        Toast.makeText(this, "Tenant saved", Toast.LENGTH_SHORT).show()
    }

    private fun requireValue(field: EditText, isMissing: Boolean): Boolean {
        field.error = if (isMissing) "Required" else null
        return isMissing
    }

    private fun refreshTenantList() {
        val apartment = currentApartment
        binding.tenantResultTextView.text = apartment?.showTenants() ?: "Saved tenants will appear here."
    }
}
