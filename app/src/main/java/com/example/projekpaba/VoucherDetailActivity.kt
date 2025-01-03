package com.example.projekpaba

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VoucherDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_voucher)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val periode = intent.getStringExtra("periode")

        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val tvPeriod: TextView = findViewById(R.id.tvPeriode)

        tvTitle.text = title
        tvDescription.text = description
        tvPeriod.text = periode
    }
}
