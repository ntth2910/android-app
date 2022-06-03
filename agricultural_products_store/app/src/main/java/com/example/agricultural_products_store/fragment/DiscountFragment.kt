package com.example.agricultural_products_store.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.CommentAdapter
import com.example.agricultural_products_store.Adapter.ViewPagerAdapter
import com.example.agricultural_products_store.Model.ModelCart
import com.example.agricultural_products_store.Model.ModelComment
import com.example.agricultural_products_store.Model.ModelDetailPayment
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var detail : ViewPagerAdapter?=null
    private val collectionReferenceComment : CollectionReference = db.collection("comments")
    var commentAdapter : CommentAdapter?=null

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
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_discount, container, false)

        fireStore = FirebaseFirestore.getInstance()
        val sdf = SimpleDateFormat("yyyy.MM.dd ")
        val currentDate = sdf.format(Date())
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser!=null) {
            var uid = currentUser?.uid
            val recyclerComment = view.findViewById<RecyclerView>(R.id.recyclerViewCMT)
            val queryComment: Query = collectionReferenceComment.whereEqualTo("idUser", uid).orderBy("date", Query.Direction.DESCENDING).startAfter(currentDate)
            val firestoreRecyclerOptionsComment: FirestoreRecyclerOptions<ModelComment> = FirestoreRecyclerOptions.Builder<ModelComment>()
                    .setQuery(queryComment, ModelComment::class.java)
                    .build()
            commentAdapter = CommentAdapter(firestoreRecyclerOptionsComment)
            recyclerComment.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
            recyclerComment.adapter = commentAdapter
        }

        return view
    }
    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser!=null) {
            commentAdapter!!.startListening()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser!=null) {
            commentAdapter!!.stopListening()
        }
    }

//    override fun onStart() {
//        super.onStart()
//        cartAdapter!!.startListening()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cartAdapter!!.stopListening()
//    }
//
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DiscountFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}