package com.tiandao.store.operation.ui.my

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.SysUserBo
import com.tiandao.store.operation.databinding.ActivityUserBinding

class UserActivity : BaseActivity<ActivityUserBinding,UserViewModel>() {

    override fun showBackArrow(): Boolean {
        return true
    }
    override fun getViewBinding(): ActivityUserBinding {
        return ActivityUserBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): UserViewModel {
        return ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun initView() {
        setTitleText("个人中心")
    }

    override fun initData() {
        viewModel.getInfo()
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
        }

        viewModel.userInfo.observe(this) {
            if (it != null) {
                setInfo(it);
            }
        }
    }

    fun  setInfo(sysUserBo: SysUserBo){
        var sysUser = sysUserBo.sysUser
        sysUser.let {
            it.avatar?.let { avatar ->
                Glide.with(binding.imageViewAvatar.context)
                    .load(avatar)
                    .centerCrop()
                    .error(R.mipmap.avatar_default)
                    .into(binding.imageViewAvatar)
            }
            binding.textViewName.text = it.nickName
            binding.textViewPhone.text = it.phonenumber
            binding.tvEmail.text = it.email
            var roleName = sysUserBo.roles?.joinToString(separator = "") { it.roleName }
            binding.tvRole.text = roleName
        }
    }

    override fun onClick(v: View?) {

    }

}