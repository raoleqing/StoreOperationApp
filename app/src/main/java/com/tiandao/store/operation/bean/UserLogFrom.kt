package com.tiandao.store.operation.bean

import java.io.Serializable

data class UserLogFrom (
    val username: String,
    val password: String,
    val code: String,
): Serializable