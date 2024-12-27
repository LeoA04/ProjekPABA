package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class activityDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)

        val btnCommunication= findViewById<ImageButton>(R.id.btnCommunication)
        val btnOrder = findViewById<TextView>(R.id.textOrder)
        val btnSeeAllReviews = findViewById<Button>(R.id.btnSeeAllReviews)
        val btnBackToRecommend = findViewById<ImageButton>(R.id.btnBackToRecommend)

        // button communication
        btnCommunication.setOnClickListener {
            val intent = Intent(this, CommunicationActivity::class.java)
            startActivity(intent)
        }

        // button see all reviews
        btnSeeAllReviews.setOnClickListener {
            val intent = Intent(this, Reviews::class.java)
            startActivity(intent)
        }

        // button back to recommendation page
        btnBackToRecommend.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }

        //button order
        btnOrder.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataIntentDetail = intent.getParcelableExtra<agencyMarketing>("kirimData", agencyMarketing::class.java)
        if (dataIntentDetail != null) {
            Picasso.get()
                .load(dataIntentDetail.foto)
                .into(_ivAgencyLogo)
            _tvAgencyName.setText(dataIntentDetail.nama)
            _tvAgencyLocation.setText(dataIntentDetail.lokasi)
            _tvAboutUs.setText(dataIntentDetail.deskripsi)
        }
    }
}