package com.example.agricultural_products_store.fragment

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.CommentAdapter
import com.example.agricultural_products_store.Model.ModelComment
import com.example.agricultural_products_store.Model.ModelRate
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var commentAdapter : CommentAdapter?=null
    private val db:FirebaseFirestore= FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("comments")
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var reference : DatabaseReference
  // private lateinit var database : FirebaseDatabase
    private var ratingg : RatingBar? = null
    private var detailProductFragment: DetailProductFragment? = null

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
        val view =inflater.inflate(R.layout.fragment_detail_product, container, false)

        val nameProduct = view.findViewById<TextView>(R.id.nameDetailProduct)
        val priceProduct = view.findViewById<TextView>(R.id.priceDetailProduct)
        val note = view.findViewById<TextView>(R.id.textProductDetails)
        val image = view.findViewById<ImageView>(R.id.imageDetailProduct)

        var nameProductt = arguments?.getString("nameProduct")
        var priceProductt = arguments?.getString("priceProduct")
        var noteProductt = arguments?.getString("noteProduct")
        var imageProductt =arguments?.getString("imageProduct")
        val idProductt =arguments?.getString("idProduct")

        nameProduct.setText(nameProductt)
        priceProduct.setText(priceProductt)
        Picasso.get().load(imageProductt).into(image)
        note.setText(noteProductt)

        val username = view.findViewById<TextView>(R.id.userNameCMT)
        val comment = view.findViewById<EditText>(R.id.editCMT)
        val addCMT = view.findViewById<Button>(R.id.submitCMT)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var uid = currentUser.uid
        if(currentUser != null){
            fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener { task ->
                        if(task.exists()){
                            val data = task.data!!
                            var usernameStore = data.get("username") as String
                            username.setText(usernameStore)
                        }else {
                        }
                    }.addOnFailureListener { exception ->
                    }
        }else{
        }

        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val currentDate = sdf.format(Date())

        //recyclerViewComment
        val recyclerViewComment = view.findViewById<RecyclerView>(R.id.recyclerComment)
        val queryComment : Query = collectionReference.whereEqualTo("idProduct", idProductt.toString())

        val firestoreRecyclerOptionsComment: FirestoreRecyclerOptions<ModelComment> = FirestoreRecyclerOptions.Builder<ModelComment>()
                .setQuery(queryComment, ModelComment::class.java)
                .build()
        commentAdapter = CommentAdapter(firestoreRecyclerOptionsComment)
        recyclerViewComment.layoutManager= LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
        recyclerViewComment.adapter= commentAdapter

//        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//            Toast.makeText(activity,"Nhập đầy đủ các ô"+rating,Toast.LENGTH_LONG).show()        }
        val ratingBar= view.findViewById<RatingBar>(R.id.ratingBar)

        var rate1 = view.findViewById<TextView>(R.id.rate1)
        var rate2 = view.findViewById<TextView>(R.id.rate2)
        var rate3 = view.findViewById<TextView>(R.id.rate3)
        var rate4 = view.findViewById<TextView>(R.id.rate4)
        var rate5 = view.findViewById<TextView>(R.id.rate5)
        var avg = view.findViewById<TextView>(R.id.number_rating)
        var sum = view.findViewById<TextView>(R.id.sumVote)

        fireStore.collection("comment").document(idProductt.toString())
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val data = task.result!!
                        var sum1 = data.get("sum1") as Number
                        var sum2 = data.get("sum2") as Number
                        var sum3 = data.get("sum3") as Number
                        var sum4 = data.get("sum4") as Number
                        var sum5 = data.get("sum5") as Number
                        var avgRate =data.get("avgRating") as Number
                        var sumRate = data.get("sumRating") as Number
                        rate1.setText(sum1.toString())
                        rate2.setText(sum2.toString())
                        rate3.setText(sum3.toString())
                        rate4.setText(sum4.toString())
                        rate5.setText(sum5.toString())
                        sum.setText(sumRate.toString())
                        avg.setText(avgRate.toString())
                    }else{
                    }
                }.addOnFailureListener { exception ->
                }

        addCMT.setOnClickListener {
            reference= FirebaseDatabase.getInstance().reference
            val title = comment.text.toString()
            var ratingg = ratingBar.rating

            if (ratingg < 6 && ratingg > 4){
                val rate5 : MutableMap<String, Any> = HashMap()
                rate5["sum5"]=FieldValue.increment(1)
                rate5["sumRating"] = FieldValue.increment(1)
                fireStore.collection("comment").document(idProductt.toString())
                        .update(rate5)
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                        }
            }
            if (ratingg < 5 && ratingg > 3){
                val rate4 : MutableMap<String, Any> = HashMap()
                rate4["sum4"]=FieldValue.increment(1)
                rate4["sumRating"] = FieldValue.increment(1)
                fireStore.collection("comment").document(idProductt.toString())
                        .update(rate4)
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                        }
            }
            if (ratingg < 4 && ratingg >2){
                val rate3 : MutableMap<String, Any> = HashMap()
                rate3["sum3"]=FieldValue.increment(1)
                rate3["sumRating"] = FieldValue.increment(1)
                fireStore.collection("comment").document(idProductt.toString())
                        .update(rate3)
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                        }
            }
            if (ratingg < 3 && ratingg > 1){
                val rate2 : MutableMap<String, Any> = HashMap()
                rate2["sum2"]=FieldValue.increment(1)
                rate2["sumRating"] = FieldValue.increment(1)
                fireStore.collection("comment").document(idProductt.toString())
                        .update(rate2)
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                        }
            }
            if (ratingg < 2){
                val rate1 : MutableMap<String, Any> = HashMap()
                rate1["sum1"]=FieldValue.increment(1)
                rate1["sumRating"] = FieldValue.increment(1)
                fireStore.collection("comment").document(idProductt.toString())
                        .update(rate1)
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                        }
            }
            fireStore.collection("comment").document(idProductt.toString())
                    .get()
                    .addOnSuccessListener { task ->
                        if(task.exists()){
                            val data = task.data!!
                            var avgRating = data.get("avgRating") as Number
                            var avggRating = avgRating.toFloat()
                            var newAvg = (ratingg + avggRating)/2
                            fireStore.collection("comment").document(idProductt.toString())
                                    .update("avgRating",newAvg)
                                    .addOnSuccessListener {  }
                                    .addOnFailureListener {  }
                        }
                    }.addOnFailureListener {
                    }
            var raating = ratingg.toString()
            val key = reference.push().key
            val commentt : MutableMap<String, Any> = HashMap()
            commentt["id"]=key.toString()
            commentt["idUser"] = uid
            commentt["idProduct"] = idProductt.toString()
            commentt["date"] = currentDate
            commentt["title"] = title
            commentt["rate"]=raating
            fireStore.collection("comments").document(key.toString())
                    .set(commentt)
                    .addOnSuccessListener {
                    }.addOnFailureListener {
                    }

        }


        return view
    }


    override fun onStart() {
        super.onStart()
        commentAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        commentAdapter!!.stopListening()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DetailProductFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}



