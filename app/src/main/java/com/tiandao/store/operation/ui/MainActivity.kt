package com.tiandao.store.operation.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.databinding.ActivityMainBinding
import com.tiandao.store.operation.ui.login.LoginActivity
import com.tiandao.store.operation.utils.UserUtils

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(){
    private lateinit var navController: NavController
    private var selectedItemId = 0

    // 登录超时监听
    private val authBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val code = intent?.getStringExtra("code")
            if (code == "401") {
                UserUtils.logout(this@MainActivity)
                val intents = Intent(this@MainActivity, LoginActivity::class.java)
                intents.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intents)
                finish()
            }
        }
    }

    private val intentFilter = IntentFilter()

    init {
        intentFilter.addAction("com.tiandao.store.operation.AuthBroadcastReceiver")
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): MainViewModel {
        return ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initView() {
        setToolbarVisibility(View.GONE);
        //注册接收广播
        registerReceiver()

        // 获取 NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 切换导行
        binding.bottomNavView.setOnItemSelectedListener { item ->
            selectedItemId = item.itemId
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.orderListFragment -> {
                    navController.navigate(R.id.orderListFragment)
                }
                R.id.myFragment -> {
                    navController.navigate(R.id.myFragment)
                }
            }
            true
        }

        // 监听导航变化
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    // 首页特定的逻辑
                }
                R.id.orderListFragment -> {
                    // 订单列表页面特定的逻辑
                }
                R.id.myFragment -> {
                    // 我的页面特定的逻辑
                }
            }
        }
        // 使用 OnBackPressedDispatcher 处理返回键逻辑
        onBackPressedDispatcher.addCallback(this){
            if (navController.currentDestination?.id == R.id.homeFragment) {
                // 如果当前在首页，退出应用
                finish()
            } else {
                // 否则导航回首页
                navController.navigate(R.id.homeFragment)
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(authBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(authBroadcastReceiver, intentFilter)
        }
    }

    override fun initData() {

    }

    override fun observeLiveData() {

    }

    override fun onClick(v: View?) {

    }
}