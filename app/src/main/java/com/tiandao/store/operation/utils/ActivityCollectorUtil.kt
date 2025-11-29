package com.tiandao.store.operation.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import kotlin.collections.removeAll
import kotlin.let
import kotlin.ranges.downTo
import kotlin.takeIf

object ActivityCollectorUtil {
    private val activityStack = mutableListOf<WeakReference<Activity>>()
    @Volatile
    private var currentActivityRef: WeakReference<Activity>? = null

    // 获取栈顶 Activity（最可靠实现）
    @Synchronized
    fun getTopActivity(): Activity? {
        // 优先级 1：当前处于 resumed 状态的 Activity
        currentActivityRef?.get()?.takeIf { !it.isFinishing }?.let {
            return it
        }

        // 优先级 2：从栈中查找最后一个未销毁的 Activity
        for (i in activityStack.size - 1 downTo 0) {
            val activity = activityStack[i].get()
            if (activity != null && !activity.isFinishing) {
                return activity
            }
        }
        return null
    }

    // 配套的生命周期回调实现
    private val lifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            addActivity(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivityRef = WeakReference(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            if (currentActivityRef?.get() == activity) {
                currentActivityRef = null
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            removeActivity(activity)
        }

        // 其他不需要的空实现
        override fun onActivityStarted(activity: Activity) = Unit
        override fun onActivityStopped(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    }

    // 初始化（在 Application 中调用）
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    @Synchronized
    private fun addActivity(activity: Activity) {
        activityStack.removeAll { it.get() == null || it.get() == activity }
        activityStack.add(WeakReference(activity))
    }

    @Synchronized
    private fun removeActivity(activity: Activity) {
        activityStack.removeAll { it.get() == null || it.get() == activity }
    }
}