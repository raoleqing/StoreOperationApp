package com.tiandao.store.operation.bean

import java.io.Serializable

class UpdatePasswordFrom (
    val phone: String,
    val oldPassword: String,
    val newPassword: String
): Serializable