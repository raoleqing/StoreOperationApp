package com.tiandao.store.operation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tiandao.store.operation.databinding.ActivitySplashBinding
import com.tiandao.store.operation.ui.login.LoginActivity
import com.tiandao.store.operation.utils.UserUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    // 定义activityScope
    private val activityScope = CoroutineScope(Dispatchers.Main + Job())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 这里可以执行轻量级初始化
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            systemBars.top
            systemBars.bottom
            systemBars.left
            systemBars.right
            WindowInsetsCompat.CONSUMED
        }

        activityScope.launch {
            // 并行执行初始化任务
            val initTask = async(Dispatchers.IO) { initApp() }
            // 至少显示3秒
            delay(3000)
            initTask.await()
            startLoginActivity(UserUtils.isLoggedIn(this@SplashActivity))
            finish()
        }

    }

    private fun initApp() {

    }

    /**
     * @param isLogin 是否已经登录
     */
    private fun startLoginActivity(isLogin: Boolean) {
        if (!this.isFinishing) {
            val intent = Intent(
                this@SplashActivity,
                if (isLogin) MainActivity::class.java else LoginActivity::class.java
            )
            startActivity(intent)
        }
        this.finish()
    }

    override fun onDestroy() {
        activityScope.cancel("Activity destroyed")
        super.onDestroy()
    }
}