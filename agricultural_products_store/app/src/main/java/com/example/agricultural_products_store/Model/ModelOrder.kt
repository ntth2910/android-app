package com.example.agricultural_products_store.Model

class ModelOrder {
    val id: String ?= null
    val idUser: String ?= null
    val username : String ?= null
    val phone : String ?=null
    val local : String ?=null
    val date : String ?=null
    val quantity : Int?=null
    val status : Int?=null
    val totalPrice : Float?=null

    constructor()
}