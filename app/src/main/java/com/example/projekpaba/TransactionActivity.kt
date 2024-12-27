package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private var transactionList = arrayListOf<agencyMarketing>()
    private lateinit var rvTransaction: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // Load transactions from SharedPreferences
        loadTransactions()

        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)
        btnBackToDetail.setOnClickListener {
            finish()
        }

        val btnConfirmOrder = findViewById<Button>(R.id.btnConfirmOrder)
        btnConfirmOrder.setOnClickListener {
            saveToHistory()
            transactionList.clear()
            saveTransactions()
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        rvTransaction.adapter = TransactionAdapter(transactionList, true)
    }

    private fun loadTransactions() {
        val gson = Gson()
        val json = sp.getString("spTransactions", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        if (json != null) {
            transactionList = gson.fromJson(json, type)
        }
    }

    private fun saveTransactions() {
        val gson = Gson()
        val json = gson.toJson(transactionList)
        sp.edit().putString("spTransactions", json).apply()
    }

    private fun saveToHistory() {
        val gson = Gson()
        val jsonHistory = sp.getString("spHistory", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        val historyList: ArrayList<agencyMarketing> = if (jsonHistory != null) {
            gson.fromJson(jsonHistory, type)
        } else {
            arrayListOf()
        }

        historyList.addAll(transactionList)
        sp.edit().putString("spHistory", gson.toJson(historyList)).apply()
    }
}