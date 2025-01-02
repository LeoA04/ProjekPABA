package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionActivity : AppCompatActivity() {

    private lateinit var rvTransaction: RecyclerView
    private val selectedServices = mutableListOf<HashMap<String, String>>() // List to hold multiple services
    private lateinit var adapter: TransactionAdapter
    private val db = FirebaseFirestore.getInstance()
    private var username: String = "Guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // Get username from intent or preferences
        username = intent.getStringExtra("username") ?: getUsernameFromPreferences()

        // Load cart data for the current user
        loadCartData()

        // Get new service data from intent and add it to the cart
        val serviceData = intent.getSerializableExtra("selectedService") as? HashMap<String, String>
        if (serviceData != null) {
            selectedServices.add(serviceData)
            saveCartData() // Save updated cart
        }

        // Set up adapter
        adapter = TransactionAdapter(selectedServices) { position ->
            // Handle item removal
            selectedServices.removeAt(position)
            saveCartData() // Save updated cart
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Service removed", Toast.LENGTH_SHORT).show()
        }

        rvTransaction.adapter = adapter

        // Button to return to the recommendation page
        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)
        btnBackToDetail.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Confirm order button functionality
        findViewById<Button>(R.id.btnConfirmOrder).setOnClickListener {
            if (selectedServices.isEmpty()) {
                Toast.makeText(this, "No services selected to confirm.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveToHistory()
        }
    }

    private fun saveToHistory() {
        selectedServices.forEach { service ->
            val transactionMap = hashMapOf(
                "userName" to username,
                "namaService" to service["namaService"],
                "namaPerusahaan" to service["namaPerusahaan"],
                "foto" to service["foto"],
                "harga" to service["harga"]
            )

            db.collection("transactionHistory")
                .add(transactionMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Order confirmed successfully.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to confirm order: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Clear cart after confirmation
        selectedServices.clear()
        saveCartData()
        adapter.notifyDataSetChanged()

        // Navigate to history after confirming all orders
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun saveCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(selectedServices)
        editor.putString("cart_$username", json) // Associate cart with username
        editor.apply()
    }

    private fun loadCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cart_$username", null) // Load cart for username
        val type = object : TypeToken<MutableList<HashMap<String, String>>>() {}.type
        val cartData: MutableList<HashMap<String, String>>? = gson.fromJson(json, type)
        if (cartData != null) {
            selectedServices.addAll(cartData)
        }
    }

    private fun getUsernameFromPreferences(): String {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("username", "Guest") ?: "Guest"
    }
}
