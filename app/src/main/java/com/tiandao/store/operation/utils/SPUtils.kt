package com.tiandao.store.operation.utils

import android.content.Context
import android.content.SharedPreferences


object SPUtils {
    private var sp: SharedPreferences? = null

    const val ACCOUNT : String = "account"
    const val SYNC_VALUE_TYPE_TIME : String = "sync_value_type_time"
    const val PROJECT_NAME : String = "project_name"
    const val MEMBER_SEARCH_RECORD : String = "member_search_record"

    fun init(context: Context) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        }
    }

    fun putString(key: String?, value: String?) {
        sp!!.edit().putString(key, value).apply()
    }

    fun getString(key: String?, defValue: String?): String? {
        return sp!!.getString(key, defValue)
    }

    fun putInt(key: String?, value: Int) {
        sp!!.edit().putInt(key, value).apply()
    }

    fun getInt(key: String?, defValue: Int): Int {
        return sp!!.getInt(key, defValue)
    }

    fun putLong(key: String?, value: Long) {
        sp!!.edit().putLong(key, value).apply()
    }

    fun getLong(key: String?, defValue: Long): Long {
        return sp!!.getLong(key, defValue)
    }

    fun putBoolean(key: String?, value: Boolean) {
        sp!!.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sp!!.getBoolean(key, defValue)
    }

    fun remove(key: String?) {
        sp!!.edit().remove(key).apply()
    }

    fun clear() {
        sp!!.edit().clear().apply()
    }

}