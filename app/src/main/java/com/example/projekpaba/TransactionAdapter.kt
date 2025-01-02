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
    private val selectedServices: MutableList<HashMap<String, String>>, // list jasa
    private val onRemoveItem: (Int) -> Unit // Callback untuk menghapus item
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

    override fun getItemCount(): Int = selectedServices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = selectedServices[position]
        holder.serviceName.text = service["namaService"]
        holder.companyName.text = service["namaPerusahaan"]
        holder.servicePrice.text = "Price: ${service["harga"]}"
        Picasso.get().load(service["foto"]).into(holder.imgService)

        // delete button
        holder.deleteButton.setOnClickListener {
            onRemoveItem(position)
        }
    }
}
