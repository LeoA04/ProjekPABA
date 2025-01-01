package com.example.projekpaba

// Import berbagai package yang dibutuhkan
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

// Mendefinisikan class activityDetail yang merupakan subclass dari AppCompatActivity
class activityDetail : AppCompatActivity() {

    private lateinit var sp: SharedPreferences

    // Method onCreate dijalankan saat activity pertama kali dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        // Mendapatkan instance SharedPreferences
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)

        val documentId = intent.getStringExtra("kirimData")

        if (documentId != null) {
            fetchAgencyDetails(documentId) // Ambil data dari Firestore
        }

        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)

        val btnCommunication = findViewById<ImageButton>(R.id.btnCommunication)
        val btnSeeAllReviews = findViewById<Button>(R.id.btnSeeAllReviews)
        val btnBackToRecommend = findViewById<ImageButton>(R.id.btnBackToRecommend)
        val btnAddToCart = findViewById<TextView>(R.id.textOrder)

        // Button communication
        btnCommunication.setOnClickListener {
            val dataIntentDetail = intent.getParcelableExtra<agencyMarketing>("kirimData", agencyMarketing::class.java)
            if (dataIntentDetail != null) {
                val intent = Intent(this, CommunicationActivity::class.java)
                intent.putExtra("gambarMarketingAgency", dataIntentDetail.foto)
                intent.putExtra("namaMarketingAgency", dataIntentDetail.nama)
                startActivity(intent)
            }
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
                val intent = Intent(this, TransactionActivity::class.java)
                startActivity(intent)
            }
        }

        // Mengatur padding untuk window insets
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

    // Method untuk menambahkan item ke cart
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

    private fun fetchAgencyDetails(documentId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("marketingAgency").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val foto = document.getString("foto") ?: ""
                    val nama = document.getString("nama") ?: ""
                    val lokasi = document.getString("lokasi") ?: ""
                    val deskripsi = document.getString("deskripsi") ?: ""

                    // Tampilkan data di UI
                    displayAgencyDetails(foto, nama, lokasi, deskripsi)
                }
            }
    }

    private fun displayAgencyDetails(foto: String, nama: String, lokasi: String, deskripsi: String) {
        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)

        // Gunakan Picasso untuk memuat gambar dari URL
        if (foto.isNotEmpty()) {
            Picasso.get().load(foto).into(_ivAgencyLogo)
        }

        // Set nilai ke TextView
        _tvAgencyName.text = nama
        _tvAgencyLocation.text = lokasi
        _tvAboutUs.text = deskripsi
    }
}
