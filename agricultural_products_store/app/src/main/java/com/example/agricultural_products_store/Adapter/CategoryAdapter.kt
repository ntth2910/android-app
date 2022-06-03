package com.example.agricultural_products_store.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Model.ModelCategory
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.fragment.CategoryFragment
import com.example.agricultural_products_store.fragment.DetailProductFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class CategoryAdapter(options: FirestoreRecyclerOptions<ModelCategory>) : FirestoreRecyclerAdapter<ModelCategory, CategoryAdapter.ViewHolder>(
    options
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_category,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelCategory) {
        holder.nameCategory.text=model.name_category
        Picasso.get().load(model.image).into(holder.image)
        val id = model.id
        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putString("idCategory", id)
            val activity =view.context as AppCompatActivity
            val categoryFragment  = CategoryFragment()
            categoryFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.layoutHome,categoryFragment).addToBackStack(null).commit()
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var image  = itemView.findViewById<ImageView>(R.id.imageCardCategory)
        var nameCategory = itemView.findViewById<TextView>(R.id.textCardCategory)
    }
}