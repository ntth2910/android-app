package com.example.agricultural_products_store

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.CartAdapter
import com.example.agricultural_products_store.Adapter.PaymentAdapter
import com.example.agricultural_products_store.Model.ModelCart
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DetailPaymentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("carts")
    var paymentAdapter : PaymentAdapter?=null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_payment)
        getSupportActionBar()?.hide()
        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var uid = currentUser.uid
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerpayment)
        val queryCart : Query = collectionReference.whereEqualTo("idUser",uid)
        val firestoreRecyclerOptionsCart: FirestoreRecyclerOptions<ModelCart> = FirestoreRecyclerOptions.Builder<ModelCart>()
            .setQuery(queryCart, ModelCart::class.java)
            .build()
        paymentAdapter = PaymentAdapter(firestoreRecyclerOptionsCart)
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.adapter= paymentAdapter

        val successfull = findViewById<TextView>(R.id.buttonSuccesFul)
        successfull.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

//        var intent = intent
//        if (intent!=null){
//            val name = intent.getStringExtra("username")
//            val phone = intent.getStringExtra("phone")
//            val locall = intent.getStringExtra("local")
//            textName.text= name
//            textPhone.text= phone
//            textLocal.text= locall
//        }

    }
    override fun onStart() {
        super.onStart()
        paymentAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        paymentAdapter!!.stopListening()
    }
}