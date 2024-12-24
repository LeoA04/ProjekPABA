package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        val btnCommunication= findViewById<Button>(R.id.btnCommunication)
        val btnShoppingCart = findViewById<Button>(R.id.btnShoppingCart)
        val btnSeeAllReviews = findViewById<Button>(R.id.btnSeeAllReviews)

        // button communication
        btnCommunication.setOnClickListener {
            val intent = Intent(this, CommunicationActivity::class.java)
            startActivity(intent)
        }

        // button shopping cart
        btnShoppingCart.setOnClickListener {
            val intent = Intent(this, ShoppingCartActivity::class.java)
            startActivity(intent)
        }

        // button see all reviews
        btnSeeAllReviews.setOnClickListener {
            val intent = Intent(this, Reviews::class.java)
            startActivity(intent)
        }

        val dataIntentDetail = intent.getParcelableExtra<agencyMarketing>("agency", agencyMarketing::class.java)
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
