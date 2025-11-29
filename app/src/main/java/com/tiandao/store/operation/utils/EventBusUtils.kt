package com.tiandao.store.operation.utils

import org.greenrobot.eventbus.EventBus
import kotlin.also
import kotlin.let
import kotlin.takeIf
import kotlin.takeUnless

object EventBusUtils {

    private val eventBus: EventBus
        get() = EventBus.getDefault()

    /**
     * 注册 EventBus
     * @param subscriber 订阅者（自动跳过已注册对象）
     */
    fun register(subscriber: Any?) {
        subscriber?.takeUnless { eventBus.isRegistered(it) }?.also {
            eventBus.register(it)
        }
    }

    /**
     * 解除注册 EventBus
     * @param subscriber 订阅者（自动跳过未注册对象）
     */
    fun unregister(subscriber: Any?) {
        subscriber?.takeIf { eventBus.isRegistered(it) }?.also {
            eventBus.unregister(it)
        }
    }

    /**
     * 发送普通事件
     * @param event 事件消息（可为 null，但 EventBus 本身可能抛出异常）
     */
    fun post(event: Any?) {
        event?.let { eventBus.post(it) }
    }

    /**
     * 发送粘性事件
     * @param event 事件消息（可为 null，但 EventBus 本身可能抛出异常）
     */
    fun postSticky(event: Any?) {
        event?.let { eventBus.postSticky(it) }
    }
}