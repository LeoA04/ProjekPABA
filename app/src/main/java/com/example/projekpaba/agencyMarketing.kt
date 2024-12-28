package com.example.projekpaba

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class agencyMarketing(
    var foto: String,
    var nama: String,
    var harga: String,
    var lokasi: String,
    var deskripsi: String,
    var quantity: Int = 1
) : Parcelable
