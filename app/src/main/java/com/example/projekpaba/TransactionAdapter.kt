package com.example.projekpaba

// Import berbagai package yang dibutuhkan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

// Mendefinisikan class TransactionAdapter yang merupakan subclass dari RecyclerView.Adapter
class TransactionAdapter(
    private val itemList: ArrayList<agencyMarketing>,
    private val showQuantity: Boolean = false,
    private val onRemoveItem: (Int) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    // Mendefinisikan ViewHolder yang akan menampung view dari item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgItem = itemView.findViewById<ImageView>(R.id.transactionImageView)
        var nameItem = itemView.findViewById<TextView>(R.id.transactionName)
        var priceItem = itemView.findViewById<TextView>(R.id.transactionPrice)
        var quantityItem = itemView.findViewById<TextView>(R.id.quantityText)
        var minusButton = itemView.findViewById<ImageButton>(R.id.minusButton)
        var plusButton = itemView.findViewById<ImageButton>(R.id.plusButton)
    }

    // Method untuk membuat ViewHolder dan meng-inflate layout item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_service, parent, false)
        return ViewHolder(view)
    }

    // Method untuk mendapatkan jumlah item dalam adapter
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Method untuk menghubungkan data dengan view item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Menggunakan Picasso untuk memuat gambar dari URL
        Picasso.get().load(item.foto).into(holder.imgItem)
        holder.nameItem.text = item.nama
        holder.priceItem.text = item.harga
        holder.quantityItem.text = item.quantity.toString()

        if (showQuantity) {
            // Menampilkan dan mengatur button quantity jika showQuantity bernilai true
            holder.quantityItem.visibility = View.VISIBLE
            holder.minusButton.visibility = View.VISIBLE
            holder.plusButton.visibility = View.VISIBLE

            // Mengatur aksi untuk button minus
            holder.minusButton.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    holder.quantityItem.text = item.quantity.toString()
                    notifyItemChanged(position)
                } else {
                    onRemoveItem(position)
                }
            }

            // Mengatur aksi untuk button plus
            holder.plusButton.setOnClickListener {
                item.quantity++
                holder.quantityItem.text = item.quantity.toString()
                notifyItemChanged(position)
            }
        } else {
            // Menyembunyikan elemen-elemen quantity jika showQuantity bernilai false
            holder.quantityItem.visibility = View.GONE
            holder.minusButton.visibility = View.GONE
            holder.plusButton.visibility = View.GONE
        }
    }
}