package com.tiandao.store.operation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tiandao.store.operation.base.BaseAdapter
import com.tiandao.store.operation.bean.StaffCommission
import com.tiandao.store.operation.databinding.StaffCommissionItemBinding

class StaffCommissionAdapter: BaseAdapter<StaffCommission, StaffCommissionItemBinding>(

diffCallback = object : DiffUtil.ItemCallback<StaffCommission>() {
    override fun areItemsTheSame(oldItem: StaffCommission, newItem: StaffCommission): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: StaffCommission, newItem: StaffCommission): Boolean {
        return oldItem == newItem
    }
}) {

    override fun getViewBinding(parent: ViewGroup, viewType: Int): StaffCommissionItemBinding {
        return StaffCommissionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: StaffCommissionItemBinding, item: StaffCommission, position: Int) {
        binding.tvName.text = item.productName
        when (item.flowType) {
            1 -> binding.tvType.text = "销售订单"
            2 -> binding.tvType.text = "团队提成"
            3 -> binding.tvType.text = "退款"
            4 -> binding.tvType.text = "团队退款"
        }
        binding.tvOrderAmount.text = "￥".plus(item.amount)
        binding.tvCommissionAmount.text = "￥".plus(item.commissionAmount)
        binding.tvStaff.text = "员工： ".plus(item.staffName)
        binding.tvTime.text = item.orderDate
    }

}