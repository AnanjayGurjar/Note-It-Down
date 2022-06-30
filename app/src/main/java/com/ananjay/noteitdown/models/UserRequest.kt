package com.ananjay.noteitdown.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String? = null
)