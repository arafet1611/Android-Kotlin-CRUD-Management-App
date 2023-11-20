package com.example.tp5

import java.io.Serializable

data class MyDataModel(
    var id: Long = 0,
    var name: String = "",
    val telephone: String = "",
    var email: String = "",
    var rue: String = "",
    var ville: String = "",
    var codePostale: String = ""
) : Serializable