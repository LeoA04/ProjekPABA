package com.example.projekpaba

// Import berbagai package yang dibutuhkan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Mendefinisikan class adapterReview yang merupakan subclass dari RecyclerView.Adapter
class adapterReview (private val listReview: ArrayList<reviewDetail>) : RecyclerView.Adapter<adapterReview.ListViewHolder>() {

    // Mendefinisikan ViewHolder yang akan menampung view dari item
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _isiReview = itemView.findViewById<TextView>(R.id.tvIsiReview)
    }

    // Method untuk membuat ViewHolder dan meng-inflate layout item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_det_reviews, parent, false)
        return ListViewHolder(view)
    }

    // Method untuk mendapatkan jumlah item dalam adapter
    override fun getItemCount(): Int {
        return listReview.size
    }

    // Method untuk menghubungkan data dengan view item
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = listReview[position]

        // Mengatur teks untuk TextView berdasarkan data review
        holder._isiReview.setText(review.review)
    }

    // Deklarasi interface untuk menangani klik item
    private lateinit var onItemClickCallback : OnItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: reviewDetail)
    }

    // Method untuk menetapkan callback klik item
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}