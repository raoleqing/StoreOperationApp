package com.tiandao.store.operation.ui.team

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.StaffHierarchy
import com.tiandao.store.operation.common.adapter.StaffHierarchyAdapter
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.ActivityTeamBinding
import com.tiandao.store.operation.ui.commission.CommissionActivity
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils

class TeamActivity : BaseActivity<ActivityTeamBinding,TeamViewModel>() {

    private val adapter = StaffHierarchyAdapter()
    var list: MutableList<StaffHierarchy> = mutableListOf()

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityTeamBinding {
        return ActivityTeamBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): TeamViewModel {
        return ViewModelProvider(this).get(TeamViewModel::class.java)
    }


    override fun initView() {
        setTitleText("我的团队")
        infoAdapter();
        binding.srlRefreshTask.setOnRefreshListener {
            // 下拉刷新回调
            viewModel.pageIndex = 1
            viewModel.getStaffHierarchyList()
        }

        binding.srlRefreshTask.setOnLoadMoreListener {
            // 上拉加载回调
            viewModel.pageIndex++
            viewModel.getStaffHierarchyList()
        }
    }

    private fun infoAdapter() {
        // 使用
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter.onItemClick = { item, position , view->
            Intent(this@TeamActivity, CommissionActivity::class.java).apply {
                putExtra(CommissionActivity.STAFF_ID, item.staffId)
                startActivity(this)
            }
        }
        val spacing = DisplayUtil.dp2px(12);
        val itemDecoration = SpaceItemDecoration(spacing)
        adapter.submitList( list)
        binding.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
        binding.recyclerView.setAdapter(adapter)
    }

    override fun initData() {
        var user = UserUtils.getCurrentUser( this)
        user?.let {
            binding.tvName.text = it.loginUser.user.nickName
            binding.tvPhone.text = it.loginUser.user.phonenumber
            viewModel.managerStaffId = it.loginUser.user.userId
            viewModel.getStaffHierarchyList();
            viewModel.getStaffHierarchy(it.loginUser.user.userId)
        }
    }

    override fun observeLiveData() {
        viewModel.loadingState.observe(this) {
            if (it) {
                showLoading()
            }else{
                hideLoading()
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                ToastUtils.showShort(it)
            }
        }

        viewModel.staffHierarchyList.observe(this){
            if(it != null){
                if(viewModel.pageIndex == 1){
                    list.clear()
                }
                list.addAll(it)
                if(it.size < 20){
                    binding.srlRefreshTask.finishLoadMoreWithNoMoreData()
                }
                adapter.notifyDataSetChanged()
                if(list.isEmpty()){
                    binding.emptyView.visibility = View.VISIBLE
                }else{
                    binding.emptyView.visibility = View.GONE
                }
                binding.srlRefreshTask.finishRefresh()
                binding.srlRefreshTask.finishLoadMore()
            }
        }

        viewModel.staffHierarchy.observe (this){
            if(it != null){
                binding.tvLevel.text = it.level.plus("级")
                binding.tvListTotal.text = "(总人数：".plus(it.memberTotal).plus(")")
            }
        }
    }

    override fun onClick(v: View?) {
    }


}