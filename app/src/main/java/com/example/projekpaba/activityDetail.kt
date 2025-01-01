package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class activityDetail : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var service: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        service = findViewById(R.id.spinnerServices)

        val documentId = intent.getStringExtra("kirimData")

        if (documentId != null) {
            fetchAgencyDetails(documentId)
        }

        fetchServicesForCompany("Tomato Digital")

        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)
        val btnAddToCart = findViewById<TextView>(R.id.textOrder)

        val dataIntentDetail = intent.getParcelableExtra<agencyMarketing>("kirimData", agencyMarketing::class.java)
        if (dataIntentDetail != null) {
            Picasso.get().load(dataIntentDetail.foto).into(_ivAgencyLogo)
            _tvAgencyName.text = dataIntentDetail.nama
            _tvAgencyLocation.text = dataIntentDetail.lokasi
            _tvAboutUs.text = dataIntentDetail.deskripsi
        }

        btnAddToCart.setOnClickListener {
            if (dataIntentDetail != null) {
                addToCart(dataIntentDetail)
            }
        }
    }

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

        val selectedService = service.selectedItem?.toString() ?: ""
        val intent = Intent(this, TransactionActivity::class.java).apply {
            putExtra("selectedService", selectedService)
        }
        startActivity(intent)
    }

    private fun fetchAgencyDetails(documentId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("marketingAgency").document(documentId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val foto = document.getString("foto") ?: ""
                val nama = document.getString("nama") ?: ""
                val lokasi = document.getString("lokasi") ?: ""
                val deskripsi = document.getString("deskripsi") ?: ""
                displayAgencyDetails(foto, nama, lokasi, deskripsi)
            }
        }
    }

    private fun displayAgencyDetails(foto: String, nama: String, lokasi: String, deskripsi: String) {
        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)
        Picasso.get().load(foto).into(_ivAgencyLogo)
        _tvAgencyName.text = nama
        _tvAgencyLocation.text = lokasi
        _tvAboutUs.text = deskripsi
    }

    private fun fetchServicesForCompany(companyName: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("service")
            .whereEqualTo("namaPerusahaan", companyName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val services = mutableListOf<String>()
                for (document in querySnapshot) {
                    val serviceName = document.getString("namaService")
                    if (serviceName != null) {
                        services.add(serviceName)
                    }
                }
                populateSpinner(services)
            }
    }

    private fun populateSpinner(services: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        service.adapter = adapter
    }
}