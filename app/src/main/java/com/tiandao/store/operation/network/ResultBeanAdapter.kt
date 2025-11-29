package com.tiandao.store.operation.network

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.tiandao.store.operation.base.ResultBean
import java.lang.reflect.Type

class ResultBeanAdapter<T>(private val gson: Gson, private val dataType: Type) : TypeAdapter<ResultBean<T>>() {

    override fun write(out: JsonWriter, value: ResultBean<T>?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.beginObject()
            .name("code").value(value.code)
            .name("msg").value(value.msg)
            .name("data")

        // 处理 data 的序列化
        if (value.data == null || value.data == "") {
            out.nullValue()
        } else {
            gson.toJson(gson.toJsonTree(value.data), out)
        }
        out.endObject()
    }

    override fun read(reader: JsonReader): ResultBean<T>? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }

        var code: String? = null
        var msg: String? = null
        var data: T? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "code" -> code = reader.nextString()
                "msg" -> msg = reader.nextString()
                "data" -> {
                    data = when (reader.peek()) {
                        JsonToken.NULL -> {
                            reader.nextNull()
                            null
                        }
                        JsonToken.STRING -> {
                            val str = reader.nextString()
                            if (dataType == String::class.java) {
                                str as T
                            } else {
                                try {
                                    gson.fromJson(str, dataType)
                                } catch (e: JsonSyntaxException) {
                                    null // 或抛出自定义异常
                                }
                            }
                        }
                        else -> {
                            val jsonElement = Streams.parse(reader)
                            gson.fromJson(jsonElement, dataType)
                        }
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return ResultBean(code, msg, data)
    }
}