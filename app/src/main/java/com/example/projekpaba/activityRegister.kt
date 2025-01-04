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

class activityRegister : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var registerButton: Button
    private lateinit var emailField: EditText
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        usernameField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        emailField = findViewById(R.id.email)
        registerButton = findViewById(R.id.btnDaftar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //button sudah punya akun
        val btnLogin = findViewById<TextView>(R.id.login_link)
        btnLogin.setOnClickListener {
            val intent = Intent(this, activityLogin::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val email = emailField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                // Simpan data pengguna ke Firestore
                val user = hashMapOf(
                    "username" to username,
                    "password" to password,
                    "email" to email
                )

                db.collection("users")
                    .document(username) // Gunakan username sebagai ID dokumen
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Register berhasil!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, activityLogin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Register gagal: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Username, Email, dan Password harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}