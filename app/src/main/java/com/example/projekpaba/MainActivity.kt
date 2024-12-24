package com.example.projekpaba

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //inisialisasi variabel _rvMarketingAgency
        _rvMarketingAgency = findViewById(R.id.rvMarketingAgency)

        //memanggil fungsi yg sdh dibuat di bawah
        SiapkanData()
        TambahData()
        TampilData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //Fungsi ini berfungsi mengambil data string array yang sudah kita masukkan ke dalam value string.
    fun SiapkanData(){
        _nama = resources.getStringArray(R.array.namaMarketingAgency)
        _harga = resources.getStringArray(R.array.hargaMarketingAgency)
        _lokasi = resources.getStringArray(R.array.lokasiMarketingAgency)
        _gambar = resources.getStringArray(R.array.gambarMarketingAgency)
    }

    //fungsi untuk menambahkan data
    fun TambahData(){
        for (position in _nama.indices){
            val data = agencyMarketing(
                _gambar[position],
                _nama[position],
                _harga[position],
                _lokasi[position]
            )
            arAgency.add(data)
        }
    }

    //fungsi untuk menampilkan data
    fun TampilData(){
        _rvMarketingAgency.layoutManager = GridLayoutManager(this, 2)
        _rvMarketingAgency.adapter = adapterRecView(arAgency)
    }
}

//variabel global
private lateinit var _nama : Array<String>
private lateinit var _harga : Array<String>
private lateinit var _lokasi : Array<String>
private lateinit var _gambar : Array<String>

//variabel utk menyimpan data" dari agency marketing
private var arAgency = arrayListOf<agencyMarketing>()

//variabel untuk recycle view
private lateinit var _rvMarketingAgency : RecyclerView