package com.frontlinehelper.app.data

data class User(
    val displayName: String,
    val frontlineAccountId: String,
    val credentialsAllowed: Boolean = false
)
