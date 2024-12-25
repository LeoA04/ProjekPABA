package com.example.projekpaba

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso

class CommunicationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication)

        val _ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val _tvNameAgency = findViewById<TextView>(R.id.tvNameAgency)
        val _ivLogoMini = findViewById<ImageView>(R.id.ivLogoMini)
        val btnBackFromCommunication = findViewById<ImageButton>(R.id.btnBackFromCommunication)

        // button back to detail page
        btnBackFromCommunication.setOnClickListener {
            finish()
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
                .into(_ivLogo)
            Picasso.get()
                .load(dataIntentDetail.foto)
                .into(_ivLogoMini)
            _tvNameAgency.setText(dataIntentDetail.nama)
        }
    }
}