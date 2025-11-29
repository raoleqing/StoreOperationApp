package com.tiandao.store.operation.ui.my

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.databinding.ActivitySetBinding
import com.tiandao.store.operation.ui.login.LoginActivity
import com.tiandao.store.operation.utils.CacheDataManager
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils

class SetActivity : BaseActivity<ActivitySetBinding, SetViewModel>() {

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivitySetBinding {
        return ActivitySetBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): SetViewModel {
        return ViewModelProvider(this).get(SetViewModel::class.java)
    }

    override fun initView() {
        setTitleText("设置")

        binding.butOut.setOnClickListener(this);
        binding.tvModifyPassword.setOnClickListener(this);
        binding.llCache.setOnClickListener(this);

    }

    override fun initData() {

    }

    override fun observeLiveData() {


        viewModel.loadingState.observe(this){
            if (it) {
                showLoading()
            }else{
                hideLoading()
            }
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            UserUtils.logout(this)
            val intents = Intent(this, LoginActivity::class.java)
            intents.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intents)
            finish()
        }

        viewModel.logoutBo.observe(this){
            if(it){
                UserUtils.logout(this)
                val intents = Intent(this, LoginActivity::class.java)
                intents.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intents)
                finish()
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.but_out -> {
                // 退出登录
                val message = "您确定要退出登录吗？"
                val dialog: AlertDialog =
                    AlertDialog.Builder(this).setTitle("温馨提示").setMessage(message)
                        .setNegativeButton("取消") { _, _ ->

                        }.setPositiveButton("确定") { _, _ ->
                            viewModel.logout()
                        }.create()
                dialog.show()
            }
            R.id.tv_modify_password -> {
                val intent = Intent(this, ForgotPassword::class.java).apply {
                    putExtra("phone_number", UserUtils.getPhoneNumber(this@SetActivity))
                }
                startActivity(intent)
            }
            R.id.ll_cache -> {
                // 清空缓存
                CacheDataManager.clearAllCache(this)
                val cacheAllSize = CacheDataManager.getTotalCacheSize(this)
                binding.tvCacheSize.text = cacheAllSize
                ToastUtils.showLong("清理成功")
            }
        }

    }
}