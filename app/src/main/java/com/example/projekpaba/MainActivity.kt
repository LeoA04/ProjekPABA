package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Cek apakah user sudah login
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            // Jika user sudah login, langsung ke dashboard
            val intent = Intent(this, dashboardPage::class.java)
            startActivity(intent)
            finish() // Tutup MainActivity agar tidak kembali ke sini
        } else {
            // Jika user belum login, arahkan ke halaman login
            val intent = Intent(this, activityLogin::class.java)
            startActivity(intent)
            finish() // Tutup MainActivity agar tidak kembali ke sini
        }
    }
}
