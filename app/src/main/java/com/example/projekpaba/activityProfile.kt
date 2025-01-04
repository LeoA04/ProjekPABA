package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class activityProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var tvProfileName = findViewById<TextView>(R.id.profile_name)
        var tvProfileEmail = findViewById<TextView>(R.id.profile_email)
        val db = FirebaseFirestore.getInstance()

        // Ambil username dari intent
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")

        if (username != null) {
            // Ambil data user dari Firestore
            db.collection("users")
                .document(username)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Tampilkan data user di profil
                        val name = document.getString("username") // Ambil nama user
                        tvProfileName.text = name
                        tvProfileEmail.text = email
                    } else {
                        tvProfileName.text = "User tidak ditemukan"
                    }
                }
                .addOnFailureListener { e ->
                    tvProfileName.text = "Error: ${e.message}"
                }
        } else {
            tvProfileName.text = "Tidak ada data user"
        }

        //button back to dashboard
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke dashboard
            startActivity(intent)
        }

        //button dashboard
        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke dashboard
            startActivity(intent)
        }

        //button recommendation
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke recommendation
            startActivity(intent)
        }

        //button transaksi
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)
        btnTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }

        //button logout
        val btnLogout = findViewById<TextView>(R.id.logout_text)
        btnLogout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear() // Hapus data pengguna dari SharedPreferences
            editor.apply()

            // Arahkan kembali ke halaman login
            val intent = Intent(this, activityLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        //button ke history transaksi
        val _historySection = findViewById<ImageView>(R.id.IvHistory)
        _historySection.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke transaksi
            startActivity(intent)
            finish()
        }
    }
}