package com.tiandao.store.operation.ui.order

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiandao.store.operation.R
import com.tiandao.store.operation.base.BaseActivity
import com.tiandao.store.operation.bean.FlowAuditRecord
import com.tiandao.store.operation.bean.FlowAuditRecordItem
import com.tiandao.store.operation.bean.PlatformOrder
import com.tiandao.store.operation.common.adapter.FlowAuditRecordItemAdapter
import com.tiandao.store.operation.common.dialog.AuditDialog
import com.tiandao.store.operation.common.event.EventCode
import com.tiandao.store.operation.common.event.EventMessage
import com.tiandao.store.operation.databinding.ActivityOrderBinding
import com.tiandao.store.operation.utils.DateUtils
import com.tiandao.store.operation.utils.EventBusUtils
import com.tiandao.store.operation.utils.StringUtils
import com.tiandao.store.operation.utils.ToastUtils
import com.tiandao.store.operation.utils.UserUtils

class OrderActivity :  BaseActivity<ActivityOrderBinding,OrderViewModel>()  {

    companion object {
        const val ORDER_ID = "order_id"
    }
    var orderId: Long = 0
    var platformOrder: PlatformOrder? = null

    //审批记录
    var flowAuditRecord: FlowAuditRecord? = null
    var list :MutableList<FlowAuditRecordItem> = mutableListOf()
    var adapter = FlowAuditRecordItemAdapter()

    override fun showBackArrow(): Boolean {
        return true
    }


    override fun getViewBinding(): ActivityOrderBinding {
        return ActivityOrderBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): OrderViewModel {
        return ViewModelProvider(this).get(OrderViewModel::class.java)
    }

    override fun initView() {
        setTitleText("订单详情")
        infoAdapter()
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun initData() {
        orderId = intent.getLongExtra(ORDER_ID, 0)
        viewModel.getPlatformOrder(orderId)
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

        viewModel.platformOrder.observe(this){
            if(it != null){
                setOrderContent(it);
            }
        }

        viewModel.audit.observe (this){
            if(it != null){
                ToastUtils.showShort("审批成功")
                EventBusUtils.post(EventMessage(EventCode.AUDIT_SUCCESS, "审批成功"))
                finish()
            }
        }
    }


    private fun infoAdapter() {
        // 审批流
        binding.rvApprovalFlow.setLayoutManager(LinearLayoutManager(this))
        adapter.onItemClick = { item, position, view ->
        }
        binding.rvApprovalFlow.adapter = adapter  // 后设置 Adapter
    }

    private fun setOrderContent(order: PlatformOrder) {
        this.platformOrder = order
        binding.tvTenantName.text = order.tenantName
        binding.tvShopName.text = order.shopName
        binding.tvOrderNo.text = order.orderNo
        var orderType = when(order.planType){
            "shop" -> "门店管理费"
            "sms" -> "短信"
            else -> ""
        }
        binding.tvName.text = order.planName
        binding.tvOrderType.text = orderType
        var unit = StringUtils.getUnitText(order.planType)
        binding.tvAmount.text = order.amount.toString().plus(unit)
        binding.tvPaymentAmount.text = "￥".plus(order.paymentAmount)
        var payType = StringUtils.getPayTypeText(order.paymentChannel)
        binding.tvPaymentChannel.text = payType
        binding.tvPaymentTime.text = order.payTime
        var auditStatus = StringUtils.getAuditStatusText(order.auditStatus)
        binding.tvAuditStatus.text = auditStatus
        binding.tvStatus.text = auditStatus
        binding.tvStaff.text = order.staffName

        order.flowAuditRecord?.let {
            this.flowAuditRecord = it
            list.clear()
            list.addAll(it.flowAuditItemList?: emptyList())
            adapter.submitList( list)

            //如果是审核者本人
            if(it.auditOpId == UserUtils.getUserId( this).toString()){
                binding.llButton.visibility = View.VISIBLE
            }
            //如查有最高权限
            if(UserUtils.isAdmin(this)){
                binding.llButton.visibility = View.VISIBLE
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_submit -> {
                showAuditDialog()
            }
        }
    }

    private fun showAuditDialog() {
        val dialog = AuditDialog(this, true,object : AuditDialog.OnFilterAppliedListener{
            override fun determine(dialog: AuditDialog, auditStatus: Int, remark: String) {
                dialog.dismiss()
                var userId = UserUtils.getUserId(this@OrderActivity)
                var userName = UserUtils.getUserName(this@OrderActivity)
                audit(userId,userName,auditStatus,remark)
            }

            override fun close(dialog: AuditDialog) {
                dialog.dismiss()
            }

        })
        dialog.show()
    }

    private fun audit(userId: Long, userName: String, auditStatus: Int, remark: String) {
        val flowAuditRecordItem: FlowAuditRecordItem? = list.find { it ->
            flowAuditRecord?.auditItemId == it.auditItemId
        }
        flowAuditRecordItem?.let {
            flowAuditRecordItem.auditStatus = auditStatus
            flowAuditRecordItem.auditOpId = userId
            flowAuditRecordItem.auditOpName = userName
            flowAuditRecordItem.auditRemark = remark
            flowAuditRecordItem.auditTime = DateUtils.getCurrentTime()
            viewModel.audit(it);
        }
    }

}