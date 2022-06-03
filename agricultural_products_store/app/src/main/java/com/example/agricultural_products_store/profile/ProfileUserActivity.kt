package com.example.agricultural_products_store.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.agricultural_products_store.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.IOException


class ProfileUserActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1234
    //lateinit var handler : Handler
    private lateinit var auth: FirebaseAuth
    private lateinit var filepath : Uri
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    //private lateinit var reference : StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        fireStore = FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        getSupportActionBar()?.hide()

        val currentUser = auth.currentUser
        if ( currentUser!=null){
            val uid = currentUser.uid
            val email = currentUser.email
            val edit_Email = findViewById<EditText>(R.id.edit_email)
            val edit_Phone = findViewById<EditText>(R.id.edit_phone)
            val edit_Sex = findViewById<EditText>(R.id.edit_sex)
            val edit_Name = findViewById<EditText>(R.id.edit_name)
            val edit_Local = findViewById<EditText>(R.id.edit_local)
            fireStore.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener{ task ->
                            //var list = ArrayList<ModelUser>()
                            if(task.exists()) {
                                //for(data in task.result!!){
                                val data = task.data!!
                                //var email = data.get("email") as String
                                var image = data.get("image") as String
                                var local = data.get("local") as String
                                var phone = data.get("phone") as String
                                var sex = data.get("sex") as String
                                var username = data.get("username") as String
                                //list.add(ModelUser(data.get("email") as String,data.get("image") as String, data.get("local") as String,data.get("phone") as String,data.get("sex") as String,data.get("username") as String, ))
                                edit_Email.setText(email)
                                edit_Phone.setText(phone)
                                edit_Local.setText(local)
                                edit_Sex.setText(sex)
                                edit_Name.setText(username)
                                val image_user = findViewById<ImageView>(R.id.image_user)
                                if (image.isEmpty()) {
                                    image_user.setImageResource(R.drawable.avatar)
                                } else{
                                    Picasso.get()
                                            .load(image)
                                            .placeholder(R.drawable.load)
                                            .into(image_user)
                                    }
                            }else{
                                Toast.makeText(this,"Vui lòng điền thông tin", Toast.LENGTH_LONG).show()
                                edit_Email.setText(email)
                            }

                    }


//            val edit_email = findViewById<EditText>(R.id.edit_email)
//            edit_email.setText(email)
        }
        val imageUser = findViewById<ImageView>(R.id.image_user)
        imageUser.setOnClickListener {
            startFileChooser()
        }
        val submit = findViewById<Button>(R.id.save_profile)
        submit.setOnClickListener {
            uploadImage()
        }
    }
    private fun uploadImage() {
        if(filepath == null ) {
        
        }
        else
        {
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()
            var reference = FirebaseStorage.getInstance().reference.child("image/")
            var file_name = reference.child("i"+filepath.lastPathSegment)
            file_name.putFile(filepath).addOnSuccessListener { task ->
                var result = task.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    var imageLink = it.toString()
                    //                    val edit = findViewById<EditText>(R.id.edit_email)
                    //                     edit.setText(imageLink)
                    fireStore = FirebaseFirestore.getInstance()
                    val currentUser = auth.currentUser
                    val edit_Email = findViewById<EditText>(R.id.edit_email)
                    val edit_Phone = findViewById<EditText>(R.id.edit_phone)
                    val edit_Sex = findViewById<EditText>(R.id.edit_sex)
                    val edit_Name = findViewById<EditText>(R.id.edit_name)
                    val edit_Local = findViewById<EditText>(R.id.edit_local)
                    var uid = currentUser.uid
                    // var email = currentUser.email
                    val edit_name = edit_Name.text.toString()
                    val edit_email = edit_Email.text.toString()
                    val edit_phone = edit_Phone.text.toString()
                    val edit_sex = edit_Sex.text.toString()
                    val edit_local = edit_Local.text.toString()
                    val user : MutableMap<String, Any> = HashMap()
                    user["Uid"] = uid
                    user["email"] = edit_email
                    user["image"] = imageLink
                    user["local"] = edit_local
                    user["phone"] = edit_phone
                    user["sex"] = edit_sex
                    user["username"] =  edit_name
                    fireStore.collection("users").document(uid)
                            .update(user)
                            .addOnSuccessListener {
                                fireStore.collection("users").document(uid)
                                        .get()
                                        .addOnSuccessListener{ task ->
                                            //var list = ArrayList<ModelUser>()
                                            if(task.exists()){
                                                //for(data in task.result!!){
                                                val data = task.data!!
                                                val currentUser = auth.currentUser
                                                var email = currentUser.email
                                                //var email = data.get("email") as String
                                                var image = data.get("image") as String
                                                var local = data.get("local") as String
                                                var phone = data.get("phone") as String
                                                var sex = data.get("sex") as String
                                                var username = data.get("username") as String
                                                //list.add(ModelUser(data.get("email") as String,data.get("image") as String, data.get("local") as String,data.get("phone") as String,data.get("sex") as String,data.get("username") as String, ))
                                                edit_Email.setText(email)
                                                edit_Phone.setText(phone)
                                                edit_Local.setText(local)
                                                edit_Sex.setText(sex)
                                                edit_Name.setText(username)
                                                val image_user = findViewById<ImageView>(R.id.image_user)
                                                Picasso.get()
                                                        .load(image)
                                                        .placeholder (R.drawable.load)
                                                        .into(image_user)
                                            }else{
                                                val currentUser = auth.currentUser
                                                var email = currentUser.email
                                                Toast.makeText(this,"Vui lòng điền thông tin", Toast.LENGTH_LONG).show()
                                                edit_Email.setText(email)
                                            }

                                        }
                            }.addOnFailureListener {

                            }
                    pd.dismiss()
                }
                        .addOnFailureListener {
                    Toast.makeText(this,"Người dùng chưa hoàn thành thông tin cá nhân",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startFileChooser() {
        var i = Intent()
//        i.setType("image/*")
//        i.setAction(Intent.ACTION_GET_CONTENT)
        i.type="image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            filepath=data.data!!
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
                val imageUser = findViewById<ImageView>(R.id.image_user)
                imageUser.setImageBitmap(bitmap)
                //imageUser.setImageURI(filepath)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
//        else{
//            Toast.makeText(this, "Choose Picture", Toast.LENGTH_LONG).show()
//            startActivity(Intent(this, ProfileUserActivity::class.java))
//        }
    }
}