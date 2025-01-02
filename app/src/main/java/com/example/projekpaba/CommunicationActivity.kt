package com.example.projekpaba

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class CommunicationActivity : AppCompatActivity() {

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

        // Ambil data dari Intent
        val gambarMarketingAgency = intent.getStringExtra("gambarMarketingAgency")
        val namaMarketingAgency = intent.getStringExtra("namaMarketingAgency")

        // Tampilkan data ke ImageView dan TextView
        if (gambarMarketingAgency != null) {
            Picasso.get()
                .load(gambarMarketingAgency)
                .into(_ivLogo)
        }
        if (gambarMarketingAgency != null) {
            Picasso.get()
                .load(gambarMarketingAgency)
                .into(_ivLogoMini)
        }

        if (namaMarketingAgency != null) {
            _tvNameAgency.text = namaMarketingAgency
        }
    }
}