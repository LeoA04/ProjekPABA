package com.example.projekpaba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class TransactionAdapter(
    private val selectedService: HashMap<String, String>, // Only one selected service
    private val onRemoveItem: () -> Unit // Callback for delete action
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgService: ImageView = itemView.findViewById(R.id.transactionImageView)
        val serviceName: TextView = itemView.findViewById(R.id.transactionService)
        val companyName: TextView = itemView.findViewById(R.id.transactionName)
        val servicePrice: TextView = itemView.findViewById(R.id.transactionPrice)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_service, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 1 // Only one item, as it's the selected service

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set data to views
        holder.serviceName.text = selectedService["namaService"]
        holder.companyName.text = selectedService["namaPerusahaan"]
        holder.servicePrice.text = "Price: ${selectedService["harga"]}"
        Picasso.get().load(selectedService["foto"]).into(holder.imgService)

        // Delete button action
        holder.deleteButton.setOnClickListener {
            onRemoveItem() // Trigger callback for deletion
        }
    }
}
