package com.example.projekpaba

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class dashboardPage : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_page)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Ambil username dari intent
        val username = sharedPreferences.getString("username", null)

        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)

        // tampilkan nama user di dahsboard
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //button dashboard
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke dashboard
            startActivity(intent)
            finish()
        }
        //button recommendation
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke recommendation
            startActivity(intent)
            finish()
        }
        //button transaksi
        btnTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke transaksi
            startActivity(intent)
            finish()
        }

        //button see all recommendation
        val btnSeeRecommendation = findViewById<TextView>(R.id.btnSeeRecommendation)
        btnSeeRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke recommendation
            startActivity(intent)
            finish()
        }

        //button ke profile
        val _ivProfile = findViewById<ImageView>(R.id.ivProfileBawah)
        _ivProfile.setOnClickListener {
            val intent = Intent(this, activityProfile::class.java)
            intent.putExtra("username", username) // Kirim username ke halaman profil
            startActivity(intent)
        }

        val _historySection = findViewById<ImageView>(R.id.ivHistory)
        _historySection.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke transaksi
            startActivity(intent)
            finish()
        }

        if (username != null) {
            db.collection("users")
                .document(username)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("username")
                        tvUsername.text = "$name!"
                    } else {
                        tvUsername.text = "User tidak ditemukan"
                    }
                }
                .addOnFailureListener { e ->
                    tvUsername.text = "Error: ${e.message}"
                }
        } else {
            tvUsername.text = "Tidak ada data user"
        }
    }
}