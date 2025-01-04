package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private var historyList = mutableListOf<HashMap<String, String>>()
    private lateinit var rvHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        sp = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()
        rvHistory = findViewById(R.id.rvHistoryTransactions)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // Load history from Firebase
        loadHistoryFromFirebase()

        //inisialisasi variabel button
        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)
        val btnProfile = findViewById<ImageView>(R.id.ivProfileBawah)

        // Ambil username dari intent
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")

        // Back button functionality
        val btnBackToHome = findViewById<ImageButton>(R.id.btnBackToHome)
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK // bersihkan dari tumpukan activity sebelumnya
            startActivity(intent)
            finish() // Menutup HistoryActivity
        }

        //button dashboard
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke dashboard
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }
        //button recommendation
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
        //button transaksi
        btnTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke transaksi
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }

        //button profile
        btnProfile.setOnClickListener {
            val intent = Intent(this, activityProfile::class.java)
            intent.putExtra("username", username) // Kirim username ke profil
            intent.putExtra("email", email)
            startActivity(intent)
            finish()
        }
    }

    private fun loadHistoryFromFirebase() {
        val userName = sp.getString("username", "Guest") // Get username from SharedPreferences

        db.collection("transactionHistory")
            .whereEqualTo("userName", userName) // Filter by username
            .get()
            .addOnSuccessListener { result ->
                historyList.clear()
                for (document in result) {
                    val item = hashMapOf(
                        "namaPerusahaan" to (document.getString("namaPerusahaan") ?: ""),
                        "namaService" to (document.getString("namaService") ?: ""),
                        "harga" to (document.getString("harga") ?: ""),
                        "foto" to (document.getString("foto") ?: "")
                    )
                    historyList.add(item)
                }
                rvHistory.adapter = HistoryAdapter(historyList)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
