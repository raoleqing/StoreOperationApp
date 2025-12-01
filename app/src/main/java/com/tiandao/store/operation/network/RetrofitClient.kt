package com.tiandao.store.operation.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.tiandao.store.operation.base.ResultBean
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val TERMS_URL = "http://120.78.175.159:8090/agreement"
    const val PRIVACY_URL = "http://120.78.175.159:8090/privacyPolicy"
    private const val BASE_URL = "https://store-operation.tiandaowangluo.com/"
   //private const val BASE_URL = "http://10.15.18.93:8091/"
    const val IMG_URL = "http://120.78.175.159:8090"
    private const val TIMEOUT = 30L

    // 自定义日志拦截器（仅Debug模式打印日志）
    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // OkHttpClient 配置
    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // 自动重试
            addInterceptor(OkHttpInterceptor()) // 认证拦截器
            addInterceptor(loggingInterceptor) // 日志拦截器
            // 添加缓存（可选）
            //cache(Cache(File("缓存目录"), 10 * 1024 * 1024L)) // 10MB缓存
        }.build()
    }

    private val gson = GsonBuilder()
        .registerTypeAdapterFactory(object : TypeAdapterFactory {
            override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
                // 只处理 ResultBean 类型
                if (type.rawType != ResultBean::class.java) {
                    return null
                }

                // 提取泛型参数 T 的实际类型（如 User、List<String> 等）
                val parameterizedType = type.type as? ParameterizedType
                    ?: return null

                val dataType = parameterizedType.actualTypeArguments[0]
                val adapter = ResultBeanAdapter<T>(gson, dataType)
                @Suppress("UNCHECKED_CAST")
                return adapter as TypeAdapter<T>
            }
        })
        .create()


    // Retrofit 实例
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // 创建API服务
    inline fun <reified T> createService(): T = retrofit.create(T::class.java)
}