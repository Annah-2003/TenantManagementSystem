package com.annah.tenantmanagementsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log // Added for lifecycle logging
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

        // Part A: Log onCreate
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Part B: Restore State after rotation
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("tenant_name")
            val phone = savedInstanceState.getString("tenant_phone")
            val rent = savedInstanceState.getDouble("tenant_rent", 0.0)
            val aptNum = savedInstanceState.getInt("tenant_apt", 0)

            if (name != null && phone != null) {
                val restoredTenant = Tenant(name, aptNum, rent, phone)
                lastTenant = restoredTenant
                currentApartment = Apartment(aptNum).apply { addTenant(restoredTenant) }
                refreshTenantList()
            }
        }

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

    // ── PART B: SAVE STATE ─────────────────────────────────────────
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onSaveInstanceState")

        val tenant = lastTenant
        if (tenant != null) {
            outState.putString("tenant_name", tenant.name)
            outState.putString("tenant_phone", tenant.phone)
            outState.putDouble("tenant_rent", tenant.rentAmount)
            outState.putInt("tenant_apt", tenant.apartmentNumber)
        }
    }

    // ── PART A: LIFECYCLE METHODS ──────────────────────────────────
    override fun onStart() {
        super.onStart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy")
    }

    // ── EXISTING HELPER METHODS ────────────────────────────────────
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
        TenantRepository.addTenant(tenant)

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

