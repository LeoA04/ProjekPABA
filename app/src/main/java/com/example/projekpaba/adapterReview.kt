package com.example.projekpaba
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterReview (private val listReview: ArrayList<reviewDetail>) : RecyclerView
.Adapter<adapterReview.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _isiReview = itemView.findViewById<TextView>(R.id.tvIsiReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_det_reviews, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listReview.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var review = listReview[position]

        holder._isiReview.setText(review.review)
    }

    private lateinit var onItemClickCallback : OnItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: reviewDetail)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}
