package com.example.projekpaba

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private var historyList = arrayListOf<agencyMarketing>()
    private lateinit var rvHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        rvHistory = findViewById(R.id.rvHistoryTransactions)
        rvHistory.layoutManager = LinearLayoutManager(this)

        // Load history from SharedPreferences
        loadHistory()

        val btnBackToHome = findViewById<ImageButton>(R.id.btnBackToHome)
        btnBackToHome.setOnClickListener {
            finish()
        }

        rvHistory.adapter = adapterRecView(historyList)
    }

    private fun loadHistory() {
        val gson = Gson()
        val json = sp.getString("spHistory", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        if (json != null) {
            historyList = gson.fromJson(json, type)
        }
    }
}
