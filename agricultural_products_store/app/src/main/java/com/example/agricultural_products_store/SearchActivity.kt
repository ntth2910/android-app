package com.example.agricultural_products_store

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.CategoryAdapter
import com.example.agricultural_products_store.Adapter.ProductAdapter
import com.example.agricultural_products_store.fragment.CategoryFragment
import com.example.agricultural_products_store.fragment.HomeFragment
import com.example.agricultural_products_store.fragment.ShowProductSearchFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import com.example.agricultural_products_store.Model.ModelProduct as ModelProduct

class SearchActivity : AppCompatActivity() {

    private var productAdapter : ProductAdapter?=null
    private var categoryAdapter : CategoryAdapter?=null
    lateinit var searchRecycler : RecyclerView
    lateinit var searchEdit : EditText
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReferenceProduct : CollectionReference =db.collection("products")
    private val RQ_SPEECH_REC = 102
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEdit = findViewById(R.id.search)
        searchRecycler= findViewById(R.id.ViewSearch)
        val queryProduct : Query = collectionReferenceProduct
        val firestoreRecyclerOptionsProduct: FirestoreRecyclerOptions<ModelProduct> = FirestoreRecyclerOptions.Builder<ModelProduct>()
            .setQuery(queryProduct, ModelProduct::class.java)
            .build()
        productAdapter = ProductAdapter(firestoreRecyclerOptionsProduct)
        //recyclerProduct.layoutManager=GridLayoutManager(activity,3)
        searchRecycler.layoutManager= GridLayoutManager(this,2)
        searchRecycler.adapter= productAdapter

        val voice = findViewById<ImageButton>(R.id.voice)
        voice.setOnClickListener {
            askSpeechInput()
        }
        val searchProduct = findViewById<ImageButton>(R.id.searchProduct)
        searchProduct.setOnClickListener {view ->
            val bundle = Bundle()
            bundle.putString("textSearch", searchEdit.text.toString())
            val activity =view.context as AppCompatActivity
            val showProductSearch  = ShowProductSearchFragment()
            showProductSearch.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.relative_submit,showProductSearch).addToBackStack(null).commit()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RQ_SPEECH_REC && resultCode== Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchEdit.setText(result?.get(0).toString())
            //val dataa = result?.get(0).toString()
            val bundle = Bundle()
            bundle.putString("textSearch",result?.get(0).toString() )
           // val activity =this as AppCompatActivity
            val showProductSearch  = ShowProductSearchFragment()
            showProductSearch.arguments = bundle
            this.supportFragmentManager.beginTransaction().replace(R.id.relative_submit,showProductSearch).addToBackStack(null).commit()

            //val text = findViewById<TextView>(R.id.text)
            //text.text= result?.get(0).toString()
        }
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this,"Speech recognition is not available", Toast.LENGTH_LONG).show()
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say")
            startActivityForResult(i,RQ_SPEECH_REC)
        }
    }

    override fun onStart() {
        super.onStart()
        productAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        productAdapter!!.stopListening()
    }
}