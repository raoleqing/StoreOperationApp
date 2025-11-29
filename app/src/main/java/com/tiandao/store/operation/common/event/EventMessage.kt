package com.tiandao.store.operation.common.event

class EventMessage<T>(
    var code: String,
    private var data: T? = null
) {
    fun getData(): T? = data

    fun setData(data: T) {
        this.data = data
    }

    override fun toString(): String {
        return "EventMessage(code='$code', data=$data)"
    }
}