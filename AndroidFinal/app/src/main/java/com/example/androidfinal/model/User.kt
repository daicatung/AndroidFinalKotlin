package com.example.androidfinal.model

import java.io.Serializable

data class User(
    var nameUser: String,
    var imgUser: String,
    var dateUser: String,
    var emailUser: String,
    var sexUser: String
) : Serializable