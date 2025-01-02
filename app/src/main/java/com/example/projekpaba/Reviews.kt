package com.example.projekpaba

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Reviews : AppCompatActivity() {

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

        _rvReviews = findViewById(R.id.RvReview)
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("reviews")
            .get()
            .addOnSuccessListener { result ->
                arReviews.clear()
                for (document in result) {
                    val reviewText = document.getString("review") ?: "No Review"
                    val rating = document.getString("rating") ?: "No Rating"
                    val review = reviewDetail(reviewText, rating)
                    arReviews.add(review)
                }
                TampilData()
            }
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