package com.tiandao.store.operation.common.exception


// 增强版异常类
data class ResultException(
    val code: String,
    override val message: String,
    val origin: Throwable? = null,
    val extraData: Any? = null
) : Exception(message, origin)