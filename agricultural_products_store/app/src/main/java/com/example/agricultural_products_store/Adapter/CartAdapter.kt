package com.example.agricultural_products_store.Adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Model.ModelCart
import com.example.agricultural_products_store.Model.ModelComment
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.fragment.CartFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.HashMap

class CartAdapter(options: FirestoreRecyclerOptions<ModelCart>): FirestoreRecyclerAdapter<ModelCart, CartAdapter.ViewHolder>(
        options
) {
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageProduct)
        val name = itemView.findViewById<TextView>(R.id.nameProduct)
        val numberCart =  itemView.findViewById<TextView>(R.id.numberCart)
        val addCart =  itemView.findViewById<ImageView>(R.id.addCart)
        val removeCart =  itemView.findViewById<ImageView>(R.id.removeCart)
        val price =  itemView.findViewById<TextView>(R.id.priceProduct)
        val totalPrice =  itemView.findViewById<TextView>(R.id.total_price)
        val deleteCart =  itemView.findViewById<ImageView>(R.id.deleteCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_cart, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelCart) {
        Picasso.get().load(model.image).into(holder.image)
        holder.name.text= model.name
        holder.numberCart.text=model.quantity.toString()
        holder.price.text=model.price.toString()
        holder.totalPrice.text=model.totalPrice.toString()
        var idProduct = model.idProduct
        holder.addCart.setOnClickListener {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            var uid = currentUser.uid
            fireStore.collection("carts").document(idProduct.toString())
                    .get()
                    .addOnSuccessListener { task ->
                        if (task.exists()) {
                            var data = task.data!!
                            var pricee = data.get("price") as Number
                            var quantity = data.get("quantity") as Number

                            var price = pricee.toFloat()
                            var quantityy = quantity.toFloat()
                            var newSum = quantityy + 1
                            var total = price * newSum
                            val updateCarts: MutableMap<String, Any> = HashMap()
                            updateCarts["totalPrice"] = total
                            updateCarts["quantity"] = newSum
                            fireStore.collection("carts").document(idProduct.toString())
                                    .update(updateCarts)
                                    .addOnSuccessListener {
                                    }
                            fireStore.collection("countCart").document(uid).get()
                                    .addOnSuccessListener { task ->
                                        if (task.exists()) {
                                            var data = task.data!!
                                            var totalCartPrice = data.get("totalPrice") as Number
                                            var totall = totalCartPrice.toFloat()
                                            var newTotal = totall + price
                                            val countCartt: MutableMap<String, Any> = HashMap()
//                                            countCartt["sumCart"] = FieldValue.increment(1)
                                            countCartt["totalPrice"] = newTotal
                                            fireStore.collection("countCart").document(uid)
                                                    .update(countCartt)
                                                    .addOnSuccessListener {
                                                    }
                                        }else{
                                            val countCarttt : MutableMap<String, Any> = HashMap()
                                            countCarttt["sumCart"] = 1
                                            countCarttt["totalPrice"] = price
                                            fireStore.collection("countCart").document(uid)
                                                .set(countCarttt)
                                                .addOnSuccessListener {
                                                }
                                        }
                                    }
                        }
                    }


                    }
        holder.removeCart.setOnClickListener {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            var uid = currentUser.uid
            fireStore.collection("carts").document(idProduct.toString())
                    .get()
                    .addOnSuccessListener { task ->
                        if (task.exists()) {
                            var data = task.data!!
                            var pricee = data.get("price") as Number
                            var quantity = data.get("quantity") as Number

                            var price = pricee.toFloat()
                            var quantityy = quantity.toFloat()
                            var newSum = quantityy - 1
                            var total = price * newSum
                            val updateCarts: MutableMap<String, Any> = HashMap()
                            updateCarts["totalPrice"] = total
                            updateCarts["quantity"] = newSum
                            fireStore.collection("carts").document(idProduct.toString())
                                    .update(updateCarts)
                                    .addOnSuccessListener {
                                    }
                            fireStore.collection("countCart").document(uid).get()
                                    .addOnSuccessListener { task ->
                                        if (task.exists()) {
                                            var data = task.data!!
                                            var totalCartPrice = data.get("totalPrice") as Number
                                            var totall = totalCartPrice.toFloat()
                                            var newTotal = totall - price
                                            val countCartt: MutableMap<String, Any> = HashMap()
//                                            countCartt["sumCart"] = FieldValue.increment(1)
                                            countCartt["totalPrice"] = newTotal
                                            fireStore.collection("countCart").document(uid)
                                                    .update(countCartt)
                                                    .addOnSuccessListener {
                                                    }
                                        }
                                    }
                        }
                    }
        }
        holder.deleteCart.setOnClickListener {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            var uid = currentUser.uid
            fireStore.collection("carts").document(idProduct.toString())
                    .get()
                    .addOnSuccessListener {task ->
                        if (task.exists()) {
                            var data = task.data!!
                            var pricee = data.get("totalPrice") as Number
                            var totalPrice = pricee.toFloat()
                            fireStore.collection("countCart").document(uid).get()
                                    .addOnSuccessListener { task ->
                                        if (task.exists()) {
                                            var data = task.data!!
                                            var totalCartPrice = data.get("totalPrice") as Number
                                            var countCart = data.get("sumCart") as Number
                                            var totall = totalCartPrice.toFloat()
                                            var countProdcutCart = countCart.toInt()
                                            var newCount = countProdcutCart - 1
                                            var newTotal = totall - totalPrice
                                            val countCartt: MutableMap<String, Any> = HashMap()
                                            countCartt["sumCart"] = newCount
                                            countCartt["totalPrice"] = newTotal
                                            fireStore.collection("countCart").document(uid)
                                                    .update(countCartt)
                                                    .addOnSuccessListener {
                                                        fireStore.collection("carts").document(idProduct.toString())
                                                                .delete()
                                                                .addOnSuccessListener { task ->
                                                                }
                                                    }
                                        }
                                    }

                        }
                    }
            //
        }
    }
}