package com.example.agricultural_products_store.Model

import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ModelCart {
    val idProduct : String ?=null
    val name : String ?= null
    val image : String ?= null
    val price : Float?=null
    val quantity : Int?=null
    val totalPrice : Float?=null
    val idUser : String ?=null

    constructor()
}