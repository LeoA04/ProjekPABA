package com.example.projekpaba

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private var historyList = arrayListOf<agencyMarketing>()
    private lateinit var rvHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()
        rvHistory = findViewById(R.id.rvHistoryTransactions)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // Load history from SharedPreferences
        loadHistory()

        // Load history dari Firebase
        loadHistoryFromFirebase()

        val btnBackToHome = findViewById<ImageButton>(R.id.btnBackToHome)
        btnBackToHome.setOnClickListener {
            finish()
        }

        rvHistory.adapter = adapterRecViewRecommendation(historyList)
    }

    private fun loadHistory() {
        val gson = Gson()
        val json = sp.getString("spHistory", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        if (json != null) {
            historyList = gson.fromJson(json, type)
        }
    }

    private fun loadHistoryFromFirebase() {
        db.collection("transactionHistory")
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
                        document.getLong("quantity")?.toInt() ?: 1
                    )
                    historyList.add(item)
                }
                rvHistory.adapter?.notifyDataSetChanged()
            }
    }
}