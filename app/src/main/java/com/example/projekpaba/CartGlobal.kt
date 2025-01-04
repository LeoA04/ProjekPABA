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

class CartGlobal : AppCompatActivity() {

    private lateinit var rvTransaction: RecyclerView
    private val selectedServices = mutableListOf<HashMap<String, String>>()
    private lateinit var adapter: TransactionAdapter
    private val db = FirebaseFirestore.getInstance()
    private var username: String = "Guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        loadCartData()

        val serviceData = intent.getSerializableExtra("selectedService") as? HashMap<String, String>
        if (serviceData != null) {
            selectedServices.add(serviceData)
            saveCartData()
        }

        adapter = TransactionAdapter(selectedServices) { position ->
            selectedServices.removeAt(position)
            saveCartData()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Service removed", Toast.LENGTH_SHORT).show()
        }

        rvTransaction.adapter = adapter

        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)
        btnBackToDetail.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("Guest", username)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnConfirmOrder).setOnClickListener {
            if (selectedServices.isEmpty()) {
                Toast.makeText(this, "No services selected to confirm.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveToHistory()
        }

        findViewById<Button>(R.id.btnConfirmOrder)
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

        selectedServices.clear()
        saveCartData()
        adapter.notifyDataSetChanged()

        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun saveCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(selectedServices)
        editor.apply()
    }

    private fun loadCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("cart_$username", null)
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


