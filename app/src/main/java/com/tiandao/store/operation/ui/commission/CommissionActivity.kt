package com.tiandao.store.operation.ui.commission

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.StaffCommission
import com.tiandao.store.operation.common.adapter.StaffCommissionAdapter
import com.tiandao.store.operation.common.dialog.DateSelectDialog
import com.tiandao.store.operation.common.weight.SpaceItemDecoration
import com.tiandao.store.operation.databinding.ActivityCommissionBinding
import com.tiandao.store.operation.utils.DateUtils
import com.tiandao.store.operation.utils.DisplayUtil
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils

class CommissionActivity : BaseActivity<ActivityCommissionBinding,CommissionViewModel>() {

    private val adapter = StaffCommissionAdapter()
    var list: MutableList<StaffCommission> = mutableListOf()

    override fun showBackArrow(): Boolean {
        return true
    }

    override fun getViewBinding(): ActivityCommissionBinding {
        return ActivityCommissionBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): CommissionViewModel {
        return ViewModelProvider(this).get(CommissionViewModel::class.java)
    }

    override fun initView() {
        setTitleText("个人提成中心")
        infoAdapter();
        binding.srlRefreshTask.setOnRefreshListener {
            // 下拉刷新回调
            viewModel.pageIndex = 1
            viewModel.getStaffCommissionListList()
        }

        binding.srlRefreshTask.setOnLoadMoreListener {
            // 上拉加载回调
            viewModel.pageIndex++
            viewModel.getStaffCommissionListList()
        }
        binding.tvMonth.setOnClickListener(this)
    }

    private fun infoAdapter() {
        // 使用
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter.onItemClick = { item, position , view->
        }
        val spacing = DisplayUtil.dp2px(12);
        val itemDecoration = SpaceItemDecoration(spacing)
        adapter.submitList( list)
        binding.recyclerView.addItemDecoration(itemDecoration)  // 先添加装饰
        binding.recyclerView.setAdapter(adapter)
    }

    override fun initData() {
        viewModel.pageIndex = 1
        val date = DateUtils.getCurrentTime(DateUtils.FORMAT_YMD_HMS)
        var month = DateUtils.getStartOfDayString( date, DateUtils.FORMAT_YMD_HMS, DateUtils.FORMAT_YM)
        binding.tvMonth.text = month

        viewModel.staffId = UserUtils.getUserId( this)
        viewModel.month = date

        viewModel.getStaffCommissionListList();
        viewModel.getStaffCountByMonth(date)

        var user = UserUtils.getCurrentUser( this)
        user?.let {
            binding.tvName.text = it.loginUser.user.nickName
            binding.tvPhone.text = it.loginUser.user.phonenumber
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

        viewModel.commissionCount.observe(this){
           if(it != null){
               binding.tvTotal.text = "￥".plus(it.total?:"0.00")
               binding.tvMonthTotal.text = "￥".plus(it.count?:"0.00")
           }
        }

        viewModel.staffCommissionList.observe(this){
            if(it != null){
                it.monthTotal?.let {
                    binding.tvListTotal.text = "(总额：".plus(it).plus(")")
                }
                if(viewModel.pageIndex == 1){
                    list.clear()
                }
                it.list?.let {
                    list.addAll(it)
                    if(it.size < 20){
                        binding.srlRefreshTask.finishLoadMoreWithNoMoreData()
                    }
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
    }

    override fun onClick(v: View?) {
         when(v?.id){
             R.id.tv_month -> {
                 showDatePickerDialog()
             }
        }
    }

    private fun showDatePickerDialog(){
        var dialog = DateSelectDialog(this, false, object : DateSelectDialog.OnFilterAppliedListener {
            override fun determine(dialog: DateSelectDialog, date: String) {
                dialog.dismiss()
                binding.tvMonth.text = DateUtils.getStartOfDayString( date, DateUtils.FORMAT_YMD, DateUtils.FORMAT_YM)
                viewModel.month = DateUtils.getStartOfMonth(date)
                viewModel.pageIndex = 1
                viewModel.getStaffCommissionListList()
            }

            override fun close(dialog: DateSelectDialog) {
                dialog.dismiss()
            }
        })
        dialog.show()
    }

}