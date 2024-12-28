package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ButtonBarLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecommendationActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences

    //variabel global
    private lateinit var _nama : Array<String>
    private lateinit var _harga : Array<String>
    private lateinit var _lokasi : Array<String>
    private lateinit var _gambar : Array<String>
    private lateinit var _deskripsi : Array<String>

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

        //memanggil fungsi yg sdh dibuat di bawah
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        SiapkanData()
        TambahData()
        TampilData()

        val gson = Gson()
        val isiSP = sp.getString("spAgency", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        if (isiSP != null)
            arAgency = gson.fromJson(isiSP, type)

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
            startActivity(intent)
        }
        //button dashboard
        btnDasboard.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            startActivity(intent)
        }
        //button recommendation
        btnRecommendation.setOnClickListener {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
        //button transaksi
        btnTransaction.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        //button profile
//        btnProfile.setOnClickListener{
//            val intent = Intent(this, profilePage::class.java)
//            startActivity(intent)
//        }
    }

    //Fungsi ini berfungsi mengambil data string array yang sudah kita masukkan ke dalam value string.
    fun SiapkanData() {
        _nama = resources.getStringArray(R.array.namaMarketingAgency)
        _harga = resources.getStringArray(R.array.hargaMarketingAgency)
        _lokasi = resources.getStringArray(R.array.lokasiMarketingAgency)
        _gambar = resources.getStringArray(R.array.gambarMarketingAgency)
        _deskripsi = resources.getStringArray(R.array.aboutUsMarketingAgency)
    }

    //fungsi untuk menambahkan data
    fun TambahData() {
        val gson = Gson()
        val editor = sp.edit()
        arAgency.clear()
        for (position in _nama.indices) {
            val data = agencyMarketing(
                _gambar[position],
                _nama[position],
                _harga[position],
                _lokasi[position],
                _deskripsi[position]
            )
            arAgency.add(data)
        }
        val json = gson.toJson(arAgency)
        editor.putString("spAgency", json)
        editor.apply()
    }

    //fungsi untuk menampilkan data
    fun TampilData() {
        _rvMarketingAgency.layoutManager = GridLayoutManager(this, 2)
        _rvMarketingAgency.adapter = adapterRecViewRecommendation(arAgency)

        val adapter_detail = adapterRecViewRecommendation(arAgency)
        _rvMarketingAgency.adapter = adapter_detail

        adapter_detail.setOnItemClickCallback(object : adapterRecViewRecommendation.OnItemClickCallback {
            override fun onItemClicked(data: agencyMarketing) {
                Toast.makeText(this@RecommendationActivity, data.nama, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@RecommendationActivity, activityDetail::class.java)
                intent.putExtra("kirimData", data)
                startActivity(intent)
            }
        })
    }
}