package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionActivity : AppCompatActivity() {

    private lateinit var rvTransaction: RecyclerView
    private var selectedService = hashMapOf<String, String>() // Holds the selected service data
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // Get selected service from intent
        val serviceData = intent.getSerializableExtra("selectedService") as? HashMap<String, String>
        if (serviceData != null) {
            selectedService = serviceData
        }

        // Back button functionality
        findViewById<ImageButton>(R.id.btnBackToDetail).setOnClickListener {
            startActivity(Intent(this, activityDetail::class.java))
        }

        // Set up adapter
        adapter = TransactionAdapter(selectedService) {
            // Handle item removal
            selectedService.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Service removed", Toast.LENGTH_SHORT).show()
        }

        rvTransaction.adapter = adapter
    }
}
