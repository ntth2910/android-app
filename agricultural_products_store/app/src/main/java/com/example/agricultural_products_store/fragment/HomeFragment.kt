package com.example.agricultural_products_store.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.agricultural_products_store.Adapter.CartAdapter
import com.example.agricultural_products_store.Adapter.CategoryAdapter
import com.example.agricultural_products_store.Adapter.ProductAdapter
import com.example.agricultural_products_store.Model.ModelCategory
import com.example.agricultural_products_store.Model.ModelProduct
import com.example.agricultural_products_store.OrderActivity
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.SearchActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var categoryAdapter : CategoryAdapter?=null
    var productAdapter : ProductAdapter?=null
    private val db:FirebaseFirestore= FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("category")
    private val collectionReferenceProduct : CollectionReference=db.collection("products")

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
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)

        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel("https://cdn.tgdd.vn/Files/2017/11/14/1041538/ban-da-biet-cach-phan-biet-trai-thom-trai-khom-va-trai-dua-202104302123366561.jpg", "Trái dứa là sản phẩm bán chạy và được ưu tiên bán hàng nhất trên ứng dụng."))
        imageList.add(SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRABoUlm2CMza9nXWml7KaJzMc_1xzHR8zygdpjSJY756GESmk7kFccfx22IyQSTWh5-I4&usqp=CAU", "Vì sức khoẻ gia đình, hãy chọn mua nông sản sạch."))
        imageList.add(SlideModel("https://lh3.googleusercontent.com/proxy/OPUjkZIv0vYuxebOL-vjrCIYvhIS6pC6WfK-4jG-bjjdQfiLo3DjL2LuhdmD5Gq4RrfGdAcmVYfNYhJrNQLtD24DIN01vjY", "Trái dứa là sản phẩm bán chạy và được ưu tiên bán hàng nhất trên ứng dụng."))
        imageList.add(SlideModel("https://lh3.googleusercontent.com/proxy/NqzWvt-U2XateenFaGUuNcIf33SKtAg7qQGmM9FOfMhjrmn8p-nYwYhd6vqGBKsQhNN6aDTpPl3ydpV7oo8FlsAIFwkKwK7AlBZvOnthG75ZD8hVGHgjo6w", "Muốn đảm tìm thực phẩm sạch cho gia đình liên hệ chúng tôi."))

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        // RecyclerView danh muc
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCategory)
        val query : Query = collectionReference
        val firestoreRecyclerOptionsCategory:FirestoreRecyclerOptions<ModelCategory> = FirestoreRecyclerOptions.Builder<ModelCategory>()
            .setQuery(query,ModelCategory::class.java)
            .build()
        categoryAdapter = CategoryAdapter(firestoreRecyclerOptionsCategory)
        recyclerView.layoutManager=LinearLayoutManager(activity,LinearLayout.HORIZONTAL,false)
        recyclerView.adapter= categoryAdapter

        // RecyclerView product
        val recyclerProduct = view.findViewById<RecyclerView>(R.id.recyclerViewProduct)
        val queryProduct : Query = collectionReferenceProduct
        val firestoreRecyclerOptionsProduct:FirestoreRecyclerOptions<ModelProduct> = FirestoreRecyclerOptions.Builder<ModelProduct>()
                .setQuery(queryProduct,ModelProduct::class.java)
                .build()
        productAdapter = ProductAdapter(firestoreRecyclerOptionsProduct)
        //recyclerProduct.layoutManager=GridLayoutManager(activity,3)
        recyclerProduct.layoutManager=LinearLayoutManager(activity,LinearLayout.HORIZONTAL,false)
        recyclerProduct.adapter= productAdapter

        val search = view.findViewById<EditText>(R.id.search)
        search.setOnClickListener {
            activity?.startActivity(Intent(activity, SearchActivity::class.java))
        }

        return view
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

