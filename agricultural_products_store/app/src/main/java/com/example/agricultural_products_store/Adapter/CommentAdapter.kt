package com.example.agricultural_products_store.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agricultural_products_store.DetaillReplyCommentActivity
import com.example.agricultural_products_store.Model.ModelComment
import com.example.agricultural_products_store.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class CommentAdapter (options: FirestoreRecyclerOptions<ModelComment>): FirestoreRecyclerAdapter<ModelComment, CommentAdapter.ViewHolder>(
        options
) {
    private val view: View?=null
    private lateinit var fireStore : FirebaseFirestore
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var username = itemView.findViewById<TextView>(R.id.user_nameComment)
        var rate = itemView.findViewById<TextView>(R.id.rateCMT)
        var date=itemView.findViewById<TextView>(R.id.dateCMT)
        var content=itemView.findViewById<TextView>(R.id.contentComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_comment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ModelComment) {
        var idUserr =model.idUser
        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("users").document(idUserr.toString())
                .get()
                .addOnSuccessListener { task ->
                    if(task.exists()){
                        val data = task.data!!
                        var usernameStore = data.get("username") as String
                        holder.username.text=usernameStore
                    }else {
                    }
                }.addOnFailureListener { exception ->
                }
        holder.rate.text=model.rate
        holder.date.text=model.date
        holder.content.text=model.title
        val idComment = model.id
        val content = model.title
        holder.itemView.setOnClickListener { view ->
            val activity =view.context as AppCompatActivity
            val intent = Intent(activity, DetaillReplyCommentActivity::class.java)
            intent.putExtra("idComment", idComment.toString())
            intent.putExtra("title", content.toString())
            activity.startActivity(intent)
        }
    }


}