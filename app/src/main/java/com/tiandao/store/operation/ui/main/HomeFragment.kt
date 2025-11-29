package com.tiandao.store.operation.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseFragment
import com.tiandao.store.operation.bean.SysMenu
import com.tiandao.store.operation.common.adapter.SysMenuListAdapter
import com.tiandao.store.operation.common.weight.GridDividerItemDecoration
import com.tiandao.store.operation.databinding.FragmentHomeBinding
import com.tiandao.store.operation.ui.audit.AuditActivity
import com.tiandao.store.operation.ui.commission.CommissionActivity
import com.tiandao.store.operation.ui.shop.ShopListActivity
import com.tiandao.store.operation.ui.tenant.TenantListActivity
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.UserUtils


/**
 * 首页
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    var list: MutableList<SysMenu> = mutableListOf()
    val adapter = SysMenuListAdapter()
    var spanCount:  Int = 3


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): HomeViewModel {
        return ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun isRegisteredEventBus(): Boolean {
        return false
    }

    override fun initView() {
        infoAdapter();
        val currentUser = UserUtils.getCurrentUser(requireContext())
        currentUser?.let {
            binding.textViewName.text = it.loginUser.user.nickName
            val map = it.loginUser.user.roles?.map { role -> role.roleName }
            binding.textViewDept.text = it.loginUser.user.phonenumber.plus("  ").plus( map?.joinToString(","))
        }
    }

    private fun infoAdapter() {
        // 使用
        binding.recyclerView.setLayoutManager(GridLayoutManager(activity, spanCount))
        adapter.onItemClick = { sysMenu, position, view ->
            startActivityToPage(sysMenu)
        }
        val spacing = DisplayUtil.dp2px(20);
        val itemDecoration = GridDividerItemDecoration(spacing, spanCount, false)
        binding.recyclerView.addItemDecoration(itemDecoration)
        binding.recyclerView.setAdapter(adapter)
    }

    private fun startActivityToPage(menu: SysMenu) {
        if(menu.code == "TenantList"){
            Intent(requireActivity(), TenantListActivity::class.java).apply {
                startActivity(this)
            }
        }else if(menu.code == "ShopList"){
            Intent(requireActivity(), ShopListActivity::class.java).apply {
                startActivity(this)
            }
        }else if(menu.code == "AuditList"){
            //审核列表
            Intent(requireActivity(), AuditActivity::class.java).apply {
                startActivity(this)
            }
        }else if(menu.code == "Commission"){
            // 个人提成
            Intent(requireActivity(), CommissionActivity::class.java).apply {
                startActivity(this)
            }
        }

    }

    override fun initData() {
        list.add(SysMenu(1, 1, "租户列表", "TenantList", R.mipmap.tenant_icon))
        list.add(SysMenu(2, 2, "门店列表", "ShopList", R.mipmap.shop_icon))
        list.add(SysMenu(2, 2, "审核列表", "AuditList", R.mipmap.audit_icon))
        list.add(SysMenu(2, 2, "提成流水", "Commission", R.mipmap.commission_icon))
        adapter.submitList( list);
    }

    override fun observeLiveData() {

    }

    override fun onClick(v: View?) {

    }

}