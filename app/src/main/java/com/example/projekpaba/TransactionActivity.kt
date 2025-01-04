package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionActivity : AppCompatActivity() {

    private lateinit var rvTransaction: RecyclerView
    private val selectedServices = mutableListOf<HashMap<String, String>>() // list bisa memilih lebih dari 1
    private lateinit var adapter: TransactionAdapter
    private val db = FirebaseFirestore.getInstance()
    private var username: String = "Guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // ambil username dari intent
        username = intent.getStringExtra("username") ?: getUsernameFromPreferences()

        // load cart
        loadCartData()

        val email = intent.getStringExtra("email")

        val serviceData = intent.getSerializableExtra("selectedService") as? HashMap<String, String>
        if (serviceData != null) {
            selectedServices.add(serviceData)
            saveCartData() // simpan ke cart
        }

        // adapter
        adapter = TransactionAdapter(selectedServices) { position ->
            // handle hapus jasa
            selectedServices.removeAt(position)
            saveCartData() // simpan update terbaru
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Service removed", Toast.LENGTH_SHORT).show()
        }

        rvTransaction.adapter = adapter

        // btn kemabli ke recommendation
        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)
        btnBackToDetail.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        // confirm order
        findViewById<Button>(R.id.btnConfirmOrder).setOnClickListener {
            if (selectedServices.isEmpty()) {
                Toast.makeText(this, "No services selected to confirm.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveToHistory()
        }
        //button dashboard
        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke dashboard
            intent.putExtra("email", email)
            startActivity(intent)
        }

        //button recommendation
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke recommendation
            intent.putExtra("email", email)
            startActivity(intent)
        }

        //button transaksi
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)
        btnTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }
        //button profile
        val btnProfile = findViewById<ImageView>(R.id.ivProfileBawah)
        btnProfile.setOnClickListener {
            val intent = Intent(this, activityProfile::class.java)
            intent.putExtra("username", username) // Kirim username ke profil
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
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

        // bersihkan cart setelah konfirmasi
        selectedServices.clear()
        saveCartData()
        adapter.notifyDataSetChanged()

        // ke history setelah order
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    //lama
//    private fun saveCartData() {
//        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val gson = Gson()
//        val json = gson.toJson(selectedServices)
//        editor.putString("cart_$username", json) // hubungkan cart dengan username
//        editor.apply()
//    }

//    //baru
    private fun saveCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(selectedServices)
        editor.putString("shared_cart", json) // hubungkan cart dengan username
        editor.apply()
    }


    private fun getUsernameFromPreferences(): String {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("username", "Guest") ?: "Guest"
    }

    //lama
//    private fun loadCartData() {
//        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
//        val gson = Gson()
//        val json = sharedPreferences.getString("cart_$username", null) // load cart sesuai username
//        val type = object : TypeToken<MutableList<HashMap<String, String>>>() {}.type
//        val cartData: MutableList<HashMap<String, String>>? = gson.fromJson(json, type)
//        if (cartData != null) {
//            selectedServices.addAll(cartData)
//        }
//    }

    //baru
    private fun loadCartData() {
        val sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("shared_cart", null) // load cart sesuai username
        val type = object : TypeToken<MutableList<HashMap<String, String>>>() {}.type
        val cartData: MutableList<HashMap<String, String>>? = gson.fromJson(json, type)
        if (cartData != null) {
            selectedServices.addAll(cartData)
        }
    }

}
