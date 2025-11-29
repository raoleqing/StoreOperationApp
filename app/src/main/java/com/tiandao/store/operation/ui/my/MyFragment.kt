package com.tiandao.store.operation.ui.my

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseFragment
import com.tiandao.store.operation.databinding.FragmentMyBinding
import com.tiandao.store.operation.ui.about.AboutActivity
import com.tiandao.store.operation.ui.message.MessageListActivity
import com.tiandao.store.operation.utils.UserUtils


/**
 * 我的
 */
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMyBinding {
        return FragmentMyBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): MyViewModel {
        return ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun isRegisteredEventBus(): Boolean {
        return false
    }

    override fun initView() {
        val currentUser = UserUtils.getCurrentUser(requireContext())
        currentUser?.let {
            binding.textViewName.text = it.loginUser.user.nickName
            val map = it.loginUser.user.roles?.map { role -> role.roleName }
            binding.textViewDept.text = it.loginUser.user.phonenumber.plus("  ").plus( map?.joinToString(","))
        }
        binding.tvInfo.setOnClickListener(this);
        binding.tvFeedback.setOnClickListener(this);
        binding.tvAbout.setOnClickListener(this);
        binding.tvSet.setOnClickListener(this);

    }

    override fun initData() {

    }

    override fun observeLiveData() {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_info -> {
                //跳转到个人信息页面
                startActivity(Intent(activity, UserActivity::class.java ))
            }
            R.id.tv_feedback -> {
                //消息中心
                startActivity(Intent(activity, MessageListActivity::class.java))
            }
            R.id.tv_about -> {
                //跳转到关于页面
                startActivity(Intent(activity, AboutActivity::class.java))
            }
            R.id.tv_set -> {
                //跳转到设置页面
                startActivity(Intent(activity, SetActivity::class.java ))
            }
        }

    }

}