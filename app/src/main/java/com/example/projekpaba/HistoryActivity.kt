package com.example.projekpaba

// Import berbagai package yang dibutuhkan
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Mendefinisikan class HistoryActivity yang merupakan subclass dari AppCompatActivity
class HistoryActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private var historyList = arrayListOf<agencyMarketing>()
    private lateinit var rvHistory: RecyclerView

    // Method onCreate dijalankan saat activity pertama kali dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Mendapatkan instance SharedPreferences dan FirebaseFirestore
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()
        rvHistory = findViewById(R.id.rvHistoryTransactions)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // Memuat riwayat dari SharedPreferences
        loadHistory()

        // Memuat riwayat dari Firebase
        loadHistoryFromFirebase()

        // Mengatur button untuk kembali ke halaman utama
        val btnBackToHome = findViewById<ImageButton>(R.id.btnBackToHome)
        btnBackToHome.setOnClickListener {
            finish()
        }

        // Mengatur adapter untuk RecyclerView
        rvHistory.adapter = adapterRecViewRecommendation(historyList)
    }

    // Method untuk memuat riwayat dari SharedPreferences
    private fun loadHistory() {
        val gson = Gson()
        val json = sp.getString("spHistory", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        if (json != null) {
            historyList = gson.fromJson(json, type)
        }
    }

    // Method untuk memuat riwayat dari Firebase
    private fun loadHistoryFromFirebase() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("username", "Guest") // Ambil username yang login

        db.collection("transactionHistory")
            .whereEqualTo("userName", userName) // Filter berdasarkan username
            .get()
            .addOnSuccessListener { result ->
                historyList.clear()
                for (document in result) {
                    val item = agencyMarketing(
                        document.getString("foto") ?: "",
                        document.getString("nama") ?: "",
                        document.getString("harga") ?: "",
                        document.getString("lokasi") ?: "",
                        document.getString("deskripsi") ?: "",
                    )
                    historyList.add(item)
                }
                rvHistory.adapter?.notifyDataSetChanged()
            }
    }
}