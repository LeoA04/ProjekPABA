package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class voucher : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var voucherArrayList: ArrayList<voucherDetail>
    private lateinit var voucherAdapter: voucherAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher)

        recyclerView = findViewById(R.id.recViewVoucher)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        voucherArrayList = arrayListOf()
        voucherAdapter = voucherAdapter(voucherArrayList) { selectedVoucher ->
            val intent = Intent(this, VoucherDetailActivity::class.java).apply {
                putExtra("title", selectedVoucher.title)
                putExtra("description", selectedVoucher.content)
                putExtra("period", selectedVoucher.content)
            }
            startActivity(intent)
        }

        recyclerView.adapter = voucherAdapter

        db = FirebaseFirestore.getInstance()
        EventChangeListener()

        val username = intent.getStringExtra("username")

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, dashboardPage::class.java)
            intent.putExtra("username", username) // Kirim username ke halaman profil
            startActivity(intent)
        }

    }

    private fun EventChangeListener() {
        db.collection("promotion")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        voucherArrayList.add(dc.document.toObject(voucherDetail::class.java))
                    }
                }

                voucherAdapter.notifyDataSetChanged()
            }
    }
}
