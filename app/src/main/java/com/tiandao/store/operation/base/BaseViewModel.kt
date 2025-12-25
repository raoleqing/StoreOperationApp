package com.tiandao.store.operation.base

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiandao.store.operation.common.exception.ResultException
import com.tiandao.store.operation.utils.ActivityCollectorUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import kotlin.code

abstract class BaseViewModel : ViewModel() {

    fun <T> http(
        api: suspend CoroutineScope.() -> ResultBean<T>,
        complete: () -> Unit = {},
        success: (T?) -> Unit = {},
        error: (Exception) -> Unit = {},
    ) {
        viewModelScope.launch {
            try {
                val bean = withContext(Dispatchers.IO) { api() }
                handleResponse(bean, success, error)
            } catch (e: Exception) {
                handleException(e, error)
            } finally {
                complete()
            }
        }
    }

    private fun <T> handleResponse(
        bean: ResultBean<T>,
        success: (T?) -> Unit,
        error: (Exception) -> Unit
    ) {
        when (bean.code) {
            "200" -> success(bean.data)
            "401" -> handleUnauthorized(bean)
            else -> error(ResultException(bean.code?:"-1", bean.msg ?: "后端错误"))
        }
    }

    private fun handleException(e: Exception, error: (Exception) -> Unit) {
        val exception = when (e) {
            is ConnectException -> ResultException("-1", "网络连接异常")
            is UnknownServiceException -> ResultException("-1", "服务器服务异常")
            is SocketTimeoutException -> ResultException("-1", "请求超时")
            is IllegalStateException -> ResultException("-1", "无权限，请联系管理员")
            is ResultException -> e
            else -> ResultException("-1", e.message ?: "未知错误")
        }
        error(exception)
    }

    private fun handleUnauthorized(bean: ResultBean<*>) {
        val intent = Intent("com.tiandao.store.operation.AuthBroadcastReceiver").apply {
            putExtra("code", bean.code)
            putExtra("msg", bean.msg)
            // 关键：显式设置此广播发送给本应用
            `package` = ActivityCollectorUtil.getTopActivity()?.packageName
            // 或者，如果知道具体的Receiver类，可以用ComponentName更精确
            // component = ComponentName(packageName, "com.tiandao.merchant.base.AuthBroadcastReceiver")
        }
        ActivityCollectorUtil.getTopActivity()?.sendBroadcast(intent)
    }




    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }

    protected fun <T> Flow<T>.collectFlow(collect: suspend (T) -> Unit) {
        launchOnUI {
            this@collectFlow.collect {
                collect(it)
            }
        }
    }

    protected open fun handleException(throwable: Throwable) {
        // 统一处理异常
        throwable.printStackTrace()
    }

}