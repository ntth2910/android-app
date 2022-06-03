package com.example.agricultural_products_store

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.Adapter.ReplyCMTAdapter
import com.example.agricultural_products_store.Model.ModelReplyCMT
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DetaillReplyCommentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference : CollectionReference = db.collection("ReplyComment")
    var replyCMTAdapter : ReplyCMTAdapter?=null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaill_reply_comment)
        getSupportActionBar()?.hide()
        var intent= intent
        val viewComment = findViewById<TextView>(R.id.viewComment)
        if(intent!=null) {
            val idComment = intent.getStringExtra("idComment")
            val title = intent.getStringExtra("title")
            viewComment.setText(title.toString())
        }
        val idComment = intent.getStringExtra("idComment")
        val edit_message = findViewById<EditText>(R.id.edit_message)
        val send = findViewById<Button>(R.id.send)
        val sdf = SimpleDateFormat("yyyy.MM.dd hh:mm")
        val sd = SimpleDateFormat("yyyy.MM.dd")
        val currrenTime = sd.format(Date())
        val currentDate = sdf.format(Date())

        send.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            var uid = currentUser?.uid
            fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("users").document(uid.toString())
                .get()
                .addOnSuccessListener { task ->
                    if (task.exists()){
                        var data = task.data!!
                        var name = data.get("username") as String
                        val idComment = intent.getStringExtra("idComment")
                        val replyComment: MutableMap<String, Any> = HashMap()
                        replyComment["title"] = edit_message.text.toString()
                        replyComment["name"] = name
                        replyComment["idComment"] = idComment.toString()
                        replyComment["date"] = currentDate
                        replyComment["idUser"] = uid.toString()
                        fireStore = FirebaseFirestore.getInstance()
                        fireStore.collection("ReplyComment").document()
                            .set(replyComment)
                            .addOnSuccessListener {
//                                finish()
//                                overridePendingTransition(0, 0)
//                                startActivity(intent)
//                                overridePendingTransition(0, 0)
                            }
                    }
                }
        }
        val recyclerReply = findViewById<RecyclerView>(R.id.recyclerViewReply)
        val queryComment : Query = collectionReference.whereEqualTo("idComment",idComment)
        val firestoreRecyclerOptionsReplyComment: FirestoreRecyclerOptions<ModelReplyCMT> = FirestoreRecyclerOptions.Builder<ModelReplyCMT>()
            .setQuery(queryComment, ModelReplyCMT::class.java)
            .build()
        replyCMTAdapter = ReplyCMTAdapter(firestoreRecyclerOptionsReplyComment)
        recyclerReply.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerReply.adapter= replyCMTAdapter
    }
    override fun onStart() {
        super.onStart()
        replyCMTAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        replyCMTAdapter!!.stopListening()
    }
}