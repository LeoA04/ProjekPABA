package com.example.projekpaba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class voucherAdapter(
    private val voucherList: ArrayList<voucherDetail>,
    private val onItemClick: (voucherDetail) -> Unit
) : RecyclerView.Adapter<voucherAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_voucher, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vouchers: voucherDetail = voucherList[position]
        holder.title.text = vouchers.title
        holder.description.text = vouchers.content
        holder.periode.text = vouchers.periode

        holder.itemView.setOnClickListener {
            onItemClick(vouchers)
        }
    }

    override fun getItemCount(): Int {
        return voucherList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val periode: TextView = itemView.findViewById(R.id.tvPeriode)
    }
}
