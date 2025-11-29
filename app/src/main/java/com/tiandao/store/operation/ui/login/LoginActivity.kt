package com.tiandao.store.operation.ui.login

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.common.dialog.TermsAndPrivacyDialog
import com.tiandao.store.operation.databinding.ActivityLoginBinding
import com.tiandao.store.operation.ui.MainActivity
import com.tiandao.store.operation.utils.SPUtils
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>()  {

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): LoginViewModel {
        return ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun initView() {
        setTitleText("用户登录")
        binding.btnLogin.setOnClickListener(this)
    }



    override fun initData() {
        val hasAcceptedTerms = SPUtils.getBoolean("has_accepted_terms",false)
        if (!hasAcceptedTerms) {
            showTermsAndPrivacyDialog(this)
        }

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
            ToastUtils.showLong(it)
        }

        viewModel.userInfo.observe(this){
            if (it != null){
                ToastUtils.showLong("登录成功")
            }
            //保存用户信息
            UserUtils.saveLoginState(this, true, it)
            UserUtils.saveUser(this, it)
            startActivity(Intent(this, MainActivity::class.java ))
            finish()
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnLogin.id -> {
                if (binding.etUsername.text.isNullOrEmpty()){
                    ToastUtils.showLong("请输入用户名")
                    return
                }
                if (binding.etPassword.text.isNullOrEmpty()){
                    ToastUtils.showLong("请输入密码")
                    return
                }
                viewModel.login(binding.etUsername.text.toString(),binding.etPassword.text.toString())
            }
        }
    }

    private fun showTermsAndPrivacyDialog(context: Activity) {

        val dialog = TermsAndPrivacyDialog(context, object : TermsAndPrivacyDialog.OnFilterAppliedListener {
            override fun determine(dialog: TermsAndPrivacyDialog) {
                // 用户点击同意，记录状态（如 SharedPreferences）
                SPUtils.putBoolean("has_accepted_terms",true)
                dialog.dismiss()
            }

            override fun close(dialog: TermsAndPrivacyDialog) {
                // 用户拒绝，退出应用或限制功能
                dialog.dismiss()
                finish()
            }
        })
        dialog.setCancelable(false) // 禁止点击外部关闭
        dialog.show()
    }

}