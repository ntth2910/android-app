package com.example.agricultural_products_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Handler as Handler

class SplashActivity : AppCompatActivity() {
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val splash_time : Long = 1000
    lateinit var handler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler.postDelayed({
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
//            if (currentUser != null){
//                fireStore = FirebaseFirestore.getInstance()
//                auth = FirebaseAuth.getInstance()
//                val currentUser = auth.currentUser
//                var uid = currentUser?.uid
//                fireStore.collection("users").document(uid.toString())
//                        .get()
//                        .addOnSuccessListener {
//                            task ->
//                            if(task.exists()){
//                                val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
//                                progressBar.setVisibility(View.INVISIBLE)
//                                val intent = Intent(this,MainActivity::class.java)
//                                startActivity(intent)
//                                finish()
//                            }else{
//                                auth = FirebaseAuth.getInstance()
//                                auth.signOut()
//                            }
//                        }
//            }
//            else{
//                startActivity(Intent(this,LoginActivity::class.java))
//                finish()
//            }
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },
        splash_time)


    }
}