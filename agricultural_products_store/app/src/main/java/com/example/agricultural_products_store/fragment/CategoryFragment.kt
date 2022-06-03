package com.example.agricultural_products_store.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.CategoryAdapter
import com.example.agricultural_products_store.Adapter.ProductAdapter
import com.example.agricultural_products_store.Model.ModelCategory
import com.example.agricultural_products_store.Model.ModelProduct
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var categoryAdapter : CategoryAdapter?=null
    var productAdapter : ProductAdapter?=null
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var reference : DatabaseReference
    private val collectionReference : CollectionReference = db.collection("category")
    private val product : CollectionReference = db.collection("products")
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
       val view = inflater.inflate(R.layout.fragment_category, container, false)

        var recyclerViewCategory = view.findViewById<RecyclerView>(R.id.recyclerView_Category)
        val queryCategory : Query = collectionReference
        val firestoreRecyclerOptionsCategory: FirestoreRecyclerOptions<ModelCategory> = FirestoreRecyclerOptions.Builder<ModelCategory>()
                .setQuery(queryCategory, ModelCategory::class.java)
                .build()
        categoryAdapter = CategoryAdapter(firestoreRecyclerOptionsCategory)
        recyclerViewCategory.layoutManager= LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
        recyclerViewCategory.adapter= categoryAdapter

        var idcategory = arguments?.getString("idCategory")
//        var idcategoryy = arguments?.getString("idCategoryyy")
        if (idcategory!=null){
            val bundle = Bundle()
            bundle.putString("idCategoryy", idcategory)
            val activity =view.context as AppCompatActivity
            val showProductFragment  = ShowProductFragment()
            showProductFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.layoutShowProduct,showProductFragment).addToBackStack(null).commit()
        }
//        if (idcategoryy!=null){
//            val bundle = Bundle()
//            bundle.putString("idCategoryyy", idcategoryy)
//            val activity =view.context as AppCompatActivity
//            val showProductFragment  = ShowProductFragment()
//            showProductFragment.arguments = bundle
//            activity.supportFragmentManager.beginTransaction().replace(R.id.layoutShowProduct,showProductFragment).addToBackStack(null).commit()
//        }
            val recyclerViewProduct = view.findViewById<RecyclerView>(R.id.recyclerView_Product)
            val queryProduct : Query = product
            val firestoreRecyclerOptionsProduct: FirestoreRecyclerOptions<ModelProduct> = FirestoreRecyclerOptions.Builder<ModelProduct>()
                    .setQuery(queryProduct, ModelProduct::class.java)
                    .build()
            productAdapter = ProductAdapter(firestoreRecyclerOptionsProduct)
            recyclerViewProduct.layoutManager= LinearLayoutManager(activity, LinearLayout.HORIZONTAL, false)
            recyclerViewProduct.adapter= productAdapter


        return  view
    }
    override fun onStart() {
        super.onStart()
        categoryAdapter!!.startListening()
        productAdapter!!.startListening()

    }

    override fun onDestroy() {
        super.onDestroy()
        categoryAdapter!!.stopListening()
        productAdapter!!.stopListening()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}