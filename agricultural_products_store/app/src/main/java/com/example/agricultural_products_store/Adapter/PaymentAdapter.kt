package com.example.agricultural_products_store.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Model.ModelCart
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class PaymentAdapter(options: FirestoreRecyclerOptions<ModelCart>) :
    FirestoreRecyclerAdapter<ModelCart, PaymentAdapter.ViewHolder>(
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
        val accept = itemView.findViewById<Button>(R.id.thanhtoan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_detailpayment, parent, false)
        )
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
        var price = model.price
        var totalPrice = model.totalPrice
        var quantity = model.quantity
        var image = model.image
        var name = model.name
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val currentDate = sdf.format(Date())
        holder.accept.setOnClickListener {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            var uid = currentUser.uid
            fireStore.collection("temporaryPayment").document(uid)
                .get()
                .addOnSuccessListener { task ->
                    if (task.exists()){
                        var data= task.data!!
                        var username = data.get("username") as String
                        var phone = data.get("phone") as String
                        var local = data.get("local") as String
                        var id = data.get("id") as String
                        var date = data.get("date") as String
                        fireStore.collection("payments").document(id)
                            .get()
                            .addOnSuccessListener { task ->
                                if (task.exists()){
                                    var data = task.data!!
                                    var pricee = data.get("totalPrice") as Number
                                    var priceee = pricee.toFloat()
                                    val newTotal = priceee + totalPrice?.toFloat()!!

                                    val addPayment : MutableMap<String, Any> = HashMap()
                                    addPayment["id"] = id
                                    addPayment["username"] = username
                                    addPayment["phone"] =  phone
                                    addPayment["local"] = local
                                    addPayment["date"] = currentDate
                                    addPayment["quantity"] = FieldValue.increment(1)
                                    addPayment["totalPrice"] = newTotal
                                    addPayment["idUser"] = uid
                                    addPayment["status"] = 1
                                    fireStore.collection("payments").document(id)
                                        .update(addPayment)
                                        .addOnSuccessListener {
                                            val addDetail : MutableMap<String, Any> = HashMap()
                                            addDetail["idPayment"] = id
                                            addDetail["image"] =  image.toString()
                                            addDetail["name"] = name.toString()
                                            addDetail["idProduct"] = idProduct.toString()
                                            addDetail["quantity"] = quantity.toString()
                                            addDetail["totalPrice"] = totalPrice.toString()
                                            fireStore.collection("detailPayment").document()
                                                .set(addDetail)
                                                .addOnSuccessListener {
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
                                                                                        .addOnSuccessListener {
                                                                                        }
                                                                                }
                                                                        }
                                                                    }

                                                            }
                                                        }
                                                }
                                        }
                                }else{
                                    val addPayment : MutableMap<String, Any> = HashMap()
                                    addPayment["id"] = id
                                    addPayment["username"] = username
                                    addPayment["phone"] =  phone
                                    addPayment["local"] = local
                                    addPayment["date"] = currentDate
                                    addPayment["quantity"] = 1
                                    addPayment["idUser"] = uid
                                    addPayment["totalPrice"] = totalPrice?.toFloat()!!
                                    addPayment["status"] = 1
                                    fireStore.collection("payments").document(id)
                                        .set(addPayment)
                                        .addOnSuccessListener {
                                            val addDetail : MutableMap<String, Any> = HashMap()
                                            addDetail["idPayment"] = id
                                            addDetail["image"] =  image.toString()
                                            addDetail["name"] = name.toString()
                                            addDetail["idProduct"] = idProduct.toString()
                                            addDetail["quantity"] = quantity.toString()
                                            addDetail["totalPrice"] = totalPrice.toString()
                                            fireStore.collection("detailPayment").document()
                                                .set(addDetail)
                                                .addOnSuccessListener {
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
                                                                                        .addOnSuccessListener {
                                                                                        }
                                                                                }
                                                                        }
                                                                    }

                                                            }
                                                        }
                                                }
                                        }
                                }
                            }
                        fireStore.collection("countPriceUser").document(uid)
                            .get()
                            .addOnSuccessListener { task ->
                                if (task.exists()){
                                    var data= task.data!!
                                    var countPrice = data.get("countTotalPrice") as Number
                                    var countPricee = countPrice.toFloat()
                                    var newCountPrice = countPricee + totalPrice?.toFloat()!!
                                    val addCountUser : MutableMap<String, Any> = HashMap()
                                    addCountUser["countOrder"] = FieldValue.increment(1)
                                    addCountUser["countTotalPrice"] = newCountPrice
                                    addCountUser["date"] = currentDate
                                    fireStore.collection("countPriceUser")
                                        .document(uid)
                                        .update(addCountUser)
                                        .addOnSuccessListener {
                                        }
                                }else{
                                    val addCountUserr : MutableMap<String, Any> = HashMap()
                                    addCountUserr["countOrder"] = 1
                                    addCountUserr["countTotalPrice"] = totalPrice?.toFloat()!!
                                    addCountUserr["date"] = currentDate
                                    addCountUserr["Uid"] = uid
                                    fireStore.collection("countPriceUser")
                                        .document(uid)
                                        .set(addCountUserr)
                                        .addOnSuccessListener {
                                        }
                                }
                            }
                        fireStore.collection("countPriceAdmin").document("QSkH4d0p1s7X1NJf702C")
                            .get()
                            .addOnSuccessListener { task ->
                                if (task.exists()){
                                    var data= task.data!!
                                    var countPriceAdmin = data.get("countTotalPrice") as Number
                                    var countPriceeAdmin = countPriceAdmin.toFloat()
                                    var newCountPriceAdmin = countPriceeAdmin + totalPrice?.toFloat()!!
                                    val addCountAdmin : MutableMap<String, Any> = HashMap()
                                    addCountAdmin["countOrder"] = FieldValue.increment(1)
                                    addCountAdmin["countTotalPrice"] = newCountPriceAdmin
                                    addCountAdmin["date"] = currentDate
                                    fireStore.collection("countPriceAdmin")
                                        .document("QSkH4d0p1s7X1NJf702C")
                                        .update(addCountAdmin)
                                        .addOnSuccessListener {
                                        }
                                }
                            }
                        fireStore.collection("products").document(idProduct.toString())
                                .get()
                                .addOnSuccessListener {
                                    task ->
                                    if (task.exists()){
                                        var data = task.data!!
                                        var amountTotal = data.get("amount") as Number
                                        var amount = amountTotal.toInt()
                                        var newAmount = amount-quantity?.toInt()!!
                                        val newAmountt : MutableMap<String, Any> = HashMap()
                                        newAmountt["amount"] = newAmount
                                        fireStore.collection("products").document(idProduct.toString())
                                                .update(newAmountt)
                                                .addOnSuccessListener {

                                                }
                                    }
                                }
                    }else{

                    }
                }
        }
    }
}