package com.example.agricultural_products_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val login_email = findViewById<EditText>(R.id.edit_text1)
        val login_pass = findViewById<EditText>(R.id.edit_text2)
        val button_login = findViewById<Button>(R.id.button_login)
        val register = findViewById<TextView>(R.id.register)

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
//                                    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
//                                    progressBar.setVisibility(View.INVISIBLE)
//                                    val intent = Intent(this,MainActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }else{
//                                    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
//                                    progressBar.setVisibility(View.INVISIBLE)
//                                    auth.signOut()
//                                    Toast.makeText(baseContext, "Đăng nhập thất bại.",
//                                            Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }

        button_login.setOnClickListener {
            if (login_email.text.toString().isNotEmpty() &&  login_pass.text.toString().isNotEmpty()){
                loginUser(login_email.text.toString(),  login_pass.text.toString())
                progressBar.setVisibility(View.VISIBLE)
//                handler  = Handler()
//                handler.postDelayed({
//
//                },3000)

            }else{
                Toast.makeText(this,"Nhập đầy đủ các ô",Toast.LENGTH_LONG).show()
            }
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loginUser(email : String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fireStore = FirebaseFirestore.getInstance()
                    auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    var uid = currentUser?.uid
                    fireStore.collection("users").document(uid.toString())
                            .get()
                            .addOnSuccessListener {
                                task ->
                                if(task.exists()){
                                    val data = task.data!!
                                    var role = data.get("role") as Number
                                    var rolee = role.toInt()
                                    if(rolee == 1) {
                                        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
                                        progressBar.setVisibility(View.INVISIBLE)
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }else{
                                    auth = FirebaseAuth.getInstance()
                                    auth.signOut()

                                }
                            }
//                    handler  = Handler()
//                    handler.postDelayed({
//                        startActivity(Intent(this,MainActivity::class.java))
//                        finish()
//                    },3000)
                }
                else{
                    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
                    progressBar.setVisibility(View.INVISIBLE)
                    Toast.makeText(baseContext, "Đăng nhập thất bại.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}