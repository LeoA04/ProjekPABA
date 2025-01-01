package com.example.projekpaba

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class reviewDetail(
    var reviewText: String,
    var rating: String
) : Parcelable
