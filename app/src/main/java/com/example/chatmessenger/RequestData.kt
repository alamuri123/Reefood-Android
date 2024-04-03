package com.example.chatmessenger

data class RequestData(
    val id: String? = null,
    val trustname: String? = null,
    val trustdescription: String? = null,
    val address: String? = null,
    val foodquantity: String? = null,
    val date: String? = null,
    val code: String? = null,
    val status: String? = "Pending" // Ensure status is not nullable
)
