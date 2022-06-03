package com.example.agricultural_products_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.agricultural_products_store.fragment.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {
    private var fragment: Fragment?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide()

//        list.add(R.drawable.ic_addphoto)
//        list.add(R.drawable.ic_cart)
//        val indicator : CircleIndicator3 = findViewById<CircleIndicator3>(R.id.indicator)
//        val view_pager2 = findViewById<ViewPager2>(R.id.view_pager2)
//        view_pager2.adapter= ViewPagerAdapter(list)
//        view_pager2.orientation= ViewPager2.ORIENTATION_HORIZONTAL
//        indicator.setViewPager(view_pager2)
        auth = FirebaseAuth.getInstance()

        var bottomBar = findViewById<ChipNavigationBar>(R.id.bottomBar)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.relative_main,HomeFragment())
                    .commit()
            bottomBar.setItemSelected(R.id.home)
        }

        bottomBar.setOnItemSelectedListener {
            when(it){
                R.id.home->{
                    fragment = HomeFragment()
                }
                R.id.discount->{
                    fragment = DiscountFragment()
                }
                R.id.cart-> {
                    fragment = CartFragment()
                }
                R.id.category->{
                    fragment = CategoryFragment()
                }
                R.id.me->{
                    fragment = UserFragment()
                }
            }
            //true
            fragment!!.let {
                supportFragmentManager.beginTransaction().replace(R.id.relative_main,fragment!!).commit()
            }
        }
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser !=null) {
            var uid = currentUser.uid
            fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("countCart").document(uid)
                    .addSnapshotListener { value, error ->
                        if (value?.exists()!!) {
                            var data = value.data!!
                            var countCart = data.get("sumCart") as Number
                            var count = countCart.toInt()
                            bottomBar.showBadge(R.id.cart, count)
                        }
                    }
        }

//        val logout = findViewById<Button>(R.id.button)
//        logout.setOnClickListener {
//            auth.signOut()
//            startActivity(Intent(this,LoginActivity::class.java))
//            finish()
//        }
//        val profile = findViewById<Button>(R.id.button1)
//        profile.setOnClickListener {
//            startActivity(Intent(this, ProfileActivity::class.java))
//        }
    }
}