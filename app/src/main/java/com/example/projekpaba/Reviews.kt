package com.example.projekpaba

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Reviews : AppCompatActivity() {

    private lateinit var sp: SharedPreferences

    private var _reviews: MutableList<String> = emptyList<String>().toMutableList()
    private var arReviews = arrayListOf<reviewDetail>()
    private lateinit var _rvReviews: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reviews)

        val btnBackToDetail = findViewById<ImageButton>(R.id.btnBackToDetail)

        // button back to detail page
        btnBackToDetail.setOnClickListener {
            finish()
        }

        _rvReviews = findViewById<RecyclerView>(R.id.RvReview)
        SiapkanData()

        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        TambahData()
        TampilData()

        val gson = Gson()
        val isiSP = sp.getString("spReviews", null)
        val type = object : TypeToken<ArrayList<reviewDetail>>() {}.type
        if (isiSP != null)
            arReviews = gson.fromJson(isiSP, type)
    }

    fun SiapkanData() {
        _reviews = resources.getStringArray(R.array.isiReview).toMutableList()
    }

    fun TambahData() {
        val gson = Gson()
        val editor = sp.edit()
        arReviews.clear()
        for (position in _reviews.indices) {
            val data = reviewDetail(
                _reviews[position]
            )
            arReviews.add(data)
        }
        val json = gson.toJson(arReviews)
        editor.putString("spReviews", json)
        editor.apply()
    }

    fun TampilData() {
        _rvReviews.layoutManager = LinearLayoutManager(this)
        _rvReviews.adapter = adapterReview(arReviews)

        val adapter_review = adapterReview(arReviews)
        _rvReviews.adapter = adapter_review

        adapter_review.setOnItemClickCallback(object : adapterReview.OnItemClickCallback {
            override fun onItemClicked(data: reviewDetail) {

                val intent = Intent(this@Reviews, DetReviews::class.java)
                intent.putExtra("kirimData", data)
                startActivity(intent)
            }
        })
    }
}