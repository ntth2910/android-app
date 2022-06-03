package com.example.agricultural_products_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.agricultural_products_store.Adapter.DetailOrderAdapter
import com.example.agricultural_products_store.Adapter.ViewPagerAdapter
import com.example.agricultural_products_store.fragment.Payment1Fragment
import com.example.agricultural_products_store.fragment.Payment2Fragment
import com.example.agricultural_products_store.fragment.Payment3Fragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class PaymentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("countPriceUser")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        getSupportActionBar()?.hide()
        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Payment1Fragment(),"Mới đặt")
        adapter.addFragment(Payment2Fragment(),"Đang giao")
        adapter.addFragment(Payment3Fragment(),"Đã giao")
        val viewpager = findViewById<ViewPager>(R.id.viewPager)
        val tabs = findViewById<TabLayout>(R.id.tabLayout)
        viewpager.adapter=adapter
        tabs.setupWithViewPager(viewpager)

        val viewCountProduct = findViewById<TextView>(R.id.viewTotalOrder)
        val viewCountPrice = findViewById<TextView>(R.id.viewTotalProduct)
        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var uid = currentUser.uid
        fireStore.collection("countPriceUser").document(uid)
            .get()
            .addOnSuccessListener {
                task ->
                if(task.exists()){
                    var data = task.data!!
                    var countOrder = data.get("countOrder") as Number
                    var countTotalPrice = data.get("countTotalPrice") as Number

                    viewCountProduct.text=countOrder.toInt().toString()
                    viewCountPrice.text=countTotalPrice.toFloat().toString()
                }
            }


//        tabs.getTabAt(0)!!.set
    }
}