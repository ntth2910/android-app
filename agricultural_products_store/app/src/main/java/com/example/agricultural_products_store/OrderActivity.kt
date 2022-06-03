package com.example.agricultural_products_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.agricultural_products_store.Adapter.CartAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("carts")
    private lateinit var reference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val viewTotalOrder = findViewById<TextView>(R.id.viewTotalOrder)
        val viewTotalProduct = findViewById<TextView>(R.id.viewTotalProduct)

        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var uid = currentUser.uid
        fireStore.collection("countCart").document(uid)
            .addSnapshotListener { value, error ->
                if (value?.exists()!!){
                    var data =value?.data!!
                    var countNumber= data.get("sumCart") as Number
                    var countTotal = data.get("totalPrice") as Number
                    var countTotall = countNumber.toInt()
                    var countNumberr = countTotal.toFloat()
                    viewTotalOrder.setText(countTotall.toString())
                    viewTotalProduct.setText(countNumberr.toString())
                }
            }
        val edit_name = findViewById<EditText>(R.id.edit_name)
        val edit_phone = findViewById<EditText>(R.id.edit_phone)
        val edit_local = findViewById<EditText>(R.id.edit_local)
        val button_accept = findViewById<Button>(R.id.nextPayment)
        fireStore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { task ->
                if (task.exists()){
                    var data = task.data!!
                    var username= data.get("username") as String
                    var phone= data.get("phone") as String
                    var local= data.get("local") as String
                    edit_name.setText(username)
                    edit_phone.setText(phone)
                    edit_local.setText(local)
                }
            }
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val currentDate = sdf.format(Date())
        reference= FirebaseDatabase.getInstance().reference
        val key = reference.push().key
        button_accept.setOnClickListener {
            if (edit_name.text.toString().isNotEmpty()&&edit_phone.text.toString().isNotEmpty()&&edit_local.text.toString().isNotEmpty()) {
                fireStore.collection("temporaryPayment").document(uid)
                    .get()
                    .addOnSuccessListener { task ->
                        if (task.exists()){
                            val updateCarts: MutableMap<String, Any> = HashMap()
                            updateCarts["id"] = key.toString()
                            updateCarts["username"] = edit_name.text.toString()
                            updateCarts["phone"] =  edit_phone.text.toString()
                            updateCarts["local"] = edit_local.text.toString()
                            updateCarts["date"] = currentDate.toString()
                            fireStore.collection("temporaryPayment").document(uid)
                                .update(updateCarts)
                                .addOnSuccessListener {
                                    val intent = Intent(this,DetailPaymentActivity::class.java)
                                    startActivity(intent)
                                }
                        }else{
                            val addCarts : MutableMap<String, Any> = HashMap()
                            addCarts["id"] = key.toString()
                            addCarts["username"] = edit_name.text.toString()
                            addCarts["phone"] =  edit_phone.text.toString()
                            addCarts["local"] = edit_local.text.toString()
                            addCarts["date"] = currentDate.toString()
                            fireStore.collection("temporaryPayment").document(uid)
                                .set(addCarts)
                                .addOnSuccessListener {
                                    val intent = Intent(this,DetailPaymentActivity::class.java)
                                    startActivity(intent)
                                }
                        }
                    }
//                    val intent = Intent(this,DetailPaymentActivity::class.java)
//    //                intent.putExtra("username",edit_name.text.toString())
//    //                intent.putExtra("phone",edit_phone.text.toString())
//    //                intent.putExtra("local",edit_local.text.toString())
//                    startActivity(intent)
            } else {
                Toast.makeText(this,"Nhập đầy đủ các ô thông tin",Toast.LENGTH_LONG).show()
            }
        }
    }
}