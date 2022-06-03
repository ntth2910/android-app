package com.example.agricultural_products_store.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Model.ModelReplyCMT
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ReplyCMTAdapter(options: FirestoreRecyclerOptions<ModelReplyCMT>): FirestoreRecyclerAdapter<ModelReplyCMT, ReplyCMTAdapter.ViewHolder>(
    options
) {
    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)  {
        val username = itemView.findViewById<TextView>(R.id.username)
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_replycmt, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelReplyCMT) {
        holder.username.text=model.name
        holder.date.text=model.date
        holder.title.text = model.title
    }

}