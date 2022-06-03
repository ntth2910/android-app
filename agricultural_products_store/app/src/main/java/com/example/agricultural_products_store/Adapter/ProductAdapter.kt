package com.example.agricultural_products_store.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.LoginActivity
import com.example.agricultural_products_store.MainActivity
import com.example.agricultural_products_store.Model.ModelCart
import com.example.agricultural_products_store.Model.ModelProduct
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.fragment.CartFragment
import com.example.agricultural_products_store.fragment.DetailProductFragment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.HashMap

class ProductAdapter(options: FirestoreRecyclerOptions<ModelProduct>): FirestoreRecyclerAdapter<ModelProduct, ProductAdapter.ViewHolder>(
        options
) {
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var image  = itemView.findViewById<ImageView>(R.id.imageCardProduct)
        var nameProduct = itemView.findViewById<TextView>(R.id.nameCardProduct)
        var price =itemView.findViewById<TextView>(R.id.priceCardProduct)
        var addCart =itemView.findViewById<ImageButton>(R.id.addCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_product, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelProduct) {
        Picasso.get().load(model.image).into(holder.image)
        holder.nameProduct.text=model.name
        holder.price.text=model.price
        holder.itemView.setOnClickListener { view ->
            val price : String? = model.price
            val image : String? = model.image
            val note : String? = model.note
            val name : String?= model.name
            val id : String?=model.id

            val bundle = Bundle()
            bundle.putString("nameProduct", name)
            bundle.putString("imageProduct", image)
            bundle.putString("noteProduct", note)
            bundle.putString("priceProduct", price)
            bundle.putString("idProduct", id)
            val activity =view.context as AppCompatActivity
            val detailProductFragment = DetailProductFragment()
            detailProductFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.layoutHome,detailProductFragment).addToBackStack(null).commit()

        }
        var idProduct = model.id
        var imagee = model.image
        var priceProduct = model.price
        var namee = model.name

        holder.addCart.setOnClickListener { view ->
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if(currentUser!=null) {
                var uid = currentUser.uid
                fireStore.collection("carts").document(idProduct.toString()).get()
                        .addOnSuccessListener { task ->
                            if (task.exists()) {
                                var data = task.data!!
//                            var image = data.get("image") as String
//                            var name = data.get("nameProduct") as String
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
                                        .addOnSuccessListener {
                                            if (it.exists()) {
                                                var data = it.data!!
                                                var totalCartPrice = data.get("totalPrice") as Number
                                                var totall = totalCartPrice.toFloat()
                                                var newTotal = totall + price
                                                val countCartt: MutableMap<String, Any> = HashMap()
                                                countCartt["sumCart"] = FieldValue.increment(1)
                                                countCartt["totalPrice"] = newTotal
                                                fireStore.collection("countCart").document(uid)
                                                        .update(countCartt)
                                                        .addOnSuccessListener {
                                                        }
                                            } else {
                                                val countCarttt: MutableMap<String, Any> = HashMap()
                                                countCarttt["sumCart"] = 1
                                                countCarttt["totalPrice"] = price
                                                fireStore.collection("countCart").document(uid)
                                                        .set(countCarttt)
                                                        .addOnSuccessListener {
                                                        }
                                            }
                                        }
                            } else {
                                val addCarts: MutableMap<String, Any> = HashMap()
                                addCarts["idUser"] = uid
                                addCarts["name"] = namee.toString()
                                addCarts["idProduct"] = idProduct.toString()
                                addCarts["image"] = imagee.toString()
                                addCarts["totalPrice"] = priceProduct?.toFloat()!!
                                addCarts["quantity"] = 1
                                addCarts["price"] = priceProduct.toFloat()
                                fireStore.collection("carts").document(idProduct.toString())
                                        .set(addCarts)
                                        .addOnSuccessListener {
                                        }
                                fireStore.collection("countCart").document(uid).get()
                                        .addOnSuccessListener {
                                            if (it.exists()) {
                                                var data = it.data!!
                                                var totalCartPrice = data.get("totalPrice") as Number
                                                var totall = totalCartPrice.toFloat()
                                                var priceProd = priceProduct.toFloat()
                                                var newTotal = totall + priceProd
                                                val countCartt: MutableMap<String, Any> = HashMap()
                                                countCartt["sumCart"] = FieldValue.increment(1)
                                                countCartt["totalPrice"] = newTotal
                                                fireStore.collection("countCart").document(uid)
                                                        .update(countCartt)
                                                        .addOnSuccessListener {
                                                        }
                                            }
                                        }
                            }

                        }
            }else{
                val activity =view.context as AppCompatActivity
                val loginActivity = LoginActivity()
                var intent = Intent(activity,loginActivity::class.java)
                activity.startActivity(intent)
                //startActivity(Intent(activity, loginActivity::class.java))
                //finish()
            }

        }
    }
}