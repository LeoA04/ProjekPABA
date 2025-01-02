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

class TransactionActivity : AppCompatActivity() {

    private lateinit var rvTransaction: RecyclerView
    private var selectedService = hashMapOf<String, String>() // Holds the selected service data
    private lateinit var adapter: TransactionAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // Get selected service from intent
        val serviceData = intent.getSerializableExtra("selectedService") as? HashMap<String, String>
        val username = intent.getStringExtra("username") ?: getUsernameFromPreferences()

        if (serviceData != null) {
            selectedService = serviceData
        }

        // Set up adapter
        adapter = TransactionAdapter(selectedService) {
            // Handle item removal
            selectedService.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Service removed", Toast.LENGTH_SHORT).show()
        }

        rvTransaction.adapter = adapter

        // Mengatur button untuk kembali ke halaman detail
        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)
        btnBackToDetail.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Confirm order button functionality
        findViewById<Button>(R.id.btnConfirmOrder).setOnClickListener {
            if (selectedService.isEmpty()) {
                Toast.makeText(this, "No service selected to confirm.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveToHistory(username)
        }
    }

    private fun saveToHistory(username: String) {
        val transactionMap = hashMapOf(
            "userName" to username,
            "namaService" to selectedService["namaService"],
            "namaPerusahaan" to selectedService["namaPerusahaan"],
            "foto" to selectedService["foto"],
            "harga" to selectedService["harga"]
        )

        db.collection("transactionHistory")
            .add(transactionMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Order confirmed successfully.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HistoryActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to confirm order: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Helper function to get username from SharedPreferences
    private fun getUsernameFromPreferences(): String {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("username", "Guest") ?: "Guest"
    }
}
