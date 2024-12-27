package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class activityDetail : AppCompatActivity() {

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)

        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)

        val btnCommunication = findViewById<Button>(R.id.btnCommunication)
        val btnSeeAllReviews = findViewById<Button>(R.id.btnSeeAllReviews)
        val btnBackToRecommend = findViewById<ImageButton>(R.id.btnBackToRecommend)
        val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)

        // Button communication
        btnCommunication.setOnClickListener {
            val intent = Intent(this, CommunicationActivity::class.java)
            startActivity(intent)
        }

        // Button see all reviews
        btnSeeAllReviews.setOnClickListener {
            val intent = Intent(this, Reviews::class.java)
            startActivity(intent)
        }

        // Button back to recommendation page
        btnBackToRecommend.setOnClickListener {
            finish()
        }

        // Button add to cart
        btnAddToCart.setOnClickListener {
            val dataIntentDetail = intent.getParcelableExtra<agencyMarketing>("kirimData", agencyMarketing::class.java)
            if (dataIntentDetail != null) {
                addToCart(dataIntentDetail)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
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

    private fun addToCart(item: agencyMarketing) {
        val gson = Gson()
        val json = sp.getString("spTransactions", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        val transactionList: ArrayList<agencyMarketing> = if (json != null) {
            gson.fromJson(json, type)
        } else {
            arrayListOf()
        }

        transactionList.add(item)
        sp.edit().putString("spTransactions", gson.toJson(transactionList)).apply()
    }
}