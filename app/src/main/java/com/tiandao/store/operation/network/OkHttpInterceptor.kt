package com.tiandao.store.operation.network

import com.tiandao.store.operation.base.StoreOperationApplication
import com.tiandao.store.operation.utils.UserUtils
import okhttp3.Interceptor
import okhttp3.Response

class OkHttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(UserUtils.isLoggedIn(StoreOperationApplication.application)){
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${getToken()}")
                .addHeader("source", "android")
                .build()
            return chain.proceed(request)
        }else{
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("source", "android")
                .build()
            return chain.proceed(request)
        }
    }

    private fun getToken(): String {
        // 从SharedPreferences或其他地方获取token
        return UserUtils.getToken(StoreOperationApplication.application)
    }
}