package com.example.projekpaba

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class dashboardPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_page)

        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //button dashboard
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            startActivity(intent)
        }
        //button recommendation
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
        //button transaksi
        btnTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }
        //button profile
//        btnProfile.setOnClickListener{
//            val intent = Intent(this, profilePage::class.java)
//            startActivity(intent)
//        }

        //button see all recommendation
        val btnSeeRecommendation = findViewById<TextView>(R.id.btnSeeRecommendation)
        btnSeeRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }

//        val _ivProfile = findViewById<ImageView>(R.id.ivProfile)
//        _ivProfile.setOnClickListener {
//            val intent = Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
//        }

        val _historySection = findViewById<ImageView>(R.id.ivHistory)
        _historySection.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        val _voucherSection = findViewById<LinearLayout>(R.id.voucher_section)
        _voucherSection.setOnClickListener {

        }

//        val _ivNotification = findViewById<ImageView>(R.id.ivNotification)
//        _ivNotification.setOnClickListener {
//
//        }


    }
}