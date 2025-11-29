package com.tiandao.store.operation.bean

import java.io.Serializable

data class User(
    val loginUser: LoginUser,
    val token: String,
): Serializable