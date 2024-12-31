package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore

class RecommendationActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences

    //variabel utk menyimpan data" dari agency marketing
    private var arAgency = arrayListOf<agencyMarketing>()

    //variabel untuk recycle view
    private lateinit var _rvMarketingAgency : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.rec_view_recommendation_page)

        //inisialisasi variabel _rvMarketingAgency
        _rvMarketingAgency = findViewById(R.id.rvMarketingAgency)

        // Ambil username dari intent
        val username = intent.getStringExtra("username")

        // Ambil data dari Firestore
//        prepareDataFromFirestore()

        //memanggil fungsi yg sdh dibuat di bawah
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)

        // Periksa apakah data sudah diunggah
//        uploadDataToFirestore()

        // Panggil data dari Firestore
        loadDataFromFirestore()

        //inisialisasi variabel button
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnDasboard = findViewById<ImageView>(R.id.ivDashboard)
        val btnRecommendation = findViewById<ImageView>(R.id.ivRecommendation)
        val btnTransaction = findViewById<ImageView>(R.id.ivTransaction)
        val btnProfile = findViewById<ImageView>(R.id.ivProfile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //button back
        btnBack.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke halaman profil
            startActivity(intent)
        }
        //button dashboard
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke dashboard
            startActivity(intent)
            finish()
        }
        //button recommendation
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
        //button transaksi
        btnTransaction.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra("username", username) // Kirim username ke transaksi
            startActivity(intent)
            finish()
        }

        //button profile
        btnProfile.setOnClickListener {
            val intent = Intent(this, activityProfile::class.java)
            intent.putExtra("username", username) // Kirim username ke profil
            startActivity(intent)
            finish()
        }
    }

    private fun prepareDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        arAgency.clear()

        db.collection("marketingAgency")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val agency = agencyMarketing(
                        foto = document.getString("foto") ?: "",
                        nama = document.getString("nama") ?: "",
                        harga = document.getString("harga") ?: "",
                        lokasi = document.getString("lokasi") ?: "",
                        deskripsi = document.getString("deskripsi") ?: ""
                    )
                    arAgency.add(agency)
                }

                TampilData()
            }
    }

    private fun loadDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        arAgency.clear() // Kosongkan daftar sebelum menambahkan data baru

        db.collection("marketingAgency")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val agency = agencyMarketing(
                        foto = document.getString("foto") ?: "",
                        nama = document.getString("nama") ?: "",
                        harga = document.getString("harga") ?: "",
                        lokasi = document.getString("lokasi") ?: "",
                        deskripsi = document.getString("deskripsi") ?: ""
                    )
                    arAgency.add(agency)
                }

                // Tampilkan data di RecyclerView setelah data dimuat
                TampilData()
            }
    }

    //fungsi untuk menampilkan data
    fun TampilData() {
        _rvMarketingAgency.layoutManager = GridLayoutManager(this, 2)
        val adapter = adapterRecViewRecommendation(arAgency)
        _rvMarketingAgency.adapter = adapter

        adapter.setOnItemClickCallback(object : adapterRecViewRecommendation.OnItemClickCallback {
            override fun onItemClicked(data: agencyMarketing) {
                val intent = Intent(this@RecommendationActivity, activityDetail::class.java)
                intent.putExtra("kirimData", data)
                startActivity(intent)
            }
        })
    }

//    private fun uploadDataToFirestore() {
//        val db = FirebaseFirestore.getInstance()
//
//        val namaMarketingAgency = resources.getStringArray(R.array.namaMarketingAgency)
//        val hargaMarketingAgency = resources.getStringArray(R.array.hargaMarketingAgency)
//        val lokasiMarketingAgency = resources.getStringArray(R.array.lokasiMarketingAgency)
//        val gambarMarketingAgency = resources.getStringArray(R.array.gambarMarketingAgency)
//        val aboutUsMarketingAgency = resources.getStringArray(R.array.aboutUsMarketingAgency)
//
//        for (i in namaMarketingAgency.indices) {
//            val agency = agencyMarketing(
//                foto = gambarMarketingAgency[i],
//                nama = namaMarketingAgency[i],
//                harga = hargaMarketingAgency[i],
//                lokasi = lokasiMarketingAgency[i],
//                deskripsi = aboutUsMarketingAgency[i]
//            )
//
//            db.collection("marketingAgency")
//                .add(agency)
//        }
//    }
}