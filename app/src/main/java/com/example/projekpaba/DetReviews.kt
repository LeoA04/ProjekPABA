package com.example.projekpaba

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetReviews : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_det_reviews)

        val _tvIsiReview = findViewById<TextView>(R.id.tvIsiReview)
        val _tvRating = findViewById<TextView>(R.id.tvRating)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataIntent = intent.getParcelableExtra<reviewDetail>("kirimData", reviewDetail::class.java)
        if (dataIntent != null) {
            _tvIsiReview.text = dataIntent.reviewText
            _tvRating.text = dataIntent.rating
        }
    }
}
