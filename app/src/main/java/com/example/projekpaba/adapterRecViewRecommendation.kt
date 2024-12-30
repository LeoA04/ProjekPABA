package com.example.projekpaba

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class adapterRecViewRecommendation (private val listAgency: ArrayList<agencyMarketing>) : RecyclerView
.Adapter<adapterRecViewRecommendation.ListViewHolder>()  {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _namaAgency = itemView.findViewById<TextView>(R.id.namaMarketingAgency)
        var _hargaAgency = itemView.findViewById<TextView>(R.id.hargaMarketingAgency)
        var _lokasiAgency = itemView.findViewById<TextView>(R.id.lokasiMarketingAgency)
        var _gambarAgency = itemView.findViewById<ImageView>(R.id.gambarMarketingAgency)
        var _btnBack = itemView.findViewById<ImageButton>(R.id.btnBack)
    }

    //fungsi untuk menampilkan data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommendation_page, parent, false)
        return ListViewHolder(view)
    }

    //fungsi untuk mengembalikan jumlah item yang tersedia untuk ditampilkan
    override fun getItemCount(): Int{
        return listAgency.size
    }

    //fungsi untuk menghubungkan data dengan view holder sesuai dengan posisi yang sdh ditentukan di recycle view
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val agency = listAgency[position]

        holder._namaAgency.text = agency.nama
        holder._hargaAgency.text = agency.harga
        holder._lokasiAgency.text = agency.lokasi

        Picasso.get()
            .load(agency.foto)
            .into(holder._gambarAgency)

        holder._gambarAgency.setOnClickListener {
            onItemClickCallback.onItemClicked(agency) // Kirim seluruh objek agency
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: agencyMarketing)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}