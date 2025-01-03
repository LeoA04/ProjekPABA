package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class dashboardPage : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_page)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()

        val username = sharedPreferences.getString("username", "Guest")
        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)

        // set nama user di dashboard
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        tvUsername.text = "$username"

        // Load recommendations
        loadRecommendations()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _historySection = findViewById<ImageView>(R.id.ivHistory)
        _historySection.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()
        }
        val _voucherSection = findViewById<LinearLayout>(R.id.voucher_section)
        _voucherSection.setOnClickListener{
            val intent = Intent(this, voucher::class.java)
            intent.putExtra("username", username)
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

    }

    private fun loadRecommendations() {
        val recommendations = mutableListOf<HashMap<String, String>>()
        val rvRecommendations = findViewById<RecyclerView>(R.id.rvRecommendations)

        rvRecommendations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        db.collection("marketingAgency")
            .limit(3) // limit tampilkan 3 agency
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val agency = hashMapOf(
                        "nama" to (document.getString("nama") ?: ""),
                        "lokasi" to (document.getString("lokasi") ?: ""),
                        "foto" to (document.getString("foto") ?: ""),
                        "deskripsi" to (document.getString("deskripsi") ?: "")
                    )
                    recommendations.add(agency)
                }

                val adapter = adapterRecViewDashboard(recommendations)
                rvRecommendations.adapter = adapter

                // Set click callback
                adapter.setOnItemClickCallback(object : adapterRecViewDashboard.OnItemClickCallback {
                    override fun onItemClicked(data: HashMap<String, String>) {
                        // konversi ke objek agencyMarketing dan kirim ke ActivityDetail
                        val agencyMarketingData = agencyMarketing(
                            foto = data["foto"] ?: "",
                            nama = data["nama"] ?: "",
                            lokasi = data["lokasi"] ?: "",
                            deskripsi = data["deskripsi"] ?: "",
                            harga = ""
                        )
                        val intent = Intent(this@dashboardPage, activityDetail::class.java)
                        intent.putExtra("kirimData", agencyMarketingData)
                        startActivity(intent)
                    }
                })
            }
    }
}
