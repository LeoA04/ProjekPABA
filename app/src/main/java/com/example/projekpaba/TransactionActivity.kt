package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var rvTransaction: RecyclerView
    private var transactionList = arrayListOf<agencyMarketing>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // Load transactions from SharedPreferences
        loadTransactions()

        // Set adapter for RecyclerView
        rvTransaction.adapter = TransactionAdapter(transactionList, true) { position ->
            transactionList.removeAt(position)
            rvTransaction.adapter?.notifyItemRemoved(position)
            saveTransactions()
            Toast.makeText(this, "Item removed successfully", Toast.LENGTH_SHORT).show()
        }

        // Back button functionality
        findViewById<ImageButton>(R.id.btnBackToDetail).setOnClickListener {
            startActivity(Intent(this, activityDetail::class.java))
        }
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
}