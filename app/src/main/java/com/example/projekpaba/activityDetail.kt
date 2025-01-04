package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
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

        val _ivAgencyLogo = findViewById<ImageView>(R.id.ivAgencyLogo)
        val _tvAgencyName = findViewById<TextView>(R.id.tvAgencyName)
        val _tvAgencyLocation = findViewById<TextView>(R.id.tvAgencyLocation)
        val _tvAboutUs = findViewById<TextView>(R.id.tvIsiAboutUs)
        val btnAddToCart = findViewById<TextView>(R.id.textOrder)
        val btnAddGlobal = findViewById<TextView>(R.id.textOrderGlobal)
        val btnCommunication = findViewById<ImageButton>(R.id.btnCommunication)
        val btnSeeAllReviews = findViewById<Button>(R.id.btnSeeAllReviews)
        val btnBackToRecommend = findViewById<ImageButton>(R.id.btnBackToRecommend)

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

        val dataIntentDetail =
            intent.getParcelableExtra<agencyMarketing>("kirimData", agencyMarketing::class.java)
        if (dataIntentDetail != null) {
            Picasso.get().load(dataIntentDetail.foto).into(_ivAgencyLogo)
            _tvAgencyName.text = dataIntentDetail.nama
            _tvAgencyLocation.text = dataIntentDetail.lokasi
            _tvAboutUs.text = dataIntentDetail.deskripsi

            // Call fetchServicesForCompany dengan nama agency
         fetchServicesForCompany(dataIntentDetail.nama)
    }

        btnAddToCart.setOnClickListener {
            if (dataIntentDetail != null) {
                addToCart(dataIntentDetail)
            }
        }
        btnAddGlobal.setOnClickListener {
            if (dataIntentDetail != null) {
                addToCartGlobal(dataIntentDetail)
            }
        }
    }

    private fun addToCart(item: agencyMarketing) {
        val selectedServiceName = service.selectedItem?.toString()
        val db = FirebaseFirestore.getInstance()

        val parts = selectedServiceName?.split(" - Rp. ") // Pisahkan nama dan harga
        val selectedServiceNama = parts?.get(0) ?: "Unknown Service" // Nama jasa
        val selectedServicePrice = if (parts?.size ?: 0 > 1) parts?.get(1) else "0" // Harga

        db.collection("service")
            .whereEqualTo("namaPerusahaan", item.nama)
            .whereEqualTo("namaService", selectedServiceNama)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val selectedServiceData = hashMapOf(
                    "namaService" to selectedServiceNama,
                    "namaPerusahaan" to item.nama,
                    "foto" to item.foto,
                    "harga" to selectedServicePrice // Masukkan harga langsung dari spinner
                )

                // menyalurkan data ke transaction activity
                val intent = Intent(this, TransactionActivity::class.java).apply {
                    putExtra("selectedService", selectedServiceData)
                }
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.e("FetchServiceError", "Error fetching service details: ${it.message}")
            }
    }

    private fun addToCartGlobal(item: agencyMarketing) {
        val selectedServiceName = service.selectedItem?.toString()
        val db = FirebaseFirestore.getInstance()

        val parts = selectedServiceName?.split(" - Rp. ") // Pisahkan nama dan harga
        val selectedServiceNama = parts?.get(0) ?: "Unknown Service" // Nama jasa
        val selectedServicePrice = if (parts?.size ?: 0 > 1) parts?.get(1) else "0" // Harga

        db.collection("service")
            .whereEqualTo("namaPerusahaan", item.nama)
            .whereEqualTo("namaService", selectedServiceNama)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val selectedServiceData = hashMapOf(
                    "namaService" to selectedServiceNama,
                    "namaPerusahaan" to item.nama,
                    "foto" to item.foto,
                    "harga" to selectedServicePrice // Masukkan harga langsung dari spinner
                )

                // menyalurkan data ke transaction activity
                val intent = Intent(this, TransactionActivityGlobal::class.java).apply {
                    putExtra("selectedService", selectedServiceData)
                }
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.e("FetchServiceError", "Error fetching service details: ${it.message}")
            }
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

    private fun fetchServicesForCompany(namaPerusahaan: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("service")
            .whereEqualTo("namaPerusahaan", namaPerusahaan)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Log.w("ServiceQuery", "No services found for $namaPerusahaan")
                }
                val services = mutableListOf<String>()
                for (document in querySnapshot) {
                    val serviceName = document.getString("namaService") ?: "Unknown Service"
                    val servicePrice = document.getString("harga") ?: "0"
                    if (serviceName != null) {
                        services.add("$serviceName - Rp. $servicePrice")
                    }
                }
                populateSpinner(services)
            }
            .addOnFailureListener { e ->
                Log.e("ServiceQueryError", "Error fetching services: ${e.message}")
            }
    }

    private fun populateSpinner(services: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        service.adapter = adapter
    }
}