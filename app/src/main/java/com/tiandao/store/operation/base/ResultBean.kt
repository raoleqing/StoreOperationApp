package com.tiandao.store.operation.base

data class ResultBean<T>(
    var code:String?,
    var msg:String?,
    val data: T?
)