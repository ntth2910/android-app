package com.example.agricultural_products_store.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.agricultural_products_store.LoginActivity
import com.example.agricultural_products_store.PaymentActivity
import com.example.agricultural_products_store.R
import com.example.agricultural_products_store.profile.ProfileUserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_user, container, false)

        fireStore = FirebaseFirestore.getInstance()
        val user_namee = view.findViewById<TextView>(R.id.user_name)
        val email_user = view.findViewById<TextView>(R.id.email)
        val image_user = view.findViewById<ImageView>(R.id.image_profile)
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
                            val show = Toast.makeText(activity,"Người dùng chưa hoàn thành thông tin cá nhân", Toast.LENGTH_LONG)
                        }
                    }.addOnFailureListener { exception ->
                    }
            email_user.setText(email)

        }else{
        }
        val user_profile = view.findViewById<TextView>(R.id.profile)

        user_profile.setOnClickListener {
            activity?.startActivity(Intent(activity, ProfileUserActivity::class.java))
//            or
//            activity?.let {
//                it.startActivity(Intent(it,ProfileUserActivity::class.java))
//                 activity?.finish()
//            }
        }
        val order = view.findViewById<TextView>(R.id.donhang)
        order.setOnClickListener {
            activity?.startActivity(Intent(activity, PaymentActivity::class.java))
        }
        val log_out = view.findViewById<Button>(R.id.logout)
        log_out.setOnClickListener {
            auth.signOut()
            activity?.let {
                it.startActivity(Intent(it,LoginActivity::class.java))
                activity?.finish()
            }
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                UserFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}