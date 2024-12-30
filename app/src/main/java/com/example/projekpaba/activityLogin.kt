package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class activityLogin : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Ambil username dari intent
        val username = intent.getStringExtra("username")
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username) // Simpan username di SharedPreferences
        editor.apply()

        usernameField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnLogin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //button belum punya akun
        val registerLink = findViewById<TextView>(R.id.register_link)
        registerLink.setOnClickListener {
            val intent = Intent(this, activityRegister::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Periksa data pengguna di Firestore
                db.collection("users")
                    .document(username)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val storedPassword = document.getString("password")
                            if (password == storedPassword) {
                                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("username", username)
                                editor.apply()

                                //login berhasil
                                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, dashboardPage::class.java)
                                intent.putExtra("username", username) // Kirim username ke profile
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Login gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Username dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}