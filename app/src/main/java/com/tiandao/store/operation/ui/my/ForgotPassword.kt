package com.tiandao.store.operation.ui.my

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.databinding.ActivityForgotPasswordBinding
import com.tiandao.store.operation.utils.ToastUtils

class ForgotPassword : BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel>()  {

    companion object {
        const val PHONE = "phone"
    }

    override fun getViewBinding(): ActivityForgotPasswordBinding {
        return ActivityForgotPasswordBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): ForgotPasswordViewModel {
        return ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun initView() {
        setTitleText("忘记密码")
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun initData() {
        var phoneNumber = this.intent.getStringExtra(PHONE)
        if (!phoneNumber.isNullOrEmpty()) {
            binding.etPhone.setText(phoneNumber)
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

        viewModel.verificationCode.observe(this){
            ToastUtils.showLong("发送成功")
        }

        viewModel.updatePassword.observe(this){
            ToastUtils.showLong("修改成功")
            finish()
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnSubmit.id -> {
                if (binding.etPhone.text.isNullOrEmpty()){
                    ToastUtils.showShort("请输入手机号")
                    return
                }
                if (binding.etOldPassword.text.isNullOrEmpty()){
                    ToastUtils.showShort("请输入旧密码")
                    return
                }

                if (binding.etNewPassword.text.isNullOrEmpty()){
                    ToastUtils.showShort("请输入密码")
                    return
                }

                if (binding.etConfirmPassword.text.isNullOrEmpty()){
                    ToastUtils.showShort("请输入确认密码")
                    return
                }
                if (binding.etNewPassword.text.toString() != binding.etConfirmPassword.text.toString()){
                    ToastUtils.showShort("两次输入的密码不一致")
                    return
                }
                viewModel.updatePassword(binding.etPhone.text.toString(),binding.etOldPassword.text.toString(),binding.etNewPassword.text.toString())
            }
        }
    }


}