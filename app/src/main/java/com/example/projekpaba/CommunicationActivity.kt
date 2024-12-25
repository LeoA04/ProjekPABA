package com.example.projekpaba

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val dataIntent = intent.getParcelableExtra<agencyMarketing>("kirimData", agencyMarketing::class.java)
        if (dataIntent != null) {
            Picasso.get()
                .load(dataIntent.foto)
                .into(_ivLogo)
            _tvNameAgency.setText(dataIntent.nama)
        }
    }
}