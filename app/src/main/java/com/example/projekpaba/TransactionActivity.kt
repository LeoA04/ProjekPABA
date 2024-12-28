package com.example.projekpaba

// Import berbagai package yang dibutuhkan
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Mendefinisikan class TransactionActivity yang merupakan subclass dari AppCompatActivity
class TransactionActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private var transactionList = arrayListOf<agencyMarketing>()
    private lateinit var rvTransaction: RecyclerView
    private val db = Firebase.firestore

    // Method onCreate dijalankan saat activity pertama kali dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Mendapatkan instance SharedPreferences
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        rvTransaction = findViewById(R.id.rvTransactionServices)
        rvTransaction.layoutManager = LinearLayoutManager(this)

        // Memuat transaksi dari SharedPreferences
        loadTransactions()

        // Mengatur button untuk kembali ke halaman detail
        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)
        btnBackToDetail.setOnClickListener {
            startActivity(Intent(this, RecommendationActivity::class.java))
        }

        // Mengatur button untuk mengonfirmasi pesanan
        val btnConfirmOrder = findViewById<Button>(R.id.btnConfirmOrder)
        btnConfirmOrder.setOnClickListener {
            saveToHistory(db)
            transactionList.clear()
            saveTransactions()
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // history imageview
        val ivHistory = findViewById<ImageView>(R.id.ivHistory)
        ivHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // Mengatur adapter untuk RecyclerView
        rvTransaction.adapter = TransactionAdapter(transactionList, true) { position ->
            // Menghapus item dari daftar transaksi
            transactionList.removeAt(position)
            rvTransaction.adapter?.notifyItemRemoved(position)

            // Menyimpan daftar transaksi yang diperbarui ke SharedPreferences
            saveTransactions()

            Toast.makeText(this, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    // Method untuk memuat transaksi dari SharedPreferences
    private fun loadTransactions() {
        val gson = Gson()
        val json = sp.getString("spTransactions", null)
        val type = object : TypeToken<ArrayList<agencyMarketing>>() {}.type
        if (json != null) {
            transactionList = gson.fromJson(json, type)
        }
    }

    // Method untuk menyimpan transaksi ke SharedPreferences
    private fun saveTransactions() {
        val gson = Gson()
        val json = gson.toJson(transactionList)
        sp.edit().putString("spTransactions", json).apply()
    }

    // Method untuk menyimpan transaksi ke riwayat dan Firebase
    private fun saveToHistory(db : FirebaseFirestore) {
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

        // Simpan data ke Firebase
        for (transaction in transactionList) {
            val transactionMap = hashMapOf(
                "foto" to transaction.foto,
                "nama" to transaction.nama,
                "harga" to transaction.harga,
                "lokasi" to transaction.lokasi,
                "deskripsi" to transaction.deskripsi,
                "quantity" to transaction.quantity
            )
            db.collection("transactionHistory")
                .add(transactionMap)
                .addOnSuccessListener {

                    // Hapus semua item dari transactionList setelah sukses
                    transactionList.clear()
                    rvTransaction.adapter?.notifyDataSetChanged()

                    // Simpan daftar transaksi kosong ke SharedPreferences
                    saveTransactions()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}