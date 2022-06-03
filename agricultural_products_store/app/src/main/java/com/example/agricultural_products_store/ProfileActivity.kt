package com.example.agricultural_products_store

import android.app.ProgressDialog
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.agricultural_products_store.profile.ProfileUserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_profile)

        fireStore = FirebaseFirestore.getInstance()
        val user_namee = findViewById<TextView>(R.id.user_name)
        val email_user = findViewById<TextView>(R.id.email)
        val image_user = findViewById<ImageView>(R.id.image_profile)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null){
            var email = currentUser.email
            var uid =currentUser.uid
            fireStore.collection("users").document(uid)
                .get()
                    .addOnSuccessListener { task ->
                        if (task.exists()){
                            val data =task.data!!
                            var username = data.get("username") as String
                            var image = data.get("image") as String
                            if( username.isNotEmpty() && image.isNotEmpty()) {
                            user_namee.setText(username)
                            Picasso.get()
                                    .load(image)
                                    .placeholder(R.drawable.load)
                                    .error(R.drawable.load)
                                    .into(image_user)
                            }
                        }else{
                            Toast.makeText(this,"Người dùng chưa hoàn thành thông tin cá nhân",Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener { exception ->
                    }
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//                        val data = task.result!!
//                        var username = data.get("username") as String
//                        var image = data.get("image") as String
////                        if( username != null && image != null) {
//                            user_namee?.setText(username)
//                            Picasso.get()
//                                    .load(image)
//                                    .placeholder(R.drawable.load)
//                                    ?.into(image_user)
////                        }
////                        else{
//
////                        }
//                    }else{
////                        var pd = ProgressDialog(this)
////                        pd.setTitle("")
////                        pd.show()
//                        val username : String = ""
//                        val image : String = ""
//                        user_namee.setText(username)
//                        Picasso.get()
//                                .load(image)
//                                .placeholder(R.drawable.load)
//                                .into(image_user)
//                    }
//                }
           // user_name.setText(uid)
            email_user.setText(email)

        }else{

        }
        val user_profile = findViewById<TextView>(R.id.profile)

        user_profile.setOnClickListener {
            startActivity(Intent(this, ProfileUserActivity::class.java))
        }
    }
}