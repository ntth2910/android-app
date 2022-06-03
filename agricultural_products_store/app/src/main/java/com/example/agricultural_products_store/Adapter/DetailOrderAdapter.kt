package com.example.agricultural_products_store.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Model.ModelCategory
import com.example.agricultural_products_store.Model.ModelOrder
import com.example.agricultural_products_store.PaymentActivity
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.fragment.DetailOrderFragment
import com.example.agricultural_products_store.fragment.DetailProductFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DetailOrderAdapter(options: FirestoreRecyclerOptions<ModelOrder>) : FirestoreRecyclerAdapter<ModelOrder, DetailOrderAdapter.ViewHolder>(
    options
){
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var name  = itemView.findViewById<TextView>(R.id.viewName)
        var phone = itemView.findViewById<TextView>(R.id.viewPhone)
        var local = itemView.findViewById<TextView>(R.id.viewLocal)
        var date = itemView.findViewById<TextView>(R.id.viewDate)
        var quantity = itemView.findViewById<TextView>(R.id.viewQuantity)
        var totalPrice=itemView.findViewById<TextView>(R.id.viewTotalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_orderpayment,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelOrder) {
        holder.name.text=model.username
        holder.phone.text=model.phone
        holder.local.text=model.local
        holder.date.text=model.date
        holder.quantity.text=model.quantity.toString()
        holder.totalPrice.text=model.totalPrice.toString()
        var idPayment = model.id
        holder.itemView.setOnClickListener {view ->
            val bundle = Bundle()
            bundle.putString("idPayment", idPayment)
            val activity =view.context as AppCompatActivity
            val detailOrderFragment = DetailOrderFragment()
            detailOrderFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.relative_mainOrder,detailOrderFragment).addToBackStack(null).commit()
        }
    }
}