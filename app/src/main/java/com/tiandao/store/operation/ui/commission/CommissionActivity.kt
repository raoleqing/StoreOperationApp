package com.tiandao.store.operation.ui.commission

import android.content.Intent
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

class CommissionActivity : BaseActivity<ActivityCommissionBinding,CommissionViewModel>() {

    companion object {
        const val STAFF_ID = "staff_id"
    }

    var staffId: Long = 0

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
        setTitleText("员工提成")
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
        staffId = intent.getLongExtra(STAFF_ID, 0)
        viewModel.staffId = staffId

        viewModel.pageIndex = 1
        var month = DateUtils.getStartOfDayString( DateUtils.getCurrentTime(DateUtils.FORMAT_YMD_HMS), DateUtils.FORMAT_YMD_HMS, DateUtils.FORMAT_YM)
        binding.tvMonth.text = month
        viewModel.month = month
        viewModel.getStaffCommissionListList();
        viewModel.getStaffHierarchy(staffId)
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

        viewModel.staffHierarchy.observe(this){
           if(it != null){
               binding.tvName.text = it.staffName
               binding.tvLevel.text = "等级：".plus(it.level)
               binding.tvTotal.text = "￥".plus(it.actualSales?:"0.00")
               binding.tvPerformancePoints.text = it.performanceScore.toString()
           }
        }

        viewModel.staffCommissionList.observe(this){
            if(it != null){
                it.monthTotal.let {
                    binding.tvMonthAmount.text = "提成金额：￥".plus(it.commissionAmount)
                    binding.tvMonthPoints.text = "成长值：".plus(it.performancePoints)
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
                val month = DateUtils.getStartOfDayString( date, DateUtils.FORMAT_YMD, DateUtils.FORMAT_YM);
                binding.tvMonth.text = month
                viewModel.month = month
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