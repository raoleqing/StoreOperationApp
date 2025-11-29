package com.tiandao.store.operation.utils


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.FileProvider
import com.tiandao.store.operation.base.StoreOperationApplication
import java.io.File
import kotlin.apply
import kotlin.let

object DisplayUtil {

    fun getAppName(): String {
        val context= StoreOperationApplication.application
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        val labelRes = packageInfo.applicationInfo?.labelRes
        labelRes?.let {
            return context.getString(it)
        }
        return ""
    }

    fun getVersionName(): String? {
        val context= StoreOperationApplication.application
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName

    }

    fun getVersionCode(): Long {
        return try {
            val context = StoreOperationApplication.application
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                0L
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // 理论上不会发生，因为查询的是自己的包名
            0L // 返回默认值
        }
    }

    fun getScreenWidth(): Int {
        val context=StoreOperationApplication.application
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        val context=StoreOperationApplication.application
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.heightPixels
    }

    fun dp2px(dp: Int): Int {
        val context=StoreOperationApplication.application
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }


    /**
     * 安装 APK 文件
     * @param context Context
     * @param apkUri 下载完成的 APK 文件 Uri
     */
    fun installApk(context: Context, apkUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 临时授权读取权限

            // Android 7.0+ 需要使用 FileProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val apkFile = File(apkUri.path ?: return)
                val providerUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider", // 与 manifest 中定义的 authority 一致
                    apkFile
                )
                setDataAndType(providerUri, "application/vnd.android.package-archive")
            } else {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
            }
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "未找到可用的安装程序", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "安装失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}