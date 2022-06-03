package com.example.agricultural_products_store.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.CartAdapter
import com.example.agricultural_products_store.DetailPaymentActivity
import com.example.agricultural_products_store.LoginActivity
import com.example.agricultural_products_store.Model.ModelCart
import com.example.agricultural_products_store.OrderActivity
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.profile.ProfileUserActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("carts")
    private var cartAdapter : CartAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser!=null) {
            var uid = currentUser.uid
            val recyclerViewCart = view.findViewById<RecyclerView>(R.id.recyclerViewCart)
            val queryCart: Query = collectionReference.whereEqualTo("idUser", uid)
            val firestoreRecyclerOptionsCart: FirestoreRecyclerOptions<ModelCart> = FirestoreRecyclerOptions.Builder<ModelCart>()
                    .setQuery(queryCart, ModelCart::class.java)
                    .build()
            cartAdapter = CartAdapter(firestoreRecyclerOptionsCart)
            recyclerViewCart.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
            recyclerViewCart.adapter= cartAdapter
            val countProd = view.findViewById<TextView>(R.id.countProduct)
            val countPri = view.findViewById<TextView>(R.id.countPrice)
            fireStore.collection("countCart").document(uid)
                    .addSnapshotListener { value, error ->
                        if (value?.exists()!!){
                            var data =value?.data!!
                            var countNumber= data.get("sumCart") as Number
                            var countTotal = data.get("totalPrice") as Number
                            var countTotall = countNumber.toInt()
                            var countNumberr = countTotal.toFloat()
                            countProd.setText(countTotall.toString())
                            countPri.setText(countNumberr.toString())
                        }
                    }
        }
        val payment = view.findViewById<Button>(R.id.submitPayment)
//        val model = ModelCart()
//        var dbb = db.collection("detailPayment").add(model)
        payment.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                activity?.startActivity(Intent(activity, OrderActivity::class.java))
            }else{
                activity?.startActivity(Intent(activity, LoginActivity::class.java))
            }
        }

        return  view
    }
    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser!=null) {
            cartAdapter!!.startListening()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser!=null) {
            cartAdapter!!.stopListening()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CartFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}