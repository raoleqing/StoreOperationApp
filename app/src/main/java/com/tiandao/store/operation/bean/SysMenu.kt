package com.tiandao.store.operation.bean

import java.io.Serializable

class SysMenu (
    val id: Long,
    val menuId : Long,
    val menuName : String,
    val code: String,
    val icon: Int
): Serializable