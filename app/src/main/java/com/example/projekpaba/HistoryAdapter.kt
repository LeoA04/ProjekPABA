package com.example.projekpaba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HistoryAdapter(
    private val historyList: List<HashMap<String, String>>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAgencyImage: ImageView = itemView.findViewById(R.id.ivHistoryItemImage)
        val tvAgencyName: TextView = itemView.findViewById(R.id.tvHistoryItemName)
        val tvServiceName: TextView = itemView.findViewById(R.id.namaService)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tvHistoryItemTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]

        // Set data to views
        holder.tvAgencyName.text = item["namaPerusahaan"] ?: "Unknown Agency"
        holder.tvServiceName.text = item["namaService"] ?: "Unknown Service"
        holder.tvTotalPrice.text = "Total: Rp. ${item["harga"] ?: "0"}"

        // Load image with Picasso
        Picasso.get().load(item["foto"]).into(holder.ivAgencyImage)
    }

    override fun getItemCount(): Int = historyList.size
}
