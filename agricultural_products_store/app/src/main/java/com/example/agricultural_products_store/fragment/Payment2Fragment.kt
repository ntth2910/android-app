package com.example.agricultural_products_store.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.DetailOrderAdapter
import com.example.agricultural_products_store.Model.ModelOrder
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Payment2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Payment2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("payments")
    var detailOrderAdapter : DetailOrderAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment1, container, false)

        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var uid = currentUser.uid
        val recyclerView1 = view.findViewById<RecyclerView>(R.id.recyclerPayment1)
        val queryOrder : Query = collectionReference.whereEqualTo("idUser",uid).whereEqualTo("status",2)
        val firestoreRecyclerOptionsOrder: FirestoreRecyclerOptions<ModelOrder> = FirestoreRecyclerOptions.Builder<ModelOrder>()
            .setQuery(queryOrder, ModelOrder::class.java)
            .build()
        detailOrderAdapter = DetailOrderAdapter(firestoreRecyclerOptionsOrder)
        recyclerView1.layoutManager= LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        recyclerView1.adapter= detailOrderAdapter



        return view
    }

    override fun onStart() {
        super.onStart()
        detailOrderAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailOrderAdapter!!.stopListening()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Payment2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Payment2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}