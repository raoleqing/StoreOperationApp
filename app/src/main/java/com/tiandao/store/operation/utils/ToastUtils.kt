package com.tiandao.store.operation.utils

import android.widget.Toast
import com.tiandao.store.operation.base.StoreOperationApplication

object ToastUtils {
    private var toast: Toast? = null

    fun showShort(message: String) {
        toast?.cancel()
        toast = Toast.makeText(StoreOperationApplication.application, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun showLong(message: String) {
        toast?.cancel()
        toast = Toast.makeText(StoreOperationApplication.application, message, Toast.LENGTH_LONG)
        toast?.show()
    }

}