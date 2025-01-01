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
    private val itemList: ArrayList<agencyMarketing>,
    private val showQuantity: Boolean = false,
    private val onRemoveItem: (Int) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgItem: ImageView = itemView.findViewById(R.id.transactionImageView)
        var nameItem: TextView = itemView.findViewById(R.id.transactionName)
        var priceItem: TextView = itemView.findViewById(R.id.transactionPrice)
        var quantityItem: TextView = itemView.findViewById(R.id.quantityText)
        var minusButton: ImageButton = itemView.findViewById(R.id.minusButton)
        var plusButton: ImageButton = itemView.findViewById(R.id.plusButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_service, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Set data to views
        Picasso.get().load(item.foto).into(holder.imgItem)
        holder.nameItem.text = item.nama
        holder.priceItem.text = item.harga
        holder.quantityItem.text = "Quantity: ${item.quantity}"

        if (showQuantity) {
            holder.quantityItem.visibility = View.VISIBLE
            holder.minusButton.visibility = View.VISIBLE
            holder.plusButton.visibility = View.VISIBLE

            // Minus button action
            holder.minusButton.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    holder.quantityItem.text = "Quantity: ${item.quantity}"
                    notifyItemChanged(position)
                } else {
                    onRemoveItem(position)
                }
            }

            // Plus button action
            holder.plusButton.setOnClickListener {
                item.quantity++
                holder.quantityItem.text = "Quantity: ${item.quantity}"
                notifyItemChanged(position)
            }
        } else {
            holder.quantityItem.visibility = View.GONE
            holder.minusButton.visibility = View.GONE
            holder.plusButton.visibility = View.GONE
        }
    }
}