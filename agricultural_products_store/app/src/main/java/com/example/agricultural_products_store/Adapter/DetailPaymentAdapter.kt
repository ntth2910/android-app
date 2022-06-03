package com.example.agricultural_products_store.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Model.ModelDetailPayment
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso


class DetailPaymentAdapter(options: FirestoreRecyclerOptions<ModelDetailPayment>): FirestoreRecyclerAdapter<ModelDetailPayment, DetailPaymentAdapter.ViewHolder>(
    options
) {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageProduct)
        val name = itemView.findViewById<TextView>(R.id.nameProduct)
        val numberCart =  itemView.findViewById<TextView>(R.id.numberCart)
        val price =  itemView.findViewById<TextView>(R.id.priceProduct)
        val totalPrice =  itemView.findViewById<TextView>(R.id.total_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_detailorderpayment, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelDetailPayment) {
        Picasso.get().load(model.image).into(holder.image)
        holder.name.text= model.name
        holder.numberCart.text=model.quantity.toString()
        holder.totalPrice.text=model.totalPrice.toString()
        var quantityy = model.quantity?.toInt()
        var totalPricee = model.totalPrice?.toFloat()
        if (totalPricee != null && quantityy != null) {
            var pricee = totalPricee/quantityy
            holder.price.text=pricee.toString()
        }
    }

}