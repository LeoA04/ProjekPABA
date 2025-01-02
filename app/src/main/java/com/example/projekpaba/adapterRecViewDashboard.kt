package com.example.projekpaba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class adapterRecViewDashboard(
    private val recommendations: List<HashMap<String, String>>
) : RecyclerView.Adapter<adapterRecViewDashboard.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: HashMap<String, String>)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        this.onItemClickCallback = callback
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAgencyImage: ImageView = itemView.findViewById(R.id.ivAgencyImage)
        val tvAgencyName: TextView = itemView.findViewById(R.id.tvAgencyName)
        val tvAgencyLocation: TextView = itemView.findViewById(R.id.tvAgencyLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recommendations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agency = recommendations[position]
        holder.tvAgencyName.text = agency["nama"]
        holder.tvAgencyLocation.text = agency["lokasi"]
        Picasso.get().load(agency["foto"]).into(holder.ivAgencyImage)

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(agency)
        }
    }
}

